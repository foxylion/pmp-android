package de.unistuttgart.ipvs.pmp.gui.tab;

import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;


public class AppPresetsTab extends Activity {
    
    @Override
    protected void onResume() {
        super.onResume();
        
        TextView tv = new TextView(this);
        
        if(PMPPreferences.getInstanace().isExpertMode()) {
            tv.setText("You can now select an assigned Preset and change it.");
        } else {
            tv.setText("You must enable the Expert Mode to use the Preset feature.");
        }
        
        addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    }
}
