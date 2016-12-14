package com.example.rodri.cashbucket.model;

/**
 * Created by rodri on 12/14/2016.
 */

public class Transaction {

    private long id;
    private double price;
    private long date;
    private String type;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Transaction() {}

    public Transaction(long id, double price, long date, String type, long userId) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.type = type;
        this.userId = userId;
    }
}
