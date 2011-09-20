package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.LocationEnum;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestLocationPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestPresets.TestPreset;
import de.unistuttgart.ipvs.pmp.model.implementations.utils.ServiceLevelCalculator;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import junit.framework.TestCase;

public class ServiceLevelCalculatorTest extends TestCase {

	private TestApp testApp;
	private IPreset[] presets1;
	private IPreset[] presets2;
	private IPreset[] presets3;

	private final static IPreset TEST_PRESET_1 = new TestPreset(
			new IPrivacyLevel[] { new TestAccuracyPrivacyLevel("1") });
	private final static IPreset TEST_PRESET_2 = new TestPreset(
			new IPrivacyLevel[] { new TestAccuracyPrivacyLevel("50") });
	private final static IPreset TEST_PRESET_3 = new TestPreset(
			new IPrivacyLevel[] { new TestAccuracyPrivacyLevel("1"),
					new TestLocationPrivacyLevel(LocationEnum.STUTTGART.toString()) });
	private final static IPreset TEST_PRESET_4 = new TestPreset(
			new IPrivacyLevel[] { new TestAccuracyPrivacyLevel("600"),
					new TestLocationPrivacyLevel(LocationEnum.BADEN_WUERTTEMBERG.toString()) });
	private final static IPreset TEST_PRESET_5 = new TestPreset(
			new IPrivacyLevel[] { new TestLocationPrivacyLevel(LocationEnum.DEUTSCHLAND.toString()) });

	/**
	 * Set up the testing environment
	 */
	protected void setUp() throws Exception {
		testApp = new TestApp();
		presets1 = new IPreset[1];
		presets2 = new IPreset[2];
		presets3 = new IPreset[3];
	}

	public void testCalculate1() {
		// Test 1: Preset 1 -> SL 0
		presets1[0] = TEST_PRESET_1;
		testApp.setPresets(presets1);
		try {
			assertEquals(0, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate2() {
		// Test 2: Preset 2 -> SL 1
		presets1[0] = TEST_PRESET_2;
		testApp.setPresets(presets1);
		try {
			assertEquals(1, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate3() {
		// Test 3: Preset 3 -> SL 0
		presets1[0] = TEST_PRESET_3;
		testApp.setPresets(presets1);
		try {
			assertEquals(0, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate4() {
		// Test 4: Preset 4 -> SL 3
		presets1[0] = TEST_PRESET_4;
		testApp.setPresets(presets1);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate5() {
		// Test 5: Preset 5 -> SL 0
		presets1[0] = TEST_PRESET_5;
		testApp.setPresets(presets1);
		try {
			assertEquals(0, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate6() {
		// Test 6: Preset 1 + Preset 2 -> SL 1
		presets2[0] = TEST_PRESET_1;
		presets2[1] = TEST_PRESET_2;
		testApp.setPresets(presets2);
		try {
			assertEquals(1, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate7() {
		// Test 7: Preset 1 + Preset 3 -> SL 0
		presets2[0] = TEST_PRESET_1;
		presets2[1] = TEST_PRESET_3;
		testApp.setPresets(presets2);
		try {
			assertEquals(0, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate8() {

		// Test 8: Preset 1 + Preset 4 -> SL 3
		presets2[0] = TEST_PRESET_1;
		presets2[1] = TEST_PRESET_4;
		testApp.setPresets(presets2);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate9() {
		// Test 9: Preset 1 + Preset 5 -> SL 0
		presets2[0] = TEST_PRESET_1;
		presets2[1] = TEST_PRESET_5;
		testApp.setPresets(presets2);
		try {
			assertEquals(0, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate10() {

		// Test 10: Preset 2 + Preset 3 -> SL 2
		presets2[0] = TEST_PRESET_2;
		presets2[1] = TEST_PRESET_3;
		testApp.setPresets(presets2);
		try {
			assertEquals(2, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate11() {
		// Test 11: Preset 2 + Preset 4 -> SL 3
		presets2[0] = TEST_PRESET_2;
		presets2[1] = TEST_PRESET_4;
		testApp.setPresets(presets2);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate12() {
		// Test 12: Preset 2 + Preset 5 -> SL 3
		presets2[0] = TEST_PRESET_2;
		presets2[1] = TEST_PRESET_5;
		testApp.setPresets(presets2);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate13() {
		// Test 13: Preset 3 + Preset 4 -> SL 3
		presets2[0] = TEST_PRESET_3;
		presets2[1] = TEST_PRESET_4;
		testApp.setPresets(presets2);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate14() {
		// Test 14: Preset 3 + Preset 5 -> SL 2
		presets2[0] = TEST_PRESET_3;
		presets2[1] = TEST_PRESET_5;
		testApp.setPresets(presets2);
		try {
			assertEquals(2, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	// Test 15: Preset 4 + Preset 5 -> SL 4
	public void testCalculate15() {
		presets2[0] = TEST_PRESET_4;
		presets2[1] = TEST_PRESET_5;
		testApp.setPresets(presets2);
		try {
			assertEquals(4, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate16() {
		// Test 16: Preset 2 + Preset 3 + Preset 4 -> SL 3
		presets3[0] = TEST_PRESET_2;
		presets3[1] = TEST_PRESET_3;
		presets3[2] = TEST_PRESET_4;
		testApp.setPresets(presets3);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate17() {
		// Test 17: Preset 3 + Preset 4 + Preset 5 -> SL 4
		presets3[0] = TEST_PRESET_3;
		presets3[1] = TEST_PRESET_4;
		presets3[2] = TEST_PRESET_5;
		testApp.setPresets(presets3);
		try {
			assertEquals(4, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

	public void testCalculate18() {
		// Test 18: Preset 1 + Preset 2 + Preset 4 -> SL 3
		presets3[0] = TEST_PRESET_1;
		presets3[1] = TEST_PRESET_2;
		presets3[2] = TEST_PRESET_4;
		testApp.setPresets(presets3);
		try {
			assertEquals(3, new ServiceLevelCalculator(testApp).calculate());
		} catch (RemoteException ignore) {
		}
	}

}
