package de.unistuttgart.ipvs.pmp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String LOG = "SQLITE";

	private static final String DB_NAME = "pmp-database";

	/**
	 * Current database version.
	 */
	private static final int DB_VERSION = 1;

	/**
	 * List of all sql-files for database-creation, the key is the version of
	 * the database.
	 */
	private static final String[] SQL_FILES = new String[] { null,
			"database-v1.sql" };

	/**
	 * DatabaseHelper-Constrcutor.
	 */
	public DatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlQuery = readSqlFile(SQL_FILES[1]);

		if (sqlQuery != null) {
			db.beginTransaction();
			db.execSQL(sqlQuery);
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new UnsupportedOperationException(
				"only version 1 of database exists.");
	}

	/**
	 * Read a sql file from assets folder.
	 * 
	 * @param filename
	 *            Filename of the sql file.
	 * 
	 * @return String represented sql querys from the file. NULL if the file
	 *         could not be read.
	 */
	private String readSqlFile(String filename) {
		String sqlQuery = null;

		try {
			InputStream is = Resources.getSystem().getAssets()
					.open("database-v1.sql");
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
			Log.e(LOG, "Reading the sql file from " + filename + " failed.");
		}

		return sqlQuery;
	}
}
