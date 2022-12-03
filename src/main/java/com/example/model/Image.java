package com.example.model;

public class Image {
	private String username;
	private String data;
	private String imageURI;
	
	public Image(){
	}
	
	
	public Image(String name, String data,String imageUri){
		this.username = name;
		this.data = data;
		this.imageURI = imageUri;
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String name) {
		this.username = name;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	


	public String getImageURI() {
		return this.imageURI;
	}
	
	public void setImageURI(String imageUri) {
		this.imageURI = imageUri;
	}

	
	@Override
	public String toString() {
		String info = String.format("Image name = %s, data = %s", username, data);
		return info;
	}
	
}
