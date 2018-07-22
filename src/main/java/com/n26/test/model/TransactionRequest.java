package com.n26.test.model;

import java.io.Serializable;

/**
 * Created by kinjal.patel on 21/7/18
 */
public class TransactionRequest implements Serializable {
	private double amount;
	private long timeStamp;
	
	public TransactionRequest() {
	}
	
	public TransactionRequest(double amount, long timeStamp) {
		this.amount = amount;
		this.timeStamp = timeStamp;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
