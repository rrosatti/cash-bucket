package com.example.rodri.cashbucket.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.example.rodri.cashbucket.model.User;

/**
 * Created by rodri on 1/14/2017.
 */

public class ManageAccountActivity extends AppCompatActivity {

    private CheckBox checkAutoLogin;
    private EditText etName;
    private EditText etUsername;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private User user;
    private boolean currentChecked;
    private boolean newChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        iniViews();
        dataSource = new MyDataSource(this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageAccountActivity.this);

                builder.setMessage(R.string.dialog_confirm_changes);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // check whether the auto login was activated or not
                        if (currentChecked != newChecked) {
                            // check if the EditTexts are empty
                            String sName = etName.getText().toString();
                            String sUsername = etUsername.getText().toString();
                            if (sName.isEmpty()) {
                                Toast.makeText(ManageAccountActivity.this, R.string.toast_name_field_empty,
                                        Toast.LENGTH_SHORT).show();
                            } else if (sUsername.isEmpty()) {
                                Toast.makeText(ManageAccountActivity.this, R.string.toast_username_field_empty,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // apply the changes
                                user.setName(sName);
                                user.setUsername(sUsername);
                                try {
                                    dataSource.open();

                                    int active = 0;
                                    if (newChecked) {
                                        active = 1;
                                    }

                                    boolean autoLoginUpdated = dataSource.updateAutoLogin(user.getId(), active);
                                    boolean userUpdated = dataSource.updateUser(user);

                                    dataSource.close();
                                    
                                    if (autoLoginUpdated && userUpdated) {
                                        Toast.makeText(ManageAccountActivity.this, R.string.toast_changes_successful,
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ManageAccountActivity.this, R.string.toast_something_went_wrong,
                                                Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dataSource.close();
                                }
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

                builder.show();
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
}
