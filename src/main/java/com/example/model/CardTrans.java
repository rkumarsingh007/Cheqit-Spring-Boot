package com.example.model;

import java.io.Serializable;

public class CardTrans implements Serializable{
	//"_id","pid","type","success","timeStamp","pro.pname","pro.cost"
	String _id;
    String pid;
    String type;
    boolean success;
    String timeStamp;
    String pname;
    String cost;
    

    public CardTrans(){}
    public CardTrans(String _id, String pid,String type, boolean success, String timeStamp,String pname, String cost  ) {
        this._id = _id;
        this.pid = pid;
        this.pname = pname;
        this.cost = cost;
        this.type = type;
        this.success = success;
        this.timeStamp = timeStamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
