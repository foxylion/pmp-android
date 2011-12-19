package de.unistuttgart.ipvs.pmp.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activity.PresetsActivity;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * Dialog for adding a Preset
 * 
 * @author Marcus Vetter
 */
public class PresetAddDialog extends Dialog {
    
    /**
     * The TextView with the name
     */
    protected TextView name;
    
    /**
     * The TextView with the description
     */
    protected TextView desc;
    
    /**
     * The button to confirm the dialog
     */
    private Button confirm;
    
    /**
     * The button to cancel the dialog
     */
    private Button cancel;
    
    /**
     * The PresetsActivity
     */
    protected PresetsActivity activity;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public PresetAddDialog(Context context) {
        super(context);
    }
    
    
    /**
     * Set the activity
     * 
     * @param activity
     *            PresetsActivity
     */
    public void setActivity(PresetsActivity activity) {
        this.activity = activity;
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_preset_add);
        
        this.name = (TextView) findViewById(R.id.presets_dialog_name_textfield);
        this.desc = (TextView) findViewById(R.id.presets_dialog_description_textfield);
        this.confirm = (Button) findViewById(R.id.presets_dialog_confirm);
        this.cancel = (Button) findViewById(R.id.presets_dialog_cancel);
        
        this.confirm.setOnClickListener(new ConfirmListener());
        this.cancel.setOnClickListener(new CancelListener());
        
        /*
         * Needed to fill the width of the screen
         */
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        
    }
    
    /**
     * Listener class needed for the confirm button
     * 
     */
    private class ConfirmListener implements android.view.View.OnClickListener {
        
        @Override
        public void onClick(View v) {
            
            if (PresetAddDialog.this.name.getText().length() == 0) {
                // no name set
                Toast.makeText(getContext(), R.string.presets_dialog_name_missing, Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Store
            IPreset createdPreset = ModelProxy.get().addPreset(null, PresetAddDialog.this.name.getText().toString(),
                    PresetAddDialog.this.name.getText().toString(), PresetAddDialog.this.desc.getText().toString());
            
            // Dismiss
            dismiss();
            
            // Update the Presets
            PresetAddDialog.this.activity.updateList();
            
            // Open Preset
            PresetAddDialog.this.activity.openPreset(createdPreset);
        }
        
    }
    
    /**
     * Listener class needed for the cancel button
     * 
     */
    private class CancelListener implements android.view.View.OnClickListener {
        
        @Override
        public void onClick(View v) {
            // Dismiss
            dismiss();
        }
        
    }
    
}
