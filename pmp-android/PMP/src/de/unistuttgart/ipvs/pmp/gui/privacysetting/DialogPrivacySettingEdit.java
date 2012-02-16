package de.unistuttgart.ipvs.pmp.gui.privacysetting;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.ICallback;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * Displays the current configuration of the {@link IPrivacySetting} and gives the user a short description what he
 * going to configure.
 * 
 * @author Jakob Jarosch
 */
public class DialogPrivacySettingEdit extends Dialog {
    
    private IPreset preset;
    private IPrivacySetting privacySetting;
    
    private ICallback callback;
    
    
    /**
     * Creates a new {@link Dialog} for editing a {@link IPrivacySetting}.
     * 
     * @param context
     *            {@link Context} for {@link Dialog} creation.
     * @param preset
     *            {@link IPreset} to which the {@link IPrivacySetting} is assigned.
     * @param privacySetting
     *            {@link IPrivacySetting} which should be edited.
     * @param callback
     *            {@link ICallback} for informing about the dismiss() of the {@link Dialog}. Can be null.
     */
    public DialogPrivacySettingEdit(Context context, IPreset preset, IPrivacySetting privacySetting, ICallback callback) {
        super(context);
        
        this.preset = preset;
        this.privacySetting = privacySetting;
        this.callback = callback;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_privacysetting_edit);
        
        buildDialog();
        
        addListener();
        
        setViewValue(this.preset.getGrantedPrivacySettingValue(this.privacySetting));
    }
    
    
    private void buildDialog() {
        ((BasicTitleView) findViewById(R.id.Title)).setTitle(getContext().getString(R.string.change) + " "
                + this.privacySetting.getName());
        
        ((TextView) findViewById(R.id.TextView_Description)).setText(this.privacySetting.getChangeDescription());
        
        ((LinearLayout) findViewById(R.id.LinearLayout_PrivacySetting)).addView(this.privacySetting
                .getView(getContext()));
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Save)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogPrivacySettingEdit.this.preset.assignPrivacySetting(DialogPrivacySettingEdit.this.privacySetting,
                        getViewValue());
                dismiss();
            }
        });
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    
    
    @Override
    public void dismiss() {
        super.dismiss();
        
        /* Remove the Privacy Setting view to allow a reuse of the view */
        ((LinearLayout) findViewById(R.id.LinearLayout_PrivacySetting)).removeAllViews();
        
        /* Inform callback if requested */
        if (this.callback != null) {
            this.callback.callback();
        }
    }
    
    
    private String getViewValue() {
        return this.privacySetting.getViewValue(getContext());
    }
    
    
    private void setViewValue(String value) {
        try {
            this.privacySetting.setViewValue(getContext(), value);
        } catch (PrivacySettingValueException e) {
            Log.d(this, "Failed to set the view value with exisiting value from preset.", e);
            
            Toast.makeText(getContext(), getContext().getString(R.string.preset_invalid_ps_value), Toast.LENGTH_LONG)
                    .show();
            try {
                this.privacySetting.setViewValue(getContext(), null);
            } catch (PrivacySettingValueException e1) {
                Log.e(this, "It was not possible to assign NULL as a view value!", e1);
            }
        }
    }
}
