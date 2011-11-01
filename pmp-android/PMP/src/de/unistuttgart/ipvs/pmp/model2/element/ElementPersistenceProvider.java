package de.unistuttgart.ipvs.pmp.model2.element;

import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model2.ModelCache;
import de.unistuttgart.ipvs.pmp.model2.PersistenceProvider;

/**
 * General persistence provider for one model element.
 * 
 * @param T
 *            {@link ModelElement} to provide persistence for
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ElementPersistenceProvider<T extends ModelElement> extends PersistenceProvider {
    
    protected T element;
    
    
    /**
     * Initializes this persistence provider to explicitly use the same database connection and {@link ModelCache} as
     * the main {@link PersistenceProvider}.
     * 
     * @param element
     *            seems redundant, but is the only way to circumvent Java Generic problems
     */
    public ElementPersistenceProvider(T element) {
        super(PersistenceProvider.getInstance());
        this.element = element;
    }
    
    
    /**
     * Internal call to activate loading of element data. Do not override this.
     */
    public void loadElementData() {
        SQLiteDatabase rdb = getDoh().getReadableDatabase();
        loadElementData(rdb);
        rdb.close();
    }
    
    
    /**
     * Internal call to activate storing of element data. Do not override this.
     */
    public void storeElementData() {
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        storeElementData(wdb);
        wdb.close();
    }
    
    
    /**
     * Loads the data of this element from the persistence.
     * 
     * @param rdb
     *            a correctly initialized readable {@link SQLiteDatabase} that will be closed afterwards
     */
    protected abstract void loadElementData(SQLiteDatabase rdb);
    
    
    /**
     * Stores the data of <b>THIS</b> element and nothing else to the persistence.
     * 
     * @param wdb
     *            a correctly initialized writable {@link SQLiteDatabase} that will be closed afterwards
     */
    protected abstract void storeElementData(SQLiteDatabase wdb);
    
}
