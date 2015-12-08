package com.manaksh.milkdiary.model;

import java.util.Calendar;
import java.util.Date;

public class DailyData {
	private Calendar date;
	private ItemType type;
	private int quantity;
	private TransactionType transactionType;
	
	public DailyData(Calendar date, ItemType type, int quantity,
			TransactionType transactionType) {
		super();
		this.date = date;
		this.type = type;
		this.quantity = quantity;
		this.transactionType = transactionType;
	}
	
	
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}


	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	
	
}
