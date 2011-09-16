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
import android.view.View.OnClickListener;
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
	parentLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
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
	    button.setLayoutParams(LayoutParamsCreator.createFPWC());
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
	    Dialog dialog = createDialog();
	    dialog.show();
	} else if (event.getAction() == event.ACTION_MOVE) {
	}
	return false;
    }

    /**
     * Create a dialog for setting up the service level
     * 
     * @return Dialog
     */
    private Dialog createDialog() {
	/* Dialog */
	final Dialog dialog = new Dialog(context);
	dialog.setCanceledOnTouchOutside(false);
	dialog.setCancelable(true);
	dialog.setTitle("Apply Service Level?");
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
	
	/* Description */
	TextView description = new TextView(context);
	description.setText("Description:" + "\n\n" + lvlDescr + "\n");
	description.setPadding(10, 0, 10, 0);

	/* Apply */
	Button apply = new Button(context);
	apply.setLayoutParams(LayoutParamsCreator.createFPFP(1f));
	apply.setText("Apply");
	/**
	 * Sets the Service Level.
	 */
	apply.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		IApp appsArray[] = ModelSingleton.getInstance().getModel()
			.getApps();
		IApp app = appsArray[appID];
		IServiceLevel levelArray[] = app.getServiceLevels();
		IServiceLevel level = levelArray[levelID];
		Log.v("appID:" + String.valueOf(appID));
		Log.v("levelID:" + String.valueOf(levelID));
		/*Set the Service Level here*/
		app.setActiveServiceLevelAsPreset(level.getLevel());

		dialog.cancel();
	    }
	});

	/* Cancel */
	Button cancel = new Button(context);
	cancel.setLayoutParams(LayoutParamsCreator.createFPFP(1f));
	cancel.setText("Cancel");

	/**
	 * Cancel the dialog
	 */
	cancel.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		dialog.cancel();
	    }
	});
	/*DialogLayout which holds the description and the buttonLayout*/
	LinearLayout dialogLayout = new LinearLayout(context);
	dialogLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
	dialogLayout.setOrientation(LinearLayout.VERTICAL);
	dialogLayout.addView(description);
	
	/*buttonLayout which holds the buttons: Apply, Cancel*/
	LinearLayout buttonLayout = new LinearLayout(context);
	buttonLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
	buttonLayout.addView(apply);
	buttonLayout.addView(cancel);
	
	dialogLayout.addView(buttonLayout);
	dialog.setContentView(dialogLayout);

	return dialog;
    }

}