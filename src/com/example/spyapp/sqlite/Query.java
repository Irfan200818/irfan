package com.example.spyapp.sqlite;

import java.util.ArrayList;
import java.util.Arrays;


public class Query extends BaseQuery {

	private StringBuilder statement = new StringBuilder();

	private ArrayList<String> values = new ArrayList<String>();

	public enum WrapType {
		BEFORE, AFTER, BOTH, _BEFORE, _AFTER, _BOTH, _BEFORE_P, _AFTER_P
	}

	/**
	 * Use select() to return all columns. Otherwise, provide a string array of the columns you wish returned. (For one,
	 * you can just use a single string)
	 * 
	 * This should be the first call made. Only call it once!
	 * 
	 * @param whatToSelect
	 * @return
	 */
	public Query select(String... whatToSelect) {
		statement.append(SELECT);
		if (whatToSelect == null || whatToSelect.length == 0) {
			statement.append(" * ");
		} else {
			statement.append(" " + getString(whatToSelect));
		}
		return this;
	}
	public Query selectCount() {
		statement.append(SELECT);
		statement.append(" COUNT(*) ");
		return this;
	}
	/**
	 * Use select() to return all columns. Otherwise, provide a string array of the columns you wish returned. (For one,
	 * you can just use a single string)
	 * 
	 * This should be the first call made. Only call it once!
	 * 
	 * @param whatToSelect
	 * @return
	 */
	public Query selectRaw(String... whatToSelect) {
		statement.append(SELECT);
		if (whatToSelect == null || whatToSelect.length == 0) {
			statement.append(" * ");
		} else {
			statement.append(" " + getStringRaw(whatToSelect));
		}
		return this;
	}
	
	/**
	 * Use this select method to select the maximum from a column.
	 * 
	 * @param column
	 * @return
	 */
	public Query selectMax(String column) {
		statement.append(SELECT);
		statement.append(" MAX(");
		statement.append(column);
		statement.append(") ");
		return this;
	}

	/**
	 * Use this select method to select the minimum from a column.
	 * 
	 * @param column
	 * @return
	 */
	public Query selectMin(String column) {
		statement.append(SELECT);
		statement.append(" MIN(");
		statement.append(column);
		statement.append(") ");
		return this;
	}

	/**
	 * Sets the table to pull this query from. This will throw an IllegalArgumentException if the table name contains a
	 * single quotation mark.
	 * 
	 * @param tableName
	 * @return
	 */
	public Query from(String tableName) {
		statement.append(FROM);
		statement.append(" " + SqlDb.checkTableName(tableName) + " ");
		return this;
	}
	
	/**
	 * Sets the table to pull this query from. This does NOT wrap the input string in quotes like the
	 * standard from method does. It does get wrapped in spaces though. This is useful when using
	 * more complicated join statements
	 * 
	 * @param tableName
	 * @return
	 */
	public Query fromRaw(String fromString) {
		statement.append(FROM);
		statement.append(" " + fromString + " ");
		return this;
	}

	/**
	 * Set the where statement. This method will throw an IllegalArgumentException if the column name contains a double
	 * quotation mark.
	 * 
	 * This can be called multiple times, for more complex queries.
	 * 
	 * @param columnName
	 * @return
	 */
	public Query where(String columnName) {
		statement.append(WHERE);
		statement.append(" " + SqlDb.checkColumnName(columnName) + " ");
		return this;
	}
	
	/**
	 * Set the where statement. This method does NOT wrap the string in quotes.
	 * This can be called multiple times, for more complex queries.
	 * 
	 * @param columnName
	 * @return
	 */
	public Query whereRaw(String whereString) {
		statement.append(WHERE);
		statement.append(" " + whereString + " ");
		return this;
	}

	public Query equalTo(String value) {
		statement.append("=? ");
		checkValues();
		values.add(value);
		return this;
	}

