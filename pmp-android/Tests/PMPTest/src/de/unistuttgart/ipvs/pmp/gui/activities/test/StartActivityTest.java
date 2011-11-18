/*
 * Copyright 2011 pmp-android development team
 * Project: PMPTest
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
 *
package de.unistuttgart.ipvs.pmp.gui.activities.test;

import android.test.ActivityInstrumentationTestCase2;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.StartActivity;

/**
 * Test class: StartActivity
 * 
 * Test if localization is correct when setting language in device. Test activity (not null).
 * 
 * @author Andre
 * 
 *

public class StartActivityTest extends ActivityInstrumentationTestCase2<StartActivity> {
    
    StartActivity mActivity;
    String appString;
    String resString;
    
    
    public StartActivityTest() {
        super("de.unistuttgart.ipvs.pmp", StartActivity.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        this.mActivity = getActivity();
        // FIXME this.appString = this.mActivity.getString(R.string.apps);
        // FIXME this.resString = this.mActivity.getString(R.string.ress);
    }
    
    
    public void testingPreconditions() {
        assertNotNull(this.mActivity);
        assertNotNull(this.appString);
        assertNotNull(this.resString);
    }
    
    
    public void testEnglishButtons() {
        assertEquals("Applications", this.appString);
        assertEquals("Resources", this.resString);
    }
    
    //    public void testGermanButtons() {
    //        assertEquals("Applikationen", this.appString);
    //        assertEquals("Ressourcen", this.resString);
    //    }
    
}
*/
// FIXME, really
