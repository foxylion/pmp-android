/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser;

/**
 * Thrown if a PMP registration activity is already found at the
 * AndroidManifest.xml
 * 
 * @author Thorsten Berberich
 * 
 */
public class PMPActivityAlreadyExistsException extends Exception {

    /**
     * Auto generated serial
     */
    private static final long serialVersionUID = -5744810497729968292L;

    /**
     * Constructor to send a message
     * 
     * @param msg
     *            message to display
     */
    public PMPActivityAlreadyExistsException(String msg) {
	super(msg);
    }
}
