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
import com.example.rodri.cashbucket.model.Login;

/**
 * Created by rodri on 12/15/2016.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btSignUp;
    private Button btLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

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
                    Toast.makeText(LoginActivity.this, R.string.toast_username_field_empty, Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.toast_password_field_empty, Toast.LENGTH_SHORT).show();
                } else {
                    Login.getInstance().login(username, password, getApplicationContext());
                    if (Login.getInstance().isConnected()) {
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
    }

}
