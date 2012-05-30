package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
/**
 *  {@link MessageArray} holds the Messages send to the caller
 * @author Alexander Wassiljew
 *
 */
public class MessageArray implements Serializable {
	/**
	 * List of messages
	 */
	List<String> messages;
	/**
	 * Constructor 
	 * @param messages
	 */
	public MessageArray(List<String> messages) {
		this.messages = messages;
	}
	/**
	 * Returns the messages
	 * @return
	 */
	public List<String> getMessages(){
		return this.messages;
	}
}
