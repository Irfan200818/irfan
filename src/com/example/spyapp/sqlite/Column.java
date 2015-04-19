package com.example.spyapp.sqlite;

public class Column {

	private String name;
	private String dataType = "text";
	
	
	public Column(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public Column setDataType(String type) {
		dataType = type;
		return this;
	}
	
}
