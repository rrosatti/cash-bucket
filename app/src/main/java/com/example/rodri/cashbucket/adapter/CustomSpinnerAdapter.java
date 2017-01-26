package com.example.rodri.cashbucket.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodri on 1/26/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<String> spinnerContent;

    public CustomSpinnerAdapter(Context context, List<String> spinnerContent) {
        this.context = context;
        this.spinnerContent = spinnerContent;
    }

    public int getCount() {
        return spinnerContent.size();
    }

    public Object getItem(int pos) {
        return spinnerContent.get(pos);
    }

    public long getItemId(int pos) {
        return (long) pos;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(spinnerContent.get(position));
        txt.setTextColor(context.getResources().getColor(R.color.colorAccent));
        return txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        Drawable img;
        Resources res = context.getResources();
        img = ContextCompat.getDrawable(context, R.drawable.ic_down);
        img.setBounds(0, 0, 50, 50);
        txt.setCompoundDrawables(null, null, img, null);
        //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        txt.setText(spinnerContent.get(position));
        txt.setTextColor(context.getResources().getColor(R.color.colorAccent));
        return txt;
    }
}
