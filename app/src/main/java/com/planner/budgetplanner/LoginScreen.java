package com.planner.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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

public class LoginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginScreen";

    private EditText email;
    private EditText pass;
    private TextView errorLabel;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        errorLabel = findViewById(R.id.errorLabel);
        email = findViewById(R.id.username);
        progressBar = findViewById(R.id.loadingHoriBar);
        pass = findViewById(R.id.password);
        findViewById(R.id.fbBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLoginButton.callOnClick();
            }
        });
        initializeGoogle();
        initializeFb();
    }

    private void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(("310439222563-tsohc49cq23l9jevgr86hf9aimah2lcr.apps.googleusercontent.com"))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initializeFb()
    {
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton = findViewById(R.id.fbOrginLogBtn);
        fbLoginButton.setPermissions("email", "public_profile");
        Log.i(TAG, "initializeFb: ");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "facebook:onSuccess:" + loginResult.getAccessToken());
                handleFbLogin(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "facebook:onCancel");
                // ...
            }


            @Override
            public void onError(FacebookException error) {
                email.setText("error");
                Log.i(TAG, "facebook:onError", error);
                // ...
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
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i(TAG, "onActivityResult account: "+account);
                handleGoogleLogin(account);

            } catch (ApiException e) {
                Log.i(TAG, "onActivityResult error: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void loginWithGoogle(View view) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.i(TAG, "loginWithGoogle: "+account);
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();//Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else
        {
            handleGoogleLogin(account);
        }
    }

    private void handleFbLogin(String token)
    {
         FirebaseManager.LoginData loginData = new FirebaseManager.LoginData("", "", "", "");
        FirebaseManager.loginWithCredential(token, FirebaseManager.LoginType.Facebook,loginData, new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        MyUtility.currentUser=user;
                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        startActivity(new Intent(LoginScreen.this,MainActivity.class));
                    }
                },null);
    }

    private void handleGoogleLogin(GoogleSignInAccount account)
    {
        Log.i(TAG, "handleGoogleLogin: ");
       // FirebaseManager.LoginData loginData = new FirebaseManager.LoginData(account.getId(), account.getGivenName(), account.getFamilyName(), account.getEmail());
//        FirebaseManager.loginWithCredential(account.getIdToken(), FirebaseManager.LoginType.Google,loginData, new OnSuccessListener<User>() {
//                    @Override
//                    public void onSuccess(User user) {
//                        MyUtility.currentUser=user;
//                        progressBar.setVisibility(View.INVISIBLE);
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        startActivity(new Intent(LoginScreen.this,MainActivity.class));
//                    }
//                },null);
        FirebaseManager.loginWithGoogl(account, new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                MyUtility.currentUser=user;
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(LoginScreen.this,MainActivity.class));
            }
        },null);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}