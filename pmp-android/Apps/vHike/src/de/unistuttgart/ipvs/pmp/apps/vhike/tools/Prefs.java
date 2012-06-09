/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    
    @Override
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
