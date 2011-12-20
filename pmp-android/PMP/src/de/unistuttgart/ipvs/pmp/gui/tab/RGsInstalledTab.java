package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;

import android.app.ListActivity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsInstalledAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class RGsInstalledTab extends ListActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        IResourceGroup[] resourceGroups = ModelProxy.get().getResourceGroups();
        RGsInstalledAdapter rgsAdapter = new RGsInstalledAdapter(this, Arrays.asList(resourceGroups));
        setListAdapter(rgsAdapter);
    }
}
