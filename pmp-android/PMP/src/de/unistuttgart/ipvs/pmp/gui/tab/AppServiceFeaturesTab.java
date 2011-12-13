package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.ServiceFeaturesAdapter;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class AppServiceFeaturesTab extends Activity {
    
    private IApp app;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_app_sfs);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.app = GUITools.handleIntent(getIntent());
        
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
        
        IServiceFeature[] sfs = app.getServiceFeatures();
        
        ListView sFs = (ListView) findViewById(R.id.ListView_SFs);
        if (sFs != null) {
            sFs.setClickable(true);
            ServiceFeaturesAdapter sFsAdapter = new ServiceFeaturesAdapter(this, Arrays.asList(sfs));
            sFs.setAdapter(sFsAdapter);
        }
    }
}
