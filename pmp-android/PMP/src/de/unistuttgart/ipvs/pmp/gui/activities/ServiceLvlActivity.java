package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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
	this.setTitle("Service Levels for "+appName);
	createParentLayout();
	// loadServiceLevels();
	loadFakeServiceLevels();
	scroll = new ScrollView(this);
	scroll.addView(parentLayout);

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

    }

    /**
     * Loads the Intent
     */
    private void loadIntentsExtras() {
	appName = this.getIntent().getExtras().getString("appName");
    }

    /**
     * ************* FAKE METHOD ********** 
     * Loads the Privacy Levels for the GUI
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
	    button.setBackgroundColor(Color.WHITE);
	    button.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		    LayoutParams.WRAP_CONTENT));
	    button.setGravity(Gravity.CENTER);
	    button.setTextSize(50);
	    button.setTextColor(Color.BLACK);
	    if(i == 3)
		button.setChecked(true);
	    button.setText(levels[i]);
	    button
	    .setOnTouchListener(new OnLevelTouchListener(this,
		    levels[i], button, this));
	    group.addView(button);
	}
	group.setGravity(Gravity.CENTER_HORIZONTAL);
	parentLayout.addView(group);
    }
}
/**
 * OnLevelTouchListener 
 * @author Alexander Wassiljew
 *
 */
class OnLevelTouchListener implements OnTouchListener {

    private String lvlName;
    private Context context;
    private RadioButton parent;
    private ServiceLvlActivity activity;
    public OnLevelTouchListener(Context context, String lvlName, RadioButton button,
	    ServiceLvlActivity activity) {
	this.lvlName = lvlName;
	this.context = context;
	this.parent = button;
	this.activity = activity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
	if (event.getAction() == event.ACTION_UP) {
	    Dialog dialog = new Dialog(context);
	    dialog.setCanceledOnTouchOutside(true);
	    dialog.setTitle("Apply Service Level?");
	    LinearLayout dialogLayout = new LinearLayout(context);
	    dialogLayout.setOrientation(LinearLayout.VERTICAL);
	    TextView description = new TextView(context);
	    description.setText("Description of the Service Level " + lvlName);
	    Button apply = new Button(context);
	    apply.setText("Apply");
	    dialog.setOnCancelListener(new OnCancelListener() {
	        @Override
	        public void onCancel(DialogInterface dialog) {
	            // Reloads the Service Level Activity if new Service Level
	            //wasn't set
	            activity.startActivity(activity.getIntent()); activity.finish();
	        }
	    });
	    /**
	     * Sets the Service Level.
	     */
	    apply.setOnTouchListener(new OnTouchListener() {
	        
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            
	            return false;
	        }
	    });
	    dialogLayout.addView(description);
	    dialogLayout.addView(apply);
	    dialog.setContentView(dialogLayout);
	    dialog.show();
	} else if (event.getAction() == event.ACTION_MOVE) {
	}
	return false;
    }

}