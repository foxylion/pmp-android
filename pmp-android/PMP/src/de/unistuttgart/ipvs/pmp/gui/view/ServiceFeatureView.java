package de.unistuttgart.ipvs.pmp.gui.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.dialog.ServiceFeatureDialog;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.gui.util.PresetMananger;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class ServiceFeatureView extends LinearLayout {
    
    private IServiceFeature serviceFeature;
    
    
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
    
    
    public void refresh() {
        TextView tvName = (TextView) findViewById(R.id.TextView_Name);
        TextView tvDescription = (TextView) findViewById(R.id.TextView_Description);
        CheckBox cb = (CheckBox) findViewById(R.id.CheckBox_SFState);
        
        // Update name
        if (tvName != null) {
            tvName.setText(serviceFeature.getName());
        }
        
        // Update description
        if (tvDescription != null) {
            tvDescription.setText(serviceFeature.getDescription());
        }
        
        // Update Checkbox
        if (cb != null) {
            cb.setChecked(serviceFeature.isActive());
            cb.setEnabled(serviceFeature.isAvailable());
            
            if (!PMPPreferences.getInstanace().isExpertMode()) {
                cb.setVisibility(View.VISIBLE);
            } else {
                cb.setVisibility(View.INVISIBLE);
            }
            
            if (serviceFeature.isActive() && serviceFeature.isAvailable()) {
                setBackgroundColor(Color.parseColor("#002200"));
            } else if (!serviceFeature.isActive() && serviceFeature.isAvailable()) {
                setBackgroundColor(Color.parseColor("#220000"));
            } else {
                setBackgroundColor(Color.parseColor("#222222"));
            }
            
        }
    }
    
    
    private void addListener() {
        setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new ServiceFeatureDialog(ServiceFeatureView.this.getContext(), serviceFeature, ServiceFeatureView.this)
                        .show();
                
                Toast.makeText(ServiceFeatureView.this.getContext(), "Tapped on the Service Feature View",
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        ((CheckBox) findViewById(R.id.CheckBox_SFState)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                boolean newState = ((CheckBox) v).isChecked();
                
                if (newState) {
                    PresetMananger.enableServiceFeature(serviceFeature);
                } else {
                    PresetMananger.disableServiceFeature(serviceFeature);
                }
                
                Toast.makeText(ServiceFeatureView.this.getContext(),
                        "The Service Feature has been " + (serviceFeature.isActive() ? "enabled" : "disabled"),
                        Toast.LENGTH_SHORT).show();
                
                refresh();
            }
        });
    }
}
