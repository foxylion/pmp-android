package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * PrivacyLvlActivity
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ServiceLvlActivity extends Activity {
    /**
     * App of which the Levels have to be load
     */
    private String appName;
    /**
     * Index of the current App
     */
    private int index;

    /**
     * If there are too much Levels, so you can scroll.
     */
    private ScrollView scroll;
    /**
     * Main Layout of the Activity, which will be draw to the Canvas
     */
    private LinearLayout parentLayout;
    private Intent intent;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	loadIntentsExtras();
	this.setTitle("Service Levels for " + appName);
	createParentLayout();
	loadServiceLevels();
	// loadFakeServiceLevels();
	scroll = new ScrollView(this);
	scroll.addView(parentLayout);
	scroll.setBackgroundColor(Color.rgb(211, 211, 211));
	setContentView(scroll);
    }

    /**
     * Creates the parent layout and sets the settings
     */
    private void createParentLayout() {
	parentLayout = new LinearLayout(this);
	parentLayout.setScrollBarStyle(0);
	parentLayout.setOrientation(LinearLayout.VERTICAL);
	parentLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.FILL_PARENT));
	parentLayout.setBackgroundColor(Color.BLACK);
    }

    /**
     * Loads the Privacy Levels for the GUI
     */
    private void loadServiceLevels() {
	IApp appsArray[] = ModelSingleton.getInstance().getModel().getApps();
	IApp app = appsArray[index];
	IServiceLevel levelArray[] = app.getServiceLevels();
	RadioGroup group = new RadioGroup(this);
	for (int i = 0; i < levelArray.length; i++) {
	    RadioButton button = new RadioButton(this);
	    button.setBackgroundColor(Color.rgb(211, 211, 211));
	    button.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		    LayoutParams.WRAP_CONTENT));
	    button.setGravity(Gravity.CENTER);
	    button.setTextColor(Color.BLACK);
	    button.setText(levelArray[i].getName());
	    if (app.getActiveServiceLevel().getLevel() == i) {
		button.setChecked(true);
	    }
	    button.setOnTouchListener(new OnLevelTouchListener(this,
		    levelArray[i].getDescription(), button, this, index, i));
	    group.addView(button);
	}
	group.setGravity(Gravity.CENTER_HORIZONTAL);
	parentLayout.addView(group);
    }

    /**
     * Loads the Intent
     */
    private void loadIntentsExtras() {
	appName = this.getIntent().getExtras().getString("appName");
	index = this.getIntent().getExtras().getInt("appID");
    }

    /**
     * ************* FAKE METHOD ********** Loads the Privacy Levels for the GUI
     * can be replaced by the method - loadPrivacyLevels()
     */
    private void loadFakeServiceLevels() {
	String levels[] = { "Level1", "Level2", "Level3", "Level1", "Level2",
		"Level3", "Level1", "Level2", "Level3", "Level1", "Level2",
		"Level3", "Level1", "Level2", "Level3", "Level1", "Level2",
		"Level3", "Level1", "Level2", "Level3" };
	String description = "Service level Description ";
	RadioGroup group = new RadioGroup(this);
	for (int i = 0; i < levels.length; i++) {
	    RadioButton button = new RadioButton(this);
	    button.setBackgroundColor(Color.rgb(211, 211, 211));
	    button.setLayoutParams(LayoutParamsCreator.createFPWC());
	    button.setGravity(Gravity.CENTER);
	    button.setTextSize(50);
	    button.setTextColor(Color.BLACK);
	    if (i == 3)
		button.setChecked(true);
	    button.setText(levels[i]);
	    // button.setOnTouchListener(new OnLevelTouchListener(this,
	    // levels[i],
	    // button, this));
	    group.addView(button);
	}
	group.setGravity(Gravity.CENTER_HORIZONTAL);
	parentLayout.addView(group);
    }

    public void reloadActivity() {
	parentLayout.removeAllViews();
	loadServiceLevels();
    }
}

/**
 * OnLevelTouchListener
 * 
 * @author Alexander Wassiljew
 * 
 */
class OnLevelTouchListener implements OnTouchListener {

    private String lvlDescr;
    private Context context;
    private RadioButton parent;
    private ServiceLvlActivity activity;
    private int levelID;
    private int appID;

    public OnLevelTouchListener(Context context, String lvlDescr,
	    RadioButton button, ServiceLvlActivity activity, int appID,
	    int levelID) {
	this.lvlDescr = lvlDescr;
	this.context = context;
	this.parent = button;
	this.activity = activity;
	this.levelID = levelID;
	this.appID = appID;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
	if (event.getAction() == event.ACTION_UP) {
	    final Dialog dialog = new Dialog(context);
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.setCancelable(true);
	    dialog.setTitle("Apply Service Level?");
	    LinearLayout dialogLayout = new LinearLayout(context);
	    dialogLayout.setOrientation(LinearLayout.VERTICAL);
	    TextView description = new TextView(context);
	    description.setText("Description:" + "\n\n" + lvlDescr + "\n");
	    description.setPadding(10, 0, 10, 0);
	    Button apply = new Button(context);
	    apply.setText("Apply");

	    Button cancel = new Button(context);
	    cancel.setText("Cancel");

	    /**
	     * Reload the ServiceLvlActivity if no changes occur
	     */
	    dialog.setOnCancelListener(new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
		    // Reloads the Service Level Activity if new Service Level
		    // wasn't set
		    activity.reloadActivity();
		}
	    });
	    /**
	     * Sets the Service Level.
	     */
	    apply.setOnTouchListener(new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		    IApp appsArray[] = ModelSingleton.getInstance().getModel()
			    .getApps();
		    IApp app = appsArray[appID];
		    IServiceLevel levelArray[] = app.getServiceLevels();
		    IServiceLevel level = levelArray[levelID];
		    Log.v("appID:" + String.valueOf(appID));
		    Log.v("levelID:" + String.valueOf(levelID));

		    try {
			app.setActiveServiceLevelAsPreset(level.getLevel());
		    } catch (Exception exc) {
			;
		    }
		    dialog.cancel();
		    return false;
		}
	    });
	    /**
	     * Cancel the dialog
	     */
	    cancel.setOnTouchListener(new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
		    dialog.cancel();
		    return false;
		}
	    });

	    dialogLayout.addView(description);

	    LinearLayout buttonLayout = new LinearLayout(context);

	    apply.setLayoutParams(new LinearLayout.LayoutParams(
		    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f));

	    cancel.setLayoutParams(new LinearLayout.LayoutParams(
		    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f));
	    buttonLayout.addView(apply);
	    buttonLayout.addView(cancel);

	    buttonLayout.setLayoutParams(LayoutParamsCreator.createFPFP());

	    dialogLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
	    dialogLayout.addView(buttonLayout);

	    dialog.setContentView(dialogLayout);
	    dialog.show();
	} else if (event.getAction() == event.ACTION_MOVE) {
	}
	return false;
    }

}