package com.example.spyapp.sqlite;

import java.util.ArrayList;

import com.example.spyapp.util.Prefs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlDb {

	private final Context context;
	private DBSchema dbData;
	private SQLiteOpenHelper dbHelper;
	private SQLiteOpenHelper setHelper;
	private SQLiteDatabase db;

	public static final String ID = "_id";

	private final Object lock = new Object();
	private static final String VERSION = "dbversion";

	private String dbName;
	private int dbVersion = 1;

	private boolean batch;
	private int batchCount;
	private int maxBatchCount = 250;

	private boolean addingTable = false;
	private ArrayList<Table> addedTables = new ArrayList<Table>();

	public SqlDb(Context context) {
		this.context = context;
		dbName = context.getPackageName() + "-SQL.db";	
		
	}

	/**
	 * Use this constructor if you plan on implementing your own DB Helper class to construct the db on your own.
	 * 
	 * Make sure to call initialize() before using the db!
	 * 
	 * @param context
	 * @param openHelper
	 */
	public SqlDb(Context context, SQLiteOpenHelper openHelper) {
		this.context = context;
		this.dbData = null;
		dbName = context.getPackageName() + ".db";
		setHelper = openHelper;
	}
	
	public void setMaxBatch(int max) {
		maxBatchCount = max;
	}

	/**
	 * Set this before initializing the db for the first time, so the db gets built properly. If you are using your own
	 * SQLiteOpenHelper, this does not need to be called.
	 * 
	 * @param dbSetup
	 */
	public void setDBSchema(DBSchema dbSetup) {
		dbData = dbSetup;
	}

	public Object getLock() {
		return lock;
	}

	public void setDBName(String name) {
		dbName = name;
	}

	public boolean inBatchMode() {
		return batch;
	}

    public String getDBName() {
        return dbName;
    }

	public String getDBPath() {
		if (db != null) {
			return db.getPath();
		} else {
			throw new IllegalStateException("SQL database is not initialized!");
		}
	}
	
	public String getVerKey() {
		return dbName + "_" + VERSION;
	}

	/**
	 * This method must be called before accessing the database in any way. This loads the database, and creates it, if
	 * necessary.
	 */
	public void initialize() {
		// Make sure to fill in dbdata object on initialize
		dbVersion = new Prefs(context).GetInt(getVerKey(), 1);
		if (setHelper != null) {
			dbHelper = setHelper;
		} else {
			dbHelper = new SQLOpenHelper(context);			
		}
		try {
			db = dbHelper.getWritableDatabase();
			dbHelper.onCreate(db);
		} catch (SQLiteException e) {
			if (e.getMessage().contains("downgrade database from version")) {
				String emsg = e.getMessage();
				String vs = emsg.split("downgrade database from version")[1].trim();
				String[] versions = vs.split("to");
				dbVersion = Integer.parseInt(versions[0].trim());
				new Prefs(context).StoreInt(getVerKey(), dbVersion);
				if (setHelper == null) {
					dbHelper = new SQLOpenHelper(context);
				}
				db = dbHelper.getWritableDatabase();
			} else {
				throw new SQLException(e.getMessage());
			}
		}
		if (dbData == null) {
			dbData = getDbData(this);
		}
	}
	
	public void initialize(boolean loadSchema) {
		dbVersion = new Prefs(context).GetInt(getVerKey(), 1);
		if (setHelper != null) {
			dbHelper = setHelper;
		} else {			
			dbHelper = new SQLOpenHelper(context);			
		}
		db = dbHelper.getWritableDatabase();
		if (loadSchema || dbData == null) {
			dbData = getDbData(this);
		}
	}

	public void startBatch() {
		batch = true;
		db.beginTransaction();
	}

	public void endBatch() {
		batch = false;
		try {
			db.setTransactionSuccessful();
			db.endTransaction();
			batchCount = 0;
		} catch (IllegalStateException e) {
		}
	}

	public long insert(String table, String... data) {
		synchronized (lock) {
			checkBatch();
			if (data != null) {
				int amt = data.length;
				ContentValues values = new ContentValues();
				for (int i = 0; i < amt; i++) {
					Table t = dbData.getTable(table);
					String column = checkColumnName(t.getColumns().get(i).getName());
					values.put(column, data[i]);
				}
				try {
					long rowId = db.insert(checkTableName(table), null, values);
					return rowId;
				} catch (SQLException e) {
					Log.e("SqlDb", "Unable to insert data. Either the table doesn't exist, or incorrect amount of data items." + table);
					e.printStackTrace();
					return -1;
				}
			} else {
				try {
					long rowid = db.insert(checkTableName(table), null, null);
					return rowid;
				} catch (SQLException e) {
					Log.e("SqlDb", "Unable to insert data. Either the table doesn't exist, or incorrect amount of data items." + table);
					e.printStackTrace();
					return -1;
				}
			}
		}
	}

	public long insert(String table, ContentValues values) {
		synchronized (lock) {
			checkBatch();
			try {
				long rowId = db.insert(checkTableName(table), null, values);
				return rowId;
			} catch (SQLException e) {
				Log.e("SqlDb", "Unable to insert data. Either the table doesn't exist, or incorrect amount of data items." + table);
				e.printStackTrace();
				return -1;
			}
		}
	}

	public void update(String table, String column, long rowId, String data) {
		synchronized (lock) {
			checkBatch();
			ContentValues values = new ContentValues();
			values.put(column, data);
			String where = "_id=?";
			String[] whereArgs = { String.valueOf(rowId) };
			db.update(checkTableName(table), values, where, whereArgs);
		}
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		synchronized (lock) {
			return db.delete(checkTableName(table), whereClause, whereArgs);
		}
	}

	public void removeRow(String table, long rowId) {
		checkBatch();
		synchronized (lock) {
			db.delete(checkTableName(table), "_id=?", new String[] { String.valueOf(rowId) });
		}
	}

	public int update(String table, ContentValues values, String where, String[] whereArgs) {
		synchronized (lock) {
			return db.update(checkTableName(table), values, where, whereArgs);
		}
	}

	public Cursor query(BaseQuery query) {
		return db.rawQuery(query.getStatement(), query.getSelectionArgs());
	}

	public Cursor rawQuery(String query, String[] selectionArgs) {
		return db.rawQuery(query, selectionArgs);
	}

	public void openDBFile(String path) {
		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		dbData = getDbData(this);
		dbVersion = db.getVersion();
		new Prefs(context).StoreInt(getVerKey(), dbVersion);
	}

	/**
	 * Adds a column to an existing table in a database.
	 * 
	 * @param table
	 *            - The table to add the column to
	 * @param columnName
	 *            - The name of the new column
	 */
	public boolean addColumn(String table, String columnName) {
		try {
			db.execSQL("ALTER TABLE " + checkTableName(table) + " ADD COLUMN " + checkColumnName(columnName));
			//dbVersion++;
			//new Prefs(context).StoreInt(getVerKey(), dbVersion);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean addIntColumn(String table, String columnName, int defaultValue) {
		try {
			db.execSQL("ALTER TABLE " + checkTableName(table) + " ADD COLUMN " + checkColumnName(columnName) + " INT default " + defaultValue);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void closeDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		if (dbHelper != null) {
			dbHelper.close();
		}
		SQLiteDatabase.releaseMemory();
	}
	
	public boolean isOpen() {
		return db != null && db.isOpen();
	}
	
	public static void clearMemory() {
		SQLiteDatabase.releaseMemory();
	}

	/*
	 * This allows you to add a table to an already existing database.
	 */
	public void addTableToExistingDatabase(Table table) {
		dbVersion++;
		new Prefs(context).StoreInt(getVerKey(), dbVersion);
		addedTables.add(table);
		addingTable = true;
		closeDb();
		initialize();
	}
	
	/*
	 * This allows you to add a table to an already existing database.
	 */
	public void addTableToExistingDatabase(String name, String[] columns) {
		dbVersion++;
		new Prefs(context).StoreInt(getVerKey(), dbVersion);
		addedTables.add(new Table(name).addColumn(columns));
		addingTable = true;
		closeDb();
		initialize();
	}
	
	/**
	 * Use this method if you need to rename a table
	 * 
	 * @param oldTable
	 *            - The old table to rename
	 * @param newTable
	 *            - The new table name
	 * @return - true if the oldtable name existed, not necessarily if the
	 *         rename succeeded.
	 */
	public void renameTable(String oldTable, String newTable) {
		db.execSQL("ALTER TABLE " + checkTableName(oldTable) + " RENAME TO " + checkTableName(newTable));		
	}

	public SQLiteDatabase getSQLiteDb() {
		return db;
	}

	public SQLiteOpenHelper getDBHelper() {
		return dbHelper;
	}

	public int getDBVersion() {
		return dbVersion;
	}

	public void setDBVersion(int version) {
		dbVersion = version;
		new Prefs(context).StoreInt(getVerKey(), dbVersion);
	}

	public int clearTable(String table) {
		int rmvd = -1;
		synchronized (lock) {
			rmvd = db.delete(checkTableName(table), "1", null);
			db.execSQL("VACUUM");
			db.close();
			initialize();
		}
		return rmvd;
	}

	private void checkBatch() {
		if (batch) {
			if (batchCount >= maxBatchCount) {
				endBatch();
				startBatch();
			}
			batchCount++;
		}
	}

	/**
	 * Check if an index exists in the db.
	 * 
	 * @param indexName
	 * @return
	 */
	public boolean indexExists(String indexName) {
		boolean b = false;
		synchronized (lock) {
			Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type=? AND name=?", new String[] { "index", indexName });
			if (c != null) {
				b = c.moveToFirst();
				c.close();
			}
		}
		return b;
	}

	/**
	 * Creates a simple index on the given table, with the given indexname, for the given column There is no need to
	 * call indexExists(), as this method checks it first.
	 * 
	 * @param table
	 * @param columnToIndex
	 * @param indexName
	 */
	public void createIndex(String table, String columnToIndex, String indexName) {
		if (!indexExists(indexName)) {
			StringBuilder b = new StringBuilder();
			b.append("CREATE INDEX ").append(indexName).append(" ON ").append(checkTableName(table)).append("(").
				append(columnToIndex).append(")");
			db.execSQL(b.toString());
		}
	}
	
	public void deleteIndex(String indexName) {
		if (indexExists(indexName)) {
			db.execSQL("DROP INDEX " + indexName + ";");
		}
	}

	/**
	 * 
	 * Deletes the table from the database. This will delete all data in the table as well.
	 * 
	 * @param table
	 *            The table to delete
	 * @return False if the table wasnt found.
	 */
	public boolean deleteTable(String table) {
		synchronized (lock) {
			try {
				db.execSQL("DROP TABLE IF EXISTS " + checkTableName(table));
				db.close();
				db = null;
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public void execSQL(String sqlString) {
		db.execSQL(sqlString);
	}

	public ArrayList<String> getTableNames() {
		return getTableNames(this);
	}

	public ArrayList<String> getColumnNames(String table) {
		return getColumnNames(this, table);
	}

	public DBSchema getDBSchema(boolean scan) {
		if (!scan && dbData != null) {
			return dbData;
		} else {
			return getDbData(this);
		}
	}

	private static DBSchema getDbData(SqlDb db) {
		DBSchema dbData = new DBSchema();
		ArrayList<String> tableNames = getTableNames(db);
		int size = tableNames.size();
		for (int i = 0; i < size; i++) {
			final Cursor cursor = db.query(new Query().select().from(tableNames.get(i)).limit(1));
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					Table t = new Table(tableNames.get(i).trim());
					String[] columns = cursor.getColumnNames();
					for (String c : columns) {
						t.addColumn(c);
					}
					dbData.addTable(t);
				}
				cursor.close();
			}
		}
		return dbData;
	}

	public static ArrayList<String> getTableNames(SqlDb db) {
		ArrayList<String> tables = new ArrayList<String>();
		Object lock = db.getLock();
		synchronized (lock) {
			final Cursor cursor = db.query(new Query().select("name").from("sqlite_master").where("type").equalTo("'table'"));
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						String table = cursor.getString(cursor.getColumnIndex("name"));
						if (!table.equals("android_metadata") && !table.equals("dummy") && !table.equals("sqlite_sequence") && !table.equals("_id")) {
							tables.add(table);
						}
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		}
		return tables;
	}

	public boolean columnExists(String table, String columnName) {
		int i = -1;
		synchronized (lock) {
			Cursor cursor = db.rawQuery("SELECT * FROM " + checkTableName(table) + " LIMIT 0, 1", null);
			i = cursor.getColumnIndex(columnName);
			cursor.close();
		}
		return i != -1;
	}

	public static ArrayList<String> getColumnNames(SqlDb db, String table) {
		return getDbData(db).getTable(table).getColumnNames();
	}

	public static String checkTableName(String name) {
		String n = name.trim();
		n = n.replace("'", "");
		n = "'" + n + "'";
		return n;
	}

	public static String checkColumnName(String name) {
		if (name.startsWith("\"") && name.endsWith("\"")) {
			return name;
		} else {
			name = name.replace("\"", "");
			return "\"" + name + "\"";
		}
	}

	private static String generateCreateStatement(Table table) {
		StringBuilder b = new StringBuilder();
		ArrayList<Column> columns = table.getColumns();
		int size = columns.size();
		b.append(" CREATE TABLE IF NOT EXISTS ");
		b.append(checkTableName(table.getName()));
		b.append(" (");
		b.append(checkColumnName(table.getIdColumn()));
		b.append(" INTEGER PRIMARY KEY, ");
		for (int i = 0; i < size; i++) {
			Column c = columns.get(i);
			b.append(checkColumnName(c.getName()));
			b.append(" ");
			b.append(c.getDataType());
			if (i != size - 1) {
				b.append(", ");
			}
		}
		b.append(")");
		return b.toString();
	}

	private class SQLOpenHelper extends SQLiteOpenHelper {

		public SQLOpenHelper(Context context) {
			super(context, dbName, null, dbVersion);
			if (dbData == null) {
				throw new RuntimeException("Table name list is empty! Can't create database.");
			}
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			int cnt = dbData.tableCount();
			for (int i = 0; i < cnt; i++) {
				db.execSQL(generateCreateStatement(dbData.next()));
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (addingTable) {
				for (Table tbl : addedTables) {
					db.execSQL(generateCreateStatement(tbl));
				}
				addedTables.clear();
				addingTable = false;
			} else {
				Log.w(SQLOpenHelper.class.getName(), "Upgrading database " + dbName + " from version " + oldVersion + " to " +
						newVersion + ".");
				int tblcnt = dbData.tableCount();
				// TODO - Right now this is a destructive upgrade. It wipes
				// the old structure, and creates the new, destroying old data in
				// the process.
				for (int i = 0; i < tblcnt; i++) {
					db.execSQL("DROP TABLE IF EXISTS " + checkTableName(dbData.getTable(i).getName()));
				}
				onCreate(db);
			}
		}
	}

}