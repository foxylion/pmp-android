package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;
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
     * Scrollable
     */
    private ScrollView scroll;
    
    /**
     * Handles the filling of the TableRows
     */
    TableRow actRow;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setTitle(R.string.ress);
        
        /* Create the 1st TableRow and set up */
        this.actRow = new TableRow(this);
        this.actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
        
        /* Create the MainLayout for the ApplicationsActivity */
        createLayout();
        
        /* Check if there are resources available */
        if (loadRes()) {
            this.scroll = new ScrollView(this);
          //  this.scroll.setBackgroundColor(Color.rgb(211, 211, 211));
            this.layout.addView(this.actRow);
            this.scroll.addView(this.layout);
            setContentView(this.scroll);
        } else {
            LinearLayout layoutEmpty = new LinearLayout(this);
           // layoutEmpty.setBackgroundColor(Color.rgb(211, 211, 211));
            setContentView(layoutEmpty);
        }
    }
    
    
    /**
     * Creating the Layout and setting the properties.
     */
    private void createLayout() {
        this.layout = new TableLayout(this);
        this.layout.setScrollBarStyle(0);
        this.layout.setStretchAllColumns(true);
        this.layout.setLayoutParams(LayoutParamsCreator.createFPFP());
      //  this.layout.setBackgroundColor(Color.rgb(211, 211, 211));
    }
    
    
    /**
     * Loading the Ressources each row 3
     */
    private boolean loadRes() {
        
        /* Used variables */
        int resCount = 0;
        IResourceGroup resArray[] = null;
        
        resCount = ModelSingleton.getInstance().getModel().getResourceGroups().length;
        
        /*Geting the resources in an array*/
        if (resCount != 0) {
            resArray = ModelSingleton.getInstance().getModel().getResourceGroups();
        }
        
        if (resArray != null) {
            
            /* Filling the Table with Apps each row 3 */
            for (int i = 0; i < resCount; i++) {
                if (i % 2 == 0) {
                    this.layout.addView(this.actRow);
                    this.actRow = new TableRow(this);
                    this.actRow.setLayoutParams(LayoutParamsCreator.createFPWC());
                }
                ImagedButton act = new ImagedButton(this, resArray[i].getName(), resArray[i].getIdentifier(),
                        R.drawable.res);
                act.setClickable(true);
                
                /* Set up the behaviour of the resource */
                act.setOnClickListener(new OnResClickListener(act));
                this.actRow.addView(act);
            }
            return true;
        }
        return false;
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
    private IResourceGroup res;
    
    
    public OnResClickListener(ImagedButton button) {
        this.parent = button;
        this.res = ModelSingleton.getInstance().getModel().getResourceGroup(this.parent.getIdentifier());
    }
    
    
    /*Creates the dialog with descriptions of resources*/
    @Override
    public void onClick(View v) {
        Dialog dialog = createDialog(this.parent.getContext(), this.parent.getName(), this.res.getDescription() + "\n");
        dialog.show();
    }
    
    
    /**
     * Create Dialog with params
     * 
     * @param context
     * @param title
     * @param description
     * @return Dialog
     */
    private Dialog createDialog(Context context, String title, String description) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle(title);
        TextView descriptionView = new TextView(context);
        
        descriptionView.setText(description);
        descriptionView.setPadding(10, 0, 10, 0);
        
        Button close = new Button(context);
        close.setText(R.string.close);
        close.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        
        LinearLayout layout = new LinearLayout(this.parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        
        ScrollView dialogScroll = new ScrollView(this.parent.getContext());
        dialogScroll.setLayoutParams(LayoutParamsCreator.createFPFP(0.5f));
        dialogScroll.addView(descriptionView);
        
        
        layout.addView(dialogScroll);
        layout.addView(close);
        
        dialog.setContentView(layout);
        
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(lp);
        
        return dialog;
    }
}
