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
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.profile;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

public class ProfilePanel implements IPanel {
    
    private TextView view;
    
    
    public ProfilePanel(Context context) {
        
        // Instantiate the view
        this.view = new TextView(context);
        
        // Set text
        this.view.setText("ProfilePanel");
        
    }
    
    
    public View getView() {
        return this.view;
    }
    
    
    public String getTitle() {
        return "Profile";
    }
    
    
    public void update() {
        // TODO Auto-generated method stub
        
    }
    
    
    public String upload() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
