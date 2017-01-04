package com.example.rodri.cashbucket.model;

import android.content.Context;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;

/**
 * Created by rodri on 1/2/2017.
 */

public class Login {

    private static Login inst = null;

    private String username;
    private String password;
    private User user;
    private long userId;
    private boolean isConnected;
    private MyDataSource dataSource;
    private Context context;


    private Login() {
        username = "";
        password = "";
        userId = 0;
        context = null;
        isConnected = false;
    }

    public static Login getInstance() {
        if (inst == null) {
            inst = new Login();
        }
        return inst;
    }

    public boolean login(String username, String password, Context context) {
        this.username = username;
        this.password = password;
        this.context = context;
        dataSource = new MyDataSource(context);
        try {
            dataSource.open();

            user = dataSource.getUser(username, password);

            if (user == null) {
                Toast.makeText(context, R.string.toast_login_failure, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(context, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                isConnected = true;
                userId = user.getId();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
            return false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public long getUserId() {
        return userId;
    }

}
