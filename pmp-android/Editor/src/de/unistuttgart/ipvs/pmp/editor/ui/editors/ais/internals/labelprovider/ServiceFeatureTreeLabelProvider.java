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
