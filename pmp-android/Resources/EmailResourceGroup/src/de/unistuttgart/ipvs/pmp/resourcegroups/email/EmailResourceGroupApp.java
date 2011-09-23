package de.unistuttgart.ipvs.pmp.resourcegroups.email;

import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

public class EmailResourceGroupApp extends ResourceGroupSingleApp<EmailResourceGroup> {

	@Override
	protected EmailResourceGroup createResourceGroup() {
		return new EmailResourceGroup(getApplicationContext());
	}

}
