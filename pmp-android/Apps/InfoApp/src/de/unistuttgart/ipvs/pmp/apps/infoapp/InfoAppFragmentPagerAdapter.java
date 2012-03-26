package de.unistuttgart.ipvs.pmp.apps.infoapp;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.LocationFragment;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.RSSFeedFragment;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.TranslationFragment;

public class InfoAppFragmentPagerAdapter extends FragmentStatePagerAdapter {
    
    private static ArrayList<Fragment> mPanels = new ArrayList<Fragment>();
    
    
    public InfoAppFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        initPanels();
    }
    
    
    private void initPanels() {
        mPanels.add(LocationFragment.newInstance("Location panel"));
        mPanels.add(TranslationFragment.newInstance("Translation panel"));
        mPanels.add(RSSFeedFragment.newInstance("RSS-Feed panel"));
    }
    
    
    @Override
    public int getCount() {
        return mPanels.size();
    }
    
    
    @Override
    public Fragment getItem(int index) {
        // tv = (TextView) findViewById(R.id.panel_current);
        // tv.setText("" + mPagerAdapter.getCount());
        return mPanels.get(index);
    }
    
    
    public ArrayList<Fragment> getPanels() {
        return mPanels;
    }
}
