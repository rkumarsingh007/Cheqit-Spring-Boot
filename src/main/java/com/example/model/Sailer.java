package com.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Sailer")
public class Sailer {
	@Id
	private String id;
    private String username;
    private String pwd;
    private String pno;
    private String name;
    private int rno;
    private String email;
    private String address;
    private String details;
    private String imageURI;
    private String upiId;
	
	public void setUpiId(String s) {
		this.upiId = s;
	}
	
	public String getUpiId() {
		return this.upiId;
	}
    
    public int getRno() {
    	return this.rno;
    }
    
    public void setRno(int rno) {
    	this.rno = rno;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }



}
