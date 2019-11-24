package com.planner.budgetplanner;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    public static final String TAG="FirebaseManager";

    private static FirebaseFirestore getDBInstance()
    {
        return FirebaseFirestore.getInstance();
    }

    private static FirebaseAuth getAuth()
    {
        return FirebaseAuth.getInstance();
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

    public static void addUserIntoDB(String key, User user, OnSuccessListener onFinished, OnFailureListener onFailure){
        Map<String, Object> data = new HashMap<>();
        data.put("id",user.getId());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("type", user.getType().toString());

        getDBInstance().collection("users").document(key).set(data).addOnSuccessListener(onFinished).addOnFailureListener(onFailure);
    }

    public static void loginWithId(Activity activity, String email, String password, final IUserLogin onFinished) {
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference docRef = getDBInstance().collection("users").document(getAuth().getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String fName = document.get("first_name").toString();
                                            String lName = document.get("last_name").toString();
                                            String emailString = document.get("email").toString();

                                            User user = new User(getAuth().getUid(),fName+" "+lName, emailString,LoginType.Email);
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

    public static void logOut(final OnSuccessListener successListener)
    {
        final FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
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

    public enum LoginType
    {
        Email,Facebook,Google
    }

    public interface IUserLogin {
        public void onSuccess(User user);
        public void onFailure(String error);
    }
}
