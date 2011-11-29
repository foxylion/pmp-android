package de.unistuttgart.ipvs.pmp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
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
        for (Map<String, PrivacySetting> plMap : this.cache.getPrivacyLevels().values()) {
            for (PrivacySetting pl : plMap.values()) {
                pl.checkCached();
            }
        }
        for (App a : this.cache.getApps().values()) {
            a.checkCached();
        }
        for (List<ServiceFeature> slList : this.cache.getServiceLevels().values()) {
            for (ServiceFeature sl : slList) {
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
        
        cacheAppsSFs(db);
        cacheRGsPLs(db);
        cachePresets(db);
        
        db.close();
        
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
        appCursor.moveToNext();
        
        while (!appCursor.isAfterLast()) {
            String appPackage = appCursor.getString(appCursor.getColumnIndex(PACKAGE));
            App app = new App(appPackage);
            app.setPersistenceProvider(new AppPersistenceProvider(app));
            
            List<ServiceFeature> thisAppsSFs = new ArrayList<ServiceFeature>();
            
            // find the local SFs (don't think join is a wise idea)
            builder.setTables(TBL_SERVICEFEATURE);
            
            Cursor sfCursor = builder.query(db, new String[] { IDENTIFIER }, APP_PACKAGE + " = ?",
                    new String[] { appPackage }, null, null, null);
            sfCursor.moveToNext();
            while (!sfCursor.isAfterLast()) {
                String sfIdentifier = sfCursor.getString(sfCursor.getColumnIndex(IDENTIFIER));
                ServiceFeature sf = new ServiceFeature(app, sfIdentifier);
                sf.setPersistenceProvider(new ServiceFeaturePersistenceProvider(sf));
                
                thisAppsSFs.add(sf);
                sfCursor.moveToNext();
            }
            sfCursor.close();
            
            // finalize App
            this.cache.getServiceLevels().put(app, thisAppsSFs);
            this.cache.getApps().put(appPackage, app);
            appCursor.moveToNext();
        }
        appCursor.close();
    }
    
    
    /**
     * Caches all the {@link ResourceGroup}s and their {@link PrivacySetting}s.
     * 
     * @param db
     */
    private void cacheRGsPLs(SQLiteDatabase db) {
        SQLiteQueryBuilder builder = this.doh.builder();
        builder.setTables(TBL_RESOURCEGROUP);
        
        Cursor rgCursor = builder.query(db, new String[] { PACKAGE }, null, null, null, null, null);
        rgCursor.moveToNext();
        
        while (!rgCursor.isAfterLast()) {
            String rgPackage = rgCursor.getString(rgCursor.getColumnIndex(PACKAGE));
            ResourceGroup rg = new ResourceGroup(rgPackage);
            rg.setPersistenceProvider(new ResourceGroupPersistenceProvider(rg));
            
            Map<String, PrivacySetting> thisRGsPLs = new HashMap<String, PrivacySetting>();
            
            // find the local PSs (don't think join is a wise idea)
            builder.setTables(TBL_PRIVACYSETTING);
            Cursor psCursor = builder.query(db, new String[] { IDENTIFIER }, RESOURCEGROUP_PACKAGE + " = ?", new String[] { rgPackage }, null, null, null);
            psCursor.moveToNext();
            while (!psCursor.isAfterLast()) {
                String plIdentifier = psCursor.getString(psCursor.getColumnIndex(IDENTIFIER));
                PrivacySetting ps = new PrivacySetting(rg, plIdentifier);
                ps.setPersistenceProvider(new PrivacySettingPersistenceProvider(ps));
                
                thisRGsPLs.put(plIdentifier, ps);
                psCursor.moveToNext();
            }
            psCursor.close();
            
            // finalize RG
            this.cache.getPrivacyLevels().put(rg, thisRGsPLs);
            this.cache.getResourceGroups().put(rgPackage, rg);
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
        SQLiteQueryBuilder builder = this.doh.builder();
        builder.setTables(TBL_PRESET);
        
       
        Cursor cursor = builder.query(db, new String[] { CREATOR, IDENTIFIER }, null, null, null, null, null);
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String creator = cursor.getString(cursor.getColumnIndex(CREATOR));
            String identifier = cursor.getString(cursor.getColumnIndex(IDENTIFIER));
            Preset p = new Preset(creator, identifier);
            p.setPersistenceProvider(new PresetPersistenceProvider(p));
            
            this.cache.getPresets().add(p);
            cursor.moveToNext();
        }
        cursor.close();
        
    }
    
}
