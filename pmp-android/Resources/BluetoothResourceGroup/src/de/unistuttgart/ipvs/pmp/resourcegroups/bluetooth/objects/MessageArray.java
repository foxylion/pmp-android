package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

/**
 * {@link MessageArray} holds the Messages send to the caller
 * 
 * @author Alexander Wassiljew
 * 
 */
public class MessageArray implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1670918836332795158L;
	/**
	 * List of messages
	 */
	List<String> messages;

	/**
	 * Constructor
	 * 
	 * @param messages
	 */
	public MessageArray(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * Returns the messages
	 * 
	 * @return
	 */
	public List<String> getMessages() {
		return this.messages;
	}
}
