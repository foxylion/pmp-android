package de.unistuttgart.ipvs.pmp.gui.privacysetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.context.DialogContextChange;
import de.unistuttgart.ipvs.pmp.gui.preset.AdapterPrivacySettings;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

public class ViewPrivacySettingPreset extends LinearLayout {
    
    private IPreset preset;
    private IPrivacySetting privacySetting;
    private AdapterPrivacySettings adapter;
    
    private boolean expanded = false;
    
    
    public ViewPrivacySettingPreset(Context context, IPreset preset, IPrivacySetting privacySetting,
            AdapterPrivacySettings adapter) {
        super(context);
        
        this.preset = preset;
        this.privacySetting = privacySetting;
        this.adapter = adapter;
        
        // Load the layout
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.listitem_preset_ps, null);
        
        v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT,
                ListView.LayoutParams.WRAP_CONTENT));
        
        addView(v);
        
        // Set LayoutParams of view to fill_parent for width.
        setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.WRAP_CONTENT));
        
        addListener();
        
        refresh();
    }
    
    
    public void toggleMenuAndContexts() {
        LinearLayout menuAndContextsLayout = (LinearLayout) findViewById(R.id.LinearLayout_MenuAndContexts);
        menuAndContextsLayout.setVisibility(expanded ? View.GONE : View.VISIBLE);
        
        ImageView stateView = (ImageView) findViewById(R.id.ImageView_State);
        stateView.setImageResource(expanded ? R.drawable.icon_expand_closed : R.drawable.icon_expand_opened);
        
        expanded = !expanded;
    }
    
    
    private void refresh() {
        ((TextView) findViewById(R.id.TextView_Name_PS)).setText(this.privacySetting.getName());
        
        TextView value = (TextView) findViewById(R.id.TextView_Value);
        try {
            value.setText(getContext().getString(R.string.value)
                    + ": "
                    + this.privacySetting.getHumanReadableValue(this.preset
                            .getGrantedPrivacySettingValue(this.privacySetting)));
        } catch (PrivacySettingValueException e) {
            Log.e(this, "The Privacy Setting value is invalid and is beeing marked red in the GUI", e);
            value.setText(getContext().getString(R.string.value) + ": "
                    + this.preset.getGrantedPrivacySettingValue(this.privacySetting));
            value.setTextColor(GUIConstants.COLOR_BG_RED);
        }
        
        for (IContextAnnotation context : this.preset.getContextAnnotations(this.privacySetting)) {
            addContext(context);
        }
    }
    
    
    private void addContext(final IContextAnnotation context) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.listitem_preset_ps_context, null);
        
        ((TextView) v.findViewById(R.id.TextView_Context_Name)).setText(context.getContext().getName());
        ((TextView) v.findViewById(R.id.TextView_Context_Value)).setText("Value wehen active: "
                + context.getOverridePrivacySettingValue());
        ((TextView) v.findViewById(R.id.TextView_Context_Description)).setText(context.getContextCondition());
        ((ImageView) v.findViewById(R.id.ImageView_Context_State)).setVisibility(context.isActive() ? View.GONE
                : View.VISIBLE);
        
        v.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogContextChange(getContext(), preset, privacySetting, context).show();
            }
        });
        
        ((LinearLayout) findViewById(R.id.LinearLayout_MenuAndContexts)).addView(v);
    }
    
    
    private void addListener() {
        ((LinearLayout) findViewById(R.id.LinearLayout_BasicInformations)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                toggleMenuAndContexts();
            }
        });
        ((LinearLayout) findViewById(R.id.LinearLayout_BasicInformations))
                .setOnLongClickListener(new OnLongClickListener() {
                    
                    @Override
                    public boolean onLongClick(View v) {
                        adapter.reactOnItemClick(ViewPrivacySettingPreset.this);
                        
                        return true;
                    }
                });
    }
}
