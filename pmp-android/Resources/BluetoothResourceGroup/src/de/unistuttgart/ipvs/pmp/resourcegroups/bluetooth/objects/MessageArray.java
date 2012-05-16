package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageArray implements Serializable {

	List<String> messages;

	public MessageArray(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getMessages(){
		return this.messages;
	}
}
