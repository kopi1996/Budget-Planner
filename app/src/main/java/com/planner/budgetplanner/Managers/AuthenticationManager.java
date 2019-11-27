package com.planner.budgetplanner.Managers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Model.User;
import com.planner.budgetplanner.Utility.MyUtility;

public class AuthenticationManager {

    private static Activity activity;
    private static GoogleSignInClient mGoogleSignInClient;
    private static CallbackManager callbackManager;
    private static OnSuccessListener<Boolean> logOutListner;
    private static LoginButton fbLoginButton;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static final String LOGIN_TYPE = "LoginType";
    private static boolean isInitStateListner = false;


    public static GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public static CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public static LoginButton getFbLoginButton() {
        return fbLoginButton;
    }

    public static void logOut(OnSuccessListener<Boolean> listener) {
        logOutListner = listener;
        FirebaseManager.logOut();
    }

    public static void initialize(Activity activity) {
        AuthenticationManager.activity = activity;
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();

        if (!isInitStateListner) {
            isInitStateListner = true;
            FirebaseManager.initializeStateListner(new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean sucess) {
                    if (sucess) {
                        editor.putString(LOGIN_TYPE, "null");
                        editor.apply();
                    }
                    if (logOutListner != null)
                        logOutListner.onSuccess(sucess);
                }
            });
        }
    }

    public static void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public static void handleAutoLogin(final OnSuccessListener<Boolean> listener) {
        if (FirebaseManager.getAuth().getCurrentUser() != null) {
            FirebaseManager.getUser(FirebaseManager.getAuth().getCurrentUser().getUid(), new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    MyUtility.currentUser = user;
                    if (listener != null)
                        listener.onSuccess(true);
                }
            });
        } else {
            if (listener != null)
                listener.onSuccess(false);
        }
    }

    public static void initializeFb(FacebookCallback<LoginResult> fbCallback) {
        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = activity.findViewById(R.id.fbOrginLogBtn);
        fbLoginButton.setPermissions("email", "public_profile");

        fbLoginButton.registerCallback(callbackManager, fbCallback);
    }

    public static void loginGoogle(OnSuccessListener<GoogleSignInAccount> listener) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        listener.onSuccess(account);
    }

    public static void loginWithFb(OnSuccessListener<AccessToken> listener) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            if (listener != null)
                listener.onSuccess(accessToken);
        } else {
            if (listener != null)
                listener.onSuccess(null);
        }
    }

    public static void loginWithEmail(String email, String pass, final OnSuccessListener<Boolean> listener) {
        FirebaseManager.loginWithId(activity, email, pass, new FirebaseManager.IUserLogin() {
            @Override
            public void onSuccess(User user) {
                MyUtility.currentUser = user;
                editor.putString(LOGIN_TYPE, FirebaseManager.LoginType.Email.toString());
                editor.apply();
                if (listener != null)
                    listener.onSuccess(true);
            }

            @Override
            public void onFailure(String error) {
                if (listener != null)
                    listener.onSuccess(false);
            }
        });
    }

    public static void handleLogin(String token, final FirebaseManager.LoginType type, final OnSuccessListener<Boolean> listener) {
        FirebaseManager.loginWithCredential(token, type, new FirebaseManager.IUserLogin() {
            @Override
            public void onSuccess(User user) {
                MyUtility.currentUser = user;
                editor.putString(LOGIN_TYPE, type.toString());
                editor.apply();
                if (user != null) {
                    FirebaseManager.fetchAllDataFromDB(user, listener);
                } else {
                    if (listener != null)
                        listener.onSuccess(true);
                }
            }

            @Override
            public void onFailure(String error) {
                if (listener != null)
                    listener.onSuccess(false);
            }
        });
    }
}
