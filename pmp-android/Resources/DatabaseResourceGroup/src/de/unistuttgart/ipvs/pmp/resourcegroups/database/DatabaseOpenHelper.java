package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    
    public DatabaseOpenHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
    }
    
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        
    }
    
}
