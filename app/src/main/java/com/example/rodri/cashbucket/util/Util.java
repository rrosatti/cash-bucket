package com.example.rodri.cashbucket.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

    public void showGreenThemeToastWithImage(Activity activity, String message, int imageId) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View customToast = inflater.inflate(R.layout.toast_green_theme_with_image, null);
        Toast toast = new Toast(context);

        ImageView img = (ImageView) customToast.findViewById(R.id.toastGreenThemeWI_img);
        TextView txt = (TextView) customToast.findViewById(R.id.toastGreenThemeWI_txtMessage);

        img.setImageResource(imageId);
        txt.setText(message);

        toast.setView(customToast);
        showToast(toast);
        
    }

    public void showRedThemeToast(Activity activity, String message) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View customToast = inflater.inflate(R.layout.toast_red_theme, null);
        Toast toast = new Toast(context);

        TextView txt = (TextView) customToast.findViewById(R.id.toastRedTheme_txtMessage);
        txt.setText(message);

        toast.setView(customToast);
        showToast(toast);
    }

    public void showGreenThemeToast(Activity activity, String message) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View customToast = inflater.inflate(R.layout.toast_green_theme, null);
        Toast toast = new Toast(context);

        TextView txt = (TextView) customToast.findViewById(R.id.toastGreenTheme_txtMessage);
        txt.setText(message);

        toast.setView(customToast);
        showToast(toast);
    }

    private void showToast(Toast toast) {
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public Dialog createCustomDialog(Activity activity, int customLayoutId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(customLayoutId);
        dialog.setCancelable(true);
        return dialog;
    }

    public String formatStringDate(int day, int month, int year) {
        String sDay = String.valueOf(day);
        String sMonth = String.valueOf(month);
        if (day < 10) {
            sDay = "0" + day;
        }
        if (month < 10) {
            sMonth = "0" + month;
        }

        String date = sDay+"/"+sMonth+"/"+year;
        return date;
    }

}
