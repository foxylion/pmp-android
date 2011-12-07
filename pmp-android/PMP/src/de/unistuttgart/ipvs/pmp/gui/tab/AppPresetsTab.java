package de.unistuttgart.ipvs.pmp.gui.tab;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

public class AppPresetsTab extends Activity {
    
    @Override
    protected void onResume() {
        super.onResume();
        
        setContentView(R.layout.tab_app_presets);
        
        /* Switch between Expert Mode and Normal Mode */
        TextView tvDescriptionNormalMode = (TextView) findViewById(R.id.TextView_Description_Normal);
        LinearLayout tvDescriptionExpertMode = (LinearLayout) findViewById(R.id.TextView_Description_Expert);
        if (PMPPreferences.getInstanace().isExpertMode()) {
            tvDescriptionNormalMode.setVisibility(View.GONE);
            tvDescriptionExpertMode.setVisibility(View.VISIBLE);
        } else {
            tvDescriptionNormalMode.setVisibility(View.VISIBLE);
            tvDescriptionExpertMode.setVisibility(View.GONE);
        }
    }
}
