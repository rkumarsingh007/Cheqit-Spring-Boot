package com.example.model;

import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection = "bharatrath")
@JsonInclude(JsonInclude.Include.NON_NULL)  
public class DemoBharatrath {

	@Id
	private String id;
	private String productName;
	private TreeMap<String,String> productDetails;
	private long mfg;
	private long exp;
	private int life;
	private ArrayList<Location> origin;
	private String farmerName;
	
	public DemoBharatrath() {
		super();
	}
	
	public DemoBharatrath(String id, String productName, TreeMap<String,String> productDetails, long mfg, long exp, int life,
			ArrayList<Location> origin, String farmerName) {
		super();
		this.id = id;
		this.productName = productName;
		this.productDetails = productDetails;
		this.mfg = mfg;
		this.exp = exp;
		this.life = life;
		this.origin = origin;
		this.farmerName = farmerName;
	}

	public TreeMap<String,String> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(TreeMap<String,String> productDetails) {
		this.productDetails = productDetails;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getMfg() {
		return mfg;
	}
	public void setMfg(long mfg) {
		this.mfg = mfg;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public ArrayList<Location> getOrigin() {
		return origin;
	}
	public void setOrigin(ArrayList<Location> origin) {
		this.origin = origin;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	
	
}
