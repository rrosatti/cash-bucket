package com.example.rodri.cashbucket.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.AutoDeposit;
import com.example.rodri.cashbucket.model.BankAccount;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 1/10/2017.
 */

public class ManageBankAccountActivity extends AppCompatActivity {

    private EditText etBalance;
    private CheckBox checkAutoDeposit;
    private EditText etAutoDepositValue;
    private EditText etDay;
    private LinearLayout containerValues;
    private LinearLayout containerTitles;
    private Button btConfirm;
    private Button btCancel;
    private MyDataSource dataSource;
    private BankAccount bankAccount;
    private AutoDeposit autoDeposit;
    private boolean oldChecked = false;
    private boolean newChecked = false;
    private Util util = new Util();

    // Custom Dialog Views
    private TextView txtMessage;
    private Button btYes;
    private Button btDialogCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bank_account);

        iniViews();
        dataSource = new MyDataSource(this);

        getDataFromDatabase();

        checkAutoDeposit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    containerTitles.setVisibility(View.VISIBLE);
                    containerValues.setVisibility(View.VISIBLE);
                    newChecked = isChecked;
                } else {
                    if (oldChecked) {

                        showAutoDepositDialog();

                    } else {
                        containerTitles.setVisibility(View.GONE);
                        containerValues.setVisibility(View.GONE);
                        newChecked = false;
                    }
                }
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmChangesDialog();
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
        etBalance = (EditText) findViewById(R.id.activityMBA_etBalance);
        checkAutoDeposit = (CheckBox) findViewById(R.id.activityMBA_checkAutoDeposit);
        etAutoDepositValue = (EditText) findViewById(R.id.activityMBA_etAutoDepositValue);
        containerValues = (LinearLayout) findViewById(R.id.activityMBA_containerValues);
        containerTitles = (LinearLayout) findViewById(R.id.activityMBA_containerTitles);
        etDay = (EditText) findViewById(R.id.activityMBA_etDay);
        btConfirm = (Button) findViewById(R.id.activityMBA_btConfirm);
        btCancel = (Button) findViewById(R.id.activityMBA_btCancel);
    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            long userId = Login.getInstance().getUserId();
            bankAccount = dataSource.getBankAccount(userId);
            autoDeposit = dataSource.getAutoDeposit(bankAccount.getId());

            if (bankAccount != null) {
                fillBankData();
            }
            if (autoDeposit != null) {
                if (autoDeposit.isActive()) {
                    fillAutoDepositData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void fillBankData() {
        String sBalance = util.formatNumberWithoutCurrency(bankAccount.getBalance());
        etBalance.setText(sBalance);
    }

    private void fillAutoDepositData() {
        oldChecked = true;
        checkAutoDeposit.setChecked(true);

        containerTitles.setVisibility(View.VISIBLE);
        containerValues.setVisibility(View.VISIBLE);

        String sAutoDepositValue = util.formatNumberWithoutCurrency(autoDeposit.getValue());
        etAutoDepositValue.setText(sAutoDepositValue);
        etDay.setText(String.valueOf(autoDeposit.getDay()));
    }

    private void deactivateAutoDeposit() {
        try {
            dataSource.open();

            boolean updated = dataSource.updateAutoDeposit(autoDeposit);

            if (updated) {
                containerTitles.setVisibility(View.GONE);
                containerValues.setVisibility(View.GONE);
                etAutoDepositValue.setText("");
                etDay.setText("");
                //oldChecked = false;
                autoDeposit.setActive(false);
            } else {
                String message = getString(R.string.toast_something_went_wrong);
                util.showRedThemeToast(ManageBankAccountActivity.this, message);
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }

    private void showAutoDepositDialog() {
        // old Dialog
        /**
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageBankAccountActivity.this);
        builder.setMessage(R.string.dialog_deactivate_auto_deposit);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deactivateAutoDeposit();
            }
        });
        builder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show(); */

        // Custom Dialog
        final Dialog dialog = util.createCustomDialog(this, R.layout.custom_dialog);

        txtMessage = (TextView) dialog.findViewById(R.id.customDialog_txtMessage);
        btYes = (Button) dialog.findViewById(R.id.customDialog_btYes);
        btDialogCancel = (Button) dialog.findViewById(R.id.customDialog_btDialogCancel);

        txtMessage.setText(R.string.dialog_deactivate_auto_deposit);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                deactivateAutoDeposit();
            }
        });
        btDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                newChecked = true;
                checkAutoDeposit.setChecked(true);
            }
        });

        dialog.show();

    }

    public void showConfirmChangesDialog() {
        final double bankBalance, autoDepositValue;
        final int day;

        if (!util.isTextViewEmpty(etBalance)) {
            String sBankBalance = etBalance.getText().toString();
            bankBalance = Double.valueOf(sBankBalance);
        } else {
            bankBalance = 0;
        }

        if (!util.isTextViewEmpty(etAutoDepositValue)) {
            String sAutoDeposit = etAutoDepositValue.getText().toString();
            autoDepositValue = Double.valueOf(sAutoDeposit);
        } else {
            autoDepositValue = 0;
        }

        if (!util.isTextViewEmpty(etDay)) {
            String sDay = etDay.getText().toString();
            day = Integer.parseInt(sDay);
        } else {
            day = 1;
        }

        // Old Dialog
        /**
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageBankAccountActivity.this);
        builder.setMessage(R.string.dialog_confirm_changes);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dataSource.open();

                    long userId = Login.getInstance().getUserId();
                    long newBankAccountId = 0; // it will be assign a value, only if need to create a new bank account

                    if (bankAccount != null) {
                        dataSource.updateBankAccount(bankAccount.getId(), bankBalance);
                    } else {
                        newBankAccountId = dataSource.createBankAccount(bankBalance);
                        dataSource.createUserBankAccount(userId, newBankAccountId);
                    }

                    if (autoDeposit != null) {
                        if (autoDepositValue > 0) {
                            autoDeposit.setValue(autoDepositValue);
                            autoDeposit.setDay(day);
                            autoDeposit.setActive(true);
                        } else {
                            autoDeposit.setActive(false);
                        }
                        dataSource.updateAutoDeposit(autoDeposit);

                    } else {
                        if (newBankAccountId != 0) {
                            dataSource.createAutoDeposit(newBankAccountId, autoDepositValue, day);
                        } else {
                            autoDeposit.setValue(autoDepositValue);
                            autoDeposit.setDay(day);
                            autoDeposit.setActive(true);
                            dataSource.updateAutoDeposit(autoDeposit);
                        }

                    }
                    dataSource.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    dataSource.close();
                }

                // kill activity
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
                applyChanges(bankBalance, autoDepositValue, day);
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

    private void applyChanges(double bankBalance, double autoDepositValue, int day) {
        try {
            dataSource.open();

            long userId = Login.getInstance().getUserId();
            long newBankAccountId = 0; // it will be assign a value, only if need to create a new bank account

            if (bankAccount != null) {
                dataSource.updateBankAccount(bankAccount.getId(), bankBalance);
            } else {
                newBankAccountId = dataSource.createBankAccount(bankBalance);
                dataSource.createUserBankAccount(userId, newBankAccountId);
            }

            if (autoDeposit != null) {
                if (autoDepositValue > 0) {
                    autoDeposit.setValue(autoDepositValue);
                    autoDeposit.setDay(day);
                    autoDeposit.setActive(true);
                } else {
                    autoDeposit.setActive(false);
                }
                dataSource.updateAutoDeposit(autoDeposit);

            } else {
                if (newBankAccountId != 0) {
                    dataSource.createAutoDeposit(newBankAccountId, autoDepositValue, day);
                } else {
                    autoDeposit.setValue(autoDepositValue);
                    autoDeposit.setDay(day);
                    autoDeposit.setActive(true);
                    dataSource.updateAutoDeposit(autoDeposit);
                }

            }
            dataSource.close();

        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }

        // kill activity
        finish();
    }
}
