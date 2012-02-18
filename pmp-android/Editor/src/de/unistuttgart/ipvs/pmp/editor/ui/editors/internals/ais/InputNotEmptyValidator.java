package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;

/**
 * Checks if the entered service feature is not empty that was entered in the
 * {@link InputDialog}
 * 
 * @author Thorsten Berberich
 * 
 */
public class InputNotEmptyValidator implements IInputValidator {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
     */
    @Override
    public String isValid(String entered) {
	if (entered == null) {
	    return "Identifier could not be empty";
	}

	if (entered.equals("")) {
	    return "Identifier could not be empty";
	}

	// Everthing ok
	return null;
    }

}
