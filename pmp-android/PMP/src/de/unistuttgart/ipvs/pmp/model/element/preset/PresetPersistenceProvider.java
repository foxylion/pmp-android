package de.unistuttgart.ipvs.pmp.model.element.preset;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;

/**
 * The persistence provider for {@link Preset}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PresetPersistenceProvider extends ElementPersistenceProvider<Preset> {
    
    public PresetPersistenceProvider(Preset element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        qb.setTables(TBL_PRESET);
        Cursor c = qb.query(rdb, new String[] { NAME, DESCRIPTION, DELETED }, CREATOR + " = ? AND " + IDENTIFIER
                + " = ?", new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier() }, null,
                null, null);
        
        if (c.moveToFirst()) {
            this.element.name = c.getString(c.getColumnIndex(NAME));
            this.element.description = c.getString(c.getColumnIndex(DESCRIPTION));
            this.element.deleted = Boolean.valueOf(c.getString(c.getColumnIndex(DELETED)));
        } else {
            throw new ModelIntegrityError(Assert.ILLEGAL_DB, "Preset", this);
        }
        c.close();
        
        // load privacy setting values
        qb.setTables(TBL_GrantPSValue);
        Cursor cps = qb.query(rdb, new String[] { PRIVACYSETTING_RESOURCEGROUP_PACKAGE, PRIVACYSETTING_IDENTIFIER,
                GRANTEDVALUE }, PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER + " = ?",
                new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier() }, null, null, null);
        
        this.element.privacySettingValues = new HashMap<IPrivacySetting, String>();
        this.element.containsUnknownElements = false;
        
        if (cps.moveToFirst()) {
            do {
                String rgPackage = cps.getString(cps.getColumnIndex(PRIVACYSETTING_RESOURCEGROUP_PACKAGE));
                String psIdentifier = cps.getString(cps.getColumnIndex(PRIVACYSETTING_IDENTIFIER));
                String grantValue = cps.getString(cps.getColumnIndex(GRANTEDVALUE));
                
                ResourceGroup rg = getCache().getResourceGroups().get(rgPackage);
                if (rg == null) {
                    Log.w("Unavailable preset cached (RG not present).");
                    this.element.containsUnknownElements = true;
                } else {
                    PrivacySetting ps = getCache().getPrivacySettings().get(rg).get(psIdentifier);
                    if (ps == null) {
                        Log.w("Unavailable preset cached (PS not found in RG).");
                        this.element.containsUnknownElements = true;
                    } else {
                        this.element.privacySettingValues.put(ps, grantValue);
                    }
                }
            } while (cps.moveToNext());
        }
        cps.close();
        
        // load assigned apps
        qb.setTables(TBL_PresetAssignedApp);
        Cursor capp = qb.query(rdb, new String[] { APP_PACKAGE }, PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                + " = ?", new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier() }, null,
                null, null);
        this.element.assignedApps = new ArrayList<IApp>();
        
        if (capp.moveToFirst()) {
            do {
                String appId = capp.getString(capp.getColumnIndex(APP_PACKAGE));
                
                App app = getCache().getApps().get(appId);
                if (app == null) {
                    Log.w("Unavailable preset cached (App not found).");
                    this.element.containsUnknownElements = true;
                } else {
                    this.element.assignedApps.add(app);
                }
            } while (capp.moveToNext());
        }
        capp.close();
        
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, this.element.name);
        cv.put(DESCRIPTION, this.element.description);
        cv.put(DELETED, String.valueOf(this.element.deleted));
        
        wdb.update(TBL_PRESET, cv, PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER + " = ?", new String[] {
                this.element.getCreatorString(), this.element.getLocalIdentifier() });
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete preset granted privacy setting value references
        wdb.rawQuery("DELETE FROM " + TBL_GrantPSValue + " WHERE " + PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                + " = ?", new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier() });
        
        // delete preset assigned apps references
        wdb.rawQuery("DELETE FROM " + TBL_PresetAssignedApp + " WHERE " + PRESET_CREATOR + " = ? AND "
                + PRESET_IDENTIFIER + " = ?",
                new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier() });
        
        // delete preset
        wdb.rawQuery("DELETE FROM " + TBL_PRESET + " WHERE " + PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                + " = ?", new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier() });
        
    }
    
    
    protected void assignApp(IApp app) {
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put(PRESET_IDENTIFIER, this.element.getLocalIdentifier());
        cv.put(PRESET_CREATOR, this.element.getCreatorString());
        cv.put(APP_PACKAGE, app.getIdentifier());
        
        wdb.insert("Preset_Apps", null, cv);
    }
    
    
    protected void removeApp(IApp app) {
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        wdb.rawQuery(
                "DELETE FROM " + TBL_PresetAssignedApp + " WHERE " + PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                        + " = ? AND " + APP_PACKAGE + " = ?",
                new String[] { this.element.getCreatorString(), this.element.getLocalIdentifier(), app.getIdentifier() });
    }
    
    
    protected void assignPrivacySetting(IPrivacySetting ps, String value) {
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put(PRIVACYSETTING_RESOURCEGROUP_PACKAGE, ps.getResourceGroup().getIdentifier());
        cv.put(PRIVACYSETTING_IDENTIFIER, ps.getLocalIdentifier());
        cv.put(PRESET_CREATOR, this.element.getCreatorString());
        cv.put(PRESET_IDENTIFIER, this.element.getIdentifier());
        cv.put(GRANTEDVALUE, value);
        
        if (wdb.insert(TBL_GrantPSValue, null, cv) == -1) {
            
            wdb.update(TBL_GrantPSValue, cv, PRIVACYSETTING_RESOURCEGROUP_PACKAGE + " = ? AND "
                    + PRIVACYSETTING_IDENTIFIER + " = ? AND " + PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                    + " = ?", new String[] { ps.getResourceGroup().getIdentifier(), ps.getLocalIdentifier(),
                    this.element.getCreatorString(), this.element.getIdentifier() });
        }
    }
    
    
    protected void removePrivacySetting(IPrivacySetting ps) {
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        wdb.rawQuery(
                "DELETE FROM " + TBL_GrantPSValue + " WHERE " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE + " = ? AND "
                        + PRIVACYSETTING_IDENTIFIER + " = ? AND " + PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                        + " = ?",
                new String[] { ps.getResourceGroup().getIdentifier(), ps.getLocalIdentifier(),
                        this.element.getCreatorString(), this.element.getIdentifier() });
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link Preset} specified with the parameters. Links this
     * {@link PresetPersistenceProvider} to the newly created object.
     * 
     * @param creator
     *            creator of the preset, null for user
     * @param identifier
     *            identifier of the preset
     * @param name
     *            name of the preset
     * @param description
     *            description of the preset
     * @return a {@link Preset} object that is linked to the newly created persistence data and this
     *         {@link PresetPersistenceProvider}, or null, if the creation was not possible
     */
    public Preset createElementData(IModelElement creator, String identifier, String name, String description) {
        // store in db
        ContentValues cv = new ContentValues();
        cv.put(CREATOR, creator == null ? PACKAGE_SEPARATOR : creator.getIdentifier());
        cv.put(IDENTIFIER, identifier);
        cv.put(NAME, name);
        cv.put(DESCRIPTION, description);
        cv.put(DELETED, Boolean.FALSE.toString());
        if (getDoh().getWritableDatabase().insert(TBL_PRESET, null, cv) == -1) {
            return null;
        }
        
        // create associated object
        Preset result = new Preset(creator, identifier);
        this.element = result;
        result.setPersistenceProvider(this);
        
        return result;
    }
    
}
