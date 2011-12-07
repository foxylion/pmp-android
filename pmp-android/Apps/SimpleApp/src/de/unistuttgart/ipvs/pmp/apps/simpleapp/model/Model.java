/*
 * Copyright 2011 pmp-android development team
 * Project: SimpleApp
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
package de.unistuttgart.ipvs.pmp.apps.simpleapp.model;

import de.unistuttgart.ipvs.pmp.apps.simpleapp.gui.activities.SimpleAppActivity;

public class Model {
    
    /**
     * Instance of this class
     */
    private static Model instance;
    
    /**
     * The context of the app
     */
    private SimpleAppActivity appContext;  
    
    /**
     * Private constructor because of singleton
     */
    private Model() {}
    
    
    /**
     * Returns the stored instance of the class or creates a new one if there is none
     * 
     * @return instance of this class
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        
        return instance;
    }
 
    
    /**
     * Sets the context of the app
     * 
     * @param context
     *            context of the app
     */
    public void setContext(SimpleAppActivity context) {
        this.appContext = context;
    }
    
    
    /**
     * Returns the context of the app
     * 
     * @return app context
     */
    public SimpleAppActivity getContext() {
        return this.appContext;
    }  
    
}
