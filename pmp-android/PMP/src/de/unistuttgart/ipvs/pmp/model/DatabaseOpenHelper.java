package de.unistuttgart.ipvs.pmp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.unistuttgart.ipvs.pmp.Log;

import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;

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

	String sqlQuery = readSqlFile(SQL_FILES[1]);

	if (sqlQuery != null) {
	    Log.d("Successfully read the database from " + SQL_FILES[1]
		    + ", executing now...");
	    
	    Log.v("------- SQL-Querys to be executed ------");
	    Log.v(sqlQuery);
	    Log.v("-------     End of SQL-Querys     ------");
	    
	    db.beginTransaction();
	    try {
		db.execSQL(sqlQuery);
	    } catch (SQLException e) {
		Log.e("Got an SQLException while creating the database", e);
	    } finally {
		Log.d("Created the database");
	    }
	    db.endTransaction();
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
	}

	return sqlQuery;
    }
}
