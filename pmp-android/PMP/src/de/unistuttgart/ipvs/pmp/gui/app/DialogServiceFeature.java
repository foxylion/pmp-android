package de.unistuttgart.ipvs.pmp.gui.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

/**
 * The {@link DialogServiceFeature} displays details about a {@link IServiceFeature}.
 * The User gets the name, a description and all required Privacy Settings.
 * For each Privacy Setting is also the required value (in a human readable representation)
 * and the current state (satisfied or not) shown.
 * 
 * @author Jakob Jarosch
 */
public class DialogServiceFeature extends Dialog {
    
    /**
     * The Service Feature which is displayed in the dialog.
     */
    protected IServiceFeature serviceFeature;
    
    /**
     * The View of the Service Feature (in the Service Feature list) which corresponds to the current displayed one.
     */
    protected ListItemServiceFeature serviceFeatureView = null;
    
    
    /**
     * @see DialogServiceFeature#ServiceFeatureDialog(Context, IServiceFeature, ListItemServiceFeature)
     */
    public DialogServiceFeature(Context context, IServiceFeature serviceFeature) {
        this(context, serviceFeature, null);
    }
    
    
    /**
     * Creates a new {@link DialogServiceFeature}.
     * 
     * @param context
     *            the {@link Context} is required for Dialog creation
     * @param serviceFeature
     *            the {@link IServiceFeature} which should be displayed
     * @param serviceFeatureView
     *            the corresponding view of the {@link IServiceFeature} in the {@link ListView}.
     */
    public DialogServiceFeature(Context context, IServiceFeature serviceFeature,
            ListItemServiceFeature serviceFeatureView) {
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
        
        /* Decide between displaying the required privacy settings or the missing resource groups */
        if (serviceFeature.isAvailable()) {
            psContainer.setVisibility(View.VISIBLE);
            rgContainer.setVisibility(View.GONE);
            
            LinearLayout psLayout = (LinearLayout) findViewById(R.id.LinearLayout_PrivacySettings);
            psLayout.removeAllViews();
            
            for (IPrivacySetting privacySetting : serviceFeature.getRequiredPrivacySettings()) {
                psLayout.addView(new PrivacySettingView(getContext(), serviceFeature, privacySetting));
            }
        } else {
            psContainer.setVisibility(View.GONE);
            rgContainer.setVisibility(View.VISIBLE);
        }
        
        /* Decide between displaying the Disable/Enable button or not */
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
    
    
    /**
     * Adds the listeners to all the GUI components.
     */
    private void addListener() {
        /* Add a listener to the close button */
        Button closeButton = (Button) findViewById(R.id.Button_Close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogServiceFeature.this.cancel();
            }
        });
        
        /* Add a listener to the enable/disable button */
        Button enableDisableButton = (Button) findViewById(R.id.Button_EnableDisable);
        enableDisableButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                boolean newState = !DialogServiceFeature.this.serviceFeature.isActive();
                
                if (DialogServiceFeature.this.serviceFeatureView != null) {
                    DialogServiceFeature.this.serviceFeatureView.reactOnChange(newState);
                }
                
                DialogServiceFeature.this.cancel();
            }
        });
        
        Button showMissingRGs = (Button) findViewById(R.id.Button_ViewMissingRGs);
        showMissingRGs.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                List<String> missingRGs = new ArrayList<String>();
                for (MissingPrivacySettingValue ps : serviceFeature.getMissingPrivacySettings()) {
                    if (!missingRGs.contains(ps.getResourceGroup())) {
                        missingRGs.add(ps.getResourceGroup());
                    }
                }
                Intent intent = GUITools.createFilterAvailableRGsIntent(missingRGs.toArray(new String[missingRGs.size()]));
                getContext().startActivity(intent);
                dismiss();
            }
        });
    }
}
