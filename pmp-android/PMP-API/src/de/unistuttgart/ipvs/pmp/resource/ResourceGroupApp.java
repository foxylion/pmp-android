package de.unistuttgart.ipvs.pmp.resource;

import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A App representation for the {@link IResourceGroupServicePMP}.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupApp implements Parcelable {

    private String identifier;
    private String publicKey;
    private Bundle privacyLevels;

    /**
     * Creates a new {@link ResourceGroupApp}.
     * 
     * @param identifier
     *            Identifier of the App
     * @param publicKey
     *            public key of the App
     * @param privacyLevels
     *            Bundle of privacy levels and their set values
     */
    public ResourceGroupApp(String identifier, String publicKey,
	    Bundle privacyLevels) {
	this.identifier = identifier;
	this.publicKey = publicKey;
	this.privacyLevels = privacyLevels;
    }

    /**
     * @return Returns the identifier of the App.
     */
    public String getIdentifier() {
	return identifier;
    }

    /**
     * @return Returns the public key of the App.
     */
    public String getPublicKey() {
	return publicKey;
    }

    /**
     * Returns the corresponding value to a privacy level.
     * 
     * @param privacyLevel
     *            name of the privacy level
     * @return value of the privacy level or NULL if it is not set
     */
    public String getPrivacyLevel(String privacyLevel) {
	return privacyLevels.getString(privacyLevel);
    }

    /**
     * Constructor for regenerating Java object of an parcel from this object.
     * Normally called by {@link Parcelable.Creator#createFromParcel(Parcel)} of
     * the {@link AppPrivacyLevel#CREATOR} variable.
     * 
     * @param source
     *            Parcel-Source
     */
    private ResourceGroupApp(Parcel source) {
	this.identifier = source.readString();
	this.publicKey = source.readString();
	this.privacyLevels = source.readBundle();
    }

    /**
     * {@link AppPrivacyLevel#writeToParcel(Parcel, int)} is called when the App
     * Object is sent through an {@link IBinder}. Therefore all data of the
     * object have to be written into the {@link Parcel}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(identifier);
	dest.writeString(publicKey);
	dest.writeBundle(privacyLevels);
    }

    @Override
    public int describeContents() {
	return this.hashCode();
    }

    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<ResourceGroupApp> CREATOR = new Parcelable.Creator<ResourceGroupApp>() {

	@Override
	public ResourceGroupApp createFromParcel(Parcel source) {
	    return new ResourceGroupApp(source);
	}

	@Override
	public ResourceGroupApp[] newArray(int size) {
	    return new ResourceGroupApp[size];
	}
    };
}
