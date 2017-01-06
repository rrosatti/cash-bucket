package com.example.rodri.cashbucket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.activity.NewCashMovementActivity;
import com.example.rodri.cashbucket.adapter.CashMovementAdapter;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.Login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodri on 1/2/2017.
 */

public class CashFlowFragment extends Fragment{

    private RecyclerView listCashFlow;
    private FloatingActionButton btNewCashMovement;
    private List<CashMovement> cashMovements;
    private MyDataSource dataSource;
    private CashMovementAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_cash_flow, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        iniViews(view);

        // setting a LayoutManager to the RecyclerList
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        listCashFlow.setLayoutManager(llm);

        listCashFlow.setAdapter(adapter);

        btNewCashMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewCashMovementActivity.class);
                startActivity(i);
            }
        });

    }

    private void iniViews(View v) {
        listCashFlow = (RecyclerView) v.findViewById(R.id.tabCashFlow_list);
        btNewCashMovement = (FloatingActionButton) v.findViewById(R.id.tabCashFlow_btNewCashMovement);
        cashMovements = new ArrayList<>();
        dataSource = new MyDataSource(getActivity());

        try {
            dataSource.open();

            cashMovements = dataSource.getAllCashMovements(Login.getInstance().getUserId());

        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }

        adapter = new CashMovementAdapter(getActivity(), cashMovements);

    }
}
