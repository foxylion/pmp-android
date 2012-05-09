package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import java.util.Map;

import android.os.RemoteException;

public class DatabaseConnectionMockImpl extends IDatabaseConnection.Stub {
    
    @Override
    public void open(String databaseName) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void close() throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public boolean isTableExisted(String tableName) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean createTable(String tableName, Map columns, String tableConstraint) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean deleteTable(String tableName) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public long insert(String table, String nullColumnHack, Map values) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public int update(String table, Map values, String whereClause, String[] whereArgs) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public int delete(String table, String whereClause, String[] whereArgs) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public long queryWithLimit(String table, String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy, String limit) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public long query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public long getRowPosition() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public String getAsString(int column) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public int getAsInteger(int column) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public double getAsDouble(int column) throws RemoteException {
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
    public boolean isAfterLast() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean next() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean goToRowPosition(int position) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
}
