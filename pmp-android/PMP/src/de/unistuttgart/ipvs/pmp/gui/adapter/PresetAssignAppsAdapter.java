package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.unistuttgart.ipvs.pmp.gui.dialog.PresetAssignAppsDialog;
import de.unistuttgart.ipvs.pmp.gui.view.PresetAssignAppsView;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * The {@link PresetAssignAppsAdapter} is the list of Apps, which can be assigned in the {@link PresetAssignAppsDialog}.
 * 
 * @author Marcus Vetter
 */
public class PresetAssignAppsAdapter extends BaseAdapter {
    
    private Context context;
    private List<IApp> apps;
    
    private Map<IApp, Boolean> checkBoxMap = new HashMap<IApp, Boolean>();
    
    public PresetAssignAppsAdapter(Context context, List<IApp> apps) {
        this.context = context;
        this.apps = apps;
        
        // Initialize the checkBoxMap
        for (int pos = 0; pos < getCount(); pos++) {
            checkBoxMap.put((IApp) getItem(pos), false);
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
        PresetAssignAppsView entryView = new PresetAssignAppsView(this.context, app, this);
        
        return entryView;
    }


    public Map<IApp, Boolean> getCheckBoxMap() {
        return checkBoxMap;
    }

}
