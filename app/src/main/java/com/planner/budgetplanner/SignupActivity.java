package com.planner.budgetplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private EditText email;
    private EditText pass;
    private EditText firstName;
    private EditText lastName;
    private TextView errorLabel;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.loading);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        errorLabel = findViewById(R.id.errorLabel);
    }


    public void loginBtnClick(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    public void signUpBtnClick(View view) {
        errorLabel.setText("");
        if (TextUtils.isEmpty(firstName.getText())) {
            errorLabel.setText("first name can't be empty");
            return;
        }
        if (TextUtils.isEmpty(lastName.getText())) {
            errorLabel.setText("last name can't be empty");
            return;
        }
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

        FirebaseManager.getAuth().createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Account create successful", Toast.LENGTH_LONG).show();
                            String fName = String.valueOf(firstName.getText());
                            String lName = String.valueOf(lastName.getText());
                            String emailTxt = String.valueOf(email.getText().toString());

                            FirebaseUser fUser = FirebaseManager.getAuth().getCurrentUser();
                            MyUtility.currentUser = new User(fUser.getUid(), fName + " " + lName, emailTxt, FirebaseManager.LoginType.Email);
                            FirebaseManager.addUserIntoDB(fUser.getUid(), MyUtility.currentUser, new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean isSuccess) {

                                    if (isSuccess) {
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            });
                        }
                    }
                });
    }
}