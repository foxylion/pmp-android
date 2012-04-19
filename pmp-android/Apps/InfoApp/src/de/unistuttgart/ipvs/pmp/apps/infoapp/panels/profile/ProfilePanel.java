package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.profile;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

public class ProfilePanel implements IPanel {
	
	private TextView view;
	
	
	public ProfilePanel(Context context) {
		
		// Instantiate the view
		this.view = new TextView(context);
		
		// Set text
		this.view.setText("ProfilePanel");
		
	}
	
	
	public View getView() {
		return this.view;
	}
	
	
	public String getTitle() {
		return "Profile";
	}
	
}
