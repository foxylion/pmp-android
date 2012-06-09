package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable Object of the {@link MessageArray}
 * 
 * @author Alexander Wassiljew
 * 
 */
public class MessageArrayParcelable implements Parcelable {
	/**
	 * {@link MessageArray}
	 */
	MessageArray messages;

	/**
	 * Constructor
	 * 
	 * @param messages
	 */
	public MessageArrayParcelable(MessageArray messages) {
		this.messages = messages;
	}

	/**
	 * Returns the messages
	 * 
	 * @return
	 */
	public MessageArray getDevices() {
		return this.messages;
	}

	/**
	 * Private Constructor
	 * 
	 * @param source
	 */
	private MessageArrayParcelable(Parcel source) {
		this.messages = (MessageArray) source.readSerializable();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(this.messages);
	}

	public static final Parcelable.Creator<MessageArrayParcelable> CREATOR = new Parcelable.Creator<MessageArrayParcelable>() {

		@Override
		public MessageArrayParcelable createFromParcel(Parcel source) {
			return new MessageArrayParcelable(source);
		}

		@Override
		public MessageArrayParcelable[] newArray(int size) {
			return new MessageArrayParcelable[size];
		}
	};
}
