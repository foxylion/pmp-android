/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser;

/**
 * Thrown if there are more than one application node at the AndroidManifest.xml
 * 
 * @author Thorsten Berberich
 * 
 */
public class AndroidApplicationException extends Exception {

    /**
     * Auto generated serial
     */
    private static final long serialVersionUID = 3962159094569678970L;

    /**
     *	Constructor to send a message
     * 
     * @param msg
     *            message to display
     */
    public AndroidApplicationException(String msg) {
	super(msg);
    }
}
