package de.unistuttgart.ipvs.pmp.model.element.preset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.ContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.ContextAnnotationPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingApp;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;
import de.unistuttgart.ipvs.pmp.util.BootReceiver;

/**
 * @see IPreset
 * @author Tobias Kuhn
 * 
 */
public class Preset extends ModelElement implements IPreset {
    
    /**
     * identifying attributes
     */
    protected IModelElement creator;
    protected String localIdentifier;
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected Map<IPrivacySetting, String> privacySettingValues;
    protected List<IApp> assignedApps;
    protected Map<IPrivacySetting, List<ContextAnnotation>> contextAnnotations;
    
    protected List<MissingPrivacySettingValue> missingPrivacySettings;
    protected List<MissingApp> missingApps;
    protected boolean deleted;
    
    
    /* organizational */
    
    public Preset(IModelElement creator, String identifier) {
        super(creator + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.creator = creator;
        this.localIdentifier = identifier;
    }
    
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [name = %s, desc = %s, psv = %s, aa = %s, mps = %s, ma = %s, d = %s]", this.name,
                        this.description, ModelElement.collapseMapToString(this.privacySettingValues),
                        ModelElement.collapseListToString(this.assignedApps),
                        ModelElement.collapseListToString(this.missingPrivacySettings),
                        ModelElement.collapseListToString(this.missingApps), String.valueOf(this.deleted));
    }
    
    
    /* interface */
    
    @Override
    public String getLocalIdentifier() {
        return this.localIdentifier;
    }
    
    
    @Override
    public boolean isBundled() {
        return this.creator != null;
    }
    
    
    @Override
    public IModelElement getCreator() {
        return this.creator;
    }
    
    
    @Override
    public String getName() {
        checkCached();
        return this.name;
    }
    
    
    @Override
    public void setName(String name) {
        checkCached();
        Assert.nonNull(name, ModelMisuseError.class, Assert.ILLEGAL_NULL, "name", name);
        this.name = name;
        persist();
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        return this.description;
    }
    
    
    @Override
    public void setDescription(String description) {
        checkCached();
        Assert.nonNull(description, ModelMisuseError.class, Assert.ILLEGAL_NULL, "description", description);
        this.description = description;
        persist();
    }
    
    
    @Override
    public List<IPrivacySetting> getGrantedPrivacySettings() {
        checkCached();
        return new ArrayList<IPrivacySetting>(this.privacySettingValues.keySet());
    }
    
    
    @Override
    public String getGrantedPrivacySettingValue(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        return this.privacySettingValues.get(privacySetting);
    }
    
    
    @Override
    public List<IApp> getAssignedApps() {
        checkCached();
        return new ArrayList<IApp>(this.assignedApps);
    }
    
    
    @Override
    public boolean isAppAssigned(IApp app) {
        checkCached();
        Assert.nonNull(app, ModelMisuseError.class, Assert.ILLEGAL_NULL, "app", app);
        return this.assignedApps.contains(app);
    }
    
    
    @Override
    public void assignApp(IApp app) {
        checkCached();
        Assert.nonNull(app, ModelMisuseError.class, Assert.ILLEGAL_NULL, "app", app);
        
        if (this.persistenceProvider != null) {
            ((PresetPersistenceProvider) this.persistenceProvider).assignApp(app);
        }
        this.assignedApps.add(app);
        
        if (app instanceof App) {
            ((App) app).addPreset(this);
        } else {
            throw new ModelIntegrityError(Assert.format(Assert.ILLEGAL_CLASS, "app", app));
        }
    }
    
    
    @Override
    public void removeApp(IApp app) {
        checkCached();
        Assert.nonNull(app, ModelMisuseError.class, Assert.ILLEGAL_NULL, "app", app);
        
        if (this.persistenceProvider != null) {
            ((PresetPersistenceProvider) this.persistenceProvider).removeApp(app);
        }
        this.assignedApps.remove(app);
        
        if (app instanceof App) {
            ((App) app).removePreset(this);
        } else {
            throw new ModelIntegrityError(Assert.format(Assert.ILLEGAL_CLASS, "app", app));
        }
    }
    
    
    @Override
    public void assignPrivacySetting(IPrivacySetting privacySetting, String value) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        Assert.nonNull(value, ModelMisuseError.class, Assert.ILLEGAL_NULL, "value", value);
        
        if (this.persistenceProvider != null) {
            ((PresetPersistenceProvider) this.persistenceProvider).assignPrivacySetting(privacySetting, value);
        }
        this.privacySettingValues.put(privacySetting, value);
        
        rollout();
    }
    
    
    @Override
    public void removePrivacySetting(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        
        if (this.persistenceProvider != null) {
            ((PresetPersistenceProvider) this.persistenceProvider).removePrivacySetting(privacySetting);
        }
        this.privacySettingValues.remove(privacySetting);
        
        rollout();
    }
    
    
    @Override
    public void assignServiceFeature(IServiceFeature serviceFeature) {
        checkCached();
        Assert.nonNull(serviceFeature, ModelMisuseError.class, Assert.ILLEGAL_NULL, "serviceFeature", serviceFeature);
        
        startUpdate();
        try {
            for (IPrivacySetting ps : serviceFeature.getRequiredPrivacySettings()) {
                assignPrivacySetting(ps, serviceFeature.getRequiredPrivacySettingValue(ps));
            }
            
        } finally {
            endUpdate();
        }
    }
    
    
    @Override
    public boolean isAvailable() {
        checkCached();
        return (this.missingPrivacySettings.size() == 0) && (this.missingApps.size() == 0);
    }
    
    
    @Override
    public List<MissingPrivacySettingValue> getMissingPrivacySettings() {
        checkCached();
        return new ArrayList<MissingPrivacySettingValue>(this.missingPrivacySettings);
    }
    
    
    @Override
    public List<MissingApp> getMissingApps() {
        checkCached();
        return new ArrayList<MissingApp>(this.missingApps);
    }
    
    
    @Override
    public void startUpdate() {
        IPCProvider.getInstance().startUpdate();
    }
    
    
    @Override
    public void endUpdate() {
        IPCProvider.getInstance().endUpdate();
    }
    
    
    @Override
    public void setDeleted(boolean deleted) {
        checkCached();
        this.deleted = deleted;
        persist();
        forceRecache();
        rollout();
    }
    
    
    @Override
    public boolean isDeleted() {
        checkCached();
        return this.deleted;
    }
    
    
    @Override
    public boolean removeMissingApp(MissingApp missingApp) {
        checkCached();
        Assert.nonNull(missingApp, ModelMisuseError.class, Assert.ILLEGAL_NULL, "missingApp", missingApp);
        
        if (this.missingApps.contains(missingApp) && (this.persistenceProvider != null)) {
            this.missingApps.remove(missingApp);
            
            // safe because App has no persistence and only uses identifier
            ((PresetPersistenceProvider) this.persistenceProvider).removeApp(new App(missingApp.getApp()));
            return true;
        }
        
        return false;
    }
    
    
    @Override
    public List<IContextAnnotation> getContextAnnotations(IPrivacySetting privacySetting) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        
        List<ContextAnnotation> psList = this.contextAnnotations.get(privacySetting);
        if (psList == null) {
            return new ArrayList<IContextAnnotation>();
        }
        return new ArrayList<IContextAnnotation>(psList);
    }
    
    
    @Override
    public void assignContextAnnotation(IPrivacySetting privacySetting, IContext context, String contextCondition,
            String overrideValue) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        Assert.nonNull(context, ModelMisuseError.class, Assert.ILLEGAL_NULL, "context", context);
        Assert.nonNull(contextCondition, ModelMisuseError.class, Assert.ILLEGAL_NULL, "contextCondition",
                contextCondition);
        Assert.nonNull(overrideValue, ModelMisuseError.class, Assert.ILLEGAL_NULL, "overrideValue", overrideValue);
        
        // the cA are linked to the cache directly
        List<ContextAnnotation> psList = this.contextAnnotations.get(privacySetting);
        if (psList == null) {
            psList = new ArrayList<ContextAnnotation>();
            this.contextAnnotations.put(privacySetting, psList);
        }
        
        ContextAnnotation ca = new ContextAnnotationPersistenceProvider(null).createElementData(this, privacySetting,
                context, contextCondition, overrideValue);
        psList.add(ca);
        
        BootReceiver.startService(PMPApplication.getContext());
        
        rollout();
    }
    
    
    @Override
    public void removeContextAnnotation(IPrivacySetting privacySetting, IContextAnnotation contextAnnotation) {
        checkCached();
        Assert.nonNull(privacySetting, ModelMisuseError.class, Assert.ILLEGAL_NULL, "privacySetting", privacySetting);
        Assert.nonNull(contextAnnotation, ModelMisuseError.class, Assert.ILLEGAL_NULL, "contextAnnotation",
                contextAnnotation);
        
        // the cA are linked to the cache directly
        List<ContextAnnotation> psList = this.contextAnnotations.get(privacySetting);
        if (psList == null) {
            return;
        }
        
        int caLoc = psList.indexOf(contextAnnotation);
        if (caLoc < 0) {
            return;
        }
        ContextAnnotation removed = psList.remove(caLoc);
        removed.delete();
        
        rollout();
    }
    
    
    @Override
    public List<IContextAnnotation> getConflictingContextAnnotations(IPreset preset) {
        Set<IContextAnnotation> result = new HashSet<IContextAnnotation>();
        
        for (List<ContextAnnotation> psCA : this.contextAnnotations.values()) {
            for (ContextAnnotation ca : psCA) {
                for (IContextAnnotation conflictCA : ca.getConflictingContextAnnotations(preset)) {
                    result.add(conflictCA);
                }
            }
        }
        
        return new ArrayList<IContextAnnotation>(result);
    }
    
    
    @Override
    public List<IPrivacySetting> getConflictingPrivacySettings(IPreset preset) {
        Set<IPrivacySetting> result = new HashSet<IPrivacySetting>();
        
        for (List<ContextAnnotation> psCA : this.contextAnnotations.values()) {
            for (ContextAnnotation ca : psCA) {
                if (ca.isPrivacySettingConflicting(preset)) {
                    result.add(ca.getPrivacySetting());
                }
            }
        }
        
        return new ArrayList<IPrivacySetting>(result);
    }
    
    
    /* inter-model communication */
    
    /**
     * Forces a rollout to all the affected apps. Useful when this preset changed its active state.
     */
    public void rollout() {
        for (IApp app : getAssignedApps()) {
            app.verifyServiceFeatures();
        }
    }
    
    
    /**
     * Removes the app when it gets deleted.
     * 
     * @param a
     */
    public void removeDeletedApp(App a) {
        Assert.nonNull(a, ModelIntegrityError.class, Assert.ILLEGAL_NULL, "a", a);
        this.assignedApps.remove(a);
    }
    
}
