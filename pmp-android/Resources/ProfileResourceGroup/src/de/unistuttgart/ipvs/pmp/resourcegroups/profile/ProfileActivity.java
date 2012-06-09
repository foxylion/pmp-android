/*
 * Copyright 2012 pmp-android development team
 * Project: ProfileResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class ProfileActivity extends Activity implements OnClickListener {
    
    private ToggleButton tb;
    
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        this.tb = (ToggleButton) findViewById(R.id.toggleButton);
        this.tb.setOnClickListener(this);
        this.tb.toggle();
        startService(new Intent(this, ProfileService.class));
    }
    
    
    @Override
    public void onClick(View v) {
        if (this.tb.isChecked()) {
            startService(new Intent(this, ProfileService.class));
        } else {
            stopService(new Intent(this, ProfileService.class));
        }
    }
}
