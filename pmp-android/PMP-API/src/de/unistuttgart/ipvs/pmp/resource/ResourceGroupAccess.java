package de.unistuttgart.ipvs.pmp.resource;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;

/**
 * A App representation for the {@link IResourceGroupServicePMP}.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupAccess implements Parcelable {

    private ResourceGroupAccessHeader header;
    private Bundle privacyLevelValues;

    /**
     * Creates a new {@link ResourceGroupAccess}.
     * 
     * @param header
     *            header of this access set.
     * @param privacyLevelValues
     *            Bundle of privacy levels and their set values
     */
    public ResourceGroupAccess(ResourceGroupAccessHeader header,
	    Bundle privacyLevelValues) {
	this.header = header;
	this.privacyLevelValues = privacyLevelValues;
    }

    public ResourceGroupAccessHeader getHeader() {
	return this.header;
    }

    /**
     * Returns the corresponding value to a privacy level.
     * 
     * @param privacyLevel
     *            name of the privacy level
     * @return value of the privacy level or NULL if it is not set
     */
    public String getPrivacyLevelValue(String privacyLevel) {
	return privacyLevelValues.getString(privacyLevel);
    }

    /**
     * @return the bundle containing the privacy level values.
     */
    public Bundle getPrivacyLevelValues() {
	return privacyLevelValues;
    }

    /**
     * Constructor for regenerating Java object of an parcel from this object.
     * Normally called by {@link Parcelable.Creator#createFromParcel(Parcel)} of
     * the {@link AppInformationSet#CREATOR} variable.
     * 
     * @param source
     *            Parcel-Source
     */
    private ResourceGroupAccess(Parcel source) {
	this.header = source.readParcelable(null);
	this.privacyLevelValues = source.readBundle();
    }

    /**
     * {@link AppInformationSet#writeToParcel(Parcel, int)} is called when the
     * App Object is sent through an {@link IBinder}. Therefore all data of the
     * object have to be written into the {@link Parcel}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeParcelable(this.header, flags);
	dest.writeBundle(privacyLevelValues);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<ResourceGroupAccess> CREATOR = new Parcelable.Creator<ResourceGroupAccess>() {

	@Override
	public ResourceGroupAccess createFromParcel(Parcel source) {
	    return new ResourceGroupAccess(source);
	}

	@Override
	public ResourceGroupAccess[] newArray(int size) {
	    return new ResourceGroupAccess[size];
	}
    };
}
