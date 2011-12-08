package de.unistuttgart.ipvs.pmp.gui.placeholder;

import java.io.IOException;
import java.util.Locale;
import java.util.Map.Entry;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupApp;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupModel;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupPrivacySetting;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupRG;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupServiceFeature;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSetParser;
import de.unistuttgart.ipvs.pmp.util.xml.app.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.app.ServiceFeature;
import de.unistuttgart.ipvs.pmp.util.xml.rg.PrivacySetting;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSetParser;
import android.content.Context;
import android.graphics.drawable.Drawable;


/**
 * 
 * @author Tobias Kuhn
 *
 */
public class MockupControl {
    
    public static void init(Context context) {
        String ident;
        MockupApp app;
        AppInformationSet ais;
        MockupRG rg;
        RgInformationSet rgis;
        
        /* ************** RG ************* */
        
        ident = "org.oracle.db";
        rgis = getRGIS(context, "db.xml");
        rg = new MockupRG(ident, getDrawable(context, R.drawable.icon_rgs), rgis);
        createPS(rgis, rg);        
        MockupModel.instance.installResourceGroup(ident, rg);
        
        ident = "gov.gps";
        rgis = getRGIS(context, "gps.xml");
        rg = new MockupRG(ident, getDrawable(context, R.drawable.test_icon8), rgis);
        createPS(rgis, rg);        
        MockupModel.instance.installResourceGroup(ident, rg);
        
        ident = "de.bka.bundestrojaner";
        rgis = getRGIS(context, "privacy.xml");
        rg = new MockupRG(ident, getDrawable(context, R.drawable.icon_search), rgis);
        createPS(rgis, rg);        
        MockupModel.instance.installResourceGroup(ident, rg);
        
        /* ************** App ************* */
        
        ident = "org.barcode.scanner";
        ais = getAIS(context, "barcode.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon1), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.google.calendar";
        ais = getAIS(context, "calendar.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon2), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.facebook.apps";
        ais = getAIS(context, "facebook.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon3), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);

        ident = "com.google.mail";
        ais = getAIS(context, "gmail.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon4), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.imdb.android";
        ais = getAIS(context, "imdb.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon5), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);

        ident = "com.google.sms";
        ais = getAIS(context, "sms.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon6), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);

        ident = "tv.sony.android";
        ais = getAIS(context, "tv.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon7), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);

        ident = "com.google.compass";
        ais = getAIS(context, "compass.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon8), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);
        
        ident = "com.adobe.rss";
        ais = getAIS(context, "rss.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon9), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);

        ident = "org.wikipedia.android";
        ais = getAIS(context, "wikipedia.xml");
        app = new MockupApp(ident, getDrawable(context, R.drawable.test_icon10), ais);
        createSF(ais, app);
        MockupModel.instance.registerApp(ident, app);

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
                if (MockupModel.instance.getResourceGroup(rrg.getRgIdentifier()) == null) {
                    available = false;
                    break;
                }
            }
            
            sf = new MockupServiceFeature(app, e.getKey(), available);
            app.addSF(e.getKey(), sf);
        }
    }

    public static RgInformationSet getRGIS(Context context, String fileName) {
        try {
            return RgInformationSetParser.createRgInformationSet(context.getAssets().open("samples2/rg/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AppInformationSet getAIS(Context context, String fileName) {
        try {
            return AppInformationSetParser.createAppInformationSet(context.getAssets().open("samples2/app/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }
}
