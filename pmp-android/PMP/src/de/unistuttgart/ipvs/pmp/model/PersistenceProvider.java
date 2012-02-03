package de.unistuttgart.ipvs.pmp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.context.TimeContext;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.AppPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.preset.PresetPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySettingPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroupPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeaturePersistenceProvider;

/**
 * General persistence provider which provides all the data necessary for the model. Can be hooked in by
 * {@link Observer}s to get the updated {@link ModelCache}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PersistenceProvider extends Observable implements PersistenceConstants {
    
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
        for (Map<String, PrivacySetting> psMap : this.cache.getPrivacySettings().values()) {
            for (PrivacySetting pl : psMap.values()) {
                pl.checkCached();
            }
        }
        for (App a : this.cache.getApps().values()) {
            a.checkCached();
        }
        for (Map<String, ServiceFeature> sfMap : this.cache.getServiceFeatures().values()) {
            for (ServiceFeature sl : sfMap.values()) {
                sl.checkCached();
            }
        }
        for (Map<String, Preset> pMap : this.cache.getPresets().values()) {
            for (Preset p : pMap.values()) {
                p.checkCached();
            }
        }
        
    }
    
    
    /**
     * Creates a new {@link ModelCache} by creating a bare framework of unlinked objects from the persistence layer.
     * Also invokes the Observable connection so the {@link Model} knows the new cache.
     */
    private void regenerateCache() {
        this.cache = new ModelCache();
        
        SQLiteDatabase db = this.doh.getReadableDatabase();
        try {
            
            cacheAppsSFs(db);
            cacheRGsPSs(db);
            cachePresets(db);
            cacheContexts(db);
            
        } finally {
            db.close();
        }
        
        setChanged();
        notifyObservers(this.cache);
    }
    
    
    /**
     * Caches all the {@link App}s and their {@link ServiceFeature}s.
     * 
     * @param db
     */
    private void cacheAppsSFs(SQLiteDatabase db) {
        SQLiteQueryBuilder builder = this.doh.builder();
        builder.setTables(TBL_APP);
        
        Cursor appCursor = builder.query(db, new String[] { PACKAGE }, null, null, null, null, null);
        
        if (appCursor.moveToFirst()) {
            do {
                String appPackage = appCursor.getString(appCursor.getColumnIndex(PACKAGE));
                App app = new App(appPackage);
                app.setPersistenceProvider(new AppPersistenceProvider(app));
                
                Map<String, ServiceFeature> thisAppsSFs = new HashMap<String, ServiceFeature>();
                
                // find the local SFs (don't think join is a wise idea)
                builder.setTables(TBL_SERVICEFEATURE);
                
                Cursor sfCursor = builder.query(db, new String[] { IDENTIFIER }, APP_PACKAGE + " = ?",
                        new String[] { appPackage }, null, null, null);
                
                if (sfCursor.moveToFirst()) {
                    do {
                        String sfIdentifier = sfCursor.getString(sfCursor.getColumnIndex(IDENTIFIER));
                        ServiceFeature sf = new ServiceFeature(app, sfIdentifier);
                        sf.setPersistenceProvider(new ServiceFeaturePersistenceProvider(sf));
                        
                        thisAppsSFs.put(sfIdentifier, sf);
                    } while (sfCursor.moveToNext());
                }
                sfCursor.close();
                
                // finalize App
                this.cache.getServiceFeatures().put(app, thisAppsSFs);
                this.cache.getApps().put(appPackage, app);
            } while (appCursor.moveToNext());
        }
        appCursor.close();
    }
    
    
    /**
     * Caches all the {@link ResourceGroup}s and their {@link PrivacySetting}s.
     * 
     * @param db
     */
    private void cacheRGsPSs(SQLiteDatabase db) {
        SQLiteQueryBuilder builder = this.doh.builder();
        builder.setTables(TBL_RESOURCEGROUP);
        
        Cursor rgCursor = builder.query(db, new String[] { PACKAGE }, null, null, null, null, null);
        
        if (rgCursor.moveToFirst()) {
            do {
                String rgPackage = rgCursor.getString(rgCursor.getColumnIndex(PACKAGE));
                ResourceGroup rg = new ResourceGroup(rgPackage);
                rg.setPersistenceProvider(new ResourceGroupPersistenceProvider(rg));
                
                Map<String, PrivacySetting> thisRGsPSs = new HashMap<String, PrivacySetting>();
                
                // find the local PSs (don't think join is a wise idea)
                builder.setTables(TBL_PRIVACYSETTING);
                Cursor psCursor = builder.query(db, new String[] { IDENTIFIER }, RESOURCEGROUP_PACKAGE + " = ?",
                        new String[] { rgPackage }, null, null, null);
                
                if (psCursor.moveToFirst()) {
                    do {
                        String psIdentifier = psCursor.getString(psCursor.getColumnIndex(IDENTIFIER));
                        PrivacySetting ps = new PrivacySetting(rg, psIdentifier);
                        ps.setPersistenceProvider(new PrivacySettingPersistenceProvider(ps));
                        
                        thisRGsPSs.put(psIdentifier, ps);
                    } while (psCursor.moveToNext());
                }
                psCursor.close();
                
                // finalize RG
                this.cache.getPrivacySettings().put(rg, thisRGsPSs);
                this.cache.getResourceGroups().put(rgPackage, rg);
            } while (rgCursor.moveToNext());
        }
        rgCursor.close();
    }
    
    
    /**
     * Caches all the {@link Preset}s and ContextAnnotations.
     * 
     * @param db
     */
    private void cachePresets(SQLiteDatabase db) {
        SQLiteQueryBuilder builder = this.doh.builder();
        builder.setTables(TBL_PRESET);
        
        Cursor cursor = builder.query(db, new String[] { CREATOR, IDENTIFIER }, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                // find the data, translate it
                String creator = cursor.getString(cursor.getColumnIndex(CREATOR));
                IModelElement creatorElement = this.cache.getApps().get(creator);
                if (creatorElement == null) {
                    creatorElement = this.cache.getResourceGroups().get(creator);
                }
                String identifier = cursor.getString(cursor.getColumnIndex(IDENTIFIER));
                
                // create item
                Preset p = new Preset(creatorElement, identifier);
                p.setPersistenceProvider(new PresetPersistenceProvider(p));
                
                // apply to cache
                Map<String, Preset> creatorMap = this.cache.getPresets().get(creatorElement);
                if (creatorMap == null) {
                    creatorMap = new HashMap<String, Preset>();
                    this.cache.getPresets().put(creatorElement, creatorMap);
                }
                creatorMap.put(identifier, p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        
    }
    
    
    /**
     * Caches all the {@link IContext}s.
     * 
     * @param db
     */
    private void cacheContexts(SQLiteDatabase db) {
        this.cache.getContexts().add(new TimeContext());
        
    }
    
}
