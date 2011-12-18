package de.unistuttgart.ipvs.pmp.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.gui.view.PrivacySettingView;
import de.unistuttgart.ipvs.pmp.gui.view.ServiceFeatureView;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class ServiceFeatureDialog extends Dialog {
    
    private IServiceFeature serviceFeature;
    private ServiceFeatureView serviceFeatureView;
    
    
    public ServiceFeatureDialog(Context context, IServiceFeature serviceFeature, ServiceFeatureView serviceFeatureView) {
        super(context);
        
        this.serviceFeature = serviceFeature;
        this.serviceFeatureView = serviceFeatureView;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_sf);
        
        BasicTitleView btv = (BasicTitleView) findViewById(R.id.Title);
        btv.setTitle(String.format(getContext().getResources().getString(R.string.service_feature_title),
                this.serviceFeature.getName()));
        
        TextView descriptionTv = (TextView) findViewById(R.id.TextView_Description);
        descriptionTv.setText(serviceFeature.getDescription());
        
        LinearLayout psContainer = (LinearLayout) findViewById(R.id.LinearLayout_Container_PS_Information);
        LinearLayout rgContainer = (LinearLayout) findViewById(R.id.LinearLayout_Container_RG_Information);
        
        if(serviceFeature.isAvailable()) {
            psContainer.setVisibility(View.VISIBLE);
            rgContainer.setVisibility(View.GONE);
            
            LinearLayout psLayout = (LinearLayout) findViewById(R.id.LinearLayout_PrivacySettings);
            psLayout.removeAllViews();
            
            for(IPrivacySetting privacySetting : serviceFeature.getRequiredPrivacySettings()) {
                psLayout.addView(new PrivacySettingView(getContext(), serviceFeature, privacySetting));
            }
        } else {
            rgContainer.setVisibility(View.VISIBLE);
            psContainer.setVisibility(View.GONE);
            
            LinearLayout rgLayout = (LinearLayout) findViewById(R.id.LinearLayout_required_ResourceGroups);
            rgLayout.removeAllViews();
            
            // TODO Implement displaying the missing resource groups
        }
        
        
        Button enableDisableButton = (Button) findViewById(R.id.Button_EnableDisable);
        if (PMPPreferences.getInstance().isExpertMode()) {
            enableDisableButton.setVisibility(View.INVISIBLE);
        } else {
            enableDisableButton.setVisibility(View.VISIBLE);
        }
        enableDisableButton.setEnabled(serviceFeature.isAvailable());
        
        if (serviceFeature.isActive()) {
            enableDisableButton.setText(getContext().getResources().getString(R.string.disable));
        } else {
            enableDisableButton.setText(getContext().getResources().getString(R.string.enable));
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
                boolean newState = !ServiceFeatureDialog.this.serviceFeature.isActive();
                
                ServiceFeatureDialog.this.serviceFeatureView.reactOnChange(newState);
                ServiceFeatureDialog.this.cancel();
            }
        });
    }
    
}
