package de.unistuttgart.ipvs.pmp.app;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable {

	public App() {
		
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
		
	}

	/**
	 * {@link App#writeToParcel(Parcel, int)} is called when the App Object is
	 * sent through an {@link IBinder}. Therefore all data of the object have to
	 * be written into the {@link Parcel}.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// Nothing to write yet.
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
