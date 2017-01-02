package com.example.rodri.cashbucket.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rodri.cashbucket.fragment.CashFlowFragment;
import com.example.rodri.cashbucket.fragment.SettingsFragment;
import com.example.rodri.cashbucket.fragment.StatisticsFragment;

/**
 * Created by rodri on 1/2/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[];
    private int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence Titles[], int numOfTabs) {
        super(fm);

        this.Titles = Titles;
        this.numOfTabs = numOfTabs;
    }

    /**
     *
     *  It return the fragment for the  every position in the View Pager
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                CashFlowFragment cashFlowFragment = new CashFlowFragment();
                return cashFlowFragment;
            }
            case 1: {
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                return statisticsFragment;
            }
            case 2: {
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            }
            default: {
                return null;
            }
        }
    }

    /**
     *
     * It return the titles of the Tabs in the Tab Strip
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    /**
     *
     * It returns the number of tabs for the Tab Strip
     *
     * @return
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
