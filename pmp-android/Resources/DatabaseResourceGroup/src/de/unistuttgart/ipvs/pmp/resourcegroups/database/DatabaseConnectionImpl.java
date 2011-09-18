package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

@SuppressWarnings("rawtypes")
public class DatabaseConnectionImpl extends IDatabaseConnection.Stub {

	private final String TABLE_NAME = "^[a-zA-Z0-9_]+$";
	private static final String COLUMN_NAME = "^((id ){0,1}[a-z0-9_]+)$";
	private static final String COLUMN_TYPE = "^[a-zA-Z0-9_\\s]+$";
	private static final String TABLE_CONSTRAINTS = "^[a-zA-Z0-9_\\s]+$";

	private String dbName;
	private int dbVersion;
	private String appID;
	private String exceptionMessage;

	private Context context;
	private DatabaseResource resource;
	private DatabaseOpenHelper helper;
	private SQLiteDatabase db;

	private Cursor cursor;

	public DatabaseConnectionImpl(Context context, DatabaseResource resource,
			String appIdentifier) {
		this.context = context;
		this.resource = resource;
		appID = appIdentifier;
		dbName = appIdentifier; // TODO Allow access to other databases
		exceptionMessage = context.getResources().getString(
				R.string.unauthorized_action_exception);
	}

	private void closeCursor() {
		if (cursor != null) {
			cursor.close();
		}
	}

	@Override
	public boolean createTable(String tableName, Map columns,
			String tableConstraint) throws RemoteException {
		if (isCreate()) {
			if ("".equals(tableName.trim()) || columns == null) {
				return false;
			}
			if (columns.isEmpty()) {
				return false;
			}

			// Check the tableName
			tableName = strip(tableName);
			if (!Pattern.matches(TABLE_NAME, tableName)) {
				Log.d("Table name '" + tableName + "' invalid. " + TABLE_NAME);
				return false;
			}

			// Build a SQL Statement
			String s = "";
			String sql = "CREATE TABLE IF NOT EXISTS " + dbName + "."
					+ tableName + " (";
			Pattern pColName = Pattern.compile(COLUMN_NAME,
					Pattern.CASE_INSENSITIVE);
			Matcher m1 = pColName.matcher("");
			Pattern pType = Pattern.compile(COLUMN_TYPE,
					Pattern.CASE_INSENSITIVE);
			Matcher m2 = pType.matcher("");

			// Check the column descriptions
			for (Object obj : columns.entrySet()) {
				Entry<String, String> e = (Entry<String, String>) obj;
				if (m1.reset(strip(e.getKey())).find()
						&& m2.reset(strip(e.getValue())).find()) {
					s += strip(e.getKey()) + " " + strip(e.getValue()) + ", ";
				}
			}
			if (tableConstraint != null) {
				Pattern p = Pattern.compile(TABLE_CONSTRAINTS,
						Pattern.CASE_INSENSITIVE);
				m1 = p.matcher(strip(tableConstraint));
				if (m1.find()) {
					s += strip(tableConstraint) + ")";
				}
			} else {
				s = s.substring(0, s.length() - 3) + ")";
			}
			closeCursor();
			openDB();
			cursor = db.rawQuery(sql, null);

			// TODO Check table's existence
			// SELECT name FROM sqlite_master WHERE type='table' AND
			// name='table_name';
			if (cursor.getCount() > 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public int delete(String table, String whereClause, String[] whereArgs)
			throws RemoteException {
		if (isModify()) {
			openDB();
			return db.delete(table, whereClause, whereArgs);
		} else {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		}
	}

	private ContentValues getContentValues(Map values) {
		if (values == null || values.isEmpty()) {
			return null;
		}
		ContentValues cv = new ContentValues();
		for (Object value : values.entrySet()) {
			// TODO test if set is in right order??????????????????????????????
			Entry e = (Entry) value;
			cv.put(e.getKey().toString(), e.getValue().toString());
		}
		return cv;
	}

	@Override
	public String[] getCurrentRow() throws RemoteException {
		if (cursor == null || !isRead()) {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		} else if (cursor.isAfterLast() || cursor.isBeforeFirst()) {
			return null;
		} else {
			String[] row = new String[cursor.getColumnCount()];
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				row[i] = cursor.getString(i);
			}
			return row;
		}
	}

	@Override
	public double getDouble(int column) throws RemoteException {
		if (cursor == null || !isRead()) {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		} else if (cursor.getColumnCount() <= column) {
			RemoteException ex = new RemoteException();
			ex.initCause(new IndexOutOfBoundsException());
			throw ex;
		} else {
			return (cursor.getDouble(column));
		}
	}

	@Override
	public int getInteger(int column) throws RemoteException {
		if (cursor == null || !isRead()) {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		} else if (cursor.getColumnCount() <= column) {
			RemoteException ex = new RemoteException();
			ex.initCause(new IndexOutOfBoundsException());
			throw ex;
		} else {
			return (cursor.getInt(column));
		}
	}

	@Override
	public String[] getRowAndNext() throws RemoteException {
		if (cursor == null || !isRead()) {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		} else if (cursor.isAfterLast() || cursor.isBeforeFirst()) {
			return null;
		} else {
			String[] row = new String[cursor.getColumnCount()];
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				row[i] = cursor.getString(i);
			}
			cursor.moveToNext();
			return row;
		}

	}

	@Override
	public String[] getRowAt(int position) throws RemoteException {
		if (cursor == null || !isRead()) {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		} else if ((cursor.getCount() <= position) || (position < 0)) {
			return null;
		} else {
			cursor.moveToPosition(position);
			String[] row = new String[cursor.getColumnCount()];
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				row[i] = cursor.getString(i);
			}
			return row;
		}
	}

