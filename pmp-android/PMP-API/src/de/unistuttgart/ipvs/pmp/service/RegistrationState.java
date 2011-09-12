package de.unistuttgart.ipvs.pmp.service;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;

/**
 * A RegistrationState representation for the registration at
 * {@link IPMPServiceRegistration}.
 * 
 * @author Jakob Jarosch
 */
public class RegistrationState implements Parcelable {

    private boolean state;
    private String message;

    /**
     * @see RegistrationState#RegistrationState(boolean, String)
     */
    public RegistrationState(boolean state) {
	this.state = state;
    }

    /**
     * Creates a new {@link RegistrationState} object, with given values.
     * 
     * @param state
     *            The state of the registration, true means successful, false
     *            means registration failed.
     * @param message
     *            A message which describes what exactly is the reason for a
     *            failed registration.
     */
    public RegistrationState(boolean state, String message) {
	this.state = state;
	this.message = message;
    }

    /**
     * @return Returns the state of the registration, true means successful,
     *         false means registration failed.
     */
    public boolean getState() {
	return state;
    }

    /**
     * @return Returns a message which describes what exactly is the reason for
     *         a failed registration.
     */
    public String getMessage() {
	return message;
    }

    /**
     * Constructor for regenerating Java object of an parcel from this object.
     * Normally called by {@link Parcelable.Creator#createFromParcel(Parcel)} of
     * the {@link RegistrationState#CREATOR} variable.
     * 
     * @param source
     *            Parcel-Source
     */
    private RegistrationState(Parcel source) {
	boolean[] bools = new boolean[1];
	source.readBooleanArray(bools);
	this.state = bools[0];

	this.message = source.readString();
    }

    /**
     * {@link RegistrationState#writeToParcel(Parcel, int)} is called when the
     * App Object is sent through an {@link IBinder}. Therefore all data of the
     * object have to be written into the {@link Parcel}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeBooleanArray(new boolean[] { state });
	dest.writeString(message);
    }

    @Override
    public int describeContents() {
	return this.hashCode();
    }

    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<RegistrationState> CREATOR = new Parcelable.Creator<RegistrationState>() {

	@Override
	public RegistrationState createFromParcel(Parcel source) {
	    return new RegistrationState(source);
	}

	@Override
	public RegistrationState[] newArray(int size) {
	    return new RegistrationState[size];
	}
    };
}
