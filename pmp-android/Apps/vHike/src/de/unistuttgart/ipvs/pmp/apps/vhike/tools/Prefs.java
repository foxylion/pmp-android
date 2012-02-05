package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;

public class Prefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.prefs);
        
        // Register a change listener
        
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
    
    
    // Inherited abstract method so it must be implemented
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(this, "Preferences changed, key=" + key);
    }
    
    
    // Static method to return the preference for whether to display compass
    public static boolean getCompass(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("compass", true);
    }
    
    
    // Static method to return the preference for the GPS precision setting
    public static String getGPSPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("gpsPref", "1");
    }
}
