package de.unistuttgart.ipvs.pmp.model.element.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSetParser;

/**
 * The persistence provider for {@link App}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class AppPersistenceProvider extends ElementPersistenceProvider<App> {
    
    public AppPersistenceProvider(App element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        
        this.element.serviceFeatures = getCache().getServiceFeatures().get(this.element);
        
        this.element.assignedPresets = new ArrayList<Preset>();
        for (Preset p : getCache().getAllPresets()) {
            if (p.isAppAssigned(this.element)) {
                this.element.assignedPresets.add(p);
            }
        }
        
        InputStream is = null;
        try {
            is = this.element.resourcesOfIdentifierPackage(PMPApplication.getContext()).getAssets()
                    .open(PersistenceConstants.APP_XML_NAME);
            this.element.ais = AppInformationSetParser.createAppInformationSet(is);
        } catch (IOException e) {
            Log.e("Did no longer find the app XML during loading its data.");
            e.printStackTrace();
        }
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete service features
        for (ServiceFeature sf : this.element.serviceFeatures.values()) {
            sf.delete();
        }
        
        // delete app preset references
        wdb.rawQuery("DELETE FROM " + TBL_PresetAssignedApp + " WHERE " + APP_PACKAGE + " = ?",
                new String[] { this.element.getIdentifier() });
        
        // delete app
        wdb.rawQuery("DELETE FROM " + TBL_APP + " WHERE " + PACKAGE + " = ?",
                new String[] { this.element.getIdentifier() });
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link App} specified with the parameters. Links this
     * {@link AppPersistenceProvider} to the newly created object.
     * 
     * @param appPackage
     *            package of the app
     * @return an {@link App} object that is linked to the newly created persistence data and this
     *         {@link AppPersistenceProvider}, or null, if the creation was not possible
     */
    public App createElementData(String appPackage) {
        // store in db
        ContentValues cv = new ContentValues();
        if (getDoh().getWritableDatabase().insert(TBL_APP, null, cv) == -1) {
            return null;
        }
        
        // create associated object
        App result = new App(appPackage);
        result.setPersistenceProvider(this);
        this.element = result;
        
        return result;
        
    }
    
}
