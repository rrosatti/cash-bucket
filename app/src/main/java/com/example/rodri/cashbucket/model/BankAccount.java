package com.example.rodri.cashbucket.model;

/**
 * Created by rodri on 12/14/2016.
 */

public class BankAccount {

    private long id;
    private double balance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public BankAccount() {}

    public BankAccount(long id, double balance) {
        this.id = id;
        this.balance = balance;
    }

}
