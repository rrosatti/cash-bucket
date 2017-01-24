package com.example.rodri.cashbucket.util;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by rodri on 1/12/2017.
 */

public class Util {

    public boolean isTextViewEmpty(TextView tv) {
        return (tv.getText().toString().isEmpty());
    }

    public String formatMoneyString(double value, String currency) {
        NumberFormat formatter = new DecimalFormat("#0.00");

        return currency + " " + formatter.format(value);
    }

    public String formatNumberWithoutCurrency(double value) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(value);
    }

}
