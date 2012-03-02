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
package de.unistuttgart.ipvs.pmp.editor.model;

import org.eclipse.ui.forms.editor.FormEditor;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.RgisEditor;

/**
 * Abstract class for the RGISModel and AISModel
 * 
 * @author Thorsten Berberich
 * 
 */
public abstract class AbstractModel {

    /**
     * Flag to represent if the model is dirty or not
     */
    private Boolean isDirty = false;

    /**
     * The {@link AisEditor} or {@link RgisEditor}
     */
    private FormEditor editor;

    /**
     * Sets an editor to fire changes
     * 
     * @param editor
     *            {@link FormEditor}
     */
    public void setEditor(FormEditor editor) {
	this.editor = editor;
    }

    /**
     * Sets the dirty status
     */
    public void setDirty(Boolean dirty) {
	this.isDirty = dirty;
	
	/*
	 * Try to cast the editor to an ais editor and fire the property changed
	 */
	try{
	    AisEditor aisEditor = (AisEditor) editor;
	    aisEditor.firePropertyChangedDirty();
	} catch (ClassCastException e){
	    
	}
	
	/*
	 * Try to cast the editor to an rgis editor and fire the property changed
	 */
	try{
	    RgisEditor rgisEditor = (RgisEditor) editor;
	    rgisEditor.firePropertyChangedDirty();
	} catch (ClassCastException e){
	    
	}
    }

    /**
     * Get the dirty status of the model
     * 
     * @return true if dirty, false otherwise
     */
    public Boolean isDirty() {
	return isDirty;
    }

}
