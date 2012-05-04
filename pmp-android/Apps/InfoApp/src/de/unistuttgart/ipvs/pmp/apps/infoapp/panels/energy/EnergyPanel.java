package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

public class EnergyPanel implements IPanel {
    
    private TextView view;
    
    
    public EnergyPanel(Context context) {
        
        // Instantiate the view
        this.view = new TextView(context);
        
        // Set text
        this.view.setText("EnergyPanel");
        
    }
    
    
    public View getView() {
        return this.view;
    }
    
    
    public String getTitle() {
        return "Energy";
    }
    
}
