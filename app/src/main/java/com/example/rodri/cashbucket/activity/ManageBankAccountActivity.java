package com.example.rodri.cashbucket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.AutoDeposit;
import com.example.rodri.cashbucket.model.BankAccount;
import com.example.rodri.cashbucket.model.Login;

/**
 * Created by rodri on 1/10/2017.
 */

public class ManageBankAccountActivity extends AppCompatActivity {

    private EditText etBalance;
    private CheckBox checkAutoDeposit;
    private EditText etAutoDepositValue;
    private EditText etDay;
    private LinearLayout containerValues;
    private LinearLayout containerTitles;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private BankAccount bankAccount;
    private AutoDeposit autoDeposit;
    private boolean checked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bank_account);

        iniViews();
        dataSource = new MyDataSource(this);

        getDataFromDatabase();



    }

    private void iniViews() {
        etBalance = (EditText) findViewById(R.id.activityMBA_etBalance);
        checkAutoDeposit = (CheckBox) findViewById(R.id.activityMBA_checkAutoDeposit);
        etAutoDepositValue = (EditText) findViewById(R.id.activityMBA_etAutoDepositValue);
        containerValues = (LinearLayout) findViewById(R.id.activityMBA_containerValues);
        containerTitles = (LinearLayout) findViewById(R.id.activityMBA_containerTitles);
        etDay = (EditText) findViewById(R.id.activityMBA_etDay);
        btConfirm = (Button) findViewById(R.id.activityMBA_btConfirm);
        btCancel = (Button) findViewById(R.id.activityMBA_btCancel);
    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            long userId = Login.getInstance().getUserId();
            bankAccount = dataSource.getBankAccount(userId);
            autoDeposit = dataSource.getAutoDeposit(bankAccount.getId());
            System.out.println("I've been here!");
            if (bankAccount != null) {
                fillBankData();
            }
            if (autoDeposit != null) {
                fillAutoDepositData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void fillBankData() {
        etBalance.setText(String.valueOf(bankAccount.getBalance()));
    }

    private void fillAutoDepositData() {
        checked = true;
        checkAutoDeposit.setChecked(true);

        containerTitles.setVisibility(View.VISIBLE);
        containerValues.setVisibility(View.VISIBLE);

        etAutoDepositValue.setText(String.valueOf(autoDeposit.getValue()));
        etDay.setText(String.valueOf(autoDeposit.getDay()));
    }
}
