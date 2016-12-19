package com.example.rodri.cashbucket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;

/**
 * Created by rodri on 12/15/2016.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText etPersonalName;
    private EditText etUsername;
    private EditText etPassword;
    private Button btConfirm;
    private MyDataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dataSource = new MyDataSource(this);
        iniViews();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, username, password = "";
                name = etPersonalName.getText().toString();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.toast_name_field_empty, Toast.LENGTH_SHORT).show();
                } else if (username.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.toast_username_field_empty, Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.toast_password_field_empty, Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(SignUpActivity.this, CreateBankAccount.class);
                    startActivity(i);

                    /**
                    try {
                        dataSource.open();

                        long userId = dataSource.createUser(name, username, password);

                        if (userId != 0) {
                            Toast.makeText(SignUpActivity.this, R.string.toast_account_created_successfully, Toast.LENGTH_SHORT)
                                    .show();
                            dataSource.close();
                            Intent i = new Intent(SignUpActivity.this, CreateBankAccount.class);
                            i.putExtra("userId", userId);
                            startActivity(i);
                        } else {
                            Toast.makeText(SignUpActivity.this, R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        dataSource.close();
                    }*/

                }
            }
        });
    }

    private void iniViews() {
        etPersonalName = (EditText) findViewById(R.id.activitySignup_etName);
        etUsername = (EditText) findViewById(R.id.activitySignup_etUsername);
        etPassword = (EditText) findViewById(R.id.activitySignup_etPassword);
        btConfirm = (Button) findViewById(R.id.activitySignup_btConfirm);
    }
}
