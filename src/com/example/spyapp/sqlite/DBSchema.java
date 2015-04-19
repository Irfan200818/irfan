package com.example.spyapp.sqlite;

import java.util.ArrayList;

public class DBSchema {
	
	private ArrayList<Table> tables;
	private int curIndex = -1;
	
	public DBSchema() {
		tables = new ArrayList<Table>();
	}
			
	public void addTable(Table table) {
		tables.add(table);
	}
	
	public void addTable(String table, String[] columns) {
		Table t = new Table(table);
		t.addColumn(columns);
		tables.add(t);
	}
	
	public void addTable(String table, String[] columns, String[] dataTypes) {
		Table t = new Table(table);
		for (int i = 0; i < columns.length; i++) {
			t.addColumn(columns[i], dataTypes[i]);
		}
		tables.add(t);
	}
	
	public int tableCount() {
		return tables.size();
	}
	
	public Table getTable(int index) {
		return tables.get(index);
	}
	
	public Table getTable(String tableName) {
		return tables.get(getTableIndex(tables, tableName));
	}
	
	public ArrayList<Table> getTables() {
		return tables;
	}
	
	public Table next() {
		curIndex++;
		if (curIndex > tables.size() - 1) {
			curIndex = tables.size() - 1;
		}
		if (curIndex == -1) {
			return null;
		}
		return tables.get(curIndex);
	}
	
	public Table previous() {
		curIndex--;
		if (curIndex < 0 && tables.size() > 0) {
			curIndex = 0;
		} else {
			return null;
		}
		return tables.get(curIndex);
	}
	
	public int tableIndex(String tableName) {
		return getTableIndex(tables, tableName);
	}
		
	private static int getTableIndex(ArrayList<Table> tables, String tableName) {
		int size = tables.size();
		for (int i = 0; i < size; i++) {
			if (tables.get(i).getName().equals(tableName)) {
				return i;
			}
		}
		return -1;
	}

}
