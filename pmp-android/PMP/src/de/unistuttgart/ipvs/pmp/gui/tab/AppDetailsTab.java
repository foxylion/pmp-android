package de.unistuttgart.ipvs.pmp.gui.tab;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class AppDetailsTab extends Activity {
    
    private IApp app;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_app_details);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //app = handleIntent(getIntent());
        this.app = ModelProxy.get().getApp("org.barcode.scanner");
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(this.app.getDescription());
    }
}
