package com.example.wenda.model;

public class User {
	private String name;
	public User(String name) {
		this.setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return "This is " + name;
	}
	
}
