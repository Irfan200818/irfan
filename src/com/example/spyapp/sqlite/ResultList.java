package com.example.spyapp.sqlite;

import java.util.ArrayList;

import com.example.spyapp.sqlite.Row.IndexSender;

public class ResultList extends ArrayList<Row> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5581089212310965964L;
	private String[] columnNames;
	private static Row.IndexSender indexSender;
	
	
	public ResultList(String[] columnNames) {
		this.columnNames = columnNames;
		indexSender = new IndexSender() {			
			@Override
			public int getColumnIndex(String name) {				 
				return getColumnIndex(name);
			}
		};
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public int getColumnIndex(String name) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean add(Row object) {
		object.setList(indexSender);
		return super.add(object);
	}
	
	@Override
	public void add(int index, Row object) {
		object.setList(indexSender);
		super.add(index, object);
	}
		
}
