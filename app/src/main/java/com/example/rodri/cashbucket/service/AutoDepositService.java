package com.example.rodri.cashbucket.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.AutoDeposit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rodri on 1/18/2017.
 */

public class AutoDepositService extends Service {

    private MyDataSource dataSource;
    private List<AutoDeposit> autoDeposits = new ArrayList<>();

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "onDestroy() was called!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try {
            dataSource = new MyDataSource(getApplicationContext());
            dataSource.open();

            autoDeposits = dataSource.getAllActiveAutoDeposits();

            Calendar cal = Calendar.getInstance();
            int currDay = cal.get(Calendar.DAY_OF_MONTH);

            if (autoDeposits != null) {
                for (AutoDeposit ap : autoDeposits) {
                    if (ap.getDay() == currDay) {

                        // check if the current deposit was already deposited or not.
                        // if the user turns his/her phone on and off many times a day,
                        // then it could replicate the deposit
                        long userId = dataSource.getUserId(ap.getBankAccountId());
                        boolean deposited = dataSource.deposit(userId, ap.getValue());

                        if (deposited) {
                            // send notification
                        }
                    }
                }
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }
}
