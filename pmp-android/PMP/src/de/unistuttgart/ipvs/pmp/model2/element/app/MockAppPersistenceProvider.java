package de.unistuttgart.ipvs.pmp.model2.element.app;

import android.database.sqlite.SQLiteDatabase;

/**
 * A persistence provider for {@link App}s which is disconnected from the persistence layer. Just like the name implies,
 * it's just a mock up for testing purposes.
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockAppPersistenceProvider extends AppPersistenceProvider {
    
    public MockAppPersistenceProvider(App element) {
        super(element);
    }
    
    
    @Override
    public void loadElementData(SQLiteDatabase rdb) {
        // do nothing, that's why we mock
    }
    
    
    @Override
    public void storeElementData(SQLiteDatabase wdb) {
        // do nothing, that's why we mock
    }
    
}
