package com.example.rodri.cashbucket.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.adapter.ViewPagerAdapter;
import com.example.rodri.cashbucket.service.AutoDepositService;
import com.example.rodri.cashbucket.tabs.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Cash Flow", "Statistics", "Settings"};
    private int numOfTabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Create the ViewPagerAdapter
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numOfTabs);

        // Assigning ViewPager views and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        // This makes the tabs space evenly in available width
        tabs.setDistributeEvenly(true);

        // Setting custom color for the scroll bar indicator
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        // Setting the ViewPager for the SlidingTabsLayout
        tabs.setViewPager(pager);
    }
}
