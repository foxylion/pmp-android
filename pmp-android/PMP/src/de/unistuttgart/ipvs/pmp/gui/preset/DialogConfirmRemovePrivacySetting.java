package de.unistuttgart.ipvs.pmp.gui.preset;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

public class DialogConfirmRemovePrivacySetting extends Dialog {
    
    protected IPrivacySetting privacySetting;
    protected TabPrivacySettings presetPSsTab;
    
    
    public DialogConfirmRemovePrivacySetting(Context context, IPreset preset, IPrivacySetting privacySetting,
            TabPrivacySettings presetPSsTab) {
        super(context);
        
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
                DialogConfirmRemovePrivacySetting.this.presetPSsTab
                        .removePrivacySetting(DialogConfirmRemovePrivacySetting.this.privacySetting);
                
                Toast.makeText(
                        DialogConfirmRemovePrivacySetting.this.getContext(),
                        DialogConfirmRemovePrivacySetting.this.getContext().getString(
                                R.string.preset_removed_ps_success), Toast.LENGTH_LONG).show();
                
                DialogConfirmRemovePrivacySetting.this.dismiss();
            }
        });
        
        Button cancelButton = (Button) findViewById(R.id.Button_Cancel);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogConfirmRemovePrivacySetting.this.dismiss();
            }
        });
    }
    
}
