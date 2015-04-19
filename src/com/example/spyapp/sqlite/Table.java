package com.example.spyapp.sqlite;

import java.util.ArrayList;


public class Table {
	
	private String name;
	private String idColumn = SqlDb.ID;
	private ArrayList<Column> columns = new ArrayList<Column>();
	
	public Table(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Table setIDColumn(String columnName) {
		idColumn = columnName;
		return this;
	}
	
	public String getIdColumn() {
		return idColumn;
	}
	
	/**
	 * This adds a column to the table, defaulting the datatype to text
	 * @param name
	 */
	public Table addColumn(String... name) {		
		if (name != null && name.length > 0) {
			for (String n : name) {
				columns.add(new Column(n));
			}
		}
		return this;
	}
	
	public Table addColumnsAndTypes(String[] columns, String[] dataTypes) {
		for (int i = 0; i < columns.length; i++) {
			addColumn(columns[i], dataTypes[i]);
		}
		return this;
	}
		
	public Table addColumn(String name, String dataType) {
		Column c = new Column(name);
		if (dataType != null && dataType.length() > 0) {
			c.setDataType(dataType);
		}
		columns.add(c);
		return this;
	}
		
	public Table addColumn(Column column) {
		columns.add(column);
		return this;
	}
	
	public ArrayList<Column> getColumns() {
		return columns;
	}
	
	public ArrayList<String> getColumnNames() {
		int size = columns.size();
		ArrayList<String> list = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			list.add(columns.get(i).getName());
		}
		return list;
	}
	
	/**
	 * This returns the column names, and datatypes in this table, separated by comma
	 * 
	 */
	public String getColumnsString() {
		StringBuilder b = new StringBuilder();
		int size = columns.size();
		for (int i = 0; i < size; i++) {
			b.append(columns.get(i).getName());
			b.append(" " + columns.get(i).getDataType());
			if (i != size - 1) {
				b.append(", ");
			}			
		}
		return b.toString();
	}

}
