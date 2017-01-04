package com.example.rodri.cashbucket.model;

/**
 * Created by rodri on 12/14/2016.
 */

public class CashMovement {

    private long id;
    private double price;
    private int type;
    private long userId;
    private int day;
    private int month;
    private int year;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CashMovement() {}

    public CashMovement(long id, double price, int type, long userId) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.userId = userId;
    }
}
