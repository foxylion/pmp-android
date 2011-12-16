package de.unistuttgart.ipvs.pmp.gui.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetAssignAppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * This is the View of one Element (IAPP) of the PresetAssignAppsAdapter
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignAppsView extends LinearLayout {
    
    /**
     * The CheckBox
     */
    private final CheckBox checkBox;
    
    /**
     * Linear layout of this view
     */
    private final LinearLayout linlay;
    
    /**
     * Preset Assign Apps Adapter
     */
    private PresetAssignAppsAdapter adapter;
    
    /**
     * The app of the view
     */
    private IApp app;
    
    
    /**
     * Constructor to instantiate the view
     * 
     * @param context
     *            context of the view
     * @param app
     *            the app of the view
     * @param presetAssignAppsAdapter
     *            the adapter of the assigned apps
     */
    public PresetAssignAppsView(Context context, IApp app, PresetAssignAppsAdapter presetAssignAppsAdapter) {
        super(context);
        
        this.app = app;
        this.adapter = presetAssignAppsAdapter;
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = (LinearLayout) inflater.inflate(R.layout.listitem_preset_assign_app, null);
        addView(entryView);
        
        /* Set icon, name, description of the requested App */
        ImageView icon = (ImageView) entryView.findViewById(R.id.ImageView_Icon);
        icon.setImageDrawable(app.getIcon());
        
        TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        name.setText(app.getName());
        
        /* CheckBox and LinearLayout */
        this.checkBox = (CheckBox) entryView.findViewById(R.id.CheckBox_AssignApp);
        this.linlay = (LinearLayout) entryView.findViewById(R.id.LinearLayout);
        
        /* Add Listener */
        addListener();
    }
    
    
    private void checkBoxChanged(boolean checked) {
        adapter.getCheckBoxMap().put(app, checked);
    }
    
    
    /**
     * Add listener to the CheckBox and LinearLayout
     */
    private void addListener() {
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxChanged(isChecked);
                if (isChecked) {
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
    }
}
