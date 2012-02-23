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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;

/**
 * Provides the Tree for the Service Features with informations out of the model
 * The Tree will look like this: - ServiceFeatureName1 -requiredRGIdentifier1
 * -requiredRGIdentifier1 - ServiceFeatureName2 -requiredRGIdentifier1
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureTreeProvider implements ITreeContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
     * .viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
     * Object)
     */
    @Override
    public Object[] getChildren(Object parent) {
	// If it's an AIS then return the ServiceFeatures of this AIS
	if (parent instanceof AIS) {
	    return ((AIS) parent).getServiceFeatures().toArray();
	}

	// If it's a service feature return the Required Rescource Groups
	if (parent instanceof AISServiceFeature) {
	    return ((AISServiceFeature) parent).getRequiredResourceGroups()
		    .toArray();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.
     * Object)
     */
    @Override
    public Object[] getElements(Object input) {
	// AIS elemtents are the Service Features
	if (input instanceof AIS) {
	    return ((AIS) input).getServiceFeatures().toArray();
	}

	// Elements of the Service Features are the Required Resource Groups
	if (input instanceof AISServiceFeature) {
	    return ((AISServiceFeature) input).getRequiredResourceGroups()
		    .toArray();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
     * )
     */
    @Override
    public Object getParent(Object parent) {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
     * Object)
     */
    @Override
    public boolean hasChildren(Object input) {
	if (input instanceof AIS) {
	    return !((AIS) input).getServiceFeatures().isEmpty();
	}

	if (input instanceof AISServiceFeature) {
	    return !((AISServiceFeature) input).getRequiredResourceGroups()
		    .isEmpty();
	}

	// Resource Groups has no children, they will be display in a table
	if (input instanceof AISRequiredResourceGroup) {
	    return false;
	}

	return false;
    }

}
