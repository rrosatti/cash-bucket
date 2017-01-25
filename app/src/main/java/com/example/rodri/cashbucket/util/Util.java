package com.example.rodri.cashbucket.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;

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

    public void showCustomToast(Activity activity, String message, int imageId) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View customToast = inflater.inflate(R.layout.toast_green_theme, null);
        Toast toast = new Toast(context);

        ImageView img = (ImageView) customToast.findViewById(R.id.toastGreenTheme_img);
        TextView txt = (TextView) customToast.findViewById(R.id.toastGreenTheme_txtMessage);

        img.setImageResource(imageId);
        txt.setText(message);

        toast.setView(customToast);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        
    }

}
