package de.unistuttgart.ipvs.pmp.service;

import de.unistuttgart.ipvs.pmp.app.App;

interface IPMPService {

	boolean registerApp(in App app);

	boolean registerResourceGroup(String resourceGroup);

	void savePrivacyLevel(String app, String resourceGroup,	String privacyLevel, String value);
}