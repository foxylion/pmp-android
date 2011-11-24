package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.ServiceFeaturesAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class AppServiceFeaturesTab extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pmp_app_sfs);
        
        /* Temporary bad stuff, to Test the Activity */
        List<IServiceFeature> sfs_enabled = new ArrayList<IServiceFeature>();
        sfs_enabled.add(new ServiceFeature("Read Entries", "The Bronze Service Feature", true, true));
        sfs_enabled.add(new ServiceFeature("Write Entries", "The Bronze Service Feature", true, true));
        sfs_enabled.add(new ServiceFeature("Share Entries", "The Bronze Service Feature", false, false));
        
        List<IServiceFeature> sfs_disabled = new ArrayList<IServiceFeature>();
        sfs_enabled.add(new ServiceFeature("Read Entries", "The Bronze Service Feature", true, true));
        sfs_enabled.add(new ServiceFeature("Write Entries", "The Bronze Service Feature", false, true));
        sfs_enabled.add(new ServiceFeature("Share Entries", "The Bronze Service Feature", false, true));
        
        List<IServiceFeature> sfs = new ArrayList<IServiceFeature>();
        sfs.addAll(sfs_enabled);
        sfs.addAll(sfs_disabled);
        
        ListView sFs = (ListView) findViewById(R.id.ListView_SFs);
        if (sFs != null) {
            sFs.setClickable(true);
            ServiceFeaturesAdapter sFsAdapter = new ServiceFeaturesAdapter(this, sfs);
            sFs.setAdapter(sFsAdapter);
        }
        
        ListView enabledSFs = (ListView) findViewById(R.id.ListView_EnabledSFs);
        if (enabledSFs != null) {
            enabledSFs.setClickable(true);
            ServiceFeaturesAdapter enabledAdapter = new ServiceFeaturesAdapter(this, sfs_enabled);
            enabledSFs.setAdapter(enabledAdapter);
        }
        
        ListView disabledSFs = (ListView) findViewById(R.id.ListView_DisabledSFs);
        if (disabledSFs != null) {
            enabledSFs.setClickable(true);
            ServiceFeaturesAdapter disabledAdapter = new ServiceFeaturesAdapter(this, sfs_disabled);
            disabledSFs.setAdapter(disabledAdapter);
        }
    }
}
