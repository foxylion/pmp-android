/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp;

import android.app.Application;
import android.content.Context;

/**
 * This acts like an internal provider for {@link Context}.
 * 
 * @author Jakob Jarosch
 * 
 */
public class PMPApplication extends Application {
    
    /**
     * Singleton instance of the Class.
     */
    private static PMPApplication instance;
    
    
    /**
     * Called when the Application is started (Activity-Call or Service-StartUp).
     */
    @Override
    public void onCreate() {
        super.onCreate();
        
        PMPApplication.instance = this;
        
        Log.setTagSufix(Constants.PMP_LOG_SUFIX);
    }
    
    
    /**
     * @return Returns an actual {@link Context} of the Application.
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }
    
}
