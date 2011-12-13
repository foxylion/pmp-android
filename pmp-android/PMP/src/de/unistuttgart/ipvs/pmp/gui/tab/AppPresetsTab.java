package de.unistuttgart.ipvs.pmp.gui.tab;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class AppPresetsTab extends Activity {
    
    private IApp app;
    
    @Override
    protected void onResume() {
        super.onResume();
        
        setContentView(R.layout.tab_app_presets);
        
        this.app = GUITools.handleIntent(getIntent());
        
        /* Switch between Expert Mode and Normal Mode */
        TextView tvDescriptionNormalMode = (TextView) findViewById(R.id.TextView_Description_Normal);
        LinearLayout tvDescriptionExpertMode = (LinearLayout) findViewById(R.id.TextView_Description_Expert);
        if (PMPPreferences.getInstance().isExpertMode()) {
            tvDescriptionNormalMode.setVisibility(View.GONE);
            tvDescriptionExpertMode.setVisibility(View.VISIBLE);
        } else {
            tvDescriptionNormalMode.setVisibility(View.VISIBLE);
            tvDescriptionExpertMode.setVisibility(View.GONE);
        }
    }
}
