package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.connections;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

public class ConnectionsPanel implements IPanel {
    
    private TextView view;
    
    
    public ConnectionsPanel(Context context) {
        
        // Instantiate the view
        this.view = new TextView(context);
        
        // Set text
        this.view.setText("ConnectionsPanel");
        
    }
    
    
    public View getView() {
        return this.view;
    }
    
    
    public String getTitle() {
        return "Connections";
    }
    
}
