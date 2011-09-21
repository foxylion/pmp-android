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
    Button apps;
    Button ressources;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createParentLayout();
        createChildren();
        
        this.parentLayout.addView(this.PMPLabel);
        this.parentLayout.addView(this.layout);
        
        this.scroll.addView(this.parentLayout);
        
        setContentView(this.scroll);
    }
    
    
    /**
     * Creating the Children and its Listeners
     */
    private void createChildren() {
        this.PMPLabel = new TextView(this);
        this.PMPLabel.setText("PMP");
        this.PMPLabel.setTextColor(Color.rgb(46, 139, 87));
        this.PMPLabel.setTextSize(150);
        this.PMPLabel.setGravity(Gravity.CENTER);
        
        this.layout = new LinearLayout(this);
        this.layout.setBackgroundColor(Color.rgb(211, 211, 211));
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
        this.parentLayout.setBackgroundColor(Color.rgb(211, 211, 211));
        
        this.scroll = new ScrollView(this);
        this.scroll.setBackgroundColor(Color.rgb(211, 211, 211));
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
