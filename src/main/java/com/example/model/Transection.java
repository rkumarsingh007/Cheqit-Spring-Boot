package com.example.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Transection")
public class Transection implements Serializable {
	@Id
	private String id;
	private String pid;                 //product
    private String cid;                 //comanyid              ??
    private String buyerId;             //enduser
    private Address_data address;       //add Of payment
    private String type;                //online ?? chash
    private String transId;               //trans
    private boolean success;
    private String timeStamp;
    private String sailerId;


    public String getId() {
    	return this.id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }


    public String getSailerId() {
        return sailerId;
    }

    public void setSailerId(String sailerId) {
        this.sailerId = sailerId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Address_data getAddress() {
        return address;
    }

    public void setAddress(Address_data address) {
        this.address = address;
    }
}
