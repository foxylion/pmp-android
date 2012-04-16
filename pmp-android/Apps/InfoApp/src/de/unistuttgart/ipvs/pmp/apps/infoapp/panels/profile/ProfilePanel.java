package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.profile;

import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ProfilePanel implements IPanel {
	
	private TextView view;

	public ProfilePanel(Context context) {

		// Instantiate the view
		view = new TextView(context);

		// Set text
		view.setText("ProfilePanel");

	}

	public View getView() {
		return view;
	}
	
	public String getTitle() {
		return "Profile";
	}

}
