package de.unistuttgart.ipvs.pmp.gui.app;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

/**
 * The {@link TabServiceFeatures} displays all Service Features which are offered by the App.
 * 
 * @author Jakob Jarosch
 */
public class TabServiceFeatures extends Activity {
    
    /**
     * The reference to the real App in the model.
     */
    private IApp app;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_app_sfs);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.app = GUITools.handleAppIntent(getIntent());
        
        /* Switch between Expert Mode and Normal Mode */
        TextView tvDescriptionNormalMode = (TextView) findViewById(R.id.TextView_Description_Normal);
        TextView tvDescriptionExpertMode = (TextView) findViewById(R.id.TextView_Description_Expert);
        if (PMPPreferences.getInstance().isExpertMode()) {
            tvDescriptionNormalMode.setVisibility(View.GONE);
            tvDescriptionExpertMode.setVisibility(View.VISIBLE);
        } else {
            tvDescriptionNormalMode.setVisibility(View.VISIBLE);
            tvDescriptionExpertMode.setVisibility(View.GONE);
        }
        
        /* Load the offered Service Features into the list. */
        IServiceFeature[] sfs = this.app.getServiceFeatures();
        
        ListView serviceFeaturesView = (ListView) findViewById(R.id.ListView_SFs);
        
        serviceFeaturesView.setClickable(true);
        serviceFeaturesView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                System.out.println("foobar");
                ((ListItemServiceFeature) view).reactOnMouseClick();
            }
        });
        
        AdapterServiceFeatures sFsAdapter = new AdapterServiceFeatures(this, Arrays.asList(sfs));
        serviceFeaturesView.setAdapter(sFsAdapter);
    }
}
