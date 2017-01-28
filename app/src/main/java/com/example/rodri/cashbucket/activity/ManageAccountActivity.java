package com.example.rodri.cashbucket.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.model.User;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 1/14/2017.
 */

public class ManageAccountActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "com.example.rodri.cashbucket";
    private static final String AUTO_LOGIN = "com.example.rodri.cashbucket.autologin";
    private static final String USER_ID = "com.example.rodri.cashbucket.userid";

    private CheckBox checkAutoLogin;
    private EditText etName;
    private EditText etUsername;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private User user;
    private boolean currentChecked;
    private boolean newChecked;
    private SharedPreferences sharedPreferences;
    private Util util = new Util();

    // Custom Dialog Views
    private TextView txtMessage;
    private Button btYes;
    private Button btDialogCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        iniViews();
        dataSource = new MyDataSource(this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        getDataFromDatabase();

        checkAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newChecked = isChecked;
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

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
        checkAutoLogin = (CheckBox) findViewById(R.id.activityMA_checkAutoLogin);
        etName = (EditText) findViewById(R.id.activityMA_etName);
        etUsername = (EditText) findViewById(R.id.activityMA_etUsername);
        btConfirm = (Button) findViewById(R.id.activityMA_btConfirm);
        btCancel = (Button) findViewById(R.id.activityMA_btCancel);
    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            user = dataSource.getUser(Login.getInstance().getUserId());
            currentChecked = dataSource.isAutoLoginActive(user.getId());

            if (user != null) {
                fillUserData();
            }

            if (currentChecked) {
                newChecked = currentChecked;
                checkAutoLogin.setChecked(true);
            } else {
                newChecked = false;
                checkAutoLogin.setChecked(false);
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void fillUserData() {
        etName.setText(user.getName());
        etUsername.setText(user.getUsername());
    }

    private void showAlertDialog() {
        // old AlertBuilder
        /**
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageAccountActivity.this);

        builder.setMessage(R.string.dialog_confirm_changes);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // check if the EditTexts are empty
                String sName = etName.getText().toString();
                String sUsername = etUsername.getText().toString();
                if (sName.isEmpty()) {
                    String message = getString(R.string.toast_name_field_empty);
                    util.showRedThemeToast(ManageAccountActivity.this, message);
                } else if (sUsername.isEmpty()) {
                    String message = getString(R.string.toast_username_field_empty);
                    util.showRedThemeToast(ManageAccountActivity.this, message);
                } else {
                    // apply the changes
                    user.setName(sName);
                    user.setUsername(sUsername);
                    try {
                        dataSource.open();

                        // check whether the auto login was activated or not
                        if (currentChecked != newChecked) {
                            int active = 0;
                            if (newChecked) {
                                active = 1;
                            }

                            dataSource.updateAutoLogin(user.getId(), active);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(AUTO_LOGIN, newChecked);
                            editor.putLong(USER_ID, user.getId());
                            editor.apply();
                        }

                        boolean userUpdated = dataSource.updateUser(user);

                        dataSource.close();

                        if (userUpdated) {
                            String message = getString(R.string.toast_changes_successful);
                            util.showGreenThemeToast(ManageAccountActivity.this, message);
                            finish();
                        } else {
                            String message = getString(R.string.toast_something_went_wrong);
                            util.showRedThemeToast(ManageAccountActivity.this, message);
                            dialog.cancel();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        dataSource.close();
                    }
                }

            }
        });

        builder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();*/

        // custom Dialog
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
            }
        });

        dialog.show();
    }

    private void applyChanges() {
        // check if the EditTexts are empty
        String sName = etName.getText().toString();
        String sUsername = etUsername.getText().toString();
        if (sName.isEmpty()) {
            String message = getString(R.string.toast_name_field_empty);
            util.showRedThemeToast(ManageAccountActivity.this, message);
        } else if (sUsername.isEmpty()) {
            String message = getString(R.string.toast_username_field_empty);
            util.showRedThemeToast(ManageAccountActivity.this, message);
        } else {
            // apply the changes
            user.setName(sName);
            user.setUsername(sUsername);
            try {
                dataSource.open();

                // check whether the auto login was activated or not
                if (currentChecked != newChecked) {
                    int active = 0;
                    if (newChecked) {
                        active = 1;
                    }

                    dataSource.updateAutoLogin(user.getId(), active);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(AUTO_LOGIN, newChecked);
                    editor.putLong(USER_ID, user.getId());
                    editor.apply();
                }

                boolean userUpdated = dataSource.updateUser(user);

                dataSource.close();

                if (userUpdated) {
                    String message = getString(R.string.toast_changes_successful);
                    util.showGreenThemeToast(ManageAccountActivity.this, message);
                    finish();
                } else {
                    String message = getString(R.string.toast_something_went_wrong);
                    util.showRedThemeToast(ManageAccountActivity.this, message);
                }

            } catch (Exception e) {
                e.printStackTrace();
                dataSource.close();
            }
        }
    }
}
