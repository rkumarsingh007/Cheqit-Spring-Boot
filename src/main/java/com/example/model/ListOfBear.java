package com.example.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ListOfBears")
public class ListOfBear {
	@Id
	private String id;
	private String dealerId;
	private List<String> list;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String username) {
		this.dealerId = username;
	}
	
	public List<String> getList() {
		return list;
	}
	
	public void setList(List<String> l) {
		this.list = l;
	}
	
}
