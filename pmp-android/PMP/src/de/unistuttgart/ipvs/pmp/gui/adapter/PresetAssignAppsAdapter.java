package de.unistuttgart.ipvs.pmp.gui.adapter;

import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.dialog.PresetAssignAppsDialog;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * The {@link PresetAssignAppsAdapter} is the list of Apps, which can be assigned in the {@link PresetAssignAppsDialog}.
 * 
 * @author Marcus Vetter
 */
public class PresetAssignAppsAdapter extends BaseAdapter {
    
    private Context context;
    private List<IApp> apps;
    
    
    public PresetAssignAppsAdapter(Context context, List<IApp> apps) {
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
        final LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.listitem_preset_assign_app, null);

        
        /* Set icon, name, description of the requested App */
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_Icon);
        icon.setImageDrawable(app.getIcon());
        
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(app.getName());
        
        /* CheckBox and LinearLayout */
        final CheckBox checkBox = (CheckBox) entryView.findViewById(R.id.CheckBox_AssignApp);
        final LinearLayout linlay = (LinearLayout) entryView.findViewById(R.id.LinearLayout);
        
        // Add Listener
        checkBox.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    linlay.setBackgroundColor(GUIConstants.COLOR_BG_GREEN);
                } else {
                    linlay.setBackgroundColor(Color.TRANSPARENT);
                }
            }
            
        });
        

        linlay.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    linlay.setBackgroundColor(Color.TRANSPARENT);
                    checkBox.setChecked(false);
                } else {
                    linlay.setBackgroundColor(GUIConstants.COLOR_BG_GREEN);
                    checkBox.setChecked(true);
                }
            }
        });
        
        return entryView;
    }
    
    
    
    private void registerListener() {

    }
}
