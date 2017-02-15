package com.example.rodri.cashbucket.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.activity.StatisticsChartActivity;
import com.example.rodri.cashbucket.adapter.CustomSpinnerAdapter;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.Login;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rodri on 1/2/2017.
 */

public class StatisticsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_statistics, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    
}
