package de.unistuttgart.ipvs.pmp.gui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
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
    
    private static final List<Throwable> youreDoingItWrong = new ArrayList<Throwable>();
    
    private static MockupApp app1;
    private static MockupApp app2;
    private static MockupApp app3;
    private static MockupApp app4;
    
    private static IPreset p1;
    private static IPreset p2;
    private static IPreset p3;
    private static IPreset p4;
    
    private static MockupRG rg1;
    private static MockupRG rg2;
    private static MockupRG rg3;
    
    
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
            
            
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                for (Throwable t : youreDoingItWrong) {
                    String msg = t.getMessage();
                    if (t instanceof XMLParserException) {
                        // yeah cause we like it intricate
                        msg = ((XMLParserException) t).getDetails();
                    }
                    
                    new AlertDialog.Builder(activityContext).setTitle("Mockup Error")
                            .setMessage(t.getClass().getCanonicalName() + ": " + msg + " (see LogCat)")
                            .setPositiveButton("Ok, I will fix it", new DialogInterface.OnClickListener() {
                                
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setCancelable(false).show();
                }
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
        p1 = MockupModel.instance.addPreset(null, ident, name, description);
        p1.assignApp(app1);
        p1.assignApp(app2);
        p1.assignApp(app3);
        p1.assignPrivacySetting(rg1.getPrivacySetting("read"), "true");
        p1.assignPrivacySetting(rg1.getPrivacySetting("modify"), "true");
        p1.assignPrivacySetting(rg1.getPrivacySetting("create"), "true");
        
        ident = "preset_2";
        name = "My second Preset";
        description = "Wooohooo, I've created another very cool preset.. wow!";
        p2 = MockupModel.instance.addPreset(null, ident, name, description);
        p2.assignApp(app3);
        p2.assignApp(app4);
        p2.assignPrivacySetting(rg1.getPrivacySetting("read"), "true");
        p2.assignPrivacySetting(rg1.getPrivacySetting("create"), "true");
        
        ident = "preset_3";
        name = "My third Preset";
        description = "Yeah, now I know how to create presets! I'm an expert!!! And therfore I write a veeeeeeeeery long description... can the gui show this?! Yes, of course!";
        p3 = MockupModel.instance.addPreset(null, ident, name, description);
        p3.assignApp(app1);
        p3.assignPrivacySetting(rg1.getPrivacySetting("modify"), "true");
        p3.assignPrivacySetting(rg1.getPrivacySetting("create"), "true");
        
        ident = "preset_4";
        name = "My first deleted Preset";
        description = "Wow, i was able to delete a preset!";
        p4 = MockupModel.instance.addPreset(null, ident, name, description);
        p4.assignApp(app1);
        p4.assignApp(app3);
        p4.setDeleted(true);
        p4.assignPrivacySetting(rg1.getPrivacySetting("read"), "true");
    }
    
    
    /**
     * @param activityContext
     */
    private static void initApps(Context activityContext) {
        String ident;
        MockupApp app;
        AppInformationSet ais;
        
        ident = "org.barcode.scanner";
        if ((ais = getAIS(activityContext, "barcode.xml")) != null) {
            app1 = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon1), ais);
            createSF(ais, app1);
            MockupModel.instance.registerApp(ident, app1);
        }
        
        ident = "com.google.calendar";
        if ((ais = getAIS(activityContext, "calendar.xml")) != null) {
            app2 = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon2), ais);
            createSF(ais, app2);
            MockupModel.instance.registerApp(ident, app2);
        }
        
        ident = "com.facebook.apps";
        if ((ais = getAIS(activityContext, "facebook.xml")) != null) {
            app3 = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon3), ais);
            createSF(ais, app3);
            MockupModel.instance.registerApp(ident, app3);
        }
        
        ident = "com.google.mail";
        if ((ais = getAIS(activityContext, "gmail.xml")) != null) {
            app4 = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon4), ais);
            createSF(ais, app4);
            MockupModel.instance.registerApp(ident, app4);
        }
        
        ident = "com.imdb.android";
        if ((ais = getAIS(activityContext, "imdb.xml")) != null) {
            app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon5), ais);
            createSF(ais, app);
            MockupModel.instance.registerApp(ident, app);
        }
        
        ident = "com.google.sms";
        if ((ais = getAIS(activityContext, "sms.xml")) != null) {
            app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon6), ais);
            createSF(ais, app);
            MockupModel.instance.registerApp(ident, app);
        }
        
        ident = "tv.sony.android";
        if ((ais = getAIS(activityContext, "tv.xml")) != null) {
            app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon7), ais);
            createSF(ais, app);
            MockupModel.instance.registerApp(ident, app);
        }
        
        ident = "com.google.compass";
        if ((ais = getAIS(activityContext, "compass.xml")) != null) {
            app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon8), ais);
            createSF(ais, app);
            MockupModel.instance.registerApp(ident, app);
        }
        
        ident = "com.adobe.rss";
        if ((ais = getAIS(activityContext, "rss.xml")) != null) {
            app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon9), ais);
            createSF(ais, app);
            MockupModel.instance.registerApp(ident, app);
        }
        
        ident = "org.wikipedia.android";
        if ((ais = getAIS(activityContext, "wikipedia.xml")) != null) {
            app = new MockupApp(ident, getDrawable(activityContext, R.drawable.test_icon10), ais);
            createSF(ais, app);
            MockupModel.instance.registerApp(ident, app);
        }
    }
    
    
    /**
     * @param activityContext
     */
    private static void initRGs(Context activityContext) {
        String ident;
        RgInformationSet rgis;
        
        ident = "org.oracle.db";
        if ((rgis = getRGIS(activityContext, "db.xml")) != null) {
            rg1 = new MockupRG(ident, getDrawable(activityContext, R.drawable.icon_rgs), rgis);
            createPS(rgis, rg1);
            /*rg1.setInstalled(true);*/
            MockupModel.instance.installResourceGroup(ident, rg1);
        }
        
        ident = "gov.gps";
        if ((rgis = getRGIS(activityContext, "gps.xml")) != null) {
            rg2 = new MockupRG(ident, getDrawable(activityContext, R.drawable.test_icon8), rgis);
            createPS(rgis, rg2);
            MockupModel.instance.installResourceGroup(ident, rg2);
        }
        
        ident = "de.bka.bundestrojaner";
        if ((rgis = getRGIS(activityContext, "privacy.xml")) != null) {
            rg3 = new MockupRG(ident, getDrawable(activityContext, R.drawable.icon_search), rgis);
            createPS(rgis, rg3);
            MockupModel.instance.installResourceGroup(ident, rg3);
        }
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
        } catch (Throwable t) {
            youreDoingItWrong.add(t);
            Log.e("Could not mock RGIS", t);
        }
        return result;
    }
    
    
    public static AppInformationSet getAIS(Context context, String fileName) {
        AppInformationSet result = null;
        try {
            result = AppInformationSetParser.createAppInformationSet(context.getAssets().open(
                    "samples2/app/" + fileName));
        } catch (Throwable t) {
            youreDoingItWrong.add(t);
            Log.e("Could not mock AIS", t);
        }
        return result;
    }
    
    
    private static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }
    
}
