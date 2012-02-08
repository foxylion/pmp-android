/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser;

/**
 * Thrown whenever the user wants to add the service, where PMP connects to, to
 * the AndroidManifest.xml but the service already exists
 * 
 * @author Thorsten Berberich
 * 
 */
public class PMPServiceAlreadyExists extends Exception {

    /**
     * Auto generated serial
     */
    private static final long serialVersionUID = -8944727079675898809L;

    /**
     * Constructor to send a message
     * 
     * @param msg
     *            message to display
     */
    public PMPServiceAlreadyExists(String msg) {
	super(msg);
    }

}
