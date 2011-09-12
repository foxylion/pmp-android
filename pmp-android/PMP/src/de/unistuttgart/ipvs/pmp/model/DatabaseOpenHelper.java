package de.unistuttgart.ipvs.pmp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a helper for opening the database used by PMP.<br/>
 * It automatically creates the required tables in the SQLite database.<br/>
 * A database instance can be got by calling
 * {@link DatabaseOpenHelper#getWritableDatabase()}.
 * 
 * @author Jakob Jarosch
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    /**
     * Name of the database.
     */
    private static final String DB_NAME = "pmp-database";

    /**
     * Current database version.
     */
    private static final int DB_VERSION = 1;

    /**
     * The context used to open the files from assets folder.
     */
    private Context context;

    /**
     * List of all SQL-files for database-creation, the key is the version of
     * the database.
     */
    private static final String[] SQL_FILES = new String[] { null,
	    "database-v1.sql" };

    /**
     * List of all SQL-files for adding sample values to the database, the key
     * is the version of the database.
     */
    private static final String[] SAMPLE_SQL_FILES = new String[] { null,
	    "samples-v1.sql" };

    /**
     * DatabaseHelper-Constructor.
     */
    public DatabaseOpenHelper(Context context) {
	super(context, DB_NAME, null, DB_VERSION);
	this.context = context;
    }

    /**
     * Called when the database is opened first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
	Log.d("creating database structure.");

	String sqlQueries = readSqlFile(SQL_FILES[1]);

	if (sqlQueries != null) {
	    Log.d("Successfully read the database from " + SQL_FILES[1]
		    + ", executing now...");
	    executeMultipleQueries(db, sqlQueries);
	    Log.d("Created the database (with, or without errors, see above).");

	    /*
	     * Load sample data into the database if it is required by
	     * Constants.USE_SAMPLE_DATA
	     */
	    if (Constants.USE_SAMLPE_DATA) {
		Log.d("Use sample data is enabled, read the queries from "
			+ SAMPLE_SQL_FILES[1]);

		sqlQueries = readSqlFile(SAMPLE_SQL_FILES[1]);
		
		if(sqlQueries != null) {
		    Log.d("Loaded sample files, inserting them into database...");
		    executeMultipleQueries(db, sqlQueries);
		    Log.d("Inserted queries into database (with, or without errors, see above).");
		}
	    }
	}
    }

    /**
     * Called when upgrading from a previous version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	throw new UnsupportedOperationException(
		"only version 1 of database exists.");
    }

    /**
     * Read a SQL file from assets folder.
     * 
     * @param filename
     *            Filename of the SQL file.
     * 
     * @return String represented SQL query from the file. NULL if the file
     *         could not be read.
     */
    private String readSqlFile(String filename) {
	String sqlQuery = null;

	try {
	    InputStream is = context.getAssets().open(filename);
	    InputStreamReader bis = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(bis);

	    StringBuilder sb = new StringBuilder();
	    String curLine = null;
	    while ((curLine = br.readLine()) != null) {
		sb.append(curLine);
		sb.append("\n");
	    }

	    br.close();
	    bis.close();
	    br.close();

	    sqlQuery = sb.toString();

	} catch (IOException e) {
	    Log.e("Reading the SQL file from " + filename + " failed.", e);
	    sqlQuery = null;
	}

	return sqlQuery;
    }

    private void executeMultipleQueries(SQLiteDatabase db, String queries) {
	Log.v("------- SQL-Queries to be executed ------");

	for (String query : queries.split(";")) {

	    /* Skipping, empty query */
	    if (query.trim().length() == 0) {
		continue;
	    }

	    Log.v(query);

	    try {
		db.execSQL(query);
		Log.d("Query execution successful");
	    } catch (SQLException e) {
		Log.e("Got an SQLException while executing query", e);
	    }
	}

	Log.v("-------     End of SQL-Queries     ------");
    }
}
