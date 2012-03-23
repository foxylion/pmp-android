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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

/**
 * This class provides image and icons used all over this plugin List of
 * available icons:
 * http://vorlesungsfrei.de/kai/index.php?location=eclipse&lang=ger
 * 
 * @author Patrick Strobel
 * 
 */
public class Images {
    
    public static final Image INFO16;
    public static final Image ERROR16;
    public static final Image FOLDER16;
    public static final Image FILE16;
    public static final Image ERROR_DEC;
    public static final Image IMG_ELCL_SYNCED;
    public static final Image IMG_DEC_FIELD_ERROR;
    public static final Image IMG_OBJ_ADD;
    public static final Image IMG_ETOOL_DELETE;
    
    static {
        ISharedImages si = PlatformUI.getWorkbench().getSharedImages();
        INFO16 = si.getImage(ISharedImages.IMG_OBJS_INFO_TSK);
        ERROR16 = si.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
        FOLDER16 = si.getImage(ISharedImages.IMG_OBJ_FOLDER);
        FILE16 = si.getImage(ISharedImages.IMG_OBJ_FILE);
        IMG_ELCL_SYNCED = si.getImage(ISharedImages.IMG_ELCL_SYNCED);
        IMG_DEC_FIELD_ERROR = si.getImage(ISharedImages.IMG_DEC_FIELD_ERROR);
        IMG_OBJ_ADD = si.getImage(ISharedImages.IMG_OBJ_ADD);
        IMG_ETOOL_DELETE = si.getImage(ISharedImages.IMG_ETOOL_DELETE);
        
        FieldDecorationRegistry fdr = FieldDecorationRegistry.getDefault();
        ERROR_DEC = fdr.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
    }
    
    
    /**
     * Gets an {@link ImageDescriptor} to a path and icon name inside this
     * plugin
     * 
     * @param path
     *            path without "/" e.g. "icons"
     * @param iconName
     *            name of the icon with the file ending e.g. "sample.gif"
     * @return the {@link ImageDescriptor}
     */
    public static ImageDescriptor getImageDescriptor(String path, String iconName) {
        Bundle bundle = Platform.getBundle("de.unistuttgart.ipvs.pmp.editor"); //$NON-NLS-1$
        URL fullPathString = BundleUtility.find(bundle, path + "/" + iconName); //$NON-NLS-1$
        return ImageDescriptor.createFromURL(fullPathString);
    }
    
}
