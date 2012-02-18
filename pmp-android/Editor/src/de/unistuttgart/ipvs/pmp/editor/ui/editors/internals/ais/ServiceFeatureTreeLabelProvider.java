package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.viewers.LabelProvider;
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

    // @Override
    // public Image getImage(Object element) {
    // // Check for errors and return the correct image
    // }
}
