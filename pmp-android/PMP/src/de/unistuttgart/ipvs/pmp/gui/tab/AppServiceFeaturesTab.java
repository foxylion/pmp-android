package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.ServiceFeaturesAdapter;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupModel;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class AppServiceFeaturesTab extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_app_sfs);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        /* Switch between Expert Mode and Normal Mode */
        TextView tvDescriptionNormalMode = (TextView) findViewById(R.id.TextView_Description_Normal);
        TextView tvDescriptionExpertMode = (TextView) findViewById(R.id.TextView_Description_Expert);
        if (PMPPreferences.getInstanace().isExpertMode()) {
            tvDescriptionNormalMode.setVisibility(View.GONE);
            tvDescriptionExpertMode.setVisibility(View.VISIBLE);
        } else {
            tvDescriptionNormalMode.setVisibility(View.VISIBLE);
            tvDescriptionExpertMode.setVisibility(View.GONE);
        }
        
        /* Temporary bad stuff, to Test the Activity */
        IServiceFeature[] sfs = MockupModel.instance.getApp("org.barcode.scanner").getServiceFeatures();
        IServiceFeature[] sfs_enabled = MockupModel.instance.getApp("org.barcode.scanner").getActiveServiceFeatures();
        List<IServiceFeature> sfs_disabled = new ArrayList<IServiceFeature>();
        for (IServiceFeature sf : sfs) {
            if (!sf.isActive()) {
                sfs_disabled.add(sf);
            }
        }
        
        ListView sFs = (ListView) findViewById(R.id.ListView_SFs);
        if (sFs != null) {
            sFs.setClickable(true);
            ServiceFeaturesAdapter sFsAdapter = new ServiceFeaturesAdapter(this, Arrays.asList(sfs));
            sFs.setAdapter(sFsAdapter);
        }
        
        ListView enabledSFs = (ListView) findViewById(R.id.ListView_EnabledSFs);
        if (enabledSFs != null) {
            enabledSFs.setClickable(true);
            ServiceFeaturesAdapter enabledAdapter = new ServiceFeaturesAdapter(this, Arrays.asList(sfs_enabled));
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
