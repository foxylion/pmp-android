package de.unistuttgart.ipvs.pmp.gui.privacysetting;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

public class DialogPrivacySettingInformation extends Dialog {
    
    public DialogPrivacySettingInformation(Context context, IPrivacySetting ps) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_privacysetting);
        
        ((BasicTitleView) findViewById(R.id.Title)).setTitle(ps.getName());
        Drawable icon = ps.getResourceGroup().getIcon();
        if (icon != null) {
            ((BasicTitleView) findViewById(R.id.Title)).setIcon(icon);
        }
        
        ((TextView) findViewById(R.id.TextView_Description)).setText(ps.getDescription());
        ((TextView) findViewById(R.id.TextView_RG_Name)).setText(ps.getResourceGroup().getName());
        ((TextView) findViewById(R.id.TextView_Identifier)).setText(ps.getIdentifier());
        
        addListener();
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogPrivacySettingInformation.this.dismiss();
            }
        });
    }
}
