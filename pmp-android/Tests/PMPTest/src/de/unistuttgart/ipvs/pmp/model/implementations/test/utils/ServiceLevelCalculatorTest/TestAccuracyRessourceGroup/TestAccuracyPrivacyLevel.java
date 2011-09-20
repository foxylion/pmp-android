package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup;

import java.util.Comparator;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.LocalizedDefaultPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;

public class TestAccuracyPrivacyLevel implements IPrivacyLevel {

	/**
	 * Bridge to RG-PrivacyLevel
	 */
	private PrivacyLevel<Integer> bridge;

	/**
	 * Actual value
	 */
	private String value;

	public TestAccuracyPrivacyLevel() {
		bridge = new LocalizedDefaultPrivacyLevel<Integer>("Accuracy",
				"Accuracy in meters", new Comparator<Integer>() {

					@Override
					public int compare(Integer object1, Integer object2) {
						// more is better
						return (object1 - object2);
					}
				}) {

			@Override
			public Integer parseValue(String value)
					throws PrivacyLevelValueException {
				try {
					return Integer.valueOf(value);
				} catch (NumberFormatException nfe) {
					throw new PrivacyLevelValueException();
				}
			}

		};
	}
	
	public TestAccuracyPrivacyLevel(String initValue) {
		this();
		setValue(initValue);
	}

	@Override
	public String getIdentifier() {
		return "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel";
	}

	@Override
	public IResourceGroup getResourceGroup() {
		return new TestAccuracyResourceGroup();
	}

	@Override
	public String getName() {
		return bridge.getName(Constants.DEFAULT_LOCALE);
	}

	@Override
	public String getDescription() {
		return bridge.getDescription(Constants.DEFAULT_LOCALE);
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getHumanReadableValue(String value) throws RemoteException {
		try {
			return bridge
					.getHumanReadableValue(Constants.DEFAULT_LOCALE, value)
					+ " m";
		} catch (PrivacyLevelValueException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@Override
	public boolean permits(String reference, String value)
			throws RemoteException {
		try {
			return bridge.permits(value, reference);
		} catch (PrivacyLevelValueException e) {
			e.printStackTrace();
			return false;
		}
	}

}
