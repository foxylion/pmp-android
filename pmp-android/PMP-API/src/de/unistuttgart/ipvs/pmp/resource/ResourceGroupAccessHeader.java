package de.unistuttgart.ipvs.pmp.resource;

import android.os.Parcel;
import android.os.Parcelable;

public class ResourceGroupAccessHeader implements Parcelable {

    private String identifier;
    private byte[] publicKey;

    /**
     * Creates a new {@link ResourceGroupAccessHeader}.
     * 
     * @param identifier
     *            identifier of the app
     * @param publicKey
     *            public key of the app
     */
    public ResourceGroupAccessHeader(String identifier, byte[] publicKey) {
	this.identifier = identifier;
	this.publicKey = publicKey.clone();
    }

    private ResourceGroupAccessHeader(Parcel parcel) {
	this.identifier = parcel.readString();
	this.publicKey = null;
	parcel.readByteArray(this.publicKey);
    }

    public String getIdentifier() {
	return this.identifier;
    }

    public byte[] getPublicKey() {
	return this.publicKey.clone();
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(identifier);
	dest.writeByteArray(publicKey);
    }

    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<ResourceGroupAccessHeader> CREATOR = new Parcelable.Creator<ResourceGroupAccessHeader>() {

	@Override
	public ResourceGroupAccessHeader createFromParcel(Parcel source) {
	    return new ResourceGroupAccessHeader(source);
	}

	@Override
	public ResourceGroupAccessHeader[] newArray(int size) {
	    return new ResourceGroupAccessHeader[size];
	}

    };

}
