package com.example.rodri.cashbucket.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.rodri.cashbucket.service.AutoDepositService;

/**
 * Created by rodri on 1/18/2017.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION)) {
            // start the service
            //Toast.makeText(context, "Something", Toast.LENGTH_SHORT).show();
            System.out.println("I've been here (BroadcastReceiver)");

            Intent serviceIntent = new Intent(context, AutoDepositService.class);
            context.startService(serviceIntent);
        }
    }
}
