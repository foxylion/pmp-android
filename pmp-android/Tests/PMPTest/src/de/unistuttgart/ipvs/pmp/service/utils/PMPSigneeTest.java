package de.unistuttgart.ipvs.pmp.service.utils;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import android.test.AndroidTestCase;

public class PMPSigneeTest extends AndroidTestCase {

	private static final String APP_IDENTIFIER = "my.app";
	private static final String RG_IDENTIFIER = "my.rg";

	private PMPSignee appSignee = null;
	private PMPSignee rgSignee = null;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		appSignee = new PMPSignee(PMPComponentType.APP, APP_IDENTIFIER,
				getContext());
		rgSignee = new PMPSignee(PMPComponentType.RESOURCE_GROUP,
				RG_IDENTIFIER, getContext());

		/* reset the PMPSignee if maybe created in a previous run */
		appSignee.clearRemotePublicKeys();
		rgSignee.clearRemotePublicKeys();
	}

	/**
	 * Test the correct {@link PMPComponentType} and the correct identifier set
	 * by setUp-method.
	 */
	public void testCorrectTypeAndIdentifier() {
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
	public void testFailingSign() {
		prepareTestCase();

		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());
		assertFalse(rgSignee.isSignatureValid(appSignee.getType(),
				APP_IDENTIFIER, RG_IDENTIFIER.getBytes(), signature));
	}

	/**
	 * The if the publicKey can be correctly added to a signee.
	 */
	public void testPublicKeyAdd() {
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
	}

	/**
	 * Test a key that can't be in the signee.
	 */
	public void testNotAddedPubicKey() {
		assertNull("rgSignee should not know this publicKey",
				rgSignee.getRemotePublicKey(PMPComponentType.PMP,
						APP_IDENTIFIER));
	}

	/**
	 * Test a successful sign.
	 */
	public void testSuccessingSign() {
		byte[] signature = appSignee.signContent(RG_IDENTIFIER.getBytes());

		assertTrue(rgSignee.isSignatureValid(PMPComponentType.APP,
				APP_IDENTIFIER, RG_IDENTIFIER.getBytes(), signature));
	}

	public void testRemovedPublicKey() {

	}

	public void prepareTestCase() {
		appSignee.save();
		appSignee.load();
	}
}
