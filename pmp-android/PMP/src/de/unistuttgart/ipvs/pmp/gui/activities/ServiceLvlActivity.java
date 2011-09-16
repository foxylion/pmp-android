package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * PrivacyLvlActivity
 * 
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
    private String identifier;

    /**
     * If there are too much Levels, so you can scroll.
     */
    private ScrollView scroll;
    /**
     * Main Layout of the Activity, which will be draw to the Canvas
     */
    private LinearLayout parentLayout;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	loadIntentsExtras();
	appName = ModelSingleton.getInstance().getModel().getApp(identifier)
		.getName();
	this.setTitle(this.getString(R.string.servive_level_for) + " "
		+ appName);
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
	IApp app = ModelSingleton.getInstance().getModel().getApp(identifier);
	
	IServiceLevel levelArray[] = app.getServiceLevels();
	Log.v("ServiceLeveltActivity: Number of Service Levels" + String.valueOf(levelArray.length)
		+ "for the app: " + app.getName());
	RadioGroup group = new RadioGroup(this);
	/*Iterate over Service Levels */
	for (int i = 0; i < levelArray.length; i++) {
	    RadioButton button = new RadioButton(this);
	    button.setBackgroundColor(Color.rgb(211, 211, 211));
	    button.setLayoutParams(LayoutParamsCreator.createFPWC());
	    button.setGravity(Gravity.CENTER);
	    button.setTextColor(Color.BLACK);
	    button.setText(levelArray[i].getName());
	    /*Check if Service Level is set*/
	    if (app.getActiveServiceLevel().getLevel() == i) {
		button.setChecked(true);
	    }
	    /*Check if Service Level is available*/
	    if(levelArray[i].isAvailable()){
	    button
		    .setOnTouchListener(new OnLevelTouchListener(this,
			    levelArray[i].getDescription(), button, this,
			    identifier, i));
	    
	    }else{
		button.setEnabled(false);
		button.setTextColor(Color.GRAY);
	    }
	    group.addView(button);
	}
	group.setGravity(Gravity.CENTER_HORIZONTAL);
	parentLayout.addView(group);
    }

    /**
     * Loads the Intent
     */
    private void loadIntentsExtras() {
	identifier = this.getIntent().getExtras().getString(
		de.unistuttgart.ipvs.pmp.Constants.INTENT_IDENTIFIER);
    }
    /**
     * Reload the activity
     */
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
    private String identifier;

    public OnLevelTouchListener(Context context, String lvlDescr,
	    RadioButton button, ServiceLvlActivity activity, String identifier,
	    int levelID) {
	this.lvlDescr = lvlDescr;
	this.context = context;
	this.parent = button;
	this.activity = activity;
	this.levelID = levelID;
	this.identifier = identifier;
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
	dialog.setTitle(R.string.apply_service_level);
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
	description.setText(context.getString(R.string.description) + "\n\n"
		+ lvlDescr + "\n");
	description.setPadding(10, 0, 10, 0);

	/* Apply */
	Button apply = new Button(context);
	apply.setLayoutParams(LayoutParamsCreator.createFPFP(1f));
	apply.setText(R.string.apply);
	/**
	 * Sets the Service Level.
	 */
	apply.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final IApp app = ModelSingleton.getInstance().getModel()
			.getApp(identifier);
		IServiceLevel levelArray[] = app.getServiceLevels();
		final IServiceLevel level = levelArray[levelID];
		/* Set the Service Level here */

		final Dialog waitingDialog = ProgressDialog.show(context,
			context.getString(R.string.please_wait), context
				.getString(R.string.set_service_level), true);
		/*
		 * Create an AsynchTask and wait till
		 * setActiveServiceLevelAsPreset is done
		 */
		new AsyncTask<Void, Void, Void>() {

		    @Override
		    protected Void doInBackground(Void... params) {
			app.setActiveServiceLevelAsPreset(level.getLevel());
			return null;
		    }

		    @Override
		    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			waitingDialog.hide();
			dialog.cancel();
		    }
		}.execute();
	    }
	});

	/* Cancel */
	Button cancel = new Button(context);
	cancel.setLayoutParams(LayoutParamsCreator.createFPFP(1f));
	cancel.setText(context.getString(R.string.cancel));

	/**
	 * Cancel the dialog
	 */
	cancel.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		dialog.cancel();
	    }
	});
	/* DialogLayout which holds the description and the buttonLayout */
	LinearLayout dialogLayout = new LinearLayout(context);
	dialogLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
	dialogLayout.setOrientation(LinearLayout.VERTICAL);
	dialogLayout.addView(description);

	/* buttonLayout which holds the buttons: Apply, Cancel */
	LinearLayout buttonLayout = new LinearLayout(context);
	buttonLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
	buttonLayout.addView(apply);
	buttonLayout.addView(cancel);

	dialogLayout.addView(buttonLayout);
	dialog.setContentView(dialogLayout);

	return dialog;
    }

}