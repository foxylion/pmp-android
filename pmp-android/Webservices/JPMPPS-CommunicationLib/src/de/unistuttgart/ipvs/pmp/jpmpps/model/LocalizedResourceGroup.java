package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.io.Serializable;


/**
 * A localized version of the {@link ResourceGroup}, saves memory and bandwidth
 * during transmission.
 * 
 * @author Jakob Jarosch
 * 
 */
public class LocalizedResourceGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String identifier;
	private long revision;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}
}
