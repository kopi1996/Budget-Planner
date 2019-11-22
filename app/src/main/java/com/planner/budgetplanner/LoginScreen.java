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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Utility.MyUtility;

public class LoginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginScreen";

    private EditText email;
    private EditText pass;
    private TextView errorLabel;
    private ProgressBar progressBar;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        errorLabel=findViewById(R.id.errorLabel);

        email = findViewById(R.id.username);

        progressBar=findViewById(R.id.loadingHoriBar);

        pass = findViewById(R.id.password);
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

        FirebaseManager.loginWithId(this, email.getText().toString(), pass.getText().toString(), new FirebaseManager.IUserInfo() {
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

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            GoogleSignInAccount acc=result.getSignInAccount();
            FirebaseManager.loginWithGoogle(acc, new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Log.i(TAG, "onSuccess user id: "+user.getId());
                    MyUtility.currentUser=user;
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    startActivity(new Intent(LoginScreen.this,MainActivity.class));
                }
            },null);
        }
    }

    public void loginWithGoogle(View view) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void loginWithFB(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}