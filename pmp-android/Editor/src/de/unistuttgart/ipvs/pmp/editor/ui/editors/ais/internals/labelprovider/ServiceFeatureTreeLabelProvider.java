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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.labelprovider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * Provides the labels for the Service Feature Tree
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureTreeLabelProvider extends LabelProvider {

    @Override
    public String getText(Object input) {
	if (input instanceof AISServiceFeature) {
	    return ((AISServiceFeature) input).getIdentifier();
	}

	if (input instanceof AISRequiredResourceGroup) {
	    return ((AISRequiredResourceGroup) input).getIdentifier();
	}

	return (String) input;
    }

    @Override
    public Image getImage(Object element) {
	// Check for errors and return the correct image
	if (element instanceof AISServiceFeature) {
	    AISServiceFeature sf =(AISServiceFeature) element;
	    if (!sf.getIssues().isEmpty()){
		return Images.ERROR16;
	    }
	}

	if (element instanceof AISRequiredResourceGroup) {
	    AISRequiredResourceGroup rg = (AISRequiredResourceGroup) element;
	    if (!rg.getIssues().isEmpty()){
		return Images.ERROR16;
	    }
	}
	
	return null;
    }
}
