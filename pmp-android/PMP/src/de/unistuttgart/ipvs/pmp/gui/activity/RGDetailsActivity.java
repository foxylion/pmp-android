package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class RGDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        Bundle extras = getIntent().getExtras();
        String identifier = extras.getString("identifier");
        
        IResourceGroup resourceGroup = ModelProxy.get().getResourceGroup(identifier);
        
        /*
        if (resourceGroup != null) {
            setContentView(R.layout.activity_rgs_details);
        } else {
            setContentView(R.layout.activity_rgs_details_stub);
        }
       
        TextView name = (TextView) findViewById(R.id.TextView_Name);
        name.setText(resourceGroup.getName());
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(resourceGroup.getDescription());
        */
        
        /*
        TextView status = (TextView) findViewById(R.id.TextView_Status);
        String text;
        if (plugin.getInstalledRevision() == 0) {
            status.setTextColor(Color.GRAY);
            text = context.getString(R.string.rgs_notinstalled);
        } else {
            if (plugin.getInstalledRevision() == plugin.getLatestRevision()) {
                status.setTextColor(Color.GREEN);
                text = context.getString(R.string.rgs_uptodate);
                text += " (r" + String.valueOf(plugin.getInstalledRevision()) + ")";
            } else {
                status.setTextColor(Color.YELLOW);
                text = context.getString(R.string.rgs_outdated);
                text += " (r" + String.valueOf(plugin.getInstalledRevision());
                text += " to r" + String.valueOf(plugin.getLatestRevision()) + ")";
            }
        }
        status.setText(text);
*/
    }
}
