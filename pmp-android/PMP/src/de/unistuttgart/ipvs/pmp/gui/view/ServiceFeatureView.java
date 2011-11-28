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

public class ServiceFeatureView extends LinearLayout {
    
    private String name;
    private String description;
    private boolean active;
    private boolean editable;
    private boolean available;
    
    
    public ServiceFeatureView(Context context) {
        super(context);
        
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
    
    
    public void setName(String name) {
        this.name = name;
        
        refresh();
    }
    
    
    public void setDescription(String description) {
        this.description = description;
        
        refresh();
    }
    
    
    public void setEditable(boolean editable) {
        this.editable = editable;
        
        refresh();
    }
    
    
    public void setActive(boolean active) {
        this.active = active;
        
        refresh();
    }
    
    
    public void setAvailable(boolean avaiblabe) {
        this.available = avaiblabe;
        
        refresh();
    }
    
    
    public boolean isAvailable() {
        return available;
    }
    
    
    public boolean getActive() {
        return active;
    }
    
    
    private void refresh() {
        TextView tvName = (TextView) findViewById(R.id.TextView_Name);
        TextView tvDescription = (TextView) findViewById(R.id.TextView_Description);
        CheckBox cb = (CheckBox) findViewById(R.id.CheckBox_SFState);
        
        // Update name
        if (tvName != null) {
            tvName.setText(this.name);
        }
        
        // Update description
        if (tvDescription != null) {
            tvDescription.setText(description);
        }
        
        // Update Checkbox
        if (cb != null) {
            cb.setChecked(active);
            cb.setEnabled(available);
            
            if (editable) {
                cb.setVisibility(View.VISIBLE);
            } else {
                cb.setVisibility(View.INVISIBLE);
            }
            
            if (active && isAvailable()) {
                setBackgroundColor(Color.parseColor("#001100"));
            } else if (!active && isAvailable()) {
                setBackgroundColor(Color.parseColor("#110000"));
            } else {
                setBackgroundColor(Color.parseColor("#111111"));
            }
            
        }
    }
    
    
    private void addListener() {
        setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceFeatureView.this.getContext(), "Tapped on the Service Feature View",
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        ((CheckBox) findViewById(R.id.CheckBox_SFState)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ServiceFeatureView.this.active = ((CheckBox) v).isChecked();
                
                Toast.makeText(ServiceFeatureView.this.getContext(), "The Checkbox is now set to " + getActive(),
                        Toast.LENGTH_SHORT).show();
                
                activeChangedCallback();
            }
        });
    }
    
    
    private void activeChangedCallback() {
        // TODO inform the model, or so
        
        refresh();
    }
}
