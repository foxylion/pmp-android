package de.unistuttgart.ipvs.pmp.gui.placeholder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class SampleModel {
    
    private static Context context;
    
    public static List<IApp> apps = new ArrayList<IApp>();
    public static List<IResourceGroup> rgs = new ArrayList<IResourceGroup>();
    
    public static List<IServiceFeature> sfEnabled = new ArrayList<IServiceFeature>();
    public static List<IServiceFeature> sfDisabled = new ArrayList<IServiceFeature>();
    
    public static List<IPrivacySetting> privacySettings = new ArrayList<IPrivacySetting>();
    
    
    public SampleModel(Context context) {
        SampleModel.context = context;
        
        apps.add(new App("Barcode Scanner",
                "Barcode Scanner can scan barcodes and view details about the scanned product.",
                loadBitmap(R.drawable.test_icon1)));
        apps.add(new App("Calendar", "The Calendar allows you to manage your appointments.",
                loadBitmap(R.drawable.test_icon2)));
        apps.add(new App("Facebook", "With this App you can surf on your facebook wall.",
                loadBitmap(R.drawable.test_icon3)));
        apps.add(new App("Google Mail", "With Google Mail you can manage your mails.",
                loadBitmap(R.drawable.test_icon4)));
        apps.add(new App("IMDb", "IMDb gives you access to the greatest movie database worldwide.",
                loadBitmap(R.drawable.test_icon5)));
        apps.add(new App("SMS", "Use the App to send short messages to your friends.",
                loadBitmap(R.drawable.test_icon6)));
        apps.add(new App("TV App", "Brings all the TV channels to your mobile device.",
                loadBitmap(R.drawable.test_icon7)));
        apps.add(new App("Compass", "Gives you the ability to get your current position.",
                loadBitmap(R.drawable.test_icon8)));
        apps.add(new App("RSS Feeds", "Access news feeds directly without any troubles.",
                loadBitmap(R.drawable.test_icon9)));
        apps.add(new App("Wikipedia", "Become access to the greatest lexica in the world.",
                loadBitmap(R.drawable.test_icon10)));
        
        rgs.add(new ResourceGroup("test.rg1", "Database Resource",
                "The Database Resource provides a basic database for storing data", loadBitmap(R.drawable.icon_rgs)));
        
        sfEnabled.add(new ServiceFeature("Use Camera", "Must be enabled to get any functionality.", true, true));
        sfEnabled.add(new ServiceFeature("Internet Connection", "Required to fetch details for a scanned product.",
                true, true));
        sfEnabled.add(new ServiceFeature("Facebook Share", "Enable it to share a product on facebook.", true, true));
        
        sfDisabled.add(new ServiceFeature("Personal Information", "If it is enabled you can post product ratings.",
                false, true));
        sfDisabled.add(new ServiceFeature("Credit Card-Details",
                "Allows you to directly buy and pay a scanned product.", false, false));
        sfDisabled.add(new ServiceFeature("Email Account", "Send product via email to your friends.", false, true));
        
        privacySettings.add(new PrivacySetting("test1", rgs.get(0), "Read Entries", "Read entries from the database"));
        privacySettings
                .add(new PrivacySetting("test2", rgs.get(0), "Write Entries", "Write entries from the database"));
        privacySettings.add(new PrivacySetting("test3", rgs.get(0), "Create Database", "Create a database for tables"));
    }
    
    
    private Drawable loadBitmap(int id) {
        return context.getResources().getDrawable(id);
    }
}
