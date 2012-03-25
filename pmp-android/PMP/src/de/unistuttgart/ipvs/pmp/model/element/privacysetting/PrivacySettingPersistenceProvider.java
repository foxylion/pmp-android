package de.unistuttgart.ipvs.pmp.model.element.privacysetting;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.ContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.plugin.PluginProvider;

/**
 * The persistence provider for {@link PrivacySetting}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PrivacySettingPersistenceProvider extends ElementPersistenceProvider<PrivacySetting> {
    
    public PrivacySettingPersistenceProvider(PrivacySetting element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        try {
            this.element.link = PluginProvider.getInstance()
                    .getResourceGroupObject(this.element.getResourceGroup().getIdentifier())
                    .getPrivacySetting(this.element.getLocalIdentifier());
        } catch (InvalidPluginException ipe) {
            this.element.resourceGroup.deactivate(ipe);
        }
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new ModelIntegrityError(Assert.format(Assert.ILLEGAL_METHOD, "storeElementData", this));
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // service feature required privacy setting values references are not supposed to be deleted,
        // we still need those for telling the SF is unavailable
        
        // preset granted privacy setting values references are not supposed to be deleted,
        // the preset will handle unavailable elements itself 
        
        // delete privacy settings
        wdb.delete(TBL_PRIVACYSETTING, RESOURCEGROUP_PACKAGE + " = ? AND " + IDENTIFIER + " = ?", new String[] {
                this.element.getResourceGroup().getIdentifier(), this.element.getLocalIdentifier() });
        
        // delete context annotations
        for (IContextAnnotation ca : getCache().getAllContextAnnotations()) {
            if (ca.getPrivacySetting() == this.element) {
                Assert.instanceOf(ca, ContextAnnotation.class, ModelIntegrityError.class, Assert.ILLEGAL_CLASS, "ca",
                        ca);
                ((ContextAnnotation) ca).delete();
            }
        }
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link PrivacySetting} specified with the parameters. Links
     * this {@link PrivacySettingPersistenceProvider} to the newly created object.
     * 
     * @param rg
     *            the rg whom this privacy setting belongs to
     * @param identifier
     *            the identifier of this privacy setting
     * @return an {@link PrivacySetting} object that is linked to the newly created persistence data and this
     *         {@link PrivacySettingPersistenceProvider}, or null, if the creation was not possible
     */
    public PrivacySetting createElementData(ResourceGroup rg, String identifier) {
        // store in db
        SQLiteDatabase sqldb = getDoh().getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(RESOURCEGROUP_PACKAGE, rg.getIdentifier());
            cv.put(IDENTIFIER, identifier);
            if (sqldb.insert(TBL_PRIVACYSETTING, null, cv) == -1) {
                Log.e(this, "Could not write service feature.");
                return null;
            }
        } finally {
            sqldb.close();
        }
        
        // create associated object
        PrivacySetting result = new PrivacySetting(rg, identifier);
        this.element = result;
        result.setPersistenceProvider(this);
        
        return result;
    }
}
