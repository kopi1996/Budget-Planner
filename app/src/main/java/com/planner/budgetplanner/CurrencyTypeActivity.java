package com.planner.budgetplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.HashMap;
import java.util.Map;

public class CurrencyTypeActivity extends LoadingActivity {

    private Spinner currencyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_type);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        currencyType = findViewById(R.id.currencyTypeSpin);
        currencyType.setSelection(18);
        findViewById(R.id.continueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("currencyType", currencyType.getSelectedItem().toString());
                MyUtility.enableLoading(CurrencyTypeActivity.this);
                FirebaseManager.updateUserIntoDB(MyUtility.currentUser.getId(), map, new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            MyUtility.currentUser.setCurrencyType(currencyType.getSelectedItem().toString());
                            goMainActivity();
                        }
                        else
                            Toast.makeText(CurrencyTypeActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        MyUtility.disableLoading(CurrencyTypeActivity.this);
                    }
                });
            }
        });
    }

    private void goMainActivity()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
