package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;

@SuppressWarnings("rawtypes")
public class DatabaseConnectionImpl extends IDatabaseConnection.Stub {
    
    private final String TABLE_NAME = "^[a-zA-Z0-9_]+$";
    private final String COLUMN_NAME = "^((id ){0,1}[a-z0-9_]+)$";
    private final String COLUMN_TYPE = "^[a-zA-Z0-9_\\s]+$";
    private final String TABLE_CONSTRAINTS = "^[a-zA-Z0-9_\\s]+$";
    
    private String dbName;
    private int dbVersion = 1;
    private String appID;
    private String exceptionMessage;
    
    private Context context;
    private DatabaseResource resource;
    private DatabaseOpenHelper helper;
    private SQLiteDatabase db;
    
    private Cursor cursor;
    
    
    public DatabaseConnectionImpl(Context context, DatabaseResource resource, String appIdentifier) {
        this.context = context;
        this.resource = resource;
        this.appID = appIdentifier;
        
        String[] appIdentSplit = this.appID.split("\\.");
        String tmp = "";
        for (String oneSplit : appIdentSplit) {
            tmp = tmp + oneSplit;
        }
        // TODO Feature: Allow access to other databases
        // TODO Feature: New Privacy Level for the maximum size of the DB.
        this.dbName = tmp;
        Log.v("Database name : " + this.dbName);
        this.exceptionMessage = context.getResources().getString(R.string.unauthorized_action_exception);
    }
    
    
    private void closeCursor() {
        if (this.cursor != null) {
            this.cursor.close();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean createTable(String tableName, Map columns, String tableConstraint) throws RemoteException {
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
            StringBuffer s = new StringBuffer("");
//            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
            String sql = "CREATE TABLE " + tableName + " (";
            Pattern pColName = Pattern.compile(COLUMN_NAME, Pattern.CASE_INSENSITIVE);
            Matcher m1 = pColName.matcher("");
            Pattern pType = Pattern.compile(COLUMN_TYPE, Pattern.CASE_INSENSITIVE);
            Matcher m2 = pType.matcher("");
            
            // Check the column descriptions
            for (Object obj : columns.entrySet()) {
                Entry<String, String> e = (Entry<String, String>) obj;
                if (m1.reset(strip(e.getKey())).find() && m2.reset(strip(e.getValue())).find()) {
                    s.append(strip(e.getKey())).append(" ").append(strip(e.getValue())).append(", ");
                } else {
                    Log.e("Column description is not valid: " + e.toString());
                    return false;
                }
            }
            if (tableConstraint != null) {
                Pattern p = Pattern.compile(TABLE_CONSTRAINTS, Pattern.CASE_INSENSITIVE);
                m1 = p.matcher(strip(tableConstraint));
                if (m1.find()) {
                    s.append(strip(tableConstraint)).append(")");
                }
            } else {
                s.delete(s.length() - 2, s.length()).append(")");
            }
            sql = sql + s;
            closeCursor();
            openDB();
            Log.v("Create table SQL query: " + sql);
            this.cursor = this.db.rawQuery(sql, null);
            String[] args = new String[1];
            args[0] = strip(tableName);
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", args);
            Log.e(cursor.toString());
            // SELECT name FROM sqlite_master WHERE type='table' AND
            // name='table_name';
            Log.v("Created " + cursor.getCount() + " table(s).");
            if (this.cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    
    @Override
    public int delete(String table, String whereClause, String[] whereArgs) throws RemoteException {
        if (isModify()) {
            openDB();
            return this.db.delete(table, whereClause, whereArgs);
        } else {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        }
    }
    
    
    // TODO Check content type!
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
        if (this.cursor == null || !isRead()) {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        } else if (this.cursor.isAfterLast() || this.cursor.isBeforeFirst()) {
            return null;
        } else {
            String[] row = new String[this.cursor.getColumnCount()];
            for (int i = 0; i < this.cursor.getColumnCount(); i++) {
                row[i] = this.cursor.getString(i);
            }
            return row;
        }
    }
    
    
    @Override
    public double getAsDouble(int column) throws RemoteException {
        if (this.cursor == null || !isRead()) {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        } else if (this.cursor.getColumnCount() <= column) {
            RemoteException ex = new RemoteException();
            ex.initCause(new IndexOutOfBoundsException());
            throw ex;
        } else {
            return (this.cursor.getDouble(column));
        }
    }
    
    
    @Override
    public int getAsInteger(int column) throws RemoteException {
        if (this.cursor == null || !isRead()) {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        } else if (this.cursor.getColumnCount() <= column) {
            RemoteException ex = new RemoteException();
            ex.initCause(new IndexOutOfBoundsException());
            throw ex;
        } else {
            return (this.cursor.getInt(column));
        }
    }
    
    
    @Override
    public String[] getRowAndNext() throws RemoteException {
        if (!isRead()) {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        } else if (this.cursor == null) {
            return null;
        } else if (this.cursor.isAfterLast() || this.cursor.isBeforeFirst()) {
            return null;
        } else {
            String[] row = new String[this.cursor.getColumnCount()];
            for (int i = 0; i < this.cursor.getColumnCount(); i++) {
                row[i] = this.cursor.getString(i);
            }
            this.cursor.moveToNext();
            return row;
        }
        
    }
    
    
    @Override
    public String[] getRowAt(int position) throws RemoteException {
        if (this.cursor == null || !isRead()) {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        } else if ((this.cursor.getCount() <= position) || (position < 0)) {
            return null;
        } else {
            this.cursor.moveToPosition(position);
            String[] row = new String[this.cursor.getColumnCount()];
            for (int i = 0; i < this.cursor.getColumnCount(); i++) {
                row[i] = this.cursor.getString(i);
            }
            return row;
        }
    }
    
    
    @Override
    public long getRowPosition() throws RemoteException {
        if (this.cursor != null || !isRead()) {
            return this.cursor.getPosition();
        } else {
            return -1;
        }
    }
    
    
    @Override
    public String getAsString(int column) throws RemoteException {
        if (this.cursor == null || !isRead()) {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        } else if (this.cursor.getColumnCount() <= column) {
            RemoteException ex = new RemoteException();
            ex.initCause(new IndexOutOfBoundsException());
            throw ex;
        } else {
            return this.cursor.getString(column);
        }
    }
    
    
    @Override
    public boolean goToRowPosition(int position) throws RemoteException {
        if (this.cursor == null || !isRead()) {
            return false;
        } else {
            return this.cursor.moveToPosition(position);
        }
    }
    
    
    @Override
    public long insert(String table, String nullColumnHack, Map values) throws RemoteException {
        if (isModify()) {
            openDB();
            return this.db.insert(table, nullColumnHack, getContentValues(values));
        } else {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        }
    }
    
    
    private boolean isCreate() {
        return ((BooleanPrivacyLevel) this.resource.getPrivacyLevel(DatabaseResourceGroup.PRIVACY_LEVEL_CREATE))
                .permits(this.appID, true);
    }
    
    
    private boolean isModify() {
        return ((BooleanPrivacyLevel) this.resource.getPrivacyLevel(DatabaseResourceGroup.PRIVACY_LEVEL_MODIFY))
                .permits(this.appID, true);
    }
    
    
    private boolean isRead() {
        return ((BooleanPrivacyLevel) this.resource.getPrivacyLevel(DatabaseResourceGroup.PRIVACY_LEVEL_READ)).permits(
                this.appID, true);
    }
    
    
    @Override
    public boolean next() throws RemoteException {
        if (this.cursor == null || !isRead()) {
            return false;
        } else {
            return this.cursor.moveToNext();
        }
    }
    
    
    private void openDB() {
        if (this.db == null) {
            // Open a database connection
            if (this.helper == null) {
                // TODO Feature: open other databases
                this.helper = new DatabaseOpenHelper(this.context, this.dbName, this.dbVersion);
            }
            this.db = this.helper.getWritableDatabase();
        }
    }
    
    
    @Override
    public long query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy) throws RemoteException {
        if (isRead()) {
            closeCursor();
            openDB();
            // TODO Fix Bug
            this.cursor = this.db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            this.cursor.moveToFirst(); // TODO movable?
            return this.cursor.getCount();
        } else {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        }
    }
    
    
    @Override
    public long queryWithLimit(String table, String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy, String limit) throws RemoteException {
        if (isRead()) {
            closeCursor();
            openDB();
            this.cursor = this.db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            this.cursor.moveToFirst();
            return this.cursor.getCount();
        } else {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        }
    }
    
    
    private String strip(String s) {
        return s.trim().replaceAll("(\\s+)", " ");
    }
    
    
    @Override
    public int update(String table, Map values, String whereClause, String[] whereArgs) throws RemoteException {
        if (isModify()) {
            openDB();
            return this.db.update(table, getContentValues(values), whereClause, whereArgs);
        } else {
            RemoteException ex = new RemoteException();
            ex.initCause(new UnauthorizedActionException(this.exceptionMessage));
            throw ex;
        }
    }
    
    
    @Override
    public void close() throws RemoteException {
        if (this.cursor != null) {
            this.cursor.close();
            this.cursor = null;
        }
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
        if (this.helper != null) {
            this.helper.close();
            this.helper = null;
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
            if (!Pattern.matches(this.TABLE_NAME, tableName)) {
                Log.d("Table name '" + tableName + "' invalid. " + this.TABLE_NAME);
                return false;
            }
            
            // Build a SQL Statement
            String sql = "DROP TABLE IF EXISTS " + this.dbName + "." + tableName;
            closeCursor();
            openDB();
            this.cursor = this.db.rawQuery(sql, null);
            
            // TODO Check table's existence
            // SELECT name FROM sqlite_master WHERE type='table' AND
            // name='table_name';
            if (this.cursor.getCount() >= 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isAfterLast() throws RemoteException {
        if (cursor == null) {
            return true;
        } else {
            return cursor.isAfterLast();
        }
    }
}