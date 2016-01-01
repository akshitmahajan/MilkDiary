package com.manaksh.milkdiary.model;

public class DailyData {
    private String date;
    private ItemType type;
    private double quantity;
    private TransactionType transactionType;

    public DailyData() {
    }

    public DailyData(String date, ItemType type, int quantity,
                     TransactionType transactionType) {
        super();
        this.date = date;
        this.type = type;
        this.quantity = quantity;
        this.transactionType = transactionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}