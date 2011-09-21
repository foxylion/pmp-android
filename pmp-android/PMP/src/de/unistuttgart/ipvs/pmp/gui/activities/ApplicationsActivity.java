package de.unistuttgart.ipvs.pmp.gui.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;

/**
 * ApplicationsActivity shows the Applications which are installed on the device.
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
     * Handles the filling of the TableRows
     */
    TableRow actRow;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setTitle(R.string.apps);
        
        /* Create the 1st TableRow and set up */
        this.actRow = new TableRow(this);
        this.actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
        
        /* Create the MainLayout for the ApplicationsActivity */
        createLayout();
        
        /* Check if there are apps available */
        if (loadApps()) {
            this.scroll = new ScrollView(this);
            this.scroll.setBackgroundColor(Color.rgb(211, 211, 211));
            this.layout.addView(this.actRow);
            this.scroll.addView(this.layout);
            setContentView(this.scroll);
        } else {
            
            LinearLayout layoutEmpty = new LinearLayout(this);
            layoutEmpty.setBackgroundColor(Color.rgb(211, 211, 211));
            setContentView(layoutEmpty);
        }
    }
    
    
    /**
     * Loading Apps to the View, 3 each row
     * 
     * @return true if apps where successfully loaded
     */
    private boolean loadApps() {
        
        /* Used variables */
        int AppsCount = 0;
        List<IApp> appList = null;
        
        /* Get the number of installed apps */
        AppsCount = ModelSingleton.getInstance().getModel().getApps().length;
        
        /* Load Apps into a List */
        if (AppsCount != 0) {
            IApp[] appArray = ModelSingleton.getInstance().getModel().getApps();
            appList = Arrays.asList(appArray);
        }
        
        /* Filling the Table with Apps each row 3 */
        if (appList != null) {
            for (int i = 0; i < AppsCount; i++) {
                if (i % 3 == 0) {
                    this.layout.addView(this.actRow);
                    this.actRow = new TableRow(this);
                    this.actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
                }
                IApp app = appList.get(i);
                // ImagedButton act = new ImagedButton(this, app.getName(), i,
                // R.drawable.app);
                ImagedButton act = new ImagedButton(this, app.getName(), app.getIdentifier(), R.drawable.app);
                act.setClickable(true);
                
                /* Set up the behaviour of the App */
                act.setOnClickListener(new OnAppClickListener(act));
                this.actRow.addView(act);
            }
            return true;
        }
        return false;
    }
    
    
    /**
     * Creating the Layout and setting the properties.
     */
    private void createLayout() {
        this.layout = new TableLayout(this);
        this.layout.setScrollBarStyle(0);
        this.layout.setStretchAllColumns(true);
        this.layout.setLayoutParams(LayoutParamsCreator.createFPFP());
        this.layout.setBackgroundColor(Color.rgb(211, 211, 211));
    }
}

/**
 * Starts the ServiceLvlActivity for the App
 * 
 * @author Alexander Wassiljew
 * 
 */
class OnAppClickListener implements OnClickListener {
    
    private ImagedButton parent;
    
    
    /**
     * Custom constructor
     * 
     * @param button
     */
    public OnAppClickListener(ImagedButton button) {
        this.parent = button;
    }
    
    
    @Override
    public void onClick(View v) {
        
        /*
         * Call Privacy Level Activity with the specified Intent
         */
        Intent intent = new Intent(v.getContext(), ServiceLvlActivity.class);
        intent.putExtra(Constants.INTENT_IDENTIFIER, this.parent.getIdentifier());
        
        if (v.getContext() != null) {
            v.getContext().startActivity(intent);
        }
    }
    
}
