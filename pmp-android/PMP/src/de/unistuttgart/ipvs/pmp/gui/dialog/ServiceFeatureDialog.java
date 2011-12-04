package de.unistuttgart.ipvs.pmp.gui.dialog;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.gui.util.PresetMananger;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleViewCompact;
import de.unistuttgart.ipvs.pmp.gui.view.ServiceFeatureView;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceFeatureDialog extends Dialog {
    
    private IServiceFeature serviceFeature;
    private ServiceFeatureView serviceFeatureView;
    
    
    public ServiceFeatureDialog(Context context, IServiceFeature serviceFeature, ServiceFeatureView serviceFeatureView) {
        super(context);
        
        this.serviceFeature = serviceFeature;
        this.serviceFeatureView = serviceFeatureView;
        
        setContentView(R.layout.dialog_sf);
        
        BasicTitleViewCompact btvc = (BasicTitleViewCompact) findViewById(R.id.Title);
        btvc.setTitle(serviceFeature.getName());
        
        TextView descriptionTv = (TextView) findViewById(R.id.TextView_Description);
        descriptionTv.setText(serviceFeature.getDescription());
        
        TextView requiredPSTv = (TextView) findViewById(R.id.TextView_PrivacySettings);
        String text = "<html>";
        
        for (IPrivacySetting ps : serviceFeature.getRequiredPrivacySettings()) {
            text += "<p>";
            text += "<b>" + ps.getResourceGroup().getName() + " - <i>" + ps.getName() + "</i></b><br/>";
            
            try {
                text += "required value: "
                        + ps.getHumanReadableValue(serviceFeature.getRequiredPrivacySettingValue(ps));
            } catch (PrivacyLevelValueException e) {
                text += "<span style=\"color:red;\">required value is invalid</span>";
            }
            text += "</p>";
        }
        text += "</html>";
        
        requiredPSTv.setText(Html.fromHtml(text));
        
        
        Button enableDisableButton = (Button) findViewById(R.id.Button_EnableDisable);
        enableDisableButton.setEnabled(!PMPPreferences.getInstanace().isExpertMode());
        
        if (serviceFeature.isActive()) {
            enableDisableButton.setText("Disable");
        } else {
            enableDisableButton.setText("Enable");
        }
        
        addListener();
    }
    
    
    private void addListener() {
        Button closeButton = (Button) findViewById(R.id.Button_Close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ServiceFeatureDialog.this.cancel();
            }
        });
        
        Button enableDisableButton = (Button) findViewById(R.id.Button_EnableDisable);
        enableDisableButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (serviceFeature.isActive()) {
                    PresetMananger.disableServiceFeature(serviceFeature);
                } else {
                    PresetMananger.enableServiceFeature(serviceFeature);
                }
                
                serviceFeatureView.refresh();
                ServiceFeatureDialog.this.cancel();
            }
        });
    }
    
}
