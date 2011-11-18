package de.unistuttgart.ipvs.pmp.gui2.adapters;

import java.util.List;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui2.activities.AppsActivity;
import de.unistuttgart.ipvs.pmp.gui2.placeholder.App;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;;

/**
 * The {@link AppsAdapter} is the list of Apps in the {@link AppsActivity}.
 * 
 * @author Jakob Jarosch
 */
public class AppsAdapter extends BaseAdapter {
    
    private Context context;
    private List<App> apps;
    
    
    public AppsAdapter(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
    }
    
    
    @Override
    public int getCount() {
        return apps.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        App app = apps.get(position);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.pmp_apps_list_item, null);
        
        /* Set icon, name, description of the requested App */
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_Icon);
        icon.setImageBitmap(app.getIcon());
        
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(app.getName());
        
        TextView description = (TextView) entryView.findViewById(R.id.TextView_Description);
        description.setText(app.getDescription());
        
        return entryView;
    }
}
