package de.unistuttgart.ipvs.pmp.app;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Currently unused</b>, but a sample implementation of a {@link Parcelable}.
 * 
 * @author Jakob Jarosch
 */
public class App implements Parcelable {

	private String identifier;
	private String url;
	
	public App(String identifier, String url) {
		this.identifier = identifier;
		this.url = url;
	}
	
	/**
	 * Constructor for regenerating Java object of an parcel from this object.
	 * Normally called by {@link Parcelable.Creator#createFromParcel(Parcel)} of
	 * the {@link App#CREATOR} variable.
	 * 
	 * @param source
	 *            Parcel-Source
	 */
	private App(Parcel source) {
		this.identifier = source.readString();
		this.url = source.readString();
	}

	/**
	 * {@link App#writeToParcel(Parcel, int)} is called when the App Object is
	 * sent through an {@link IBinder}. Therefore all data of the object have to
	 * be written into the {@link Parcel}.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(identifier);
		dest.writeString(url);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	/**
	 * Required Creator for the {@link Parcelable} regeneration.
	 */
	public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {

		@Override
		public App createFromParcel(Parcel source) {
			return new App(source);
		}

		@Override
		public App[] newArray(int size) {
			return new App[size];
		}
	};
}