	public Query equalTo(int value) {
		statement.append("=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query equalTo(long value) {
		statement.append("=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query equalTo(boolean value) {
		statement.append("=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query notEqualTo(String value) {
		statement.append("!=? ");
		checkValues();
		values.add(value);
		return this;
	}
	
	public Query not() {
		statement.append("NOT ");		
		return this;
	}

    public Query in() {
        statement.append("IN ");
        return this;
    }

	public Query notEqualTo(int value) {
		statement.append("!=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}
	
	public Query unionAll() {
		statement.append("UNION ALL ");
		return this;
	}

	public Query notEqualTo(long value) {
		statement.append("!=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query notEqualTo(boolean value) {
		statement.append("!=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query lessThan(String value) {
		statement.append("<? ");
		checkValues();
		values.add(value);
		return this;
	}

	public Query lessThanEqual(String value) {
		statement.append("<=? ");
		checkValues();
		values.add(value);
		return this;
	}

	public Query lessThan(int value) {
		statement.append("<? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query lessThanEqual(int value) {
		statement.append("<=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query lessThan(long value) {
		statement.append("<? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query lessThanEqual(long value) {
		statement.append("<=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query greaterThan(String value) {
		statement.append(">? ");
		checkValues();
		values.add(value);
		return this;
	}

	public Query greaterThanEqual(String value) {
		statement.append(">=? ");
		checkValues();
		values.add(value);
		return this;
	}

	public Query greaterThan(int value) {
		statement.append(">? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query greaterThanEqual(int value) {
		statement.append(">=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query greaterThan(long value) {
		statement.append(">? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query greaterThanEqual(long value) {
		statement.append(">=? ");
		checkValues();
		values.add(String.valueOf(value));
		return this;
	}

	public Query as(String as) {
		statement.append(AS);
		statement.append(" " + as + " ");
		return this;
	}

	public Query join(String table) {
		statement.append(JOIN);
		statement.append(" " + table + " ");
		return this;
	}

	public Query on(String onstatement) {
		statement.append(ON);
		statement.append(" " + onstatement + " ");
		return this;
	}

	/**
	 * This method automatically wraps the value with the % symbol. This will match any amount of characters before and
	 * after the value. If you want more control, use like(String value, WrapType type).
	 * 
	 * @param value
	 *            *
	 */
	public Query like(String value) {
		statement.append(LIKE);
		statement.append(" ? ");
		values.add("%" + value + "%");
		return this;
	}

	/**
	 * This method doesn't wrap the value in any wildcards.
	 * 
	 * @param value
	 * @return
	 */
	public Query likeNoWild(String value) {
		statement.append(LIKE);
		statement.append(" ? ");
		values.add(value);
		return this;
	}

	/**
	 * Use this method if you want to use _, instead of % for the wildcard.
	 * 
	 * The _ only matches a single character The % matches any amount of characters
	 * 
	 * WrapType.BEFORE puts a _ before the value, and a % after WrapType.AFTER puts a % before the value, and a _ after
	 * WrapType.BOTH puts a _ before and after the value
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public Query like(String value, WrapType type) {
		statement.append(LIKE);
		statement.append(" ? ");
		switch (type) {
			case _AFTER:
				values.add(value + "_");
				break;
			case _AFTER_P:
				values.add("%" + value + "_");
				break;
			case _BEFORE:
				values.add("_" + value);
				break;
			case _BEFORE_P:
				values.add("_" + value + "%");
				break;
			case _BOTH:
				values.add("_" + value + "_");
				break;
			case AFTER:
				values.add(value + "%");
				break;
			case BEFORE:
				values.add("%" + value);
				break;
			case BOTH:
				values.add("%" + value + "%");
				break;			
		}
		return this;
	}

	/**
	 * This method automatically wraps the value with the % symbol. This will match any amount of characters before and
	 * after the value. If you want more control, use like(String value, WrapType type).
	 * 
	 * @param value
	 *            *
	 */
	public Query like(int value) {
		statement.append(LIKE);
		statement.append(" ? ");
		values.add("%" + String.valueOf(value) + "%");
		return this;
	}

	/**
	 * Use this method if you want to use _, instead of % for the wildcard.
	 * 
	 * The _ only matches a single character The % matches any amount of characters
	 * 
	 * WrapType.BEFORE puts a _ before the value, and a % after WrapType.AFTER puts a % before the value, and a _ after
	 * WrapType.BOTH puts a _ before and after the value
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public Query like(int value, WrapType type) {
		statement.append(LIKE);
		statement.append(" ? ");
		switch (type) {
			case _AFTER:
				values.add(String.valueOf(value) + "_");
				break;
			case _AFTER_P:
				values.add("%" + String.valueOf(value) + "_");
				break;
			case _BEFORE:
				values.add("_" + String.valueOf(value));
				break;
			case _BEFORE_P:
				values.add("_" + String.valueOf(value) + "%");
				break;
			case _BOTH:
				values.add("_" + String.valueOf(value) + "_");
				break;
			case AFTER:
				values.add(String.valueOf(value) + "%");
				break;
			case BEFORE:
				values.add("%" + String.valueOf(value));
				break;
			case BOTH:
				values.add("%" + String.valueOf(value) + "%");
				break;
		}
		return this;
	}

	/**
	 * This method automatically wraps the value with the % symbol. This will match any amount of characters before and
	 * after the value. If you want more control, use like(String value, WrapType type).
	 * 
	 * @param value
	 *            *
	 */
	public Query like(long value) {
		statement.append(LIKE);
		statement.append(" ? ");
		values.add("%" + String.valueOf(value) + "%");
		return this;
	}

	/**
	 * Use this method if you want to use _, instead of % for the wildcard.
	 * 
	 * The _ only matches a single character The % matches any amount of characters
	 * 
	 * WrapType.BEFORE puts a _ before the value, and a % after WrapType.AFTER puts a % before the value, and a _ after
	 * WrapType.BOTH puts a _ before and after the value
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public Query like(long value, WrapType type) {
		statement.append(LIKE);
		statement.append(" ? ");
		switch (type) {
			case _AFTER:
				values.add(String.valueOf(value) + "_");
				break;
			case _AFTER_P:
				values.add("%" + String.valueOf(value) + "_");
				break;
			case _BEFORE:
				values.add("_" + String.valueOf(value));
				break;
			case _BEFORE_P:
				values.add("_" + String.valueOf(value) + "%");
				break;
			case _BOTH:
				values.add("_" + String.valueOf(value) + "_");
				break;
			case AFTER:
				values.add(String.valueOf(value) + "%");
				break;
			case BEFORE:
				values.add("%" + String.valueOf(value));
				break;
			case BOTH:
				values.add("%" + String.valueOf(value) + "%");
				break;
		}
		return this;
	}

	public Query and(String column) {
		statement.append(AND);
		statement.append(" ");
		statement.append(column);
		statement.append(" ");
		return this;
	}

	public Query andOpp(String column) {
		statement.append(AND  + "( ");
		statement.append(" ");
		statement.append(column);
		statement.append(" ");
		return this;
	}

	/*
	 * Open parenthesis. Don't forget to call clp to close the parenthesis. This is useful when making a more robust
	 * query (for example, you want one column to match, and one of many others).
	 */
	public Query opp() {
		statement.append("( ");
		return this;
	}

	/**
	 * Close parenthesis.
	 * 
	 * @return
	 */
	public Query clp() {
		statement.append(") ");
		return this;
	}

	public Query or(String column) {
		statement.append(OR);
		statement.append(" ");
		statement.append(column);
		statement.append(" ");
		return this;
	}

	public Query orOpp(String column) {
		statement.append(OR).append(" ( ");
		statement.append(" ");
		statement.append(column);
		statement.append(" ");
		return this;
	}
	public Query orderBy(String order) {
		statement.append(ORDER);
		statement.append(" ").append(order).append(" ");
		return this;
	}

    public Query descendingOrder(String column) {
        statement.append(ORDER);
        statement.append(" ").append(column).append(" DESC ");
        return this;
    }

	public Query limit(String limit) {
		statement.append(LIMIT);
		statement.append(" ").append(limit);
		return this;
	}

	public Query limit(int limit) {
		statement.append(LIMIT);
		statement.append(" ").append(limit);
		return this;
	}

	public Query manualStatement(String statement) {
		this.statement = new StringBuilder();
		this.statement.append(statement);
		return this;
	}

	public Query manualStatement(String statement, String[] args) {
		this.statement = new StringBuilder();
		this.statement.append(statement);
		this.values = new ArrayList<String>(Arrays.asList(args));
		return this;
	}

	private void checkValues() {
		if (values == null) {
			values = new ArrayList<String>();
		}
	}

	private static String getString(String[] what) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < what.length; i++) {
			b.append(SqlDb.checkColumnName(what[i]));
			if (i != what.length - 1) {
				b.append(", ");
			} else {
				b.append(" ");
			}
		}
		return b.toString();
	}

	private static String getStringRaw(String[] what) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < what.length; i++) {
			b.append(what[i]);
			if (i != what.length - 1) {
				b.append(", ");
			} else {
				b.append(" ");
			}
		}
		return b.toString();
	}

	@Override
	public String getStatement() {
		if (statement != null) {
			return statement.toString();
		}
		return "";
	}

	@Override
	public String[] getSelectionArgs() {
		if (values == null) {
			return null;
		} else {
			return values.toArray(new String[0]);
		}
	}

}