package de.unistuttgart.ipvs.pmp.apps.infoapp;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.LocationFragment;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.RSSFeedFragment;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.TranslationFragment;

public class InfoAppFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private static ArrayList<Fragment> panels = new ArrayList<Fragment>();

	public InfoAppFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		initPanels();
	}

	private void initPanels() {
		panels.add(LocationFragment.newInstance("LocationFragment Fragment"));
		panels.add(TranslationFragment.newInstance("TranslationFragment Fragment"));
		panels.add(RSSFeedFragment.newInstance("RSS-Feed Fragment"));
	}

	@Override
	public int getCount() {
		return panels.size();
	}

	@Override
	public Fragment getItem(int index) {
		TextView tv;
		// tv = (TextView) findViewById(R.id.panel_current);
		// tv.setText("" + mPagerAdapter.getCount());
		return panels.get(index);
	}
}