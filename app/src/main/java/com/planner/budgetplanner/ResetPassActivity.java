package com.planner.budgetplanner;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.planner.budgetplanner.Managers.AuthenticationManager;
import com.planner.budgetplanner.Utility.MyUtility;

public class ResetPassActivity extends LoadingActivity implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private static final String TAG = "ResetPassActivity";
    private TextInputEditText resetEmail;
    private TextInputLayout resetEmailPar;
    private TextView continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        resetEmailPar=findViewById(R.id.resetEmailPar);
        resetEmail=findViewById(R.id.resetEmail);
        continueBtn=findViewById(R.id.continuePassBtn);
        continueBtn.setOnClickListener(this);
        resetEmail.addTextChangedListener(this);
        resetEmail.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.continuePassBtn)
        {
            if(!MyUtility.isValidEmail(resetEmail.getText()))
            {
                resetEmailPar.setError("Invalid email.");
                if(getCurrentFocus()!=null)
                {
                    getCurrentFocus().clearFocus();
                    MyUtility.hideKeyboardFrom(this,getCurrentFocus());
                }
            }
            else
            {
                MyUtility.enableLoading(this);
                AuthenticationManager.forgotPassword(resetEmail.getText().toString(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "onComplete password: "+task.isSuccessful());
                        MyUtility.disableLoading(ResetPassActivity.this);
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(ResetPassActivity.this,"Account does't exist or no network",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            alertView("Rset password link successfully send check your email");
                        }
                    }
                });
            }
        }
    }

    private void alertView( String message ) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Reset Password")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        finish();
                    }
                }).show();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if(!hasFocus)
            return;
        switch (id) {
            case R.id.resetEmail:
                resetEmailPar.setError(null);
                break;
        }
    }
}
