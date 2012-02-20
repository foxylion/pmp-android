package de.unistuttgart.ipvs.pmp.gui.privacysetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

/**
 * View used to display basic informations about a specific {@link IPrivacySetting}.
 * 
 * @author Jakob Jarosch
 */
public class ViewPrivacySettingBasicInformation extends LinearLayout {
    
    /**
     * {@link IPrivacySetting} which is represented by this view.
     */
    protected IPrivacySetting privacySetting;
    
    
    /**
     * Creates a new View.
     * 
     * @param context
     *            {@link Context} which is required for view creation.
     * @param privacySetting
     *            {@link IPrivacySetting} which should be represented.
     */
    public ViewPrivacySettingBasicInformation(Context context, IPrivacySetting privacySetting) {
        super(context);
        
        this.privacySetting = privacySetting;
        
        /* Load the layout */
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.listitem_rg_ps, null);
        addView(v);
        
        refresh();
        addListener();
    }
    
    
    /**
     * Updates the UI.
     */
    private void refresh() {
        ((TextView) findViewById(R.id.TextView_Title)).setText(privacySetting.getName());
        ((TextView) findViewById(R.id.TextView_Description)).setText(privacySetting.getDescription());
    }
    
    
    /**
     * Adds listener to all clickable UI elements.
     */
    private void addListener() {
        setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogPrivacySettingInformation(getContext(),
                        ViewPrivacySettingBasicInformation.this.privacySetting).show();
            }
        });
    }
}
