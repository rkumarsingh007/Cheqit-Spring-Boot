package com.example.model;

import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection = "generic_product")
@JsonInclude(JsonInclude.Include.NON_NULL)  
public class GenericProduct {
	
	
	private String id;
	private String productName;
	private TreeMap<String,String> productDetails;
	private long mfg;
	private ArrayList<Location> origin;
	
	public GenericProduct() {
		super();
	}
	public GenericProduct(String id, String productName, TreeMap<String, String> productDetails, long mfg,
			ArrayList<Location> origin) {
		super();
		this.id = id;
		this.productName = productName;
		this.productDetails = productDetails;
		this.mfg = mfg;
		this.origin = origin;
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
	public TreeMap<String, String> getProductDetails() {
		return productDetails;
	}
	public void setProductDetails(TreeMap<String, String> productDetails) {
		this.productDetails = productDetails;
	}
	public long getMfg() {
		return mfg;
	}
	public void setMfg(long mfg) {
		this.mfg = mfg;
	}
	public ArrayList<Location> getOrigin() {
		return origin;
	}
	public void setOrigin(ArrayList<Location> origin) {
		this.origin = origin;
	}
	
	

}
