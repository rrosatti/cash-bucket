package com.example.rodri.cashbucket.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.model.Wallet;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 1/13/2017.
 */

public class ManageWalletActivity extends AppCompatActivity {

    private EditText etBalance;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private Wallet wallet;
    private boolean walletExists = false;
    private double currentBalance;
    private Util util = new Util();

    // Custom Dialog
    private TextView txtMessage;
    private Button btYes;
    private Button btDialogCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wallet);

        iniViews();
        dataSource = new MyDataSource(this);

        getDataFromDatabase();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

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
        etBalance = (EditText) findViewById(R.id.activityMW_etBalance);
        btConfirm = (Button) findViewById(R.id.activityMW_btConfirm);
        btCancel = (Button) findViewById(R.id.activityMW_btCancel);
    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            wallet = dataSource.getWallet(Login.getInstance().getUserId());

            if (wallet != null) {
                walletExists = true;
                currentBalance = wallet.getBalance();
                fillWalletData();
            } else {
                currentBalance = 0.00;
                etBalance.setText(String.valueOf(0.00));
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void fillWalletData() {
        String sBalance = util.formatNumberWithoutCurrency(wallet.getBalance());
        etBalance.setText(sBalance);
    }

    private void showDialog() {
        // Old Dialog
        /**
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageWalletActivity.this);

        builder.setMessage(R.string.dialog_confirm_changes);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // check whether the balance value changed or not
                String sBalance = etBalance.getText().toString();
                double newBalance = Double.valueOf(sBalance);

                if (newBalance == currentBalance) {
                    String message = getString(R.string.toast_no_changes_were_made);
                    util.showRedThemeToast(ManageWalletActivity.this, message);
                    dialog.cancel();
                } else {

                    try {
                        dataSource.open();

                        if (walletExists) {
                            dataSource.updateWallet(wallet.getId(), newBalance);
                        } else {
                            long walletId = dataSource.createWallet(newBalance);
                            dataSource.createUserWallet(Login.getInstance().getUserId(), walletId);
                        }

                        dataSource.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        dataSource.close();
                    }

                }
                finish();

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
        final Dialog dialog = util.createCustomDialog(this, R.layout.custom_dialog);

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
        // check whether the balance value changed or not
        String sBalance = etBalance.getText().toString();
        double newBalance = Double.valueOf(sBalance);

        if (newBalance == currentBalance) {
            String message = getString(R.string.toast_no_changes_were_made);
            util.showRedThemeToast(ManageWalletActivity.this, message);
        } else {

            try {
                dataSource.open();

                if (walletExists) {
                    dataSource.updateWallet(wallet.getId(), newBalance);
                } else {
                    long walletId = dataSource.createWallet(newBalance);
                    dataSource.createUserWallet(Login.getInstance().getUserId(), walletId);
                }

                dataSource.close();
            } catch (Exception e) {
                e.printStackTrace();
                dataSource.close();
            }

        }
        finish();
    }
}
