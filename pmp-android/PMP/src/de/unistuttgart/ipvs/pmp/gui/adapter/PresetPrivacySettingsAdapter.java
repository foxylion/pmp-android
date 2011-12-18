package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * The {@link PresetPrivacySettingsAdapter} is the list of Privacy Settings in the {@link PresetPSsTabTab}.
 * 
 * @author Marcus Vetter
 */
public class PresetPrivacySettingsAdapter extends BaseExpandableListAdapter {
    
    private Context context;
    
    private ArrayList<IResourceGroup> rgList;
    
    private ArrayList<ArrayList<IPrivacySetting>> psList;
    
    
    public PresetPrivacySettingsAdapter(Context context, ArrayList<IResourceGroup> rgList,
            ArrayList<ArrayList<IPrivacySetting>> psList) {
        this.context = context;
        this.rgList = rgList;
        this.psList = psList;
    }
    
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return psList.get(groupPosition).get(childPosition);
    }
    
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        
        // Get the Privacy Setting
        IPrivacySetting ps = (IPrivacySetting) getChild(groupPosition, childPosition);
        
        // Inflate the layout
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = infalInflater.inflate(R.layout.listitem_preset_ps, null);
        
        // Set name and value of one Privacy Setting
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name_PS);
        name.setText(ps.getName());
        
        TextView value = (TextView) entryView.findViewById(R.id.TextView_Value);
        // TODO: value.setText(ps.getViewValue(ps.getView()));
        value.setText("Value");
        
        return entryView;
    }
    
    
    @Override
    public int getChildrenCount(int groupPosition) {
        return psList.get(groupPosition).size();
    }
    
    
    @Override
    public Object getGroup(int groupPosition) {
        return rgList.get(groupPosition);
    }
    
    
    @Override
    public int getGroupCount() {
        return rgList.size();
    }
    
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        
        // Get the ResourceGroup
        IResourceGroup rg = (IResourceGroup) getGroup(groupPosition);
        
        // Inflate the layout
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = infalInflater.inflate(R.layout.listitem_preset_rg, null);
        
        // Set name and icon of the ResourceGrouop
        TextView name = (TextView) entryView.findViewById(R.id.TextView_RG_Name);
        name.setText(rg.getName());
        
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_RG_Icon);
        icon.setImageDrawable(rg.getIcon());
        
        return entryView;
    }
    
    
    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    
    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
    
    
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
    
}
