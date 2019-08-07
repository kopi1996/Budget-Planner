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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText email;
    private FirebaseAuth mAuth;
    private EditText pass;
    private TextView errorLabel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        mAuth = FirebaseAuth.getInstance();

        errorLabel=findViewById(R.id.errorLabel);

        email = findViewById(R.id.username);
        email.setOnFocusChangeListener(this);

        progressBar=findViewById(R.id.loading);

        pass = findViewById(R.id.password);
        pass.setOnFocusChangeListener(this);
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
                        } else {
                            Toast.makeText(LoginScreen.this, "Account Login error", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
    }
}