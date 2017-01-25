package com.example.rodri.cashbucket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.util.Util;

/**
 * Created by rodri on 1/9/2017.
 */

public class CreateWalletActivity extends AppCompatActivity {

    private EditText etValue;
    private Button btConfirm;
    private MyDataSource dataSource;
    private long userId = 0;
    private Util util = new Util();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        iniViews();
        dataSource = new MyDataSource(this);
        userId = getIntent().getExtras().getLong("userId");

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sValue = etValue.getText().toString();
                if (sValue.isEmpty()) {
                    String message = getString(R.string.toast_wallet_value_empty);
                    util.showRedThemeToast(CreateWalletActivity.this, message);
                    return;
                } else {
                    try {
                        dataSource.open();

                        double walletValue = Double.valueOf(sValue);

                        long walletId = dataSource.createWallet(walletValue);
                        if (walletId != 0) {
                            boolean userWallet = dataSource.createUserWallet(userId, walletId);
                            if (!userWallet) {
                                String message = getString(R.string.toast_something_went_wrong);
                                util.showRedThemeToast(CreateWalletActivity.this, message);
                                return;
                            }
                            String message = getString(R.string.toast_wallet_created_successfully);
                            util.showGreenThemeToast(CreateWalletActivity.this, message);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dataSource.close();
                    }
                }
            }
        });

    }

    private void  iniViews() {
        etValue = (EditText) findViewById(R.id.activityNewW_etValue);
        btConfirm = (Button) findViewById(R.id.activityNewW_btConfirm);
    }
}
