package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.hardware;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

public class HardwarePanel implements IPanel {
	
	private TextView view;
	
	
	public HardwarePanel(Context context) {
		
		// Instantiate the view
		this.view = new TextView(context);
		
		// Set text
		this.view.setText("HardwarePanel");
		
	}
	
	
	public View getView() {
		return this.view;
	}
	
	
	public String getTitle() {
		return "Hardware";
	}
	
}
