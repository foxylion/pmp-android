package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.viewers.LabelProvider;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
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

	if (input instanceof AISRequiredPrivacySetting) {
	    return ((AISRequiredPrivacySetting) input).getIdentifier() + " : "
		    + ((AISRequiredPrivacySetting) input).getValue();
	}

	return (String) input;
    }
}
