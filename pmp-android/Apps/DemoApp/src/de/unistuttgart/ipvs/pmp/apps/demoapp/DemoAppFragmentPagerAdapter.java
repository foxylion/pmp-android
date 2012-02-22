package de.unistuttgart.ipvs.pmp.apps.demoapp;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.unistuttgart.ipvs.pmp.apps.demoapp.panels.Location;
import de.unistuttgart.ipvs.pmp.apps.demoapp.panels.RSSFeed;
import de.unistuttgart.ipvs.pmp.apps.demoapp.panels.Translation;

public class DemoAppFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private static ArrayList<Fragment> panels = new ArrayList<Fragment>();

	public DemoAppFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		initPanels();
	}

	private void initPanels() {
		panels.add(Location.newInstance("Location Fragment"));
		panels.add(Translation.newInstance("Translation Fragment"));
		panels.add(RSSFeed.newInstance("RSS-Feed Fragment"));
	}

	@Override
	public int getCount() {
		return panels.size();
	}

	@Override
	public Fragment getItem(int index) {
		return panels.get(index);
	}
}