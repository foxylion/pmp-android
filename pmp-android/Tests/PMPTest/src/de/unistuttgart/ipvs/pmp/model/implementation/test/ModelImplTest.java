package de.unistuttgart.ipvs.pmp.model.implementation.test;

import junit.framework.TestCase;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

public class ModelImplTest extends TestCase {

	IApp apps[];
	IResourceGroup ress[];
	
	public ModelImplTest(String name) {
		super(name);
		
	}

	protected void setUp() throws Exception {
		apps = ModelSingleton.getInstance().getModel().getApps();
		ress = ModelSingleton.getInstance().getModel().getResourceGroups();
		System.out.println(apps.length);
	}
	
	public void testPreconditions(){
		assertNotNull(apps);
		assertNotNull(ress);
	}
	
	public void testAppsLength(){
		assertEquals(2, apps.length);
	}

}
