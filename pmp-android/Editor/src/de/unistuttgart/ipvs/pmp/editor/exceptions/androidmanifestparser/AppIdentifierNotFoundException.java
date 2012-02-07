/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser;

/**
 * Thrown whenever the app identifier is not found at the AndroidManifest.xml
 * 
 * @author Thorsten Berberich
 * 
 */
public class AppIdentifierNotFoundException extends Exception {

    /**
     * Auto generated serial
     */
    private static final long serialVersionUID = 5121971098708108147L;

    /**
     * Constructor to send a message
     * 
     * @param msg
     *            message to display
     */
    public AppIdentifierNotFoundException(String msg) {
	super(msg);
    }
}
