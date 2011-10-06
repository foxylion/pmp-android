package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;

/**
 * StartActivity is the main activity of PMP.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class StartActivity extends Activity {
    
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
    TextView PMPDescriptionLabel;
    Button apps;
    Button ressources;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        createParentLayout();
//        createChildren();
        
//        this.parentLayout.addView(this.PMPLabel);
//        this.parentLayout.addView(this.PMPDescriptionLabel);
//        this.parentLayout.addView(this.layout);
//        
//        this.scroll.addView(this.parentLayout);
//        
//        setContentView(this.scroll);
        
        setContentView(R.layout.start);
        
        ((Button) findViewById(R.id.start_button_application)).setOnTouchListener(new ApplicationsListener());
        ((Button) findViewById(R.id.start_button_resources)).setOnTouchListener(new RessourcesListener());
    }
    
    
    /**
     * Creating the Children and its Listeners
     */
    private void createChildren() {
        this.PMPLabel = new TextView(this);
        this.PMPLabel.setText("PMP");
        this.PMPLabel.setTextColor(Color.rgb(120, 120, 120));
        this.PMPLabel.setTextSize(150);
        this.PMPLabel.setPadding(10, 10, 10, 10);
        this.PMPLabel.setGravity(Gravity.CENTER);
        
        this.PMPDescriptionLabel = new TextView(this);
        this.PMPDescriptionLabel.setText("Privacy Management Platform");
        this.PMPDescriptionLabel.setTextColor(Color.rgb(75, 75, 75));
        this.PMPLabel.setPadding(10, 10, 10, 10);
        this.PMPDescriptionLabel.setGravity(Gravity.CENTER);
        
        this.layout = new LinearLayout(this);
        this.layout.setBackgroundColor(Color.rgb(0, 0, 0));
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.layout.setVerticalGravity(Gravity.CENTER);
        
        this.apps = new Button(this);
        this.apps.setText(R.string.apps);
        this.apps.setOnTouchListener(new ApplicationsListener());
        
        this.ressources = new Button(this);
        this.ressources.setText(R.string.ress);
        this.ressources.setOnTouchListener(new RessourcesListener());
        
        this.layout.addView(this.apps);
        this.layout.addView(this.ressources);
    }
    
    
    /**
     * Creates the Parent Layout and setting the properties.
     */
    private void createParentLayout() {
        this.parentLayout = new LinearLayout(this);
        this.parentLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
        this.parentLayout.setOrientation(LinearLayout.VERTICAL);
        this.parentLayout.setBackgroundColor(Color.rgb(0, 0, 0));
        
        this.scroll = new ScrollView(this);
        this.scroll.setBackgroundColor(Color.rgb(0, 0, 0));
        this.scroll.setLayoutParams(LayoutParamsCreator.createFPFP());
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
        if (MotionEvent.ACTION_UP == event.getAction()) {
            
            Intent intent = new Intent(v.getContext(), ApplicationsActivity.class);
            if (v.getContext() != null) {
                v.getContext().startActivity(intent);
            }
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
        if (MotionEvent.ACTION_UP == event.getAction()) {
            Intent intent = new Intent(v.getContext(), RessourcesActivity.class);
            if (v.getContext() != null) {
                v.getContext().startActivity(intent);
            }
            return false;
        }
        return false;
    }
}
