package de.unistuttgart.ipvs.pmp.apps.infoapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.viewpagerindicator.TitleProvider;

import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.connections.ConnectionsPanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.EnergyPanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.example.ExamplePanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.hardware.HardwarePanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.profile.ProfilePanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.rss.RSSPanel;

public class ViewPagerAdapter extends PagerAdapter implements TitleProvider {
	
	private final List<IPanel> panels = new ArrayList<IPanel>();
	
	
	public ViewPagerAdapter(Context context) {
		
		if (!this.panels.isEmpty()) {
			this.panels.clear();
		}
		this.panels.add(new ExamplePanel(context));
		this.panels.add(new ConnectionsPanel(context));
		this.panels.add(new EnergyPanel(context));
		this.panels.add(new ProfilePanel(context));
		this.panels.add(new HardwarePanel(context));
		this.panels.add(new RSSPanel(context));
	}
	
	
	public String getTitle(int position) {
		return this.panels.get(position).getTitle();
	}
	
	
	@Override
	public int getCount() {
		return this.panels.size();
	}
	
	
	@Override
	public Object instantiateItem(View pager, int position) {
		
		View v = this.panels.get(position).getView();
		((ViewPager) pager).addView(v, 0);
		
		return v;
	}
	
	
	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}
	
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
	
}
