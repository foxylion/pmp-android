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
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
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
    
    private IPrivacySetting privacySetting;
    
    private IPrivacySettingEditCallback callback;
    
    private String value;
    
    
    /**
     * Creates a new {@link Dialog} for editing a {@link IPrivacySetting}.
     * 
     * @param context
     *            {@link Context} for {@link Dialog} creation.
     * @param preset
     *            {@link IPreset} to which the {@link IPrivacySetting} is assigned.
     * @param privacySetting
     *            {@link IPrivacySetting} which should be edited.
     * @param contextAnnotation
     *            {@link IContextAnnotation} which refers to the Privacy Setting value. Can be null.
     * @param callback
     *            {@link ICallback} for informing about the dismiss() of the {@link Dialog}. Can be null.
     */
    public DialogPrivacySettingEdit(Context context, IPrivacySetting privacySetting, String value,
            IPrivacySettingEditCallback callback) {
        super(context);
        
        this.privacySetting = privacySetting;
        this.callback = callback;
        this.value = value;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_privacysetting_edit);
        setCancelable(false);
        
        buildDialog();
        
        addListener();
        
        setViewValue(this.value);
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
                callback.result(true, getViewValue());
                dismiss();
            }
        });
        ((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                callback.result(false, getViewValue());
                dismiss();
            }
        });
    }
    
    
    @Override
    public void dismiss() {
        super.dismiss();
        
        /* Remove the Privacy Setting view to allow a reuse of the view */
        ((LinearLayout) findViewById(R.id.LinearLayout_PrivacySetting)).removeAllViews();
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
