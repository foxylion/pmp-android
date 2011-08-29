package de.unistuttgart.ipvs.pmp.app;

import de.unistuttgart.ipvs.pmp.service.IAppService;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A PrivacyLevel representation for the {@link IAppService}.
 * 
 * @author Jakob Jarosch
 */
public class AppPrivacyLevel implements Parcelable {

	private String resourceGroup;
	private String privacyLevel;
	private String value;

	/**
	 * Creates a new {@link AppPrivacyLevel}.
	 * 
	 * @param resourceGroup identifier of the resource group
	 * @param privacyLevel identifier of the privacy level
	 * @param value set value of the privacy level
	 */
	public AppPrivacyLevel(String resourceGroup, String privacyLevel,
			String value) {
		this.resourceGroup = resourceGroup;
		this.privacyLevel = privacyLevel;
		this.value = value;
	}
	
	/**
	 * @return Returns the identifier of the resource group.
	 */
	public String getResourceGroup() {
		return resourceGroup;
	}
	
	/**
	 * @return Returns the identifier of the privacy level.
	 */
	public String getPrivacyLevel() {
		return privacyLevel;
	}
	
	/**
	 * @return Returns the set value of the privacy level.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Constructor for regenerating Java object of an parcel from this object.
	 * Normally called by {@link Parcelable.Creator#createFromParcel(Parcel)} of
	 * the {@link AppPrivacyLevel#CREATOR} variable.
	 * 
	 * @param source
	 *            Parcel-Source
	 */
	private AppPrivacyLevel(Parcel source) {
		this.resourceGroup = source.readString();
		this.privacyLevel = source.readString();
		this.value = source.readString();
	}

	/**
	 * {@link AppPrivacyLevel#writeToParcel(Parcel, int)} is called when the App Object is
	 * sent through an {@link IBinder}. Therefore all data of the object have to
	 * be written into the {@link Parcel}.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(resourceGroup);
		dest.writeString(privacyLevel);
		dest.writeString(value);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	/**
	 * Required Creator for the {@link Parcelable} regeneration.
	 */
	public static final Parcelable.Creator<AppPrivacyLevel> CREATOR = new Parcelable.Creator<AppPrivacyLevel>() {

		@Override
		public AppPrivacyLevel createFromParcel(Parcel source) {
			return new AppPrivacyLevel(source);
		}

		@Override
		public AppPrivacyLevel[] newArray(int size) {
			return new AppPrivacyLevel[size];
		}
	};
}
