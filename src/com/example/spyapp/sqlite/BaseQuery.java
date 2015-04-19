package com.example.spyapp.sqlite;

import java.util.ArrayList;
import android.database.Cursor;

public abstract class BaseQuery {
	
	protected final static String SELECT = "SELECT";
	protected final static String WHERE = "WHERE";
	protected final static String FROM = "FROM";
	protected final static String LIKE = "LIKE";
	protected final static String AND = "AND";
	protected final static String OR = "OR";
	protected final static String AS = "AS";
	protected final static String JOIN = "JOIN";
	protected final static String ON = "ON";
	protected final static String ORDER = "ORDER BY";
	protected final static String LIMIT = "LIMIT";
	
	
	public abstract String getStatement();
	public abstract String[] getSelectionArgs();
	
	public Cursor executeQuery(SqlDb db) {
		return db.query(this);
	}
	
	/**
	 * Executes the query, returning a ResultList of the data. The ResultList
	 * is an ArrayList, which also has the column Names.
	 * @param db
	 * @return
	 */
	public ResultList execute(SqlDb db) {
		Object lock = db.getLock();
		ResultList results = null;
		synchronized (lock) {
			final Cursor cursor = db.query(this);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					String[] names = cursor.getColumnNames();
					results = new ResultList(names);
					do {
						Row result = new Row();						
						String[] data = getDataArray(cursor);
						result.setColumnData(data);
						results.add(result);
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		}
		return results;
	}
	
	protected static String[] getDataArray(Cursor cursor) {
		int size = cursor.getColumnCount();
		String[] data = new String[size];
		for (int i = 0; i < size; i++) {
			data[i] = cursor.getString(i);
		}
		return data;
	}

}
