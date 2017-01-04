package com.example.rodri.cashbucket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.activity.NewCashMovementActivity;
import com.example.rodri.cashbucket.model.Login;

/**
 * Created by rodri on 1/2/2017.
 */

public class CashFlowFragment extends Fragment{

    private ListView listCashFlow;
    private FloatingActionButton btNewCashMovement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_cash_flow, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        iniViews(view);

        btNewCashMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewCashMovementActivity.class);
                startActivity(i);
            }
        });

    }

    private void iniViews(View v) {
        listCashFlow = (ListView) v.findViewById(R.id.tabCashFlow_list);
        btNewCashMovement = (FloatingActionButton) v.findViewById(R.id.tabCashFlow_btNewCashMovement);
    }
}
