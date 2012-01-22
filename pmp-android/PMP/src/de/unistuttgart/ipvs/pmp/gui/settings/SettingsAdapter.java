package de.unistuttgart.ipvs.pmp.gui.settings;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class SettingsAdapter extends BaseAdapter {
    
    /**
     * The context
     */
    private Context context;
    
    /**
     * List of all Settings
     */
    private List<Setting> settings;
    
    
    /**
     * Constructor
     * 
     * @param context
     *            context
     * @param settings
     *            list of Settings
     */
    public SettingsAdapter(Context context, List<Setting> settings) {
        this.context = context;
        this.settings = settings;
    }
    
    
    @Override
    public int getCount() {
        return this.settings.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.settings.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        Setting setting = this.settings.get(position);
        SettingsView entryView = new SettingsView(this.context, setting);
        
        return entryView;
    }
    
}
