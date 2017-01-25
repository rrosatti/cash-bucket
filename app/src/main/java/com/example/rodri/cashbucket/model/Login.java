package com.example.rodri.cashbucket.model;

import android.app.Activity;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.util.Util;

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
    private Activity activity;
    private Util util = new Util();

    private Login() {
        username = "";
        password = "";
        userId = 0;
        activity = null;
        isConnected = false;
    }

    public static Login getInstance() {
        if (inst == null) {
            inst = new Login();
        }
        return inst;
    }

    public boolean login(String username, String password, Activity activity) {
        this.username = username;
        this.password = password;
        this.activity = activity;
        dataSource = new MyDataSource(activity);
        try {
            dataSource.open();

            user = dataSource.getUser(username, password);

            if (user == null) {
                String message = activity.getString(R.string.toast_login_failure);
                util.showRedThemeToast(this.activity, message);
                return false;
            } else {
                String message = activity.getString(R.string.toast_login_success);
                util.showGreenThemeToastWithImage(activity, message, R.drawable.money_bag);
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
