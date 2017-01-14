package com.example.rodri.cashbucket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.model.User;

/**
 * Created by rodri on 1/14/2017.
 */

public class ManageAccountActivity extends AppCompatActivity {

    private CheckBox checkAutoLogin;
    private EditText etName;
    private EditText etUsername;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private User user;
    private boolean currentChecked;
    private boolean newChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        iniViews();
        dataSource = new MyDataSource(this);
        getDataFromDatabase();

        checkAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newChecked = isChecked;
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void iniViews() {
        checkAutoLogin = (CheckBox) findViewById(R.id.activityMA_checkAutoLogin);
        etName = (EditText) findViewById(R.id.activityMA_etName);
        etUsername = (EditText) findViewById(R.id.activityMA_etUsername);
        btConfirm = (Button) findViewById(R.id.activityMA_btConfirm);
        btCancel = (Button) findViewById(R.id.activityMA_btCancel);
    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            user = dataSource.getUser(Login.getInstance().getUserId());
            currentChecked = dataSource.isAutoLoginActive(user.getId());

            if (user != null) {
                fillUserData();
            }

            if (currentChecked) {
                newChecked = currentChecked;
                checkAutoLogin.setChecked(true);
            } else {
                newChecked = false;
                checkAutoLogin.setChecked(false);
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void fillUserData() {
        etName.setText(user.getName());
        etUsername.setText(user.getUsername());
    }
}
