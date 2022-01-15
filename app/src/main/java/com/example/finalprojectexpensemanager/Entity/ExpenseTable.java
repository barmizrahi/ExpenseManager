package com.example.finalprojectexpensemanager.Entity;

public class ExpenseTable {

    private int id;
    private String expenseName;
    private String amount;
    private String date;
    private String description;
    private String category;

    public ExpenseTable() {
    }

    public ExpenseTable(String expenseName, String amount, String date, String description, String category, int id) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

}

