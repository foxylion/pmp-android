package de.unistuttgart.ipvs.pmp.model.element;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public abstract class MockElementPersistenceProvider<T extends ModelElement> extends ElementPersistenceProvider<T> {
    
    public MockElementPersistenceProvider(T element) {
        super(element);
    }
    
    
    @Override
    public abstract void loadElementData();
    
    
    @Override
    public void storeElementData() {
    }
    
    
    @Override
    public void deleteElementData() {
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
    }
    
}
