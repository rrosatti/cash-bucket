package com.example.rodri.cashbucket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.adapter.SettingsAdapter;
import com.example.rodri.cashbucket.other.SimpleDividerItemDecorator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rodri on 1/2/2017.
 */

public class SettingsFragment extends Fragment {

    private RecyclerView listSettings;
    private List<String> listSettingsOp;
    private SettingsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_settings, container, false);

        iniViews(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listSettingsOp = Arrays.asList(getActivity().getResources().getStringArray(R.array.settings_options));
        adapter = new SettingsAdapter(getActivity(), listSettingsOp, this);

        // Setting up the Divider Decoration
        listSettings.addItemDecoration(new SimpleDividerItemDecorator(getContext()));

        // Setting the LayoutManager to the Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        listSettings.setLayoutManager(llm);
        listSettings.setAdapter(adapter);

    }

    private void iniViews(View v) {
        listSettings = (RecyclerView) v.findViewById(R.id.tabSettings_listSettings);
    }

}
