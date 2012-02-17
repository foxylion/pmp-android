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
    private List<SettingAbstract<?>> settings;
    
    
    /**
     * Constructor
     * 
     * @param context
     *            context
     * @param settings
     *            list of Settings
     */
    public SettingsAdapter(Context context, List<SettingAbstract<?>> settings) {
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
    public boolean hasStableIds() {
        return true;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View entryView = convertView;
        if (entryView == null || !(entryView instanceof SettingListItem)) {
            entryView = new SettingListItem(this.context, this.settings.get(position));
        }
        
        return entryView;
    }
    
}
