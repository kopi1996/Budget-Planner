package com.planner.budgetplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseManager.OnLogoutListner {

    private static final String TAG = "LoginScreen";
    private static final String LOGIN_TYPE = "LoginType";

    private EditText email;
    private EditText pass;
    private TextView errorLabel;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static FirebaseManager.OnLogoutListner onLogoutListner=new FirebaseManager.OnLogoutListner() {
        @Override
        public void onSuccess(boolean isLogOut) {
            if (isLogOut) {
                editor.putString(LOGIN_TYPE, "null");
                //FirebaseManager.removeLogOutListner(this);
                Log.i(TAG, "onSuccess logout: " + isLogOut);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Log.i(TAG, "onMyCreate: ");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        errorLabel = findViewById(R.id.errorLabel);
        email = findViewById(R.id.username);
        progressBar = findViewById(R.id.loadingHoriBar);
        pass = findViewById(R.id.password);
        findViewById(R.id.fbBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFb();
            }
        });
        FirebaseManager.removeLogOutListner(onLogoutListner);
        FirebaseManager.addLogOutListner(onLogoutListner);
        initializeGoogle();
        initializeFb();
        if (FirebaseManager.getAuth().getCurrentUser() != null) {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            FirebaseManager.getUser(FirebaseManager.getAuth().getCurrentUser().getUid(), new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    MyUtility.currentUser = user;
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    startActivity(new Intent(LoginScreen.this,MainActivity.class));
                }
            });

        }
    }

    private void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initializeFb() {
        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = findViewById(R.id.fbOrginLogBtn);
        fbLoginButton.setPermissions("email", "public_profile");
        Log.i(TAG, "initializeFb: ");

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "facebook:onSuccess:" + loginResult.getAccessToken());
                handleLogin(loginResult.getAccessToken().getToken(), FirebaseManager.LoginType.Facebook);
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "facebook:onCancel");
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }


            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "facebook:onError", error);
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public void signUpBtnClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void loginBtnClick(View view) {
        errorLabel.setText("");
        if (!MyUtility.isValidEmail(email.getText())) {
            errorLabel.setText("email address can't be emty or not valid format");
            return;
        }
        if (TextUtils.isEmpty(pass.getText())) {
            errorLabel.setText("password can't be emty");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        FirebaseManager.loginWithId(this, email.getText().toString(), pass.getText().toString(), new FirebaseManager.IUserLogin() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginScreen.this, "Account Login successful", Toast.LENGTH_LONG).show();
                MyUtility.currentUser = user;
                editor.putString(LOGIN_TYPE, FirebaseManager.LoginType.Email.toString());
                editor.apply();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                startActivity(new Intent(LoginScreen.this, MainActivity.class));
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(LoginScreen.this, "Account Login error", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i(TAG, "onActivityResult account: " + account);
                handleLogin(account.getIdToken(), FirebaseManager.LoginType.Google);

            } catch (ApiException e) {
                Log.i(TAG, "onActivityResult error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void loginWithGoogle(View view) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.i(TAG, "loginWithGoogle: " + account);
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();//Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Log.i(TAG, "loginWithGoogle already token: " + account.getIdToken());
            handleLogin(account.getIdToken(), FirebaseManager.LoginType.Google);
        }
    }

    private void handleLogin(String token, final FirebaseManager.LoginType type) {
        FirebaseManager.loginWithCredential(token, type, new FirebaseManager.IUserLogin() {
            @Override
            public void onSuccess(User user) {
                MyUtility.currentUser = user;
                editor.putString(LOGIN_TYPE, type.toString());
                editor.apply();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(LoginScreen.this, MainActivity.class));
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void loginWithFb() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (isLoggedIn) {
            //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));

            handleLogin(accessToken.getToken(), FirebaseManager.LoginType.Facebook);
        } else {
            fbLoginButton.callOnClick();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onSuccess(boolean isLogOut) {
        if (isLogOut) {
            editor.putString(LOGIN_TYPE, "null");
            //FirebaseManager.removeLogOutListner(this);
            Log.i(TAG, "onSuccess logout: " + isLogOut);
        }
    }
}