package com.example.spyapp.sqlite;

public class Row {
	
	
	private String[] columns;
	private IndexSender list;
	
	
	public Row() {		
	}
	
	public void setList(IndexSender list) {
		this.list = list;
	}
	
	public void setColumnData(String[] data) {		
		columns = data;
	}
	
	public int columns() {
		if (columns != null) {
			return columns.length;
		} else {
			return 0;
		}
	}
	
	public long getLong(int column) {
		return Long.parseLong(columns[column]);
	}
	
	public long getLong(String columnName) {
		return Long.parseLong(columns[list.getColumnIndex(columnName)]);
	}
	
	public int getInt(int column) {
		return Integer.parseInt(columns[column]);
	}
	
	public int getInt(String columnName) {
		return Integer.parseInt(columns[list.getColumnIndex(columnName)]);
	}
	
	public boolean getBoolean(int column) {
		return Boolean.parseBoolean(columns[column]);
	}
	
	public boolean getBoolean(String columnName) {
		return Boolean.parseBoolean(columns[list.getColumnIndex(columnName)]);
	}
	
	public String getString(int column) {
		return columns[column];
	}
	
	public String getString(String columnName) {
		return columns[list.getColumnIndex(columnName)];
	}
	
	public interface IndexSender {
		public int getColumnIndex(String name);
	}

}