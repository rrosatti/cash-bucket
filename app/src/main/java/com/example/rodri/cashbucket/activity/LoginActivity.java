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
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 12/15/2016.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "com.example.rodri.cashbucket";
    private static final String AUTO_LOGIN = "com.example.rodri.cashbucket.autologin";
    private static final String USER_ID = "com.example.rodri.cashbucket.userid";

    private EditText etUsername;
    private EditText etPassword;
    private Button btSignUp;
    private Button btLogin;
    private CheckBox checkRememberMe;
    private boolean checked = false;
    private MyDataSource dataSource;
    private SharedPreferences sharedPreferences;
    private Util util = new Util();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        dataSource = new MyDataSource(this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        checkRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password = "";
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                if (username.isEmpty()) {
                    String message = getString(R.string.toast_username_field_empty);
                    util.showRedThemeToast(LoginActivity.this, message);
                } else if (password.isEmpty()) {
                    String message = getString(R.string.toast_password_field_empty);
                    util.showRedThemeToast(LoginActivity.this, message);
                } else {
                    Login.getInstance().login(username, password, LoginActivity.this);
                    if (Login.getInstance().isConnected()) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // check if the user turned the auto login on
                        if (checked) {
                            try {
                                dataSource.open();

                                dataSource.updateAutoLogin(Login.getInstance().getUserId(), 1);
                                // save in Shared Preferences
                                editor.putBoolean(AUTO_LOGIN, true);

                                dataSource.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                dataSource.close();
                            }
                        }

                        editor.putLong(USER_ID, Login.getInstance().getUserId());
                        editor.apply();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }

    private void initViews(){
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btSignUp = (Button) findViewById(R.id.btSignup);
        btLogin = (Button) findViewById(R.id.btLogin);
        checkRememberMe = (CheckBox) findViewById(R.id.activityLogin_checkRememberMe);
    }

}
