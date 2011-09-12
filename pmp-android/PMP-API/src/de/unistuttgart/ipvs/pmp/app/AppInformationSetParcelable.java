package de.unistuttgart.ipvs.pmp.app;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.service.app.IAppService;

/**
 * A PrivacyLevel representation for the {@link IAppService}.
 * 
 * @author Jakob Jarosch
 */
public class AppInformationSetParcelable implements Parcelable {

    private AppInformationSet set;

    /**
     * Creates a new {@link AppInformationSet}.
     * 
     * @param resourceGroup
     *            identifier of the resource group
     * @param privacyLevel
     *            identifier of the privacy level
     * @param value
     *            set value of the privacy level
     */
    public AppInformationSetParcelable(AppInformationSet set) {
	this.set = set;
    }

    /**
     * @return Returns the {@link AppInformationSet}
     */
    public AppInformationSet getAppInformationSet() {
	return set;
    }

    /**
     * Constructor for regenerating Java object of an parcel from this object.
     * Normally called by {@link Parcelable.Creator#createFromParcel(Parcel)} of
     * the {@link AppInformationSetParcelable#CREATOR} variable.
     * 
     * @param source
     *            Parcel-Source
     */
    private AppInformationSetParcelable(Parcel source) {
	this.set = (AppInformationSet) source.readSerializable();
    }

    /**
     * {@link AppInformationSet#writeToParcel(Parcel, int)} is called when the
     * App Object is sent through an {@link IBinder}. Therefore all data of the
     * object have to be written into the {@link Parcel}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeSerializable(set);
    }

    @Override
    public int describeContents() {
	return this.hashCode();
    }

    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<AppInformationSetParcelable> CREATOR = new Parcelable.Creator<AppInformationSetParcelable>() {

	@Override
	public AppInformationSetParcelable createFromParcel(Parcel source) {
	    return new AppInformationSetParcelable(source);
	}

	@Override
	public AppInformationSetParcelable[] newArray(int size) {
	    return new AppInformationSetParcelable[size];
	}
    };
}
