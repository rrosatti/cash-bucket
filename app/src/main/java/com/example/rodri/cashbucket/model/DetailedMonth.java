package com.example.rodri.cashbucket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rodri on 2/17/2017.
 */

public class DetailedMonth {

    private int month;
    private int year;
    private List<CashMovement> cashMovements;

    public DetailedMonth() {}

    public DetailedMonth(int month, int year, List<CashMovement> cashMovements) {
        this.month = month;
        this.year = year;
        this.cashMovements = cashMovements;
    }

    public DetailedMonth(int month, int year) {
        this.month = month;
        this.year = year;
        this.cashMovements = new ArrayList<>();
    }

    public void addCashMovement(CashMovement cashMovement) {
        cashMovements.add(cashMovement);
    }

    public CashMovement getCashMovement(int pos) {
        return cashMovements.get(pos);
    }

    public List<CashMovement> sortCashMovements() {
        // it will sort in ascending order
        Collections.sort(cashMovements, new Comparator<CashMovement>() {
            @Override
            public int compare(CashMovement cm1, CashMovement cm2) {
                return cm1.getDay() - cm2.getDay();
            }
        });

        return cashMovements;
    }

    public int getNumOfCashMovements() {
        return cashMovements.size();
    }
}
