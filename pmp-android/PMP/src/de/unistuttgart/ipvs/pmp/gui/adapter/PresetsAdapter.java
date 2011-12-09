package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * The {@link PresetsAdapter} is the list of presets in the {@link PresetsActivity}.
 * 
 * @author Marcus Vetter
 */
public class PresetsAdapter extends BaseAdapter {
    
    private Context context;
    private List<IPreset> presets;
    
    
    public PresetsAdapter(Context context, List<IPreset> presets) {
        this.context = context;
        this.presets = presets;
    }
    
    
    @Override
    public int getCount() {
        return this.presets.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.presets.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IPreset preset = this.presets.get(position);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_preset, null);
        
        /* Set name and description of the requested Preset */
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(preset.getName());
        
        TextView description = (TextView) entryView.findViewById(R.id.TextView_Description);
        description.setText(preset.getDescription());
        
        return entryView;
    }
}
