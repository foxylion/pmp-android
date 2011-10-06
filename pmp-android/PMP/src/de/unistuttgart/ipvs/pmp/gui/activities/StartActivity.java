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
package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;

/**
 * StartActivity is the main activity of PMP.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class StartActivity extends Activity {
    
    /**
     * Buttons Layout
     */
    LinearLayout layout;
    /**
     * Main Layout of the Activity, which will be draw to the Canvas
     */
    LinearLayout parentLayout;
    
    /**
     * ScrollLayout for the horizontal mode
     */
    ScrollView scroll;
    /**
     * Views of the Activity
     */
    TextView PMPLabel;
    TextView PMPDescriptionLabel;
    Button apps;
    Button ressources;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //        createParentLayout();
        //        createChildren();
        
        //        this.parentLayout.addView(this.PMPLabel);
        //        this.parentLayout.addView(this.PMPDescriptionLabel);
        //        this.parentLayout.addView(this.layout);
        //        
        //        this.scroll.addView(this.parentLayout);
        //        
        //        setContentView(this.scroll);
        
        setContentView(R.layout.start);
        
        ((Button) findViewById(R.id.start_button_application)).setOnTouchListener(new ApplicationsListener());
        ((Button) findViewById(R.id.start_button_resources)).setOnTouchListener(new RessourcesListener());
    }
}

/**
 * ApplicationListener start the ApplicationsActivity
 * 
 * @author Alexander Wassiljew
 * 
 */
class ApplicationsListener implements OnTouchListener {
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            
            Intent intent = new Intent(v.getContext(), ApplicationsActivity.class);
            if (v.getContext() != null) {
                v.getContext().startActivity(intent);
            }
            return false;
        }
        return false;
    }
}

/**
 * RessourcesListener start the RessourcesActivity
 * 
 * @author Alexander Wassiljew
 * 
 */
class RessourcesListener implements OnTouchListener {
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            Intent intent = new Intent(v.getContext(), RessourcesActivity.class);
            if (v.getContext() != null) {
                v.getContext().startActivity(intent);
            }
            return false;
        }
        return false;
    }
}
