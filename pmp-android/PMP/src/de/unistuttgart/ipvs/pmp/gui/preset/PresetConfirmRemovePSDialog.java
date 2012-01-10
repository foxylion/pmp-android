package de.unistuttgart.ipvs.pmp.gui.preset;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PresetConfirmRemovePSDialog extends Dialog {
    
    private IPrivacySetting privacySetting;
    private IPreset preset;
    private PresetPSsTab presetPSsTab;
    
    
    public PresetConfirmRemovePSDialog(Context context, IPreset preset, IPrivacySetting privacySetting, PresetPSsTab presetPSsTab) {
        super(context);
        
        this.preset = preset;
        this.privacySetting = privacySetting;
        this.presetPSsTab = presetPSsTab;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_rgs_confirm_delete);
        
        TextView tvDescription = (TextView) findViewById(R.id.TextView_Description);
        tvDescription.setText(getContext().getString(R.string.preset_confirm_remove_ps_description,
                privacySetting.getResourceGroup().getName() + " - " + privacySetting.getName()));
        
        addListener();
    }
    
    
    private void addListener() {
        Button confirmButton = (Button) findViewById(R.id.Button_Confirm);
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                presetPSsTab.removePrivacySetting(privacySetting);
                
                Toast.makeText(PresetConfirmRemovePSDialog.this.getContext(),
                        PresetConfirmRemovePSDialog.this.getContext().getString(R.string.preset_removed_ps_success),
                        Toast.LENGTH_LONG).show();
                
                PresetConfirmRemovePSDialog.this.dismiss();
            }
        });
        
        Button cancelButton = (Button) findViewById(R.id.Button_Cancel);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PresetConfirmRemovePSDialog.this.dismiss();
            }
        });
    }
    
}
