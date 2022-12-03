package com.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "main")
public class Main {
	@Id
	private String id;
	private int call;
	private int rno;
	private int transId;
	private String ourUPI;
	
	public String getOurUPI() {
		return this.ourUPI;
	}
	
	public void setOurUPI(String ourUPI) {
		this.ourUPI = ourUPI;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getCall() {
		return this.call;
	}
	
	public void setCall(int call) {
		this.call = call;
	}
	
	public int getRno(){
		return this.rno;
	}
	
	public void setRno(int rno) {
		this.rno = rno;
	}
	
	public int getTransId() {
		return this.transId;
	}
	
	public void setTransId(int transId) {
		this.transId = transId;
	}

}
