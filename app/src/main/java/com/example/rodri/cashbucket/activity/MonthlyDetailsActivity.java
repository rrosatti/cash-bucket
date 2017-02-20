package com.example.rodri.cashbucket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.adapter.CustomSpinnerAdapter;
import com.example.rodri.cashbucket.adapter.DetailedMonthAdapter;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.DetailedMonth;
import com.example.rodri.cashbucket.model.Login;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by rodri on 2/18/2017.
 */

public class MonthlyDetailsActivity extends AppCompatActivity {

    private Spinner spinnerYears;
    private Button btOk;
    private ExpandableListView months;
    private String[] years;
    private CustomSpinnerAdapter spinnerAdapter;
    private MyDataSource dataSource;
    private DetailedMonthAdapter detailedMonthAdapter;
    private int selectedYear;
    private LinkedHashMap<Integer, DetailedMonth> detailedMonths;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_details);

        iniViews();
        dataSource = new MyDataSource(this);
        years = generateYears(50);

        spinnerAdapter = new CustomSpinnerAdapter(this, Arrays.asList(years));
        spinnerYears.setAdapter(spinnerAdapter);

        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = Integer.valueOf(years[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailedMonths(selectedYear);
            }
        });
    }

    private void iniViews() {
        spinnerYears = (Spinner) findViewById(R.id.activityMD_spinnerYears);
        btOk = (Button) findViewById(R.id.activityMD_btOK);
        months = (ExpandableListView) findViewById(R.id.activityMD_listDetailedMonths);
    }

    private String[] generateYears(int range) {
        String[] tempYears = new String[range];
        for (int i=0; i<range; i++) {
            tempYears[i] = String.valueOf(2017+i);
        }

        return tempYears;
    }

    private void showDetailedMonths(int year) {
        detailedMonths = getDetailedMonths(year);
        detailedMonthAdapter = new DetailedMonthAdapter(this, detailedMonths);
        months.setAdapter(detailedMonthAdapter);
    }

    private LinkedHashMap<Integer, DetailedMonth> getDetailedMonths(int year) {
        LinkedHashMap<Integer, DetailedMonth> temp;
        try {
            dataSource.open();

            long userId = Login.getInstance().getUserId();
            temp = dataSource.getDetailedMonths(userId, year);

            if (temp == null) {
                temp = new LinkedHashMap<>();
            }

            dataSource.close();
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
            return null;
        }
    }
}
