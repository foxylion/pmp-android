package de.unistuttgart.ipvs.pmp.model.implementations.test;

import junit.framework.TestCase;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
/**
 * Testing the Model
 * @author Alexander Wassiljew
 *
 */
public class ModelImplTest extends TestCase {

	IApp apps[];
	IResourceGroup ress[];
	
	public ModelImplTest(String name) {
		super(name);
		
	}
	/**
	 * Set up the testing environment
	 */
	protected void setUp() throws Exception {
		apps = ModelSingleton.getInstance().getModel().getApps();
		ress = ModelSingleton.getInstance().getModel().getResourceGroups();
	}
	
	/**
	 * Testing the Preconditions
	 */
	public void testPreconditions(){
		assertNotNull(apps);
		assertNotNull(ress);
	}
	
	public void testLength(){
		assertEquals(2, apps.length);
		assertEquals(3, ress.length);
	}
	
	public void testNameApp1() {
		assertEquals("Test App 1", apps[0].getName());
	}
	public void testNameApp2() {
		assertEquals("Test App 2", apps[1].getName());
	}
	public void testIdentifierApp1(){
		assertEquals("Sample#pmp.test.app1", apps[0].getIdentifier());
	}
	public void testIdentifierApp2(){
		assertEquals("Sample#pmp.test.app2", apps[1].getIdentifier());
	}

}
