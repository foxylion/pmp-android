package de.unistuttgart.ipvs.pmp.model.interfaces;

import android.os.RemoteException;

/**
 * The {@link IPrivacyLevel} interface represents a PrivacyLevel of a
 * {@link IResourceGroup}. It can be fetched from an {@link IServiceLevel},
 * {@link IPreset} or {@link IResourceGroup}.<br>
 * 
 * If it is fetch from {@link IPreset} or {@link IServiceLevel} the
 * {@link IPrivacyLevel#getValue()} will return the set value for this
 * {@link IPrivacyLevel}.<br>
 * 
 * The {@link IPrivacyLevel} from the {@link IResourceGroup} is only a
 * representation of an {@link IPrivacyLevel} assigned to this
 * {@link IResourceGroup} and has not its own value, so
 * {@link IPrivacyLevel#getValue()} will always return NULL.
 * 
 * @author Jakob Jarosch
 */
public interface IPrivacyLevel {

    /**
     * @return Returns the {@link IResourceGroup} wide unique identifier of the
     *         {@link IPrivacyLevel}.
     */
    public String getIdentifier();

    /**
     * @return Returns the parent of the {@link IPrivacyLevel} (a
     *         {@link IResourceGroup}).
     */
    public IResourceGroup getResourceGroup();

    /**
     * @return Returns the localized name of the {@link IPrivacyLevel}.
     */
    public String getName();

    /**
     * @return Returns the localized description of the {@link IPrivacyLevel}.
     */
    public String getDescription();

    /**
     * Returns the set value of the {@link IPrivacyLevel}.
     * 
     * @return Returns the set value, NULL if it is fetched from an
     *         {@link IResourceGroup} instance, see {@link IPrivacyLevel}
     *         JavaDoc comment for more details.
     */
    public String getValue();

    /**
     * Returns an human readable representation of the a given value.<br>
     * <b>This method uses service communication and is slow, use with
     * caution.</b>
     * 
     * <p>
     * <b>This method is uses blocking access to other Services, you must call
     * this method in another Thread than the Main-Worker-Thread! Otherwise you
     * will end in a deadlock</b>
     * </p>
     * 
     * @param value
     *            Value which should be returned as a human readable
     *            representation.
     * @return the human readable representation of the given value.
     * 
     * @throws RemoteException
     *             Is thrown when the connection to the {@link IResourceGroup}s
     *             service can not be correctly established.
     */
    public String getHumanReadableValue(String value) throws RemoteException;

    /**
     * Checks is a given value satisfies the given reference, in general it
     * means value is better than or equals reference.<br>
     * <b>This method uses service communication and is slow, use with
     * caution.</b>
     * 
     * <p>
     * <b>This method is uses blocking access to other Services, you must call
     * this method in another Thread than the Main-Worker-Thread! Otherwise you
     * will end in a deadlock</b>
     * </p>
     * 
     * @param reference
     *            The reference value which should be used to check
     *            satisfaction.
     * @param value
     *            The value which should be checked against the reference value.
     * @return True when value satisfies reference, otherwise false.
     * @throws RemoteException
     *             Is thrown when the connection to the {@link IResourceGroup}s
     *             service can not be correctly established.
     */
    public boolean satisfies(String reference, String value)
	    throws RemoteException;
}
