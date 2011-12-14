package de.unistuttgart.ipvs.pmp.gui.tab;

import android.view.View;
import android.widget.ListView;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activity.RGDetailsActivity;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.pluginmanager.GetAvailablePluginsTask;

public class RGsAvailableTab extends ListActivity {
    
    protected IResourceGroup[] resourceGroups;
    public ProgressDialog pd;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        pd = new ProgressDialog(this);
        pd.setMessage(this.getString(R.string.rgs_getting_resource_groups));
        pd.setIndeterminate(false);
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
        GetAvailablePluginsTask task = new GetAvailablePluginsTask(this);
        task.execute();
        
        // TODO better handling, only show pd when fetching first time
  
        //ListView lv = getListView();
        //lv.setTextFilterEnabled(true);      
    }
    
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(RGsAvailableTab.this, RGDetailsActivity.class);
        String identifier = resourceGroups[position].getIdentifier();
        intent.putExtra("identifier", identifier);
         startActivity(intent);
    }
}
