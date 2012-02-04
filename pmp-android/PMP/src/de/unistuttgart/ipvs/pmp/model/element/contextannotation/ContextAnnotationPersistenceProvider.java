package de.unistuttgart.ipvs.pmp.model.element.contextannotation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.model.PersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

/**
 * The persistence provider for {@link ContextAnnotation}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ContextAnnotationPersistenceProvider extends ElementPersistenceProvider<ContextAnnotation> {
    
    public ContextAnnotationPersistenceProvider(ContextAnnotation element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        qb.setTables(TBL_CONTEXT_ANNOTATIONS);
        Cursor c = qb.query(
                rdb,
                new String[] { CONTEXT_TYPE, CONTEXT_CONDITION, OVERRIDE_GRANTED_VALUE },
                PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER + " = ? AND " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE
                        + " = ? AND " + PRIVACYSETTING_IDENTIFIER + " = ?",
                new String[] { PersistenceProvider.getPresetCreatorString(this.element.preset), this.element.preset.getLocalIdentifier(),
                        this.element.privacySetting.getResourceGroup().getIdentifier(),
                        this.element.privacySetting.getLocalIdentifier() }, null, null, null);
        
        if (c.moveToFirst()) {
            this.element.context = PersistenceProvider.findContext(c.getString(c.getColumnIndex(CONTEXT_TYPE)));
            this.element.condition = c.getString(c.getColumnIndex(CONTEXT_CONDITION));
            this.element.overrideValue = c.getString(c.getColumnIndex(OVERRIDE_GRANTED_VALUE));
        } else {
            throw new ModelIntegrityError(Assert.format(Assert.ILLEGAL_DB, "ContextAnnotation", this));
        }
        c.close();
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        ContentValues cv = new ContentValues();
        cv.put(CONTEXT_TYPE, this.element.context.getIdentifier());
        cv.put(CONTEXT_CONDITION, this.element.condition);
        cv.put(OVERRIDE_GRANTED_VALUE, this.element.overrideValue);
        
        wdb.update(
                TBL_CONTEXT_ANNOTATIONS,
                cv,
                PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER + " = ? AND " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE
                        + " = ? AND " + PRIVACYSETTING_IDENTIFIER + " = ?",
                new String[] { PersistenceProvider.getPresetCreatorString(this.element.preset), this.element.preset.getLocalIdentifier(),
                        this.element.privacySetting.getResourceGroup().getIdentifier(),
                        this.element.privacySetting.getLocalIdentifier() });
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete this annotation
        wdb.execSQL(
                "DELETE FROM " + TBL_CONTEXT_ANNOTATIONS + " WHERE " + PRESET_CREATOR + " = ? AND " + PRESET_IDENTIFIER
                        + " = ? AND " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE + " = ? AND " + PRIVACYSETTING_IDENTIFIER
                        + " = ?",
                new String[] { PersistenceProvider.getPresetCreatorString(this.element.preset), this.element.preset.getLocalIdentifier(),
                        this.element.privacySetting.getResourceGroup().getIdentifier(),
                        this.element.privacySetting.getLocalIdentifier() });
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link ContextAnnotation} specified with the parameters. Links
     * this {@link ContextAnnotationPersistenceProvider} to the newly created object.
     * 
     * @param preset
     *            annotated preset
     * @param privacySetting
     *            annotated privacy setting
     * @param context
     *            context
     * @param condition
     *            condition under which annotation is active
     * @param overrideValue
     *            value to override privacy setting value in preset with when annotation is active
     * @return a {@link ContextAnnotation} object that is linked to the newly created persistence data and this
     *         {@link ContextAnnotationPersistenceProvider}, or null, if the creation was not possible
     */
    public ContextAnnotation createElementData(IPreset preset, IPrivacySetting privacySetting, IContext context,
            String condition, String overrideValue) {
        // store in db
        SQLiteDatabase sqldb = getDoh().getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(PRESET_CREATOR, PersistenceProvider.getPresetCreatorString(preset));
            cv.put(PRESET_IDENTIFIER, preset.getLocalIdentifier());
            cv.put(PRIVACYSETTING_RESOURCEGROUP_PACKAGE, privacySetting.getResourceGroup().getIdentifier());
            cv.put(PRIVACYSETTING_IDENTIFIER, privacySetting.getLocalIdentifier());
            cv.put(CONTEXT_TYPE, context.getIdentifier());
            cv.put(CONTEXT_CONDITION, condition);
            cv.put(OVERRIDE_GRANTED_VALUE, overrideValue);
            
            if (sqldb.insert(TBL_PRESET, null, cv) == -1) {
                return null;
            }
        } finally {
            sqldb.close();
        }
        
        // create associated object
        ContextAnnotation result = new ContextAnnotation(preset, privacySetting);
        this.element = result;
        result.setPersistenceProvider(this);
        
        return result;
    }
}
