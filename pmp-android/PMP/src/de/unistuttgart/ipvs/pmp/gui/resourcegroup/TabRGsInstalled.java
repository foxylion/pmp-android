package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.Arrays;

import android.app.ListActivity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class TabRGsInstalled extends ListActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        IResourceGroup[] resourceGroups = ModelProxy.get().getResourceGroups();
        AdapterRGsInstalled rgsAdapter = new AdapterRGsInstalled(this, Arrays.asList(resourceGroups));
        setListAdapter(rgsAdapter);
    }
}
