package de.unistuttgart.ipvs.pmp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * Utility to get a Maps API key using an Asset.
 * 
 * @author Tobias Kuhn
 * 
 */
public class MapsAPIKeyAsset {
    
    private static final String TAG = "MapsAPIKeyAsset";
    
    private static final String FILE_NAME = "mapsKey.properties";
    private static final String PROPERTY_NAME = "mapsKey";
    
    
    /**
     * 
     * @param context
     * @return the key stored in the mapsKey file, or null, if an error occured
     */
    public static String getKey(Context context) {
        try {
            InputStream is = context.getAssets().open(FILE_NAME);
            Properties p = new Properties();
            p.load(is);
            return p.getProperty(PROPERTY_NAME);
            
        } catch (IOException ioe) {
            Log.e(TAG, "Could not get the API key from " + PROPERTY_NAME + " in assets/" + FILE_NAME
                    + ". Does it exist?", ioe);
            return null;
        }
    }
}