	@Override
	public long getRowPosition() throws RemoteException {
		if (cursor != null || !isRead()) {
			return cursor.getPosition();
		} else
			return -1;
	}

	@Override
	public String getString(int column) throws RemoteException {
		if (cursor == null || !isRead()) {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		} else if (cursor.getColumnCount() <= column) {
			RemoteException ex = new RemoteException();
			ex.initCause(new IndexOutOfBoundsException());
			throw ex;
		} else {
			return cursor.getString(column);
		}
	}

	@Override
	public boolean goToRowPosition(int position) throws RemoteException {
		if (cursor == null || !isRead()) {
			return false;
		} else {
			return cursor.moveToPosition(position);
		}
	}

	@Override
	public long insert(String table, String nullColumnHack, Map values)
			throws RemoteException {
		if (isModify()) {
			openDB();
			return db.insert(table, nullColumnHack, getContentValues(values));
		} else {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		}
	}

	private boolean isCreate() {
		return ((BooleanPrivacyLevel) resource
				.getPrivacyLevel(DatabaseResourceGroup.PRIVACY_LEVEL_CREATE))
				.permits(appID, true);
	}

	private boolean isModify() {
		return ((BooleanPrivacyLevel) resource
				.getPrivacyLevel(DatabaseResourceGroup.PRIVACY_LEVEL_MODIFY))
				.permits(appID, true);
	}

	private boolean isRead() {
		return ((BooleanPrivacyLevel) resource
				.getPrivacyLevel(DatabaseResourceGroup.PRIVACY_LEVEL_READ))
				.permits(appID, true);
	}

	@Override
	public boolean next() throws RemoteException {
		if (cursor == null || !isRead()) {
			return false;
		} else {
			return cursor.moveToNext();
		}
	}

	private void openDB() {
		if (db == null) {
			// Open a database connection
			if (helper == null) {
				// TODO Feature: open other databases
				helper = new DatabaseOpenHelper(context, dbName, dbVersion);
			}
			db = helper.getWritableDatabase();
		}
	}

	@Override
	public long query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) throws RemoteException {
		if (isRead()) {
			closeCursor();
			openDB();
			cursor = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
			cursor.moveToFirst();
			return cursor.getCount();
		} else {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		}
	}

	@Override
	public long queryWithLimit(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) throws RemoteException {
		if (isRead()) {
			closeCursor();
			openDB();
			cursor = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy, limit);
			return cursor.getCount();
		} else {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		}
	}

	private String strip(String s) {
		return s.trim().replaceAll("(\\s+)", " ");
	}

	@Override
	public int update(String table, Map values, String whereClause,
			String[] whereArgs) throws RemoteException {
		if (isModify()) {
			openDB();
			return db.update(table, getContentValues(values), whereClause,
					whereArgs);
		} else {
			RemoteException ex = new RemoteException();
			ex.initCause(new UnauthorizedActionException(exceptionMessage));
			throw ex;
		}
	}

	@Override
	public void close() throws RemoteException {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		if (db != null) {
			db.close();
			db = null;
		}
		if (helper != null) {
			helper.close();
			helper = null;
		}
	}

	@Override
	public boolean deleteTable(String tableName) throws RemoteException {
		if (isCreate()) {
			if ("".equals(tableName.trim())) {
				return false;
			}

			// Check the tableName
			tableName = strip(tableName);
			if (!Pattern.matches(TABLE_NAME, tableName)) {
				Log.d("Table name '" + tableName + "' invalid. " + TABLE_NAME);
				return false;
			}

			// Build a SQL Statement
			String sql = "DROP TABLE IF EXISTS " + dbName + "." + tableName;
			closeCursor();
			openDB();
			cursor = db.rawQuery(sql, null);

			// TODO Check table's existence
			// SELECT name FROM sqlite_master WHERE type='table' AND
			// name='table_name';
			if (cursor.getCount() >= 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}