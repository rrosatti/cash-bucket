package com.example.rodri.cashbucket.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.model.User;

/**
 * Created by rodri on 1/15/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "com.example.rodri.cashbucket";
    private static final String AUTO_LOGIN = "com.example.rodri.cashbucket.autologin";
    private static final String USER_ID = "com.example.rodri.cashbucket.userid";

    private MyDataSource dataSource;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        dataSource = new MyDataSource(this);

        if (sharedPreferences.getBoolean(AUTO_LOGIN, false)) {
            try {
                dataSource.open();

                long userId = sharedPreferences.getLong(USER_ID, 0);
                User user = dataSource.getUser(userId);
                Login.getInstance().login(user.getUsername(), user.getPassword(), SplashScreenActivity.this);

                if (Login.getInstance().isConnected()) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(SplashScreenActivity.this, R.string.toast_something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                dataSource.close();
            } catch (Exception e) {
                e.printStackTrace();
                dataSource.close();
            }
        } else {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
