package de.unistuttgart.ipvs.pmp.gui.activities.test;

import android.test.ActivityInstrumentationTestCase2;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.RessourcesActivity;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

/**
 * Test ResourceActivity
 * 
 * @author Andre
 * 
 */

public class ResourcesActivityTest extends ActivityInstrumentationTestCase2<RessourcesActivity> {
    
    RessourcesActivity mActivity;
    IResourceGroup[] ress;
    int resCount;
    String headerString;
    
    
    public ResourcesActivityTest() {
        super("de.unistuttgart.ipvs.pmp", RessourcesActivity.class);
    }
    
    
    @Override
    protected void setUp() {
        this.mActivity = getActivity();
        this.ress = ModelSingleton.getInstance().getModel().getResourceGroups();
        this.resCount = this.ress.length;
        this.headerString = this.mActivity.getString(R.string.ress);
    }
    
    
    public void testEnglishHeader() {
        assertEquals("Resources", this.headerString);
    }
    
    
    public void testGermanHeader() {
        assertEquals("Ressourcen", this.headerString);
    }
    
    
    public void testResCount() {
        assertEquals(3, this.resCount);
    }
    
}
