package com.example.rodri.cashbucket.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.adapter.CustomSpinnerAdapter;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.fragment.StatisticsFragment;
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
 * Created by rodri on 2/15/2017.
 */

public class StatisticsChartActivity extends AppCompatActivity {

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
    private CustomSpinnerAdapter customAdapterMonths;
    private CustomSpinnerAdapter customAdapterYears;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_chart);

        iniViews();
        dataSource = new MyDataSource(this);

        months = getResources().getStringArray(R.array.months);
        years = generateYears(50);

        //ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, months);
        customAdapterMonths = new CustomSpinnerAdapter(this, Arrays.asList(months));
        spinnerMonths.setAdapter(customAdapterMonths);

        //ArrayAdapter<String> adapterYears = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        customAdapterYears = new CustomSpinnerAdapter(this, Arrays.asList(years));
        spinnerYears.setAdapter(customAdapterYears);

        if (savedInstanceState != null) {
            selectedMonth = savedInstanceState.getInt(STATE_MONTH);
            selectedYear = savedInstanceState.getInt(STATE_YEAR);
            StatisticsChartActivity.GetDataFromDatabase task = new StatisticsChartActivity.GetDataFromDatabase();
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
                StatisticsChartActivity.GetDataFromDatabase task = new StatisticsChartActivity.GetDataFromDatabase();
                task.execute("");
            }
        });

    }

    private void iniViews() {
        spinnerMonths = (Spinner) findViewById(R.id.tabStatistics_spinnerMonths);
        spinnerYears = (Spinner) findViewById(R.id.tabStatistics_spinnerYears);
        btOk = (Button) findViewById(R.id.tabStatistics_btOk);
        barChart = (BarChart) findViewById(R.id.tabStatistics_chart);
        progressBar = (ProgressBar) findViewById(R.id.tabStatistics_progressBar);
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

        String[] tempLabels = getResources().getStringArray(R.array.list_cash_movement_types);
        labels = Arrays.asList(tempLabels);
    }

    private int[] getColors() {
        int[] barColors = {
                getResources().getColor(R.color.bar_color_1),
                getResources().getColor(R.color.bar_color_2),
                getResources().getColor(R.color.bar_color_3),
                getResources().getColor(R.color.bar_color_4)
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
