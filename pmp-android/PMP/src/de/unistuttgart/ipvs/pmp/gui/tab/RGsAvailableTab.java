package de.unistuttgart.ipvs.pmp.gui.tab;

import android.view.View;
import android.widget.ListView;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.gui.activity.RGDetailsActivity;
//import de.unistuttgart.ipvs.pmp.pluginmanager.AvailablePlugins;
//import de.unistuttgart.ipvs.pmp.pluginmanager.tasks.GetAvailablePluginsTask;

public class RGsAvailableTab extends ListActivity {
    
    public ProgressDialog pd;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        GetAvailablePluginsTask task = new GetAvailablePluginsTask(this);
//        task.execute();
        
        // TODO better handling, only show pd when fetching first time
    }  
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, RGDetailsActivity.class);
//        AvailablePlugins.Plugin plugin =  (AvailablePlugins.Plugin)this.getListAdapter().getItem(position);
//        intent.putExtra("identifier", (String)plugin.getIdentifier());
//        if (plugin.getInstalledRevision() > 0) {
//            intent.putExtra("is_stub", 0);
//        } else {
//            intent.putExtra("is_stub", 1);
//            intent.putExtra("position", (int)position);
//        }
//        Log.e("Getting details for " + plugin.getIdentifier());
        startActivity(intent);
    }       
}
