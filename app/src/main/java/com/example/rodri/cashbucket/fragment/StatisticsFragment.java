package com.example.rodri.cashbucket.fragment;

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
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.Login;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rodri on 1/2/2017.
 */

public class StatisticsFragment extends Fragment {

    private static final String STATE_MONTH = "month";
    private static final String STATE_YEAR = "year";

    private Spinner spinnerMonths;
    private Spinner spinnerYears;
    private Button btOk;
    private BarChart barChart;
    private ProgressBar progressBar;
    private String[] months;
    private String[] years;
    private List<CashMovement> cashMovements;
    private MyDataSource dataSource;
    private int selectedMonth;
    private int selectedYear;
    private double[] balanceByType = new double[4];

    private List<BarEntry> entries = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private BarDataSet barDataSet;
    private BarData barData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_statistics, container, false);

        iniViews(v);
        dataSource = new MyDataSource(getActivity());

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        months = getActivity().getResources().getStringArray(R.array.months);
        years = generateYears(50);

        ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, months);
        spinnerMonths.setAdapter(adapterMonths);

        ArrayAdapter<String> adapterYears = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        spinnerYears.setAdapter(adapterYears);

        if (savedInstanceState != null) {
            selectedMonth = savedInstanceState.getInt(STATE_MONTH);
            selectedYear = savedInstanceState.getInt(STATE_YEAR);
            GetDataFromDatabase task = new GetDataFromDatabase();
            task.execute("");
        }

        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                cashMovements = new ArrayList<>();
                entries = new ArrayList<>();
                labels = new ArrayList<>();
                barDataSet = null;
                balanceByType = new double[4];
                GetDataFromDatabase task = new GetDataFromDatabase();
                task.execute("");
            }
        });
    }

    private void iniViews(View v) {
        spinnerMonths = (Spinner) v.findViewById(R.id.tabStatistics_spinnerMonths);
        spinnerYears = (Spinner) v.findViewById(R.id.tabStatistics_spinnerYears);
        btOk = (Button) v.findViewById(R.id.tabStatistics_btOk);
        barChart = (BarChart) v.findViewById(R.id.tabStatistics_chart);
        progressBar = (ProgressBar) v.findViewById(R.id.tabStatistics_progressBar);
    }

    private String[] generateYears(int range) {
        String[] years = new String[range];
        for (int i=0; i<range; i++) {
            years[i] = String.valueOf(2017+i);
        }

        return years;
    }

    private class GetDataFromDatabase extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000);
                dataSource.open();

                long userId = Login.getInstance().getUserId();
                cashMovements = dataSource.getCashMovements(userId, selectedMonth, selectedYear);

                if (cashMovements != null) {
                    for (CashMovement cm : cashMovements) {
                        balanceByType[cm.getType()-1] += cm.getValue();
                    }
                }

                setEntriesAndLabels();
                int[] barColors = getColors();

                barDataSet = new BarDataSet(entries, null);
                barDataSet.setColors(barColors);
                barData = new BarData(labels, barDataSet);

                dataSource.close();
            } catch (Exception e) {
                e.printStackTrace();
                dataSource.close();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            showChartData();
        }
    }

    private void setEntriesAndLabels() {
        int count = 0;
        for (int i=0; i<balanceByType.length; i++) {
            entries.add(new BarEntry( (float) balanceByType[count], count++));
        }

        String[] tempLabels = getActivity().getResources().getStringArray(R.array.list_cash_movement_types);
        labels = Arrays.asList(tempLabels);
    }

    private int[] getColors() {
        int[] barColors = {
                getActivity().getResources().getColor(R.color.bar_color_1),
                getActivity().getResources().getColor(R.color.bar_color_2),
                getActivity().getResources().getColor(R.color.bar_color_3),
                getActivity().getResources().getColor(R.color.bar_color_4)
        };

        return barColors;
    }

    private void showChartData() {
        barChart.setData(barData);
        barChart.setDescription(months[selectedMonth-1]+"/"+selectedYear);
        barChart.getLegend().setEnabled(true);
        barChart.animateY(1200);
        barChart.invalidate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_MONTH, selectedMonth);
        outState.putInt(STATE_YEAR, selectedYear);
    }
}
