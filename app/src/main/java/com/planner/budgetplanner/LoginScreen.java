package com.planner.budgetplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText email;
    private FirebaseAuth mAuth;
    private EditText pass;
    private TextView errorLabel;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        errorLabel=findViewById(R.id.errorLabel);

        email = findViewById(R.id.username);
        email.setOnFocusChangeListener(this);

        progressBar=findViewById(R.id.loading);

        pass = findViewById(R.id.password);
        pass.setOnFocusChangeListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    public void signUpBtnClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            v.setBackgroundResource(R.drawable.focus_text_style);
        } else {
            v.setBackgroundResource(R.drawable.lost_focus_text_style);
        }
    }

    public void loginBtnClick(View view) {
        errorLabel.setText("");
        if(!MyUtility.isValidEmail(email.getText()))
        {
            errorLabel.setText("email address can't be emty or not valid format");
            return;
        }
        if(TextUtils.isEmpty(pass.getText()))
        {
            errorLabel.setText("password can't be emty");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginScreen.this, "Account Login successful", Toast.LENGTH_LONG).show();
                            DocumentReference docRef = db.collection("users").document(mAuth.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String fName=document.get("first_name").toString();
                                            String lName=document.get("last_name").toString();
                                            String emailString=document.get("email").toString();

                                            MyUtility.currentUser=new UserProfile(fName,lName,emailString);
                                        }
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    startActivity(new Intent(LoginScreen.this,MainActivity.class));
                                }
                            });
                        } else {
                            Toast.makeText(LoginScreen.this, "Account Login error", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Map<String, Object> user = new HashMap<>();
            user.put("first_name", account.getGivenName());
            user.put("last_name", account.getFamilyName());
            user.put("email", account.getEmail());

            uploadDataIntoFireStore(user,account.getId());

        } catch (ApiException e) {
            Toast.makeText(LoginScreen.this,"e: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void gotoMainActivity(String fName,String lName,String email)
    {
        MyUtility.currentUser=new UserProfile(fName,lName,email);
        startActivity(new Intent(this,MainActivity.class));
    }

    public void loginWithGoogle(View view) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null)
        {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else
        {
            Map<String, Object> user = new HashMap<>();
            user.put("first_name", account.getGivenName());
            user.put("last_name", account.getFamilyName());
            user.put("email", account.getEmail());

            uploadDataIntoFireStore(user,account.getId());
        }
    }

    private void uploadDataIntoFireStore(final Map<String,Object> data, String key)
    {
        db.collection("users").document(key).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                Toast.makeText(LoginScreen.this, "Successfully added into database ", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                MyUtility.currentUser=new UserProfile(data.get("first_name").toString(),data.get("last_name").toString(),data.get("email").toString());
                startActivity(new Intent(LoginScreen.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginScreen.this, "Error uploading", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public void loginWithFB(View view) {

    }
}