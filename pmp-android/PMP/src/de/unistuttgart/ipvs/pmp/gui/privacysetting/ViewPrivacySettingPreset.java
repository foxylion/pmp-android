package de.unistuttgart.ipvs.pmp.gui.privacysetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.context.DialogContextChange;
import de.unistuttgart.ipvs.pmp.gui.preset.AdapterPrivacySettings;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * The {@link ViewPrivacySettingPreset} represents one assigned {@link IPrivacySetting}. Each view has an expandable
 * list of all assigned {@link IContextAnnotation}s. If no annotations are assigned the list can't be expanded. So the
 * context menu will be shown directly on a short touch.
 * 
 * @author Jakob Jarosch
 */
public class ViewPrivacySettingPreset extends LinearLayout {
    
    /**
     * The {@link IPreset} to which the {@link IPrivacySetting} is assigned.
     */
    private IPreset preset;
    
    /**
     * The {@link IPrivacySetting} which is represented by this view.
     */
    private IPrivacySetting privacySetting;
    
    /**
     * The {@link Adapter} which holds all the {@link IPrivacySetting}s assigned to the {@link IPreset}.
     */
    private AdapterPrivacySettings adapter;
    
    
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
    
    
    private boolean isListExpanded() {
        return (((LinearLayout) findViewById(R.id.LinearLayout_MenuAndContexts)).getVisibility() == View.VISIBLE);
    }
    
    
    public void toggleMenuAndContexts() {
        ImageView stateView = (ImageView) findViewById(R.id.ImageView_State);
        stateView.setImageResource(isListExpanded() ? R.drawable.icon_expand_closed : R.drawable.icon_expand_opened);
        
        LinearLayout menuAndContextsLayout = (LinearLayout) findViewById(R.id.LinearLayout_MenuAndContexts);
        menuAndContextsLayout.setVisibility(isListExpanded() ? View.GONE : View.VISIBLE);
    }
    
    
    private void refresh() {
        if (preset.getContextAnnotations(privacySetting).size() == 0) {
            ((ImageView) findViewById(R.id.ImageView_State)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.LinearLayout_MenuAndContexts)).setVisibility(View.GONE);
        } else {
            ((ImageView) findViewById(R.id.ImageView_State)).setVisibility(View.VISIBLE);
        }
        
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
        
        ((LinearLayout) findViewById(R.id.LinearLayout_Contexts)).removeAllViews();
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
        
        ((LinearLayout) v.findViewById(R.id.LinearLayout_Context)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogContextChange(getContext(), preset, privacySetting, context,
                        new DialogContextChange.ICallback() {
                            
                            @Override
                            public void callback() {
                                refresh();
                            }
                        }).show();
            }
        });
        
        ((LinearLayout) findViewById(R.id.LinearLayout_Contexts)).addView(v);
    }
    
    
    private void addListener() {
        ((LinearLayout) findViewById(R.id.LinearLayout_BasicInformations)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (preset.getContextAnnotations(privacySetting).size() > 0) {
                    toggleMenuAndContexts();
                } else {
                    adapter.reactOnItemClick(ViewPrivacySettingPreset.this);
                }
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
        
        ((ImageButton) findViewById(R.id.ImageButton_Info)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogPrivacySettingInformation(getContext(), privacySetting).show();
            }
        });
        
        ((ImageButton) findViewById(R.id.ImageButton_Edit)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogPrivacySettingEdit(getContext(), privacySetting, preset
                        .getGrantedPrivacySettingValue(privacySetting), new DialogPrivacySettingEdit.ICallback() {
                    
                    @Override
                    public void result(boolean changed, String newValue) {
                        if (changed) {
                            try {
                                preset.assignPrivacySetting(privacySetting, newValue);
                            } catch (PrivacySettingValueException e) {
                                Log.e(ViewPrivacySettingPreset.this, "Couldn't set new value for PrivacySetting, PSVE",
                                        e);
                                GUITools.showToast(getContext(),
                                        getContext().getString(R.string.failure_invalid_ps_value), Toast.LENGTH_LONG);
                            }
                            
                            refresh();
                        }
                    }
                }).show();
            }
        });
        
        ((ImageButton) findViewById(R.id.ImageButton_AddContext)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogContextChange(getContext(), preset, privacySetting, null,
                        new DialogContextChange.ICallback() {
                            
                            @Override
                            public void callback() {
                                refresh();
                            }
                        });
            }
        });
        
        ((ImageButton) findViewById(R.id.ImageButton_Delete)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                adapter.removePrivacySetting(privacySetting);
            }
        });
    }
}
