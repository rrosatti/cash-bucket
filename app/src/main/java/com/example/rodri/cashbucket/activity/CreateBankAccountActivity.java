package com.example.rodri.cashbucket.activity;

import android.content.Intent;
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

/**
 * Created by rodri on 12/19/2016.
 */

public class CreateBankAccountActivity extends AppCompatActivity {

    private EditText etBalance;
    private EditText etValue;
    private EditText etDay;
    private CheckBox checkAutoDeposit;
    private Button btConfirm;
    private MyDataSource dataSource;
    private boolean checked = false;
    private long userId = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bank_account);

        dataSource = new MyDataSource(this);
        iniViews();

        try {
            userId = getIntent().getExtras().getLong("userId");

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(CreateBankAccountActivity.this, R.string.toast_no_user_id, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateBankAccountActivity.this, R.string.toast_balance_field_empty, Toast.LENGTH_SHORT).show();
                } else {
                    if (checked) {
                        if (sValue.isEmpty()) {
                            Toast.makeText(CreateBankAccountActivity.this, R.string.toast_value_field_empty, Toast.LENGTH_SHORT).show();
                        } else if (sDay.isEmpty()) {
                            Toast.makeText(CreateBankAccountActivity.this, R.string.toast_day_field_empty, Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CreateBankAccountActivity.this,
                                                    R.string.toast_bank_account_created_successfully, Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(CreateBankAccountActivity.this, CreateWalletActivity.class);
                                            i.putExtra("userId", userId);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(CreateBankAccountActivity.this,
                                                    R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(CreateBankAccountActivity.this,
                                                R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(CreateBankAccountActivity.this,
                                            R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CreateBankAccountActivity.this,
                                            R.string.toast_bank_account_created_successfully, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(CreateBankAccountActivity.this,
                                            R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CreateBankAccountActivity.this,
                                        R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
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
