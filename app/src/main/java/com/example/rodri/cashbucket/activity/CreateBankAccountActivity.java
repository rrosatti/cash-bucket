package com.example.rodri.cashbucket.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 12/19/2016.
 */

public class CreateBankAccountActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "com.example.rodri.cashbucket";
    private static final String NOTIFY = "com.example.rodri.cashbucket.notify";

    private EditText etBalance;
    private EditText etValue;
    private EditText etDay;
    private CheckBox checkAutoDeposit;
    private Button btConfirm;
    private MyDataSource dataSource;
    private boolean checked = false;
    private long userId = -1;
    private SharedPreferences sharedPreferences;
    private Util util = new Util();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bank_account);

        dataSource = new MyDataSource(this);
        iniViews();
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        try {
            userId = getIntent().getExtras().getLong("userId");

        } catch (NullPointerException e) {
            e.printStackTrace();
            String message = getString(R.string.toast_no_user_id);
            util.showRedThemeToast(CreateBankAccountActivity.this, message);
        }

        checkAutoDeposit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                if (isChecked) {
                    etValue.setVisibility(View.VISIBLE);
                    etDay.setVisibility(View.VISIBLE);
                } else {
                    etValue.setVisibility(View.GONE);
                    etDay.setVisibility(View.GONE);
                }
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sBalance, sValue, sDay = "";
                double balance, value = 0.0;
                int day = 0;
                sBalance = etBalance.getText().toString();
                sValue = etValue.getText().toString();
                sDay = etDay.getText().toString();
                balance = Double.parseDouble(sBalance);
                value = Double.parseDouble(sValue);
                day = Integer.parseInt(sDay);

                if (sBalance.isEmpty()) {
                    String message = getString(R.string.toast_balance_field_empty);
                    util.showRedThemeToast(CreateBankAccountActivity.this, message);
                } else {
                    if (checked) {
                        if (sValue.isEmpty()) {
                            String message = getString(R.string.toast_value_field_empty);
                            util.showRedThemeToast(CreateBankAccountActivity.this, message);
                        } else if (sDay.isEmpty()) {
                            String message = getString(R.string.toast_day_field_empty);
                            util.showRedThemeToast(CreateBankAccountActivity.this, message);
                        } else {

                            // create bank account and auto deposit
                            try {
                                dataSource.open();

                                long bankAccountId = dataSource.createBankAccount(balance);
                                if (bankAccountId != 0) {
                                    boolean userBankAccount = dataSource.createUserBankAccount(userId, bankAccountId);
                                    if (userBankAccount) {
                                        long autoDepositId = dataSource.createAutoDeposit(bankAccountId, value, day);
                                        if (autoDepositId != 0) {
                                            // set auto deposit notification as default
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(NOTIFY, true);
                                            editor.apply();

                                            String message = getString(R.string.toast_bank_account_created_successfully);
                                            util.showGreenThemeToast(CreateBankAccountActivity.this, message);

                                            Intent i = new Intent(CreateBankAccountActivity.this, CreateWalletActivity.class);
                                            i.putExtra("userId", userId);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            String message = getString(R.string.toast_something_went_wrong);
                                            util.showRedThemeToast(CreateBankAccountActivity.this, message);
                                        }
                                    } else {
                                        String message = getString(R.string.toast_something_went_wrong);
                                        util.showRedThemeToast(CreateBankAccountActivity.this, message);
                                    }
                                } else {
                                    String message = getString(R.string.toast_something_went_wrong);
                                    util.showRedThemeToast(CreateBankAccountActivity.this, message);
                                }
                                dataSource.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                dataSource.close();
                            }

                        }
                    } else {

                        // create only bank account
                        try {
                            dataSource.open();

                            long bankAccountId = dataSource.createBankAccount(balance);
                            if (bankAccountId != 0) {
                                boolean created = dataSource.createUserBankAccount(userId, bankAccountId);
                                if (created) {
                                    String message = getString(R.string.toast_bank_account_created_successfully);
                                    util.showGreenThemeToast(CreateBankAccountActivity.this, message);
                                    finish();
                                } else {
                                    String message = getString(R.string.toast_something_went_wrong);
                                    util.showRedThemeToast(CreateBankAccountActivity.this, message);
                                }
                            } else {
                                String message = getString(R.string.toast_something_went_wrong);
                                util.showRedThemeToast(CreateBankAccountActivity.this, message);
                            }
                            dataSource.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                            dataSource.close();
                        }
                    }
                }

            }
        });
    }

    private void iniViews() {
        etBalance = (EditText) findViewById(R.id.activityCBAccount_etBalance);
        etValue = (EditText) findViewById(R.id.activityCBAccount_etValue);
        etDay = (EditText) findViewById(R.id.activityCBAccount_etDay);
        checkAutoDeposit = (CheckBox) findViewById(R.id.activityCBAccount_checkAutoDeposit);
        btConfirm = (Button) findViewById(R.id.activityCBAccount_btConfirm);
    }


}
