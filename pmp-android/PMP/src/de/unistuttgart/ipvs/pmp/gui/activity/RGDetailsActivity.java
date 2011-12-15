package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.pluginmanager.AvailablePlugins;
import de.unistuttgart.ipvs.pmp.pluginmanager.PluginManager;

public class RGDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        Bundle extras = getIntent().getExtras();
        int isStub = extras.getInt("is_stub");
        
        if (isStub == 0) {
            createFullView(extras.getString("identifier"));
            Log.e("Full View");
        } else if (isStub == 1) {
            createStubView(extras.getInt("position"));
            Log.e("Stub View");
        }
    }

    private void createStubView(int position) {
        setContentView(R.layout.activity_rgs_details_stub);
        AvailablePlugins.Plugin plugin = PluginManager.getInstance().getAvailablePlugins().getPlugins().get(position);
        TextView name = (TextView) findViewById(R.id.TextView_Name);
        name.setText(plugin.getName());
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(plugin.getDescription());
    }

    private void createFullView(String string) {
        setContentView(R.layout.activity_rgs_details);
        /*
        TextView name = (TextView) findViewById(R.id.TextView_Name);
        name.setText(resourceGroup.getName());
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(resourceGroup.getDescription());
        
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
