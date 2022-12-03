package com.example.model;


public class AuthProduct {
	String dealerId;
	String code;
	String pid;
	public AuthProduct() {
		
	}
	public AuthProduct(AuthProduct pro) {
		this.dealerId = pro.getDealerId();
		this.pid = pro.pid;
		this.code = pro.code;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getDealerId() {
		return this.dealerId;
	}
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String str) {
		this.code = str;
	}
	public String getPid() {
		return this.pid;
	}
	
	public void setPid(String id) {
		this.pid = id;
	}
}
