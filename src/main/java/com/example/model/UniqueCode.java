package com.example.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "UniqueCode")
public class UniqueCode {
	@Id
	private String id;
	private String username;
	private String code;
	private Date date;
	
	public UniqueCode() {}
	
	public UniqueCode(String id,String username,String code,Date date) {
		this.id = id;
		this.username = username;
		this.code = code;
		this.date = date;
		
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	

}
