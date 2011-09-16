package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
import android.os.RemoteException;

public class DatabaseConnectionImpl extends IDatabaseConnection.Stub{
    
    public DatabaseConnectionImpl() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public boolean createTable(String tableName, String[] columnNames)
	    throws RemoteException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public long insert(String table, String nullColumnHack, String[] values)
	    throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int update(String table, String[] values, String whereClause,
	    String[] whereArgs) throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs)
	    throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public long queryWithLimit(String table, String[] columns,
	    String selection, String[] selectionArgs, String groupBy,
	    String having, String orderBy, String limit) throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public long query(String table, String[] columns, String selection,
	    String[] selectionArgs, String groupBy, String having,
	    String orderBy) throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public long getRowPosition() throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public String getString(int column) throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getInteger(int column) throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public double getDouble(int column) throws RemoteException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public String[] getRowAt(int position) throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String[] getCurrentRow() throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String[] getRowAndNext() throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean next() throws RemoteException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isNext() throws RemoteException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean goToRowAt(int position) throws RemoteException {
	// TODO Auto-generated method stub
	return false;
    }
}
