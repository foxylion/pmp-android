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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.viewpagerindicator.TitleProvider;

import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.EnergyPanel;

/**
 * Adapter for all panels
 * 
 * @author Marcus Vetter, Thorsten Berberich
 * 
 */
public class ViewPagerAdapter extends PagerAdapter implements TitleProvider {
    
    private final List<IPanel> panels = new ArrayList<IPanel>();
    
    
    public ViewPagerAdapter(Context context, InfoAppActivity activity) {
        
        if (!this.panels.isEmpty()) {
            this.panels.clear();
        }
        //        this.panels.add(new ExamplePanel(context));
        //        this.panels.add(new ConnectionsPanel(context, activity));
        this.panels.add(new EnergyPanel(context, activity));
        //        this.panels.add(new ProfilePanel(context));
        //        this.panels.add(new HardwarePanel(context));
        //this.panels.add(new RSSPanel(context));
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
    
    
    /**
     * Get a panel to the given position
     * 
     * @param position
     *            position of the panel
     * @return the {@link IPanel} at the position
     */
    public IPanel getPanel(int position) {
        return panels.get(position);
    }
    
}
