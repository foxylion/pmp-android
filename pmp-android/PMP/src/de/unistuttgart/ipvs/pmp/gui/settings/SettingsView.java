package de.unistuttgart.ipvs.pmp.gui.settings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class SettingsView extends LinearLayout {
    
    /**
     * The CheckBox
     */
    protected final CheckBox checkBox;
    
    /**
     * Linear layout of this view
     */
    private final LinearLayout linlay;
    
    /**
     * The app of the view
     */
    private Setting setting;
    
    
    /**
     * Constructor
     * 
     * @param context
     *            the context
     * @param setting
     *            the current Setting
     */
    public SettingsView(Context context, Setting setting) {
        super(context);
        
        this.setting = setting;
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = inflater.inflate(R.layout.listitem_setting, null);
        addView(entryView);
        
        /* Set name, description, icon and checked state of the checkbox of the requested Setting */
        TextView name = (TextView) entryView.findViewById(R.id.Setting_Name);
        name.setText(setting.getName());
        
        TextView description = (TextView) entryView.findViewById(R.id.Setting_Description);
        description.setText(setting.getDescription());
        
        ImageView icon = (ImageView) entryView.findViewById(R.id.Setting_Icon);
        icon.setImageDrawable(setting.getIcon());
        
        /* CheckBox and LinearLayout */
        this.checkBox = (CheckBox) entryView.findViewById(R.id.Setting_CheckBox);
        this.linlay = (LinearLayout) entryView.findViewById(R.id.Setting_Linear_Layout);
        
        /* Update check box */
        checkBoxChanged(setting.isEnabled());
        this.checkBox.setChecked(setting.isEnabled());
        
        /* Add Listener */
        addListener();
    }
    
    
    /**
     * Update the Linear Layout (Color) and the setting, when the CheckBox has changed
     * 
     * @param checked
     *            true, if the CheckBox is now selected
     */
    private void checkBoxChanged(boolean checked) {
        this.setting.setEnabled(checked);
        if (checked) {
            this.linlay.setBackgroundColor(GUIConstants.COLOR_BG_GREEN);
        } else {
            this.linlay.setBackgroundColor(Color.TRANSPARENT);
        }
    }
    
    
    /**
     * Add listener to the CheckBox and LinearLayout
     */
    private void addListener() {
        this.checkBox.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                checkBoxChanged(checkBox.isChecked());
            }
        });
        
        this.linlay.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                checkBoxChanged(!SettingsView.this.checkBox.isChecked());
                SettingsView.this.checkBox.setChecked(!SettingsView.this.checkBox.isChecked());
            }
        });
    }
}
