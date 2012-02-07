/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser;

/**
 * Thrown when the user wants to add the PMP registration activity to the
 * AndroidManifest.xml, but there is no main activity declared in the
 * AndroidMainfest.xml
 * 
 * @author Thorsten Berberich
 * 
 */
public class NoMainActivityException extends Exception {

    /**
     * Auto generated serial
     */
    private static final long serialVersionUID = 3621590944193678970L;

    /**
     * The message to display
     * 
     * @param message
     */
    public NoMainActivityException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

}
