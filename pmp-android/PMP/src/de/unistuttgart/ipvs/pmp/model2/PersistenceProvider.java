package de.unistuttgart.ipvs.pmp.model2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model2.element.app.App;
import de.unistuttgart.ipvs.pmp.model2.element.app.AppPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model2.element.preset.PresetPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevelPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.ResourceGroupPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.servicelevel.ServiceLevel;
import de.unistuttgart.ipvs.pmp.model2.element.servicelevel.ServiceLevelPersistenceProvider;

/**
 * General persistence provider which provides all the data necessary for the model. Can be hooked in by
 * {@link Observer}s to get the updated {@link ModelCache}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PersistenceProvider extends Observable {
    
    /**
     * Singleton stuff
     */
    private static final PersistenceProvider instance = new PersistenceProvider();
    
    
    public static PersistenceProvider getInstance() {
        return instance;
    }
    
    
    /**
     * Singleton constructor
     */
    private PersistenceProvider() {
    }
    
    
    /**
     * Constructor for descendants.
     * 
     * @param parent
     *            the singleton object obviously
     */
    protected PersistenceProvider(PersistenceProvider parent) {
        this.doh = parent.doh;
        this.cache = parent.cache;
    }
    
    /**
     * D'oh!
     */
    private DatabaseOpenHelper doh;
    
    /**
     * Cache
     */
    private ModelCache cache;
    
    
    /**
     * Reinitializes the database connection. Strongly recommended after a context change in
     * {@link PMPApplication#getContext()}.
     */
    public void reloadDatabaseConnection() {
        this.doh = new DatabaseOpenHelper(PMPApplication.getContext());
        regenerateCache();
    }
    
    
    /**
     * Tells the {@link PersistenceProvider} and conclusively the {@link Model} to release the cached data.
     * Should probably only be invoked when Android explicitly requests this behavior. Afterwards, all data
     * from the persistence has to be cached again.
     */
    public void releaseCache() {
        this.cache = null;
        setChanged();
        notifyObservers(this.cache);
        
        System.gc();
    }
    
    
    /**
     * 
     * @return the {@link DatabaseOpenHelper} for this persistence layer.
     */
    protected DatabaseOpenHelper getDoh() {
        return this.doh;
    }
    
    
    /**
     * @return the current cache, if one is present, or null if none was created
     */
    protected ModelCache getCache() {
        return this.cache;
    }
    
    
    /**
     * Caches all objects right now. This means, that the {@link ModelCache} object will only contain fully cached,
     * fully linked objects. Useful, if you want the non-lazy initialization behavior, maybe because you are going to
     * access all data of it in rapid succession anyway.
     */
    public void cacheEverythingNow() {
        if (this.doh == null) {
            reloadDatabaseConnection();
        }
        if (this.cache == null) {
            regenerateCache();
        }
        
        for (ResourceGroup rg : this.cache.getResourceGroups().values()) {
            rg.checkCached();
        }
        Log.d("");
        for (Map<String, PrivacyLevel> plMap : this.cache.getPrivacyLevels().values()) {
            for (PrivacyLevel pl : plMap.values()) {
                pl.checkCached();
            }
        }
        for (App a : this.cache.getApps().values()) {
            a.checkCached();
        }
        for (List<ServiceLevel> slList : this.cache.getServiceLevels().values()) {
            for (ServiceLevel sl : slList) {
                sl.checkCached();
            }
        }
        for (Preset p : this.cache.getPresets()) {
            p.checkCached();
        }
        
    }
    
    
    /**
     * Creates a new {@link ModelCache} by creating a bare framework of unlinked objects from the persistence layer.
     * Also invokes the Observable connection so the {@link Model} knows the new cache.
     */
    private void regenerateCache() {
        this.cache = new ModelCache();
        
        SQLiteDatabase db = this.doh.getReadableDatabase();
        
        cacheAppsSLs(db);
        cacheRGsPLs(db);
        cachePresets(db);
        
        db.close();
        
        setChanged();
        notifyObservers(this.cache);
    }
    
    
    /**
     * Caches all the {@link App}s and their {@link ServiceLevel}s.
     * 
     * @param db
     */
    private void cacheAppsSLs(SQLiteDatabase db) {
        Cursor appCursor = db.rawQuery("SELECT Identifier FROM App", null);
        appCursor.moveToNext();
        
        while (!appCursor.isAfterLast()) {
            String appIdentifier = appCursor.getString(appCursor.getColumnIndex("Identifier"));
            App app = new App(appIdentifier);
            app.setPersistenceProvider(new AppPersistenceProvider(app));
            
            List<ServiceLevel> thisAppsSLs = new ArrayList<ServiceLevel>();
            
            // find the local SLs (don't think join is a wise idea)
            Cursor slCursor = db.rawQuery("SELECT Level FROM ServiceLevel WHERE App_Identifier = ? ORDER BY Level ASC",
                    new String[] { appIdentifier });
            slCursor.moveToNext();
            while (!slCursor.isAfterLast()) {
                int slLevel = slCursor.getInt(slCursor.getColumnIndex("Level"));
                ServiceLevel sl = new ServiceLevel(slLevel, app);
                sl.setPersistenceProvider(new ServiceLevelPersistenceProvider(sl));
                
                thisAppsSLs.add(sl);
                slCursor.moveToNext();
            }
            
            // finalize App
            this.cache.getServiceLevels().put(app, thisAppsSLs);
            this.cache.getApps().put(appIdentifier, app);
            appCursor.moveToNext();
        }
        appCursor.close();
    }
    
    
    /**
     * Caches all the {@link ResourceGroup}s and their {@link PrivacyLevel}s.
     * 
     * @param db
     */
    private void cacheRGsPLs(SQLiteDatabase db) {
        Cursor rgCursor = db.rawQuery("SELECT Identifier FROM ResourceGroup", null);
        rgCursor.moveToNext();
        
        while (!rgCursor.isAfterLast()) {
            String rgIdentifier = rgCursor.getString(rgCursor.getColumnIndex("Identifier"));
            ResourceGroup rg = new ResourceGroup(rgIdentifier);
            rg.setPersistenceProvider(new ResourceGroupPersistenceProvider(rg));
            
            Map<String, PrivacyLevel> thisRGsPLs = new HashMap<String, PrivacyLevel>();
            
            // find the local PLs (don't think join is a wise idea)
            Cursor plCursor = db.rawQuery("SELECT Identifier FROM PrivacyLevel WHERE ResourceGroup_Identifier = ?",
                    new String[] { rgIdentifier });
            plCursor.moveToNext();
            while (!plCursor.isAfterLast()) {
                String plIdentifier = plCursor.getString(plCursor.getColumnIndex("Identifier"));
                PrivacyLevel pl = new PrivacyLevel(plIdentifier, rg);
                pl.setPersistenceProvider(new PrivacyLevelPersistenceProvider(pl));
                
                thisRGsPLs.put(plIdentifier, pl);
                plCursor.moveToNext();
            }
            
            // finalize RG
            this.cache.getPrivacyLevels().put(rg, thisRGsPLs);
            this.cache.getResourceGroups().put(rgIdentifier, rg);
            rgCursor.moveToNext();
        }
        rgCursor.close();
    }
    
    
    /**
     * Caches all the {@link Preset}s.
     * 
     * @param db
     */
    private void cachePresets(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT Identifier, Type FROM Preset", null);
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String identifier = cursor.getString(cursor.getColumnIndex("Identifier"));
            String type = cursor.getString(cursor.getColumnIndex("Type"));
            Preset p = new Preset(identifier, PMPComponentType.valueOf(type));
            p.setPersistenceProvider(new PresetPersistenceProvider(p));
            
            this.cache.getPresets().add(p);
            cursor.moveToNext();
        }
        
    }
    
}
