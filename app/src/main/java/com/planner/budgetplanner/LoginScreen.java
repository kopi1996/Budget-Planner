package com.planner.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.planner.budgetplanner.Managers.AuthenticationManager;
import com.planner.budgetplanner.Utility.MyUtility;

public class LoginScreen extends LoadingActivity implements GoogleApiClient.OnConnectionFailedListener,FacebookCallback<LoginResult> {

    private static final String TAG = "LoginScreen";
    private static final String LOGIN_TYPE = "LoginType";

    private EditText email;
    private EditText pass;
    private TextView errorLabel;
    private static final int RC_SIGN_IN = 9001;
    private int num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        num++;
        initialize();
    }


    private void goMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    public void initialize() {
        super.initialize();
        errorLabel = findViewById(R.id.errorLabel);
        email = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        findViewById(R.id.fbBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFb();
            }
        });

        FirebaseManager.initialize();

        AuthenticationManager.initialize(this);
        AuthenticationManager.initializeGoogle();
        AuthenticationManager.initializeFb(this);
        MyUtility.enableLoading(this);
        AuthenticationManager.handleAutoLogin(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                if (success) {
                    FirebaseManager.fetchAllDataFromDB(MyUtility.currentUser, new OnSuccessListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean success) {
                            MyUtility.disableLoading(LoginScreen.this);
                            if(success)
                                goMainActivity();
                        }
                    });
                }
                else
                    MyUtility.disableLoading(LoginScreen.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onMyDestroy: "+hashCode());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onMyResume : "+toString()+" "+hashCode());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onMyPause: "+toString()+" "+hashCode());
    }


    public void signUpBtnClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
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
        MyUtility.enableLoading(this);
        AuthenticationManager.loginWithEmail(email.getText().toString(), pass.getText().toString(), new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                MyUtility.disableLoading(LoginScreen.this);
                if (success) {
                    goMainActivity();
                } else {
                    errorLabel.setText("Account does not exist");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthenticationManager.handleLogin(account.getIdToken(), FirebaseManager.LoginType.Google, new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        MyUtility.disableLoading(LoginScreen.this);
                        if(!success)
                            return;
                        goMainActivity();
                    }
                });

            } catch (ApiException e) {
                Log.i(TAG, "onActivityResult error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void loginWithGoogle(View view) {
        MyUtility.enableLoading(this);
        AuthenticationManager.loginGoogle(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount account) {
                if (account == null) {
                    MyUtility.disableLoading(LoginScreen.this);
                    Intent signInIntent = AuthenticationManager.getmGoogleSignInClient().getSignInIntent();//Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {
                    AuthenticationManager.handleLogin(account.getIdToken(), FirebaseManager.LoginType.Google, new OnSuccessListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean success) {
                            MyUtility.disableLoading(LoginScreen.this);
                            if (!success)
                                return;
                            goMainActivity();
                        }
                    });
                }
            }
        });
    }


    private void loginWithFb() {
        MyUtility.enableLoading(this);
        AuthenticationManager.loginWithFb(new OnSuccessListener<AccessToken>() {
            @Override
            public void onSuccess(AccessToken accessToken) {
                if(accessToken!=null)
                {
                    AuthenticationManager.handleLogin(accessToken.getToken(), FirebaseManager.LoginType.Facebook, new OnSuccessListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean success) {
                            MyUtility.disableLoading(LoginScreen.this);
                            if(success)
                            {
                                goMainActivity();
                            }
                        }
                    });
                }
                else
                {
                    MyUtility.disableLoading(LoginScreen.this);
                    AuthenticationManager.getFbLoginButton().callOnClick();
                }
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        AuthenticationManager.handleLogin(loginResult.getAccessToken().getToken(), FirebaseManager.LoginType.Facebook, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                MyUtility.disableLoading(LoginScreen.this);
                goMainActivity();
            }
        });
    }

    @Override
    public void onCancel() {
        MyUtility.disableLoading(LoginScreen.this);
    }

    @Override
    public void onError(FacebookException error) {
        MyUtility.disableLoading(LoginScreen.this);
    }
}