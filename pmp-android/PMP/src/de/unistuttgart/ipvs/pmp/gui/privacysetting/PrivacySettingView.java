package de.unistuttgart.ipvs.pmp.gui.privacysetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

public class PrivacySettingView extends LinearLayout {
    
    protected IPrivacySetting privacySetting;
    
    
    public PrivacySettingView(Context context, IPrivacySetting privacySetting) {
        super(context);
        
        this.privacySetting = privacySetting;
        
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.listitem_rg_ps, null);
        addView(v);
        
        ((TextView) findViewById(R.id.TextView_Title)).setText(privacySetting.getName());
        ((TextView) findViewById(R.id.TextView_Description)).setText(privacySetting.getDescription());
        
        addListener();
    }
    
    
    private void addListener() {
        setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogPrivacySetting(getContext(), PrivacySettingView.this.privacySetting).show();
            }
        });
    }
}
