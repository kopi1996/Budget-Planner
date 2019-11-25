package com.planner.budgetplanner;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private static final String USERS_REF="users";
    private static final String INCOMES_REF="Incomes";
    private static final String CATEGORIES_REF="Categories";
    private static final String EXPENSES_REF="Expenses";



    private static final ArrayList<OnLogoutListner> logOutCallbackListners = new ArrayList<>();

    public static final String TAG="FirebaseManager";

    public static FirebaseFirestore getDBInstance()
    {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseAuth getAuth()
    {
        return FirebaseAuth.getInstance();
    }

    public static void addLogOutListner(OnLogoutListner listner)
    {
        logOutCallbackListners.add(listner);
    }

    public static void removeLogOutListner(OnLogoutListner listner)
    {
        boolean remove = logOutCallbackListners.remove(listner);
        Log.i(TAG, "removeLogOutListner: "+remove);
    }

    public static void isUserExist(String key, final OnSuccessListener<Boolean> onSuccessListener, final OnFailureListener onFailureListener) {

        getDBInstance().collection("users").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (onSuccessListener != null)
                        onSuccessListener.onSuccess(task.getResult().exists());
                } else {
                    if (onFailureListener != null)
                        onFailureListener.onFailure(new NullPointerException());
                }
            }
        });
    }

    public static void getUser(final String key, final OnSuccessListener<User> listener)
    {
        getDBInstance().collection(USERS_REF).document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String emailString = document.get("email").toString();

                        String fName = document.get("name").toString();

                        User user = new User(key, fName, emailString, LoginType.valueOf(document.get("type").toString()));
                        if (listener != null)
                            listener.onSuccess(user);
                    }
                } else {
                    if (listener != null)
                        listener.onSuccess(null);
                }
            }
        });
    }

    public static void addUserIntoDB(String key, User user, OnSuccessListener onFinished, OnFailureListener onFailure){
        Map<String, Object> data = new HashMap<>();
        data.put("id",user.getId());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("type", user.getType().toString());

        getDBInstance().collection(USERS_REF).document(key).set(data).addOnSuccessListener(onFinished).addOnFailureListener(onFailure);
    }

    public static void loginWithId(Activity activity, String email, String password, final IUserLogin onFinished) {
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference docRef = getDBInstance().collection("users").document(getAuth().getCurrentUser().getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String fName = document.get("name").toString();
                                            String emailString = document.get("email").toString();
                                            User user = new User(getAuth().getUid(),fName, emailString,LoginType.Email);
                                            onFinished.onSuccess(user);
                                        }
                                    } else {
                                        onFinished.onFailure(task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            onFinished.onFailure("Account login failed");
                        }

                    }
                });
    }

    public static void loginWithCredential(String token, final LoginType type, final IUserLogin listner) {
        if(type==LoginType.Email)
            return;

        AuthCredential credential =type==LoginType.Google? GoogleAuthProvider.getCredential(token,null): FacebookAuthProvider.getCredential(token);

        getAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = getAuth().getCurrentUser();
                            Log.i(TAG, "onComplete: current user: "+ currentUser.getDisplayName());
                            final User user=new User(currentUser.getUid(),currentUser.getDisplayName(),currentUser.getEmail(),type);
                            isUserExist(user.getId(), new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    if (!result) {
                                        addUserIntoDB(getAuth().getUid(), user, new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                if (listner != null)
                                                    listner.onSuccess(user);
                                            }
                                        }, new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                listner.onFailure("Something wrong");
                                            }
                                        });
                                    } else {
                                        Log.i(TAG, "onSuccess check exist: " + result);
                                        listner.onSuccess(user);
                                    }
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listner.onFailure("Something went wrong");
                                }
                            });
                        } else {
                            Log.i(TAG, "onComplete: failure: "+task.getException());
                            if(listner!=null)
                                listner.onFailure("Null");
                        }
                    }
                });
    }

    public static void logOut(final OnLogoutListner successListener)
    {
        final FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.i(TAG, "onAuthStateChanged: "+logOutCallbackListners.size());
                for (OnLogoutListner listner : logOutCallbackListners) {
                    //if (listner != null)
                        listner.onSuccess(user == null);
                }
                if (user != null) {
                    successListener.onSuccess(false);
                } else {
                    successListener.onSuccess(true);
                }
            }
        };
        getAuth().removeAuthStateListener(authStateListener);
        getAuth().addAuthStateListener(authStateListener);
        getAuth().signOut();
    }

    // Add Budget Items into database methods

    public static void addIncomeIntoDB(final Income income) {

        getDBInstance().collection(INCOMES_REF).add(income).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentReference> incomeAddTask) {

                getDBInstance().collection(USERS_REF).document(MyUtility.currentUser.getId()).collection("incomesIds").document("lists").update("ids",FieldValue.arrayUnion(incomeAddTask.getResult().getId())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful())
                        {
                            ArrayList<String> data=new ArrayList<>();
                            data.add(incomeAddTask.getResult().getId());
                            Map<String,Object> map=new HashMap<>();
                            map.put("ids",data);
                            getDBInstance().collection(USERS_REF).document(MyUtility.currentUser.getId()).collection("incomesIds").document("lists").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                });
            }
        });
    }


    public enum LoginType
    {
        Email,Facebook,Google
    }

    public interface OnLogoutListner
    {
        void onSuccess(boolean isLogOut);
    }

    public interface IUserLogin {
        public void onSuccess(User user);
        public void onFailure(String error);
    }
}
