package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * StartActivity is the main activity of PMP.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class StartActivity extends Activity {
    /**
     * LayoutParams for the layouts
     */
    LayoutParams fpfp;
    LayoutParams fpwc;
    LayoutParams wcfp;
    LayoutParams wcwc;
    /**
     * Buttons Layout
     */
    LinearLayout layout;
    /**
     * Main Layout of the Activity, which will be draw to the Canvas
     */
    LinearLayout parentLayout;

    /**
     * ScrollLayout for the horizontal mode
     */
    ScrollView scroll;
    /**
     * Views of the Activity
     */
    TextView PMPLabel;
    Button apps;
    Button ressources;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	createLayoutParams();
	createParentLayout();
	createChildren();

	parentLayout.addView(PMPLabel);
	parentLayout.addView(layout);

	scroll.addView(parentLayout);
	
	setContentView(scroll);
    }

    /**
     * Creating the Children and its Listeners
     */
    private void createChildren() {
	PMPLabel = new TextView(this);
	PMPLabel.setText("PMP");
	PMPLabel.setTextColor(Color.rgb(46, 139, 87));
	PMPLabel.setTextSize(150);
	PMPLabel.setGravity(Gravity.CENTER);

	layout = new LinearLayout(this);
	layout.setBackgroundColor(Color.rgb(211, 211, 211));
	layout.setOrientation(LinearLayout.VERTICAL);
	layout.setVerticalGravity(Gravity.CENTER);

	apps = new Button(this);
	apps.setText("Applications");
	apps.setOnTouchListener(new ApplicationsListener());

	ressources = new Button(this);
	ressources.setText("Ressources");
	ressources.setOnTouchListener(new RessourcesListener());

	
	layout.addView(apps);
	layout.addView(ressources);

    }

    /**
     * Creates the Parent Layout and setting the properties.
     */
    private void createParentLayout() {
	parentLayout = new LinearLayout(this);
	parentLayout.setLayoutParams(fpfp);
	parentLayout.setOrientation(LinearLayout.VERTICAL);
	parentLayout.setBackgroundColor(Color.rgb(211, 211, 211));
	
	scroll = new ScrollView(this);
	scroll.setBackgroundColor(Color.rgb(211, 211, 211));
	scroll.setLayoutParams(fpfp);
    }

    /**
     * Creating the LayoutParams for the Layout
     */
    private void createLayoutParams() {
	fpfp = new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.FILL_PARENT);
	fpwc = new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.WRAP_CONTENT);
	wcfp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		LayoutParams.FILL_PARENT);
	wcwc = new LayoutParams(LayoutParams.WRAP_CONTENT,
		LayoutParams.WRAP_CONTENT);
    }
}

/**
 * ApplicationListener start the ApplicationsActivity
 * 
 * @author Alexander Wassiljew
 * 
 */
class ApplicationsListener implements OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
	if (event.ACTION_UP == event.getAction()) {

	    Intent intent = new Intent(v.getContext(),
		    ApplicationsActivity.class);
	    if (v.getContext() != null)
		v.getContext().startActivity(intent);
	    return false;
	}
	return false;
    }
}

/**
 * RessourcesListener start the RessourcesActivity
 * 
 * @author Alexander Wassiljew
 * 
 */
class RessourcesListener implements OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
	if (event.ACTION_UP == event.getAction()) {
	    Intent intent = new Intent(v.getContext(), RessourcesActivity.class);
	    if (v.getContext() != null)
		v.getContext().startActivity(intent);
	    return false;
	}
	return false;
    }
}
