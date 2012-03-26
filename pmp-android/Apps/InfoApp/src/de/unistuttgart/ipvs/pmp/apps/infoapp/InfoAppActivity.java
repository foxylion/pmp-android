/*
 * Copyright 2012 pmp-android development team
 * Project: App3
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.viewpagerindicator.CirclePageIndicator;

public class InfoAppActivity extends FragmentActivity {
    
    private ViewPager mPager;
    private InfoAppFragmentPagerAdapter mAdapter;
    private CirclePageIndicator mIndicator;
    
    SharedPreferences mSettings;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mPager = (ViewPager) findViewById(R.id.viewpager);
        this.mAdapter = new InfoAppFragmentPagerAdapter(getSupportFragmentManager());
        this.mPager.setAdapter(this.mAdapter);
        
        this.mIndicator = (CirclePageIndicator) findViewById(R.id.navigation);
        this.mIndicator.setViewPager(this.mPager);
        
        mSettings = getSharedPreferences("settings", 0);
        
        this.mIndicator.setCurrentItem(mSettings.getInt("panel", 0));
        
        // You can also do: indicator.setViewPager(pager, initialPage);
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.options:
                Intent optIntent = new Intent(this, SettingsActivity.class);
                String[] panelNames = new String[mAdapter.getCount()];
                for (int j = 0; j < mAdapter.getCount(); j++) {
                    Fragment asd = mAdapter.getPanels().get(j);
                    panelNames[j] = asd.getArguments().getString("title");
                }
                optIntent.putExtra("panels", panelNames);
                startActivityForResult(optIntent, 1);
                return true;
            case R.id.help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
        }
    }
}
