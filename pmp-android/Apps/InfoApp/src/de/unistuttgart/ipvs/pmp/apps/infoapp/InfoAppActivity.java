package de.unistuttgart.ipvs.pmp.apps.infoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;

public class InfoAppActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ViewPagerAdapter adapter = new ViewPagerAdapter(this);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
	}
}
