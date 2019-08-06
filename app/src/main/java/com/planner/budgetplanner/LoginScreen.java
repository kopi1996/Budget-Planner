package com.planner.budgetplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginScreen extends AppCompatActivity {

    int txtInfoCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        final EditText email=findViewById(R.id.username);
        txtInfoCol=email.getHintTextColors().getDefaultColor();
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    v.setBackgroundResource(R.drawable.focus_text_style);
                    email.setHintTextColor(getResources().getColor(R.color.txtFocus));
                }
                else
                {
                    v.setBackgroundResource(R.drawable.lost_focus_text_style);
                    email.setHintTextColor(txtInfoCol);
                }
            }
        });


        final EditText pass=findViewById(R.id.password);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    v.setBackgroundResource(R.drawable.focus_text_style);
                    pass.setHintTextColor(getResources().getColor(R.color.txtFocus));
                }
                else
                {
                    v.setBackgroundResource(R.drawable.lost_focus_text_style);
                    pass.setHintTextColor(txtInfoCol);
                }
            }

        });
    }


    public void signUpBtnClick(View view) {
        Intent intent=new Intent(this,SignupActivity.class);
        startActivity(intent);
    }
}
