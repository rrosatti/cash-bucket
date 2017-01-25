package com.example.rodri.cashbucket.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.activity.NewCashMovementActivity;
import com.example.rodri.cashbucket.adapter.CashMovementAdapter;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.BankAccount;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.Login;
import com.example.rodri.cashbucket.model.Wallet;
import com.example.rodri.cashbucket.other.SimpleDividerItemDecorator;
import com.example.rodri.cashbucket.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodri on 1/2/2017.
 */

public class CashFlowFragment extends Fragment {

    private RecyclerView listCashFlow;
    private FloatingActionButton btNewCashMovement;
    private TextView etBankAccount;
    private TextView etWallet;
    private List<CashMovement> cashMovements;
    private MyDataSource dataSource;
    private CashMovementAdapter adapter;
    private BankAccount bankAccount;
    private Wallet wallet;
    private Util util = new Util();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_cash_flow, container, false);

        iniViews(v);
        getDataFromDatabase();

        // Setting up the Divider decoration
        listCashFlow.addItemDecoration(new SimpleDividerItemDecorator(getContext()));

        // setting a LayoutManager to the RecyclerList
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        listCashFlow.setLayoutManager(llm);

        listCashFlow.setAdapter(adapter);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // it will update the Bank Account and Wallet balances
        updateBalances();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getDataFromDatabase();
                listCashFlow.setAdapter(adapter);
                updateBalances();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        btNewCashMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewCashMovementActivity.class);
                startActivityForResult(i, 1);
            }
        });

    }

    private void iniViews(View v) {
        listCashFlow = (RecyclerView) v.findViewById(R.id.tabCashFlow_list);
        btNewCashMovement = (FloatingActionButton) v.findViewById(R.id.tabCashFlow_btNewCashMovement);
        etBankAccount = (TextView) v.findViewById(R.id.tabCashFlow_etBankAccount);
        etWallet = (TextView) v.findViewById(R.id.tabCashFlow_etWallet);
        cashMovements = new ArrayList<>();
        dataSource = new MyDataSource(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.tabCashFlow_swipeContainer);


    }

    private void getDataFromDatabase() {
        try {
            dataSource.open();

            long userId = Login.getInstance().getUserId();

            cashMovements = dataSource.getAllCashMovements(userId);
            bankAccount = dataSource.getBankAccount(userId);
            wallet = dataSource.getWallet(userId);

        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }

        if (cashMovements != null) {
            adapter = new CashMovementAdapter(getActivity(), cashMovements);
        } else {
            cashMovements = new ArrayList<>();
            adapter = new CashMovementAdapter(getActivity(), cashMovements);
        }

    }

    private void updateBalances() {
        String sBankAccount = util.formatMoneyString(bankAccount.getBalance(), "R$");
        String sWallet = util.formatMoneyString(wallet.getBalance(), "R$");

        etBankAccount.setText(sBankAccount);
        etWallet.setText(sWallet);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    //adapter.notifyDataSetChanged();
                    getDataFromDatabase();
                    listCashFlow.setAdapter(adapter);
                    updateBalances();
                }
            }
        }
    }

}
