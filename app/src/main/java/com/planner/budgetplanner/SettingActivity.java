package com.planner.budgetplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.planner.budgetplanner.Managers.AuthenticationManager;
import com.planner.budgetplanner.Model.User;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends CustomAppBarActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private static final String TAG = "SettingActivity";
    private TextInputLayout fNamePar;
    private TextInputLayout lNamePar;
    private TextInputLayout currPasPar;
    private TextInputLayout newPassPar;
    private TextInputLayout conPassPar;


    private TextInputEditText firstNameTxt;
    private TextInputEditText lastNameTxt;
    private TextInputEditText currPassTxt;
    private TextInputEditText newPassTxt;
    private TextInputEditText confirmPassTxt;
    private TextView changePassBtn;
    private TextView deleteBtn;
    private View changePassBox;
    private View passCardBox;

    private boolean needPassChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initialize("Settings");
    }

    @Override
    public void initialize(String title) {
        super.initialize(title);
        enableBackBtn();

        firstNameTxt = findViewById(R.id.firstName);
        lastNameTxt = findViewById(R.id.lastName);
        currPassTxt = findViewById(R.id.currPass);
        newPassTxt = findViewById(R.id.newPass);
        confirmPassTxt = findViewById(R.id.confirmPass);

        fNamePar = findViewById(R.id.firstNamePar);
        lNamePar = findViewById(R.id.lastNamePar);
        currPasPar = findViewById(R.id.currPassPar);
        newPassPar = findViewById(R.id.newPassPar);
        conPassPar = findViewById(R.id.confirmPassPar);

        changePassBtn = findViewById(R.id.chageBtn);
        changePassBox = findViewById(R.id.passChangeBox);
        passCardBox = findViewById(R.id.passBoxCard);
        deleteBtn = findViewById(R.id.deleteAccBtn);
        deleteBtn.setOnClickListener(this);
        changePassBtn.setOnClickListener(this);

        firstNameTxt.setOnFocusChangeListener(this);
        lastNameTxt.setOnFocusChangeListener(this);
        currPassTxt.setOnFocusChangeListener(this);
        newPassTxt.setOnFocusChangeListener(this);
        confirmPassTxt.setOnFocusChangeListener(this);

        currPassTxt.addTextChangedListener(this);
        newPassTxt.addTextChangedListener(this);
        confirmPassTxt.addTextChangedListener(this);

        User currentUser = MyUtility.currentUser;
        String[] s = currentUser.getName().split(" ");
        firstNameTxt.setText(s.length > 0 ? s[0] : "");
        lastNameTxt.setText(s.length > 1 ? s[1] : "");

        if (currentUser.getType() != FirebaseManager.LoginType.Email) {
            passCardBox.setVisibility(View.INVISIBLE);
            firstNameTxt.setFocusable(false);
            firstNameTxt.setFocusableInTouchMode(false);
            lastNameTxt.setFocusable(false);
            lastNameTxt.setFocusableInTouchMode(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.saveBtn:
                if(MyUtility.currentUser.getType()== FirebaseManager.LoginType.Email)
                {
                    update();
                }
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAcc() {
        MyUtility.enableLoading(this);
        FirebaseManager.deleteAccount(MyUtility.currentUser, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                MyUtility.disableLoading(SettingActivity.this);
                if (aBoolean) {
                    MyUtility.currentUser = null;
                    startActivity(new Intent(SettingActivity.this, LoginScreen.class));
                    finish();
                } else
                    Toast.makeText(SettingActivity.this, "Problem to delete account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update() {
        if(getCurrentFocus()!=null) {
            getCurrentFocus().clearFocus();
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
        }
        if (firstNameTxt.getText().toString().isEmpty()) {
            fNamePar.setError("First name can't be empty");
            return;
        }
        if (lastNameTxt.getText().toString().isEmpty()) {
            lNamePar.setError("Last name can't be empty");
            return;
        }
        if(needPassChange)
        {
            Log.i(TAG, "update: "+needPassChange);
            passwordChange(new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    if(!aBoolean) {
                        Log.i(TAG, "onSuccess disable: "+aBoolean);
                        MyUtility.disableLoading(SettingActivity.this);
                    }
                    else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", firstNameTxt.getText().toString() + " " + lastNameTxt.getText().toString());
                        FirebaseManager.updateUserIntoDB(MyUtility.currentUser.getId(), map, new OnSuccessListener<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                MyUtility.disableLoading(SettingActivity.this);
                                if (aBoolean) {
                                    finish();
                                }
                            }
                        });
                    }
                }
            });
        }
        else
        {
            MyUtility.enableLoading(this);
            Map<String, Object> map = new HashMap<>();
            map.put("name", firstNameTxt.getText().toString() + " " + lastNameTxt.getText().toString());
            FirebaseManager.updateUserIntoDB(MyUtility.currentUser.getId(), map, new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    MyUtility.disableLoading(SettingActivity.this);
                    if (aBoolean) {
                        finish();
                    } else {
                        MyUtility.disableLoading(SettingActivity.this);
                    }
                }
            });
        }
    }

    private void passwordChange(final OnSuccessListener<Boolean> listener) {
        if (!MyUtility.passwordValidation(newPassTxt.getText().toString())) {
            newPassPar.setError("8 characters or longer. Combine upper and lowercase letters.");
            if (listener != null)
                listener.onSuccess(false);
            return;
        }
        if (!MyUtility.passwordValidation(confirmPassTxt.getText().toString())) {
            conPassPar.setError("8 characters or longer. Combine upper and lowercase letters.");
            if(listener!=null)
                listener.onSuccess(false);
            return;
        }

        if (!newPassTxt.getText().toString().equals(confirmPassTxt.getText().toString())) {
            conPassPar.setError("Password doesn't match");
            if(listener!=null)
                listener.onSuccess(false);
            return;
        }

        AuthCredential credential = EmailAuthProvider
                .getCredential(MyUtility.currentUser.getEmail(), currPassTxt.getText().toString());

        MyUtility.enableLoading(this);
        FirebaseManager.getAuth().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    if(listener!=null)
                        listener.onSuccess(task.isSuccessful());
                    currPasPar.setError("Invlid Password");
                } else {
                    AuthenticationManager.updatePassword(currPassTxt.getText().toString(),newPassTxt.getText().toString(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(listener!=null)
                                listener.onSuccess(task.isSuccessful());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.deleteAccBtn:
                deleteAcc();
                break;
            case R.id.chageBtn:
                changePassBox.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (!hasFocus)
            return;
        switch (id) {
            case R.id.firstName:
                fNamePar.setError(null);
                break;
            case R.id.lastName:
                lNamePar.setError(null);
                break;
            case R.id.currPass:
                currPasPar.setError(null);
                break;
            case R.id.newPass:
                newPassPar.setError(null);
                break;
            case R.id.confirmPass:
                conPassPar.setError(null);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!currPassTxt.getText().toString().isEmpty() || !newPassTxt.getText().toString().isEmpty() || !confirmPassTxt.getText().toString().isEmpty()) {
            needPassChange = true;
        } else
            needPassChange = false;
    }
}