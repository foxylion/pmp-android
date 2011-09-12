package de.unistuttgart.ipvs.pmp.gui.activities;

import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * RessourcesActivity shows the Ressources which are installed on the device.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class RessourcesActivity extends Activity {
    TableLayout layout;

    /**
     * If there is too much Ressources, so you can scroll.
     */
    private ScrollView scroll;
    /**
     * Is needed for the creation of the Activity
     */
    TableRow actRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	actRow = new TableRow(this);
	actRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.WRAP_CONTENT));
	createLayout();
	loadRes();
	scroll = new ScrollView(this);
	layout.addView(actRow);
	scroll.addView(layout);
	setContentView(scroll);
    }

    private void createLayout() {
	layout = new TableLayout(this);
	layout.setScrollBarStyle(0);
	layout.setStretchAllColumns(true);
	layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.FILL_PARENT));
	layout.setBackgroundColor(Color.WHITE);
    }

    /**
     * Loading Apps to the View, 3 each row
     */
    private void loadRes() {
	int AppsCount = 16;
	for (int i = 0; i < AppsCount; i++) {
	    if (i % 3 == 0) {
		layout.addView(actRow);
		actRow = new TableRow(this);
		actRow.setLayoutParams(new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		ImagedButton act = new ImagedButton(this, "Beispiel Res: " + i);
		actRow.addView(act);
	    } else {
		ImagedButton act = new ImagedButton(this, "Beispiel Res: " + i);

		actRow.addView(act);
	    }
	}

    }
}