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
    
    /**
     * Callback interface for returning the result to the caller.
     * 
     * @author Jakob Jarosch
     */
    public interface ICallback {
        
        /**
         * Method is being directly called after the dismiss of the {@link DialogPrivacySettingEdit}.
         * 
         * @param changed
         *            True when the save button was clicked, False when Dialog was canceled.
         * @param newValue
         *            The new value which has been set.
         */
        public void result(boolean save, String newValue);
    }
    
    /**
     * The {@link IPrivacySetting} which is referenced in the dialog.
     */
    private IPrivacySetting privacySetting;
    
    /**
     * The value which should be initially displayed.
     */
    private String value;
    
    /**
     * The callback which is invoked at the end.
     */
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
     * @param contextAnnotation
     *            {@link IContextAnnotation} which refers to the Privacy Setting value. Can be null.
     * @param callback
     *            {@link ICallback} for informing about the dismiss() of the {@link Dialog}. Can be null.
     */
    public DialogPrivacySettingEdit(Context context, IPrivacySetting privacySetting, String value, ICallback callback) {
        super(context);
        
        this.privacySetting = privacySetting;
        this.callback = callback;
        this.value = value;
        
        /* Initiate basic dialog */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_privacysetting_edit);
        setCancelable(false);
        
        refresh();
        addListener();
        
    }
    
    
    /**
     * Refreshs the UI.
     */
    private void refresh() {
        /*
         * Set the name of the Privacy Setting.
         */
        ((BasicTitleView) findViewById(R.id.Title)).setTitle(getContext().getString(R.string.change) + " "
                + this.privacySetting.getName());
        
        /* 
         * Set the change description of the Privacy Setting.
         */
        ((TextView) findViewById(R.id.TextView_Description)).setText(this.privacySetting.getChangeDescription());
        
        /*
         * Update the UI for the changing view.
         */
        ((LinearLayout) findViewById(R.id.LinearLayout_PrivacySetting)).removeAllViews();
        ((LinearLayout) findViewById(R.id.LinearLayout_PrivacySetting)).addView(this.privacySetting
                .getView(getContext()));
        
        setViewValue(this.value);
    }
    
    
    /**
     * Adds all listeners to clickable UI components.
     */
    private void addListener() {
        /*
         * Save Button.
         */
        ((Button) findViewById(R.id.Button_Save)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                callback.result(true, getViewValue());
                dismiss();
            }
        });
        
        /*
         * Cancel Button.
         */
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
        
        /*
         * Remove the Privacy Setting view to allow a reuse of the view.
         */
        ((LinearLayout) findViewById(R.id.LinearLayout_PrivacySetting)).removeAllViews();
    }
    
    
    /**
     * @return Returns the actual value of the privacy setting view.
     */
    private String getViewValue() {
        return this.privacySetting.getViewValue(getContext());
    }
    
    
    /**
     * Tries to set a new value for the privacy setting view. If the value is invalid the user will be informed by a
     * {@link Toast} and a null value will be set.
     * 
     * @param value
     *            The new value which shall be set.
     * @return True when the value was successfully set, otherwise false.
     */
    private boolean setViewValue(String value) {
        try {
            this.privacySetting.setViewValue(getContext(), value);
            return true;
        } catch (PrivacySettingValueException e) {
            /*
             * Setting the Privacy Setting failed, now trying to set null value and create a dialog to inform the user.
             */
            Log.d(this, "Failed to set the view value with exisiting value from preset.", e);
            
            Toast.makeText(getContext(), getContext().getString(R.string.preset_invalid_ps_value), Toast.LENGTH_LONG)
                    .show();
            try {
                this.privacySetting.setViewValue(getContext(), null);
            } catch (PrivacySettingValueException e1) {
                Log.e(this, "It was not possible to assign NULL as a view value!", e1);
            }
        }
        
        return false;
    }
}
