package de.unistuttgart.ipvs.pmp.gui.preset;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * Dialog for adding a Preset
 * 
 * @author Marcus Vetter
 */
public class PresetAddEditDialog extends Dialog {
    
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
     * The Preset, if this dialog is used to modify the name and description of a Preset
     */
    protected IPreset preset;
    
    
    /**
     * * Necessary constructor
     * 
     * @param context
     *            the context
     * 
     * @param preset
     *            if it's null, an empty dialog will be created (add a new Preset); if it's not null, a dialog with
     *            prefilled text areas will be created (edit a Preset)
     */
    public PresetAddEditDialog(Context context, PresetsActivity activity, IPreset preset) {
        super(context);
        this.activity = activity;
        this.preset = preset;
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
        
        // Fill text fields, if it's an edit of a Preset
        if (preset != null) {
            this.name.setText(preset.getName());
            this.desc.setText(preset.getDescription());
        }
        
        this.confirm.setOnClickListener(new ConfirmListener());
        this.cancel.setOnClickListener(new CancelListener());
        
    }
    
    /**
     * Listener class needed for the confirm button
     * 
     */
    private class ConfirmListener implements android.view.View.OnClickListener {
        
        @Override
        public void onClick(View v) {
            
            if (PresetAddEditDialog.this.name.getText().length() == 0) {
                // no name set
                Toast.makeText(getContext(), R.string.presets_dialog_name_missing, Toast.LENGTH_SHORT).show();
                return;
            }
            
            String name = PresetAddEditDialog.this.name.getText().toString();
            String descr = PresetAddEditDialog.this.desc.getText().toString();
            
            if (preset == null) {
                // Add a new Preset
                IPreset createdPreset = ModelProxy.get().addUserPreset(name, descr);
                
                // Open Preset
                PresetAddEditDialog.this.activity.openPreset(createdPreset);
            } else {
                // Edit the Preset
                PresetAddEditDialog.this.preset.setName(name);
                PresetAddEditDialog.this.preset.setDescription(descr);
                
                // Update the Presets
                PresetAddEditDialog.this.activity.updateList();
            }
            
            // Dismiss
            dismiss();
            
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
