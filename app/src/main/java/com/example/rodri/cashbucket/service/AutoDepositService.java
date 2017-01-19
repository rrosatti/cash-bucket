package com.example.rodri.cashbucket.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.activity.SplashScreenActivity;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.AutoDeposit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rodri on 1/18/2017.
 */

public class AutoDepositService extends Service {

    private static final String MY_PREFERENCES = "com.example.rodri.cashbucket";
    private static final String USER_ID = "com.example.rodri.cashbucket.userid";
    private static final String NOTIFY = "com.example.rodri.cashbucket.notify";

    private MyDataSource dataSource;
    private List<AutoDeposit> autoDeposits = new ArrayList<>();
    private SharedPreferences sharedPreferences;

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

            sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

            autoDeposits = dataSource.getAllActiveAutoDeposits();

            Calendar cal = Calendar.getInstance();
            int currDay = cal.get(Calendar.DAY_OF_MONTH);
            int currMonth = cal.get(Calendar.MONTH) + 1;

            if (autoDeposits != null) {
                for (AutoDeposit ap : autoDeposits) {
                    if (ap.getDay() == currDay) {
                        // check if the current deposit was already deposited or not.
                        // if the user turns his/her phone on and off many times a day,
                        // then it could replicate the deposit
                        long dateOfLastDeposit = dataSource.getDataOfLastDeposit(ap.getId());

                        if (dateOfLastDeposit != 0) {
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTimeInMillis(dateOfLastDeposit);
                            long lastDepositDay = cal.get(Calendar.DAY_OF_MONTH);
                            long lastDepositMonth = cal.get(Calendar.MONTH) + 1;
                            if (currMonth == lastDepositMonth && currDay == lastDepositDay) {
                                return;
                            }
                        }

                        long userId = dataSource.getUserId(ap.getBankAccountId());
                        boolean deposited = dataSource.deposit(userId, ap.getValue());

                        if (deposited) {
                            // save it in the auto deposit log and send notification
                            dataSource.createAutoDepositLog(ap.getId(), ap.getValue(), cal.getTimeInMillis());

                            boolean notify = sharedPreferences.getBoolean(NOTIFY, false);
                            if (true) {
                                showNotification(ap.getValue());
                            }

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

    private void showNotification(double value) {
        NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.deposit_notification)
                .setContentTitle(getResources().getString(R.string.notification_new_deposit))
                .setContentText("R$ " + value)
                .setAutoCancel(true);

        Intent resultIntent = new Intent(this, SplashScreenActivity.class);

        // The Stack Builder ensures that navigating backwards from the Activity leads out of
        // the application to the Home Screen
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashScreenActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId, mBuilder.build());

    }

}
