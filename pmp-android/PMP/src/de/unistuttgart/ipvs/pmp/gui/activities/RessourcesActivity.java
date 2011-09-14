package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

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
//	loadFakeRes();
	scroll = new ScrollView(this);
	scroll.setBackgroundColor(Color.rgb(211, 211, 211));
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
	layout.setBackgroundColor(Color.rgb(211, 211, 211));
    }

    /**
     * ************ FAKE METHOD********** Loading Apps to the View, 3 each row
     * Can be replaced with loadRes()
     */
    private void loadFakeRes() {
	int AppsCount = 16;
	for (int i = 0; i < AppsCount; i++) {
	    if (i % 3 == 0) {
		layout.addView(actRow);
		actRow = new TableRow(this);
		actRow.setLayoutParams(new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    }
	    ImagedButton act = new ImagedButton(this, "Beispiel Res: " + i, i,
		    R.drawable.res);
	    act.setClickable(true);
	    actRow.addView(act);
	}

    }

    /**
     * Loaading the Ressources each row 3
     */
    private void loadRes() {
	int resCount = ModelSingleton.getInstance().getModel()
		.getResourceGroups().length;
	IResourceGroup resArray[] = ModelSingleton.getInstance().getModel()
		.getResourceGroups();
	
	for (int i = 0; i < resCount; i++) {
	    if (i % 3 == 0) {
		layout.addView(actRow);
		actRow = new TableRow(this);
		actRow.setLayoutParams(new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    }
	    ImagedButton act = new ImagedButton(this, resArray[i].getName(), i,
		    R.drawable.res);
	    act.setClickable(true);
	    act.setOnClickListener(new OnResClickListener(act));
	    actRow.addView(act);
	}

    }
}
/**
 * Shows the Description Dialog
 * 
 * @author Alexander Wassiljew
 * 
 */
class OnResClickListener implements OnClickListener {
    private ImagedButton parent;
    private IResourceGroup resArray[];
    public OnResClickListener(ImagedButton button) {
	this.parent = button;
	 resArray = ModelSingleton.getInstance().getModel()
	.getResourceGroups();
    }

    @Override
    public void onClick(View v) {
	final Dialog dialog = new Dialog(parent.getContext());
	dialog.setTitle(parent.getName());
	TextView description = new TextView(parent.getContext());
	Button close = new Button(parent.getContext());
	close.setText("Close");
	close.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		dialog.cancel();
	    }
	});
	description.setText("Description: \n\n" + resArray[parent.getIndex()].getDescription()+"\n");
	description.setPadding(10, 0, 10, 0);
	LinearLayout layout = new LinearLayout(parent.getContext());
	layout.setOrientation(LinearLayout.VERTICAL);
	layout.addView(description);
	layout.addView(close);
	
	dialog.setContentView(layout);
	dialog.show();
    }

}