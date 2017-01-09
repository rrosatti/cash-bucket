package com.example.rodri.cashbucket.model;

/**
 * Created by rodri on 12/14/2016.
 */

public class CashMovement {

    private long id;
    private double value;
    private int type;
    private long userId;
    private int day;
    private int month;
    private int year;
    private String desc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CashMovement() {}

    public CashMovement(long id, double value, int type, int day, int month, int year, String desc, long userId) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.day = day;
        this.month = month;
        this.year = year;
        this.desc = desc;
        this.userId = userId;
    }
}
