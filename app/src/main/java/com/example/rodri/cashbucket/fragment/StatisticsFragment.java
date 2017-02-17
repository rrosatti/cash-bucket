package com.example.rodri.cashbucket.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.rodri.cashbucket.adapter.StatisticsAdapter;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.other.SimpleDividerItemDecorator;
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

    private RecyclerView listStatisticsOptions;
    private StatisticsAdapter adapter;
    private List<String> options = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_statistics, container, false);

        iniViews(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        options = Arrays.asList(getActivity().getResources().getStringArray(R.array.statistics_list));
        adapter = new StatisticsAdapter(this, options);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        listStatisticsOptions.addItemDecoration(new SimpleDividerItemDecorator(getContext()));
        listStatisticsOptions.setLayoutManager(llm);
        listStatisticsOptions.setAdapter(adapter);
    }

    private void iniViews(View v) {
        listStatisticsOptions = (RecyclerView) v.findViewById(R.id.tabStatistics_statisticsList);
    }


}
