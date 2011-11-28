package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.ServiceFeaturesAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ServiceFeature;
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
        List<IServiceFeature> sfs_enabled = new ArrayList<IServiceFeature>();
        sfs_enabled.add(new ServiceFeature("Use Camera", "Must be enabled to get any functionality.", true, true));
        sfs_enabled.add(new ServiceFeature("Internet Connection", "Required to fetch details for a scanned product.", true, true));
        sfs_enabled.add(new ServiceFeature("Facebook Share", "Enable it to share a product on facebook.", true, true));
        
        List<IServiceFeature> sfs_disabled = new ArrayList<IServiceFeature>();
        sfs_enabled.add(new ServiceFeature("Personal Information", "If it is enabled you can post product ratings.", false, true));
        sfs_enabled.add(new ServiceFeature("Credit Card-Details", "Allows you to directly buy and pay a scanned product.", false, false));
        sfs_enabled.add(new ServiceFeature("Email Account", "Send product via email to your friends.", false, true));
        
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
