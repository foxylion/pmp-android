package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy;

import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class EnergyPanel implements IPanel {

	private TextView view;

	public EnergyPanel(Context context) {

		// Instantiate the view
		view = new TextView(context);

		// Set text
		view.setText("EnergyPanel");

	}

	public View getView() {
		return view;
	}

	public String getTitle() {
		return "Energy";
	}

}
