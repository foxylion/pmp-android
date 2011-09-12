package de.unistuttgart.ipvs.pmp.gui.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * ApplicationsActivity shows the Applications which are installed on the
 * device.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ApplicationsActivity extends Activity {
    /**
     * Main Layout of the Activity, which will be draw to the Canvas
     */
    private TableLayout layout;

    /**
     * If there is too much Apps, so you can scroll.
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
	actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
	createLayout();
	//loadFakeApps(); 
	loadApps();
	scroll = new ScrollView(this);
	layout.addView(actRow);
	scroll.addView(layout);
	setContentView(scroll);
    }

    /**
     * Loading Apps to the View, 3 each row 
     * Can be replaced with: - loadApps()
     * if not needed delete!!!!
     */
    private void loadFakeApps() {
	int AppsCount = 16;
	for (int i = 0; i < AppsCount; i++) {
	    if (i % 3 == 0) {
		layout.addView(actRow);
		actRow = new TableRow(this);
		actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
	    }
	    ImagedButton act = new ImagedButton(this, "Beispiel App: " + i);
	    act.setClickable(true);
	    act.setOnClickListener(new OnAppClickListener(act));
	    actRow.addView(act);
	}
    }

    /**
     * Loading Apps to the View, 3 each row
     */
    private void loadApps() {
	int AppsCount = 0;
	AppsCount = ModelSingleton.getInstance().getModel().getApps().length;
	List<IApp> appList = null;
	if(AppsCount!= 0){
	    IApp[] appArray = ModelSingleton.getInstance().getModel().getApps();
	     appList = Arrays.asList(appArray);    
	}
	if (appList != null) {
	    for (int i = 0; i < AppsCount; i++) {
		if (i % 3 == 0) {
		    layout.addView(actRow);
		    actRow = new TableRow(this);
		    actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
		}
		ImagedButton act = new ImagedButton(this, appList.get(i)
			.getName());
	
		act.setClickable(true);
		act.setOnClickListener(new OnAppClickListener(act));
		actRow.addView(act);
	    }
	}
    }

    /**
     * Creating the Layout and setting the properties.
     */
    private void createLayout() {
	layout = new TableLayout(this);
	layout.setScrollBarStyle(0);
	layout.setStretchAllColumns(true);
	layout.setLayoutParams(LayoutParamsCreator.createFPFP());
	layout.setBackgroundColor(Color.WHITE);
    }
}
/**
 * Starts the ServiceLvlActivity for the App
 * @author neon
 *
 */
class OnAppClickListener implements OnClickListener {
    private ImagedButton parent;

    public OnAppClickListener(ImagedButton button) {
	this.parent = button;
    }

    @Override
    public void onClick(View v) {
	// Call Privacy Level Activity
	Intent intent = new Intent(v.getContext(), ServiceLvlActivity.class);
	intent.putExtra("appName", parent.getAppName());
	if (v.getContext() != null)
	    v.getContext().startActivity(intent);
    }

}