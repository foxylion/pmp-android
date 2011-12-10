package de.unistuttgart.ipvs.pmp.gui.placeholder;

import java.io.IOException;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupApp;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupModel;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupPrivacySetting;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupRG;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupServiceFeature;
import de.unistuttgart.ipvs.pmp.gui.util.LongTaskProgressDialog;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSetParser;
import de.unistuttgart.ipvs.pmp.util.xml.app.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.app.ServiceFeature;
import de.unistuttgart.ipvs.pmp.util.xml.rg.PrivacySetting;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSetParser;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupControl {
    
    public static void init(final Context activityContext) {
        
        ProgressDialog pd = new ProgressDialog(activityContext);
        pd.setTitle("Mocking Model");
        pd.setMessage("Loading mockups...");
        pd.setCancelable(false);
        LongTaskProgressDialog<Void, Void, Void> ltpd = new LongTaskProgressDialog<Void, Void, Void>(pd) {
            
            @Override
            public Void run(Void... params) {
                initRGs(activityContext);
                initApps(activityContext);
                initPresets();
                return null;
            }
            
        };
        ltpd.execute();
    }
    
    
    /**
     * 
     */
    private static void initPresets() {
        String ident;
        String name;
        String description;
        
        ident = "preset_1";
        name = "My first Preset";
        description = "Wooohooo, I've created a cool preset.. wow!";
        MockupModel.instance.addPreset(null, ident, name, description);
        
        ident = "preset_2";
        name = "My second Preset";
        description = "Wooohooo, I've created another very cool preset.. wow!";
        MockupModel.instance.addPreset(null, ident, name, description);
        
        ident = "preset_3";
        name = "My third Preset";
        description = "Yeah, now I know how to create presets! I'm an expert!!! And therfore I write a veeeeeeeeery long description... can the gui show this?! Yes, of course!";
        MockupModel.instance.addPreset(null, ident, name, description);
        
        ident = "preset_4";
        name = "My first deleted Preset";
        description = "Wow, i was able to delete a preset!";
        IPreset p = MockupModel.instance.addPreset(null, ident, name, description);
        p.setDeleted(true);
    }
    
    
    /**
     * @param activityContext
     */
    private static void initApps(Context activityContext) {
        String ident;
        MockupApp app;
        AppInformationSet ais;
        
        ident = "org.barcode.scanner";
        ais = getAIS(activityContext, "barcode.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon1), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.google.calendar";
        ais = getAIS(activityContext, "calendar.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon2), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.facebook.apps";
        ais = getAIS(activityContext, "facebook.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon3), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.google.mail";
        ais = getAIS(activityContext, "gmail.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon4), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.imdb.android";
        ais = getAIS(activityContext, "imdb.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon5), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.google.sms";
        ais = getAIS(activityContext, "sms.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon6), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "tv.sony.android";
        ais = getAIS(activityContext, "tv.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon7), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.google.compass";
        ais = getAIS(activityContext, "compass.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon8), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.adobe.rss";
        ais = getAIS(activityContext, "rss.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon9), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "org.wikipedia.android";
        ais = getAIS(activityContext, "wikipedia.xml");
        app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon10), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
    }
    
    
    /**
     * @param activityContext
     */
    private static void initRGs(Context activityContext) {
        String ident;
        MockupRG rg;
        RgInformationSet rgis;
        
        ident = "org.oracle.db";
        rgis = getRGIS(activityContext, "db.xml");
        rg = new MockupRG(ident, getDrawable(activityContext, R.drawable.icon_rgs), rgis);
        createPS(rgis, rg);
        MockupModel.instance.installResourceGroup(ident, rg);
        
        ident = "gov.gps";
        rgis = getRGIS(activityContext, "gps.xml");
        rg = new MockupRG(ident, getDrawable(activityContext, R.drawable.test_icon8), rgis);
        createPS(rgis, rg);
        MockupModel.instance.installResourceGroup(ident, rg);
        
        ident = "de.bka.bundestrojaner";
        rgis = getRGIS(activityContext, "privacy.xml");
        rg = new MockupRG(ident, getDrawable(activityContext, R.drawable.icon_search), rgis);
        createPS(rgis, rg);
        MockupModel.instance.installResourceGroup(ident, rg);
    }
    
    
    public static void createPS(RgInformationSet rgis, MockupRG rg) {
        MockupPrivacySetting ps;
        for (Entry<String, PrivacySetting> e : rgis.getPrivacySettingsMap().entrySet()) {
            ps = new MockupPrivacySetting(rg, e.getKey(), new BooleanPrivacySetting());
            rg.addPS(e.getKey(), ps);
        }
    }
    
    
    public static void createSF(AppInformationSet ais, MockupApp app) {
        MockupServiceFeature sf;
        for (Entry<String, ServiceFeature> e : ais.getServiceFeaturesMap().entrySet()) {
            
            boolean available = true;
            for (RequiredResourceGroup rrg : e.getValue().getRequiredResourceGroups()) {
                IResourceGroup rg = MockupModel.instance.getResourceGroup(rrg.getRgIdentifier());
                if (rg == null) {
                    available = false;
                    break;
                    
                } else {
                    for (Entry<String, String> f : rrg.getPrivacySettingsMap().entrySet()) {
                        IPrivacySetting ps = rg.getPrivacySetting(f.getKey());
                        if (ps == null) {
                            available = false;
                            break;
                        }
                    }
                }
            }
            
            sf = new MockupServiceFeature(app, e.getKey(), available);
            for (RequiredResourceGroup rrg : e.getValue().getRequiredResourceGroups()) {
                IResourceGroup rg = MockupModel.instance.getResourceGroup(rrg.getRgIdentifier());
                if (rg != null) {
                    for (Entry<String, String> f : rrg.getPrivacySettingsMap().entrySet()) {
                        IPrivacySetting ps = rg.getPrivacySetting(f.getKey());
                        if (ps != null) {
                            sf.addPS((MockupPrivacySetting) ps, f.getValue());
                        }
                    }
                }
            }
            app.addSF(e.getKey(), sf);
        }
    }
    
    
    public static RgInformationSet getRGIS(Context context, String fileName) {
        RgInformationSet result = null;
        try {
            result = RgInformationSetParser.createRgInformationSet(context.getAssets().open("samples2/rg/" + fileName));
        } catch (IOException e) {
            Log.e("Could not mock RGIS", e);
        }
        return result;
    }
    
    
    public static AppInformationSet getAIS(Context context, String fileName) {
        AppInformationSet result = null;
        try {
            result = AppInformationSetParser.createAppInformationSet(context.getAssets().open(
                    "samples2/app/" + fileName));
        } catch (IOException e) {
            Log.e("Could not mock AIS", e);
        }
        return result;
    }
    
    
    private static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }
}
