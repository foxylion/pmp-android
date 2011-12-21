package de.unistuttgart.ipvs.pmp.gui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.dialog.ServiceFeatureDialog;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.simple.SimpleModel;

/**
 * Displays the some Basic informations (name, description, state) about a Service Feature.
 * 
 * @author Jakob Jarosch
 */
public class ServiceFeatureView extends LinearLayout {
    
    /**
     * The reference to the real Service Feature in the model.
     */
    protected IServiceFeature serviceFeature;
    
    
    /**
     * Creates a new {@link ServiceFeatureView}.
     * 
     * @param context
     *            The context of the {@link Activity} which creates this view
     * @param serviceFeature
     *            The corresponding {@link IServiceFeature}
     */
    public ServiceFeatureView(Context context, IServiceFeature serviceFeature) {
        super(context);
        
        this.serviceFeature = serviceFeature;
        
        if (!isInEditMode()) {
            /* Not in edit mode, load the xml-layout. */
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.listitem_app_sf, null);
            addView(v);
            
            addListener();
            
            refresh();
        } else {
            /* In edit mode, just load a very basic representation of the real contents. */
            setOrientation(LinearLayout.VERTICAL);
            
            TextView tv = new TextView(getContext());
            tv.setText("[EditViewMode] ServiceFeatureView");
            tv.setPadding(5, 10, 5, 10);
            addView(tv);
        }
    }
    
    
    /**
     * Updates all displayed informations of the {@link ServiceFeatureView}.
     * Informations are directly fetched from the model.
     */
    public void refresh() {
        TextView tvName = (TextView) findViewById(R.id.TextView_Name);
        TextView tvDescription = (TextView) findViewById(R.id.TextView_Description);
        CheckBox cb = (CheckBox) findViewById(R.id.CheckBox_SFState);
        
        // Update name
        if (tvName != null) {
            tvName.setText(this.serviceFeature.getName());
        }
        
        // Update description
        if (tvDescription != null) {
            tvDescription.setText(this.serviceFeature.getDescription());
        }
        
        // Update Checkbox
        if (cb != null) {
            cb.setChecked(this.serviceFeature.isActive());
            cb.setEnabled(this.serviceFeature.isAvailable());
            
            if (!PMPPreferences.getInstance().isExpertMode()) {
                cb.setVisibility(View.VISIBLE);
            } else {
                cb.setVisibility(View.INVISIBLE);
            }
            
            if (this.serviceFeature.isActive() && this.serviceFeature.isAvailable()) {
                setBackgroundColor(GUIConstants.COLOR_BG_GREEN);
            } else if (!this.serviceFeature.isActive() && this.serviceFeature.isAvailable()) {
                setBackgroundColor(GUIConstants.COLOR_BG_RED);
            } else {
                setBackgroundColor(GUIConstants.COLOR_BG_GRAY);
            }
        }
    }
    
    
    /**
     * Adds the listeners to all components in the view.
     */
    private void addListener() {
        // Listener of the check box
        ((CheckBox) findViewById(R.id.CheckBox_SFState)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                boolean newState = ((CheckBox) v).isChecked();
                
                reactOnChange(newState);
            }
        });
    }
    
    
    /**
     * Method is invoked when the state of the Service Feature has changed.
     * 
     * @param newState
     *            The new State of the Service Feature.
     */
    public void reactOnChange(boolean newState) {
        SimpleModel.getInstance().setServiceFeatureActive(ModelProxy.get(), ServiceFeatureView.this.serviceFeature,
                newState);
        
        String toastText = getResources().getString(
                (newState ? R.string.app_servicefeature_enabled : R.string.app_servicefeature_disabled));
        Toast.makeText(ServiceFeatureView.this.getContext(), toastText, Toast.LENGTH_SHORT).show();
        
        refresh();
    }


    public void reactOnMouseClick() {
        new ServiceFeatureDialog(ServiceFeatureView.this.getContext(), ServiceFeatureView.this.serviceFeature,
                ServiceFeatureView.this).show();
    }
}
