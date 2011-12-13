package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.tab.PresetAppsTab;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * The {@link PresetAppsAdapter} is the list of Apps in the {@link PresetAppsTab}.
 * 
 * @author Marcus Vetter
 */
public class PresetAppsAdapter extends BaseAdapter {
    
    private Context context;
    private List<IApp> apps;
    
    
    public PresetAppsAdapter(Context context, List<IApp> apps) {
        this.context = context;
        this.apps = apps;
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
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_preset_app, null);
        
        /* Set icon, name, description of the requested App */
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_Icon);
        icon.setImageDrawable(app.getIcon());
        
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(app.getName());
        
        return entryView;
    }
}
