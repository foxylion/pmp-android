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
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

/**
 * The {@link PresetPrivacySettingsAdapter} is the list of Privacy Settings in the {@link PresetPSsTabTab}.
 * 
 * @author Marcus Vetter
 */
public class PresetPrivacySettingsAdapter extends BaseAdapter {
    
    private Context context;
    private List<IPrivacySetting> privacySettings;
    
    
    public PresetPrivacySettingsAdapter(Context context, List<IPrivacySetting> privacySettings) {
        this.context = context;
        this.privacySettings = privacySettings;
    }
    
    
    @Override
    public int getCount() {
        return this.privacySettings.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.privacySettings.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IPrivacySetting ps = this.privacySettings.get(position);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_preset_ps, null);
        
        /* Set icon, name, value of the Privacy Setting */
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_Icon);
        icon.setImageDrawable(ps.getResourceGroup().getIcon());
        
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(ps.getName());
        
        TextView value = (TextView) entryView.findViewById(R.id.TextView_Value);
        // TODO: value.setText(ps.getViewValue(ps.getView()));
        value.setText("Value");
        
        return entryView;
    }
}
