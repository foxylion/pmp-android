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
    /**
     * Name of the field
     */
    String field;

    /**
     * Constructs the validator
     * 
     * @param field
     *            name of your field that could not be empty
     */
    public InputNotEmptyValidator(String field) {
	this.field = field;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
     */
    @Override
    public String isValid(String entered) {
	if (entered == null) {
	    return field + " could not be empty";
	}

	if (entered.equals("")) {
	    return field + " could not be empty";
	}

	// Everthing ok
	return null;
    }

}
