package de.unistuttgart.ipvs.pmp.service;

import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;

/**
 * Implementation of the {@link IPMPServiceApp.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceAppStubImpl extends IPMPServiceApp.Stub {

    private String identifier = null;

    public PMPServiceAppStubImpl(String identifier) {
	this.identifier = identifier;
    }
}