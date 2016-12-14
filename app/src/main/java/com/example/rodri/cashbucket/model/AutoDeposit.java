package com.example.rodri.cashbucket.model;

/**
 * Created by rodri on 12/14/2016.
 */

public class AutoDeposit {

    private long id;
    private long bankAccountId;
    private double value;
    private int day;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public double getValue() {
        return value;
    }

    public AutoDeposit() {}

    public AutoDeposit(long id, long bankAccountId, double value, int day) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.value = value;
        this.day = day;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
