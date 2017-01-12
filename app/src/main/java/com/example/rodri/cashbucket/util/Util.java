package com.example.rodri.cashbucket.util;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by rodri on 1/12/2017.
 */

public class Util {

    public boolean isTextViewEmpty(TextView tv) {
        return (tv.getText().toString().isEmpty());
    }

}
