package de.unistuttgart.ipvs.pmp.service;


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