package de.unistuttgart.ipvs.pmp.apps.infoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class InfoAppActivity extends Activity {
    
    ViewPagerAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.mAdapter = new ViewPagerAdapter(this);
        
        this.mPager = (ViewPager) findViewById(R.id.pager);
        this.mPager.setAdapter(this.mAdapter);
        
        this.mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        this.mIndicator.setViewPager(this.mPager);
    }
}
