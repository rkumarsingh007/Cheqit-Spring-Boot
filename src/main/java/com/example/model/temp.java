package com.example.model;

public class temp {
	
	private String name;
	private int rno;
	
	public temp(String name,int rno) {
		this.name = name;
		this.rno = rno;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	 
	public String getName() {
		return this.name;
	}
	
	public void setAge(int rno)
	{
		this.rno = rno;
	}
	
	public int getAge() {
		return this.rno;
	}
}
