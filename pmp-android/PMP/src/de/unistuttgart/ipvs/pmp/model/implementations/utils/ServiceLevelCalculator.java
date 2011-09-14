package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.HashMap;
import java.util.Map;

import android.os.RemoteException;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Calculates the appropriate service level.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ServiceLevelCalculator {

    /**
     * Separator between RG and PL in
     * {@link ServiceLevelCalculator#getUniquePLIdentifier(IPrivacyLevel)}.
     */
    private static final String RG_PL_SEPARATOR = "::";

    private IApp app;

    public ServiceLevelCalculator(IApp app) {
	this.app = app;
    }

    /**
     * Finds the best values of the presets of the app.
     * 
     * @return a map from
     *         {@link ServiceLevelCalculator#getUniquePLIdentifier(IPrivacyLevel)}
     *         to the best value assigned
     * @throws RemoteException 
     */
    private Map<String, String> getBestValues() throws RemoteException {
	Map<String, String> bestValues = new HashMap<String, String>();

	// of all presets
	for (IPreset preset : app.getAssignedPresets()) {
	    // with every value
	    for (IPrivacyLevel value : preset.getUsedPrivacyLevels()) {
		// update the best one
		String currentBest = bestValues
			.get(getUniquePLIdentifier(value));

		if (currentBest == null) {
		    // set for the first time
		    bestValues.put(getUniquePLIdentifier(value),
			    value.getValue());
		} else {
		    // check which one is equal or better
		    if (value.satisfies(currentBest, value.getValue())) {
			// value >= currentBest
			bestValues.put(getUniquePLIdentifier(value),
				value.getValue());
		    }
		}
	    } /* for privacy levels */
	} /* for presets */

	return bestValues;
    }

    /**
     * 
     * @param privacyLevel
     *            the privacy level
     * @return a String uniquely identifying this privacy level in the system
     */
    private String getUniquePLIdentifier(IPrivacyLevel privacyLevel) {
	return privacyLevel.getResourceGroup().getIdentifier()
		+ RG_PL_SEPARATOR + privacyLevel.getIdentifier();
    }

    public int calculate() throws RemoteException {
	// we first need to know which are the best privacy levels availabel
	Map<String, String> bestValues = getBestValues();

	IServiceLevel[] appSL = app.getServiceLevels();

	// try the best first, then advance to the worst
	for (int serviceLevel = appSL.length - 1; serviceLevel > 0; serviceLevel--) {

	    // we now try to confirm that all privacy levels are satisfied
	    IServiceLevel testSL = appSL[serviceLevel];
	    boolean confirmed = true;
	    for (IPrivacyLevel toConfirmPL : testSL.getPrivacyLevels()) {
		if (!toConfirmPL.satisfies(toConfirmPL.getValue(),
			bestValues.get(getUniquePLIdentifier(toConfirmPL)))) {
		    confirmed = false;
		    continue;
		}
	    }

	    // if we we're successful, choose this one
	    if (confirmed) {
		return serviceLevel;
	    }
	} /* for service levels */

	// none found, default to zero
	return 0;
    }

}
