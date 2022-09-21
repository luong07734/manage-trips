package com.example.tripmanagement.model;

import java.util.Date;

public class Expense {
    private int expenseId;
    private String type;
    private Double amount;
    private String time;
    private String comment;
    private int tripId;

    public Expense(int expenseId, String type, Double amount, String time, String comment, int tripId) {
        this.expenseId = expenseId;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.comment = comment;
        this.tripId = tripId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}
