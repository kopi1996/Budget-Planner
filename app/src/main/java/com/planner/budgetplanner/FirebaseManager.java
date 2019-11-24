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
import com.google.firebase.auth.FirebaseAuth;
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
        data.put("first_name", user.getF_name());
        data.put("last_name", user.getL_name());
        data.put("email", user.getEmail());

        getDBInstance().collection("users").document(key).set(data).addOnSuccessListener(onFinished).addOnFailureListener(onFailure);
    }

    public static void loginWithId(Activity activity, String email, String password, final IUserInfo onFinished) {
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

                                            User user = new User(getAuth().getUid(),fName, lName, emailString);
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


    public static void loginWithGoogle(final GoogleSignInAccount acct, final OnSuccessListener<User> onFinished, final OnFailureListener onFailureListener) {

        Log.i(TAG, "loginWithGoogle id token: "+acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);

        getAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: "+getAuth().getUid());
                            final User user=new User(getAuth().getUid(),acct.getGivenName(),acct.getFamilyName(),acct.getEmail());
                            isUserExist(user.getId(), new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    if(!result) {
                                        addUserIntoDB(getAuth().getUid(), user, new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                if (onFinished != null)
                                                    onFinished.onSuccess(user);
                                            }
                                        }, onFailureListener);
                                    }
                                    else
                                    {
                                        Log.i(TAG, "onSuccess check exist: "+result);
                                        onFinished.onSuccess(user);
                                    }
                                }
                            },onFailureListener);


                        } else {
                            if(onFailureListener!=null)
                                onFailureListener.onFailure(new NullPointerException());
                        }
                    }
                });
    }

    public interface IUserInfo{
        public void onSuccess(User user);
        public void onFailure(String error);
    }
}
