package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * 
 * @author Frieder Schüler
 */
public class RGsInstalledAdapter extends BaseAdapter {
    
    private Context context;
    private List<IResourceGroup> resourceGroups;
    
    
    public RGsInstalledAdapter(Context context, List<IResourceGroup> resourceGroups) {
        this.context = context;
        this.resourceGroups = resourceGroups;
    }
    
    
    @Override
    public int getCount() {
        return this.resourceGroups.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.resourceGroups.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IResourceGroup resourceGroup = this.resourceGroups.get(position);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_resourcegroups, null);
        
        /* Set icon, name, description of the requested ResourceGroup */
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_Icon);
        icon.setImageDrawable(resourceGroup.getIcon());
        
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(resourceGroup.getName());
        
        TextView description = (TextView) entryView.findViewById(R.id.TextView_Description);
        description.setText(resourceGroup.getDescription());
        
        TextView status = (TextView) entryView.findViewById(R.id.TextView_Status);
        String text;
        if (resourceGroup.isInstalled() == true) {
            status.setTextColor(Color.GREEN);
            String version = String.valueOf(resourceGroup.getVersion());
            text = "Already installed (V." + version + ")";
        } else {
            status.setTextColor(Color.YELLOW);
            text = "Available for install";
        }
        status.setText(text);
        return entryView;
    }
}
