package de.unistuttgart.ipvs.pmp.gui.preset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * The {@link AdapterAppsAssign} is the list of Apps, which can be assigned in the {@link DialogAppsAssign}.
 * 
 * @author Marcus Vetter
 */
public class AdapterAppsAssign extends BaseAdapter {
    
    private Context context;
    private List<IApp> apps;
    
    private Map<IApp, Boolean> checkBoxMap = new HashMap<IApp, Boolean>();
    
    
    public AdapterAppsAssign(Context context, List<IApp> apps) {
        this.context = context;
        this.apps = apps;
        
        // Initialize the checkBoxMap
        for (int pos = 0; pos < getCount(); pos++) {
            this.checkBoxMap.put((IApp) getItem(pos), false);
        }
    }
    
    
    @Override
    public int getCount() {
        return this.apps.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.apps.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        IApp app = this.apps.get(position);
        ViewAppsAssign entryView = new ViewAppsAssign(this.context, app, this);
        
        return entryView;
    }
    
    
    public Map<IApp, Boolean> getCheckBoxMap() {
        return this.checkBoxMap;
    }
    
}
