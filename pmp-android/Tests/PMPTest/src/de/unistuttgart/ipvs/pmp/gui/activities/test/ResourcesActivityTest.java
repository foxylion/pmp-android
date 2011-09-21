package de.unistuttgart.ipvs.pmp.gui.activities.test;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.RessourcesActivity;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Test ResourceActivity
 * 
 * @author Andre
 *
 */

public class ResourcesActivityTest extends
		ActivityInstrumentationTestCase2<RessourcesActivity> {

	RessourcesActivity mActivity;
	IResourceGroup[] ress;
	int resCount;
	String headerString;
	
	public ResourcesActivityTest() {
		super("de.unistuttgart.ipvs.pmp.gui.activities", RessourcesActivity.class);
	}

	protected void setUp() {
		mActivity = this.getActivity();
		ress = ModelSingleton.getInstance().getModel().getResourceGroups();
		resCount = ress.length;
		headerString = mActivity.getString(R.string.ress);
	}
	
	public void testEnglishHeader() {
		assertEquals("Resources", headerString);
	}
	
	public void testGermanHeader() {
		assertEquals("Ressourcen", headerString);
	}
	
	public void testResCount() {
		assertEquals(3, resCount);
	}
	
}
