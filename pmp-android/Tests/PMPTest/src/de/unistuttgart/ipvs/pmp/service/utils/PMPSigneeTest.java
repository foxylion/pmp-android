package de.unistuttgart.ipvs.pmp.service.utils;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import android.test.AndroidTestCase;

public class PMPSigneeTest extends AndroidTestCase {

	private static final String APP_IDENTIFIER = "my.app";
	private static final String RG_IDENTIFIER = "my.rg";

	private static PMPSignee appSignee = null;
	private static PMPSignee rgSignee = null;

	private static boolean initializedTest = false;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		if (!PMPSigneeTest.initializedTest) {
			appSignee = new PMPSignee(PMPComponentType.APP, APP_IDENTIFIER,
					getContext());
			rgSignee = new PMPSignee(PMPComponentType.RESOURCE_GROUP,
					RG_IDENTIFIER, getContext());

			/* reset the PMPSignee if maybe created in a previous run */
			appSignee.clearRemotePublicKeys();
			rgSignee.clearRemotePublicKeys();

			PMPSigneeTest.initializedTest = true;
		}
	}

	/**
	 * Test the correct {@link PMPComponentType} and the correct identifier set
	 * by setUp-method.
	 */
	public void test01_correctTypeAndIdentifier() {
		prepareTestCase();

		assertEquals("appSignee - equal PMPComponentType",
				PMPComponentType.APP, appSignee.getType());
		assertEquals("rgSignee - equal PMPComponentType",
				PMPComponentType.RESOURCE_GROUP, rgSignee.getType());

		assertEquals("appSignee - equal identifier", APP_IDENTIFIER,
				appSignee.getIdentifier());
		assertEquals("rgSignee - equal identifier", RG_IDENTIFIER,
				rgSignee.getIdentifier());
	}

	/**
	 * Test a sign, while rgSignee does not know the appSignee-publicKey. Should
	 * normally fail.
	 */
	public void test02_failingSign() {
		prepareTestCase();

		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());
		assertFalse(rgSignee.isSignatureValid(appSignee.getType(),
				APP_IDENTIFIER, RG_IDENTIFIER.getBytes(), signature));
	}

	/**
	 * The if the publicKey can be correctly added to a signee.
	 */
	public void test03_publicKeyAdd() {
		prepareTestCase();

		rgSignee.setRemotePublicKey(PMPComponentType.APP, APP_IDENTIFIER,
				appSignee.getLocalPublicKey());

		byte[] appPublicKey = appSignee.getLocalPublicKey();
		byte[] rgAppPublicKey = rgSignee.getRemotePublicKey(
				PMPComponentType.APP, APP_IDENTIFIER);

		for (int i = 0; i < appPublicKey.length; i++) {
			assertEquals("rgSignee should know the publicKey of appSignee",
					appPublicKey[i], rgAppPublicKey[i]);
		}

		prepareTestCase();

		/* Test that again after save and load. */
		rgAppPublicKey = rgSignee.getRemotePublicKey(PMPComponentType.APP,
				APP_IDENTIFIER);

		for (int i = 0; i < appPublicKey.length; i++) {
			assertEquals(
					"rgSignee should still know the publicKey of appSignee",
					appPublicKey[i], rgAppPublicKey[i]);
		}
	}

	/**
	 * Test a key that can't be in the signee.
	 */
	public void test04_notAddedPubicKey() {
		assertNull("rgSignee should not know this publicKey",
				rgSignee.getRemotePublicKey(PMPComponentType.PMP,
						APP_IDENTIFIER));
	}

	/**
	 * Test a successful sign.
	 */
	public void test05_successingSign() {
		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());

		assertTrue(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, RG_IDENTIFIER.getBytes(), signature));
	}

	/**
	 * Test a Sign with different sign contents.
	 */
	public void test06_failingSign() {
		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());

		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, APP_IDENTIFIER.getBytes(), signature));
	}

	/**
	 * Test if the sign fails immediately when the public key is removed.
	 */
	public void test07_removePublicKey() {
		rgSignee.removeRemotePublicKey(PMPComponentType.APP, APP_IDENTIFIER);

		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());

		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, RG_IDENTIFIER.getBytes(), signature));
	}

	/**
	 * Test some unlikely usages of the {@link PMPSignee}.
	 */
	public void test08_unlikelyUsage() {
		/* try empty byte arrays and null, should always fail */
		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, new byte[] {}, null));

		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, null, new byte[] {}));

		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, null, null));

		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, new byte[] {}, new byte[] {}));

		/* Set remotePublicKey to null */
		try {
			rgSignee.setRemotePublicKey(PMPComponentType.APP, APP_IDENTIFIER, null);
			fail("No exception thrown");
		} catch (NullPointerException npe) {			
		}

		/* Set remotePublicKey to a empty byte arrays */
		rgSignee.setRemotePublicKey(PMPComponentType.APP, APP_IDENTIFIER,
				new byte[] {});

		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());

		assertFalse(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, RG_IDENTIFIER.getBytes(), signature));
	}

	/**
	 * Prepares the TestCase (PMPSignees are saved and loaded again).
	 */
	public void prepareTestCase() {
		appSignee.save();
		appSignee.load();
		
		rgSignee.save();
		rgSignee.load();
	}
}
