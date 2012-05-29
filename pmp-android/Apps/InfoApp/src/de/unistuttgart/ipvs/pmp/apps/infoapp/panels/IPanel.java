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
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels;

import android.app.ProgressDialog;
import android.view.View;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IPanel {
    
    /**
     * The view of the panel
     * 
     * @return the view of the panel
     */
    public View getView();
    
    
    /**
     * The title of the panel
     * 
     * @return the title of the panel
     */
    public String getTitle();
    
    
    /**
     * This method is called, if the panel should update its data
     */
    public void update();
    
    
    /**
     * This method is called, if the panel should upload the data to the statistics server
     * 
     * @return Return the URL of the statistics website
     */
    public String upload(ProgressDialog dialog);
    
}
