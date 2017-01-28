package com.example.rodri.cashbucket.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.AutoDeposit;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 1/20/2017.
 */

public class ManageNotificationsActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "com.example.rodri.cashbucket";
    private static final String NOTIFY = "com.example.rodri.cashbucket.notify";

    private CheckBox checkAutoDepositNotification;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private AutoDeposit autoDeposit;
    private boolean checked = false;
    private SharedPreferences sharedPreferences;
    private Util util = new Util();

    // Custom Dialog
    private TextView txtMessage;
    private Button btYes;
    private Button btDialogCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_notifications);

        iniViews();
        dataSource = new MyDataSource(this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        getDataFromDatabase();

        checkAutoDepositNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked != autoDeposit.isActive()) {
                    showConfirmChangesDialog();
                }
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
        checkAutoDepositNotification = (CheckBox) findViewById(R.id.activityMN_checkAutoDepositNotification);
        btConfirm = (Button) findViewById(R.id.activityMN_btConfirm);
        btCancel = (Button) findViewById(R.id.activityMN_btCancel);
    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            long userId = Login.getInstance().getUserId();
            long bankAccountId = dataSource.getBankAccountId(userId);
            autoDeposit = dataSource.getAutoDeposit(bankAccountId);

            if (autoDeposit.isActive()) {
                checkAutoDepositNotification.setChecked(true);
                checked = true;
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void showConfirmChangesDialog() {
        // Old Dialog
        /**
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_changes);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applyChanges();
            }
        });

        builder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();*/

        // Custom Dialog
        final Dialog dialog = util.createCustomDialog(this);

        txtMessage = (TextView) dialog.findViewById(R.id.customDialog_txtMessage);
        btYes = (Button) dialog.findViewById(R.id.customDialog_btYes);
        btDialogCancel = (Button) dialog.findViewById(R.id.customDialog_btDialogCancel);

        txtMessage.setText(R.string.dialog_confirm_changes);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                applyChanges();
            }
        });
        btDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                checked = autoDeposit.isActive();
                checkAutoDepositNotification.setChecked(checked);
            }
        });

        dialog.show();

    }

    private void applyChanges() {
        try {
            dataSource.open();

            // update database
            autoDeposit.setActive(checked);
            dataSource.updateAutoDeposit(autoDeposit);

            // update shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFY, checked);
            editor.apply();

            // finish activity
            finish();

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }
}
