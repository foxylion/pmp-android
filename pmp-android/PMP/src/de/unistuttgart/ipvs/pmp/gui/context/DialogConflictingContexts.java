package de.unistuttgart.ipvs.pmp.gui.context;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.preset.ActivityPreset;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

public class DialogConflictingContexts extends Dialog {
    
    private IPreset preset;
    private IContextAnnotation contextAnnotation;
    private List<IPreset> conflictingPrivacySettings;
    private List<IContextAnnotation> conflictingContextAnnotations;
    
    
    public DialogConflictingContexts(Context context, IPreset preset, IContextAnnotation contextAnnotation,
            List<IPreset> conflictingPriacySettings, List<IContextAnnotation> conflictingContextAnnotations) {
        super(context);
        
        this.preset = preset;
        this.contextAnnotation = contextAnnotation;
        this.conflictingPrivacySettings = conflictingPriacySettings;
        this.conflictingContextAnnotations = conflictingContextAnnotations;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_context_conflicting);
        
        refresh();
        addListener();
    }
    
    
    private void refresh() {
        LinearLayout presetsConflicting = (LinearLayout) findViewById(R.id.LinearLayout_PresetConflicts);
        LinearLayout contextConflicting = (LinearLayout) findViewById(R.id.LinearLayout_ContextConflicts);
        
        for (final IPreset pr : conflictingPrivacySettings) {
            TextView tv = new TextView(getContext());
            tv.setText("Preset '" + pr.getName() + "' has a conflict.");
            tv.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ActivityPreset.class);
                    i.putExtra(GUIConstants.PRESET_IDENTIFIER, pr.getLocalIdentifier());
                    GUITools.startIntent(i);
                }
            });
            presetsConflicting.addView(tv);
        }
        
        for (final IContextAnnotation ca : conflictingContextAnnotations) {
            
            /* Read out context condition */
            String condition;
            try {
                condition = ca.getHumanReadableContextCondition();
            } catch (InvalidConditionException e) {
                Log.e(this, "Condition can't be converted into a human readable version", e);
                condition = ca.getContextCondition();
            }
            
            /* Read out override value */
            String overrideValue;
            try {
                overrideValue = ca.getPrivacySetting().getHumanReadableValue(ca.getOverridePrivacySettingValue());
            } catch (PrivacySettingValueException e) {
                Log.e(this, "PrivacySetting value can't be converted into a human readable version", e);
                overrideValue = ca.getOverridePrivacySettingValue();
            }
            
            /* create the text view */
            TextView tv = new TextView(getContext());
            tv.setText(getContext().getString(R.string.context_other_context_conflict_text, ca.getPreset().getName(),
                    condition, overrideValue));
            tv.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ActivityPreset.class);
                    i.putExtra(GUIConstants.PRESET_IDENTIFIER, ca.getPreset().getLocalIdentifier());
                    GUITools.startIntent(i);
                }
            });
            contextConflicting.addView(tv);
        }
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    
}
