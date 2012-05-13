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
package de.unistuttgart.ipvs.pmp.apps.infoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import de.unistuttgart.ipvs.pmp.api.PMP;

public class InfoAppActivity extends Activity {
    
    ViewPagerAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.mAdapter = new ViewPagerAdapter(this, this);
        
        this.mPager = (ViewPager) findViewById(R.id.pager);
        this.mPager.setAdapter(this.mAdapter);
        
        this.mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        this.mIndicator.setViewPager(this.mPager);
        
        PMP.get(getApplication()).register(this);
    }
}
