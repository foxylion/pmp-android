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
	this.publicKey = publicKey;
    }
    
    private ResourceGroupAccessHeader(Parcel parcel) {
	this.identifier = parcel.readString();
	parcel.readByteArray(this.publicKey);
    }
    
    public String getIdentifier() {
	return this.identifier;
    }
    
    public byte[] getPublicKey() {
	return this.publicKey;
    }    
    
    @Override
    public boolean equals(Object o) {
	if (o instanceof ResourceGroupAccessHeader) {
	    ResourceGroupAccessHeader rgah = (ResourceGroupAccessHeader) o;
	    return (this.identifier.equals(rgah) && this.publicKey.equals(rgah.publicKey));
	}
        return false;
    }

    @Override
    public int describeContents() {
	return this.identifier.hashCode() + this.publicKey.hashCode();
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
