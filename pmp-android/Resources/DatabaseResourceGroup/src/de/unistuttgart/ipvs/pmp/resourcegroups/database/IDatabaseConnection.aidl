package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import java.util.Map;

interface IDatabaseConnection {
        
    /**
     * 
     * @param tableName
     * @param columnNames
     * @return true if successful, false otherwise
     */
    boolean createTable(String tableName, in Map columns);
    
    /**
     * Convenience method for inserting a row into the database
     * @param table
     * @param nullColumnHack
     * @param values
     * @return
     */
    long insert(String table, String nullColumnHack, in Map values);

    /**
     * Convenience method for updating rows in the database.
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return the number of rows affected
     */
    int update(String table, in Map values, String whereClause, in String[] whereArgs);

    /**
     * Convenience method for deleting rows in the database.
     * 
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int delete(String table, String whereClause, in String[] whereArgs);
    
    /**
     * Query the given table, returning a Cursor over the result set.
     * 
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return Number of rows in the result
     */
    long queryWithLimit(String table, in String[] columns, String selection, in String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
    
    /**
     * Query the given table, returning a Cursor over the result set.
     * 
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return Number of rows in the result
     */
    long query(String table, in String[] columns, String selection, in String[] selectionArgs, String groupBy, String having, String orderBy);
    
    long getRowPosition();

    String getString(int column);
    int getInteger(int column);
    double getDouble(int column);

    String[] getRowAt(int position);
    String[] getCurrentRow();
    String[] getRowAndNext();
    boolean next();
    boolean goToRowPosition(int position);
}