/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;

/**
 * Abstract class for getting the url of a resource group
 * 
 * @author Thorsten Berberich
 * 
 */
public abstract class AbstractRequestRessourceHandler extends PMPRequestResourceHandler {
    
    /**
     * Stored url
     */
    private String URL = null;
    
    /**
     * Shown {@link ProgressDialog}
     */
    protected ProgressDialog dialog;
    
    /**
     * The {@link Activity}
     */
    protected Activity activity;
    
    
    /**
     * Constructor
     */
    public AbstractRequestRessourceHandler(ProgressDialog dialog, Activity activity) {
        this.dialog = dialog;
        this.activity = activity;
    }
    
    
    /**
     * @return the uRL
     */
    public String getURL() {
        return this.URL;
    }
    
    
    /**
     * @param uRL
     *            the uRL to set
     */
    public void setURL(String url) {
        this.URL = url;
    }
    
    
    /**
     * Open the url with browser
     */
    public void openURLwithBrowser() {
        Log.d(this, "The returned URL was: " + this.URL);
        if (this.URL == null || this.URL.equals("")) {
            Looper.prepare();
            Toast.makeText(this.activity, this.activity.getString(R.string.upload_error) + "\"" + this.URL + "\"",
                    Toast.LENGTH_LONG).show();
            Looper.loop();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(this.URL));
            this.activity.startActivity(i);
            Log.d(this, "Started browser");
        }
        
    }
}
