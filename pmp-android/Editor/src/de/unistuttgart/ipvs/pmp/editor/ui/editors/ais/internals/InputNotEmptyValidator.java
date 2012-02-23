/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals;

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
