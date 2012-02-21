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

    static {
	ISharedImages si = PlatformUI.getWorkbench().getSharedImages();
	INFO16 = si.getImage(ISharedImages.IMG_OBJS_INFO_TSK);
	ERROR16 = si.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
	FOLDER16 = si.getImage(ISharedImages.IMG_OBJ_FOLDER);
	FILE16 = si.getImage(ISharedImages.IMG_OBJ_FILE);

	FieldDecorationRegistry fdr = FieldDecorationRegistry.getDefault();
	ERROR_DEC = fdr.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
		.getImage();
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
    public static ImageDescriptor getImageDescriptor(String path,
	    String iconName) {
	Bundle bundle = Platform.getBundle("de.unistuttgart.ipvs.pmp.editor");
	URL fullPathString = BundleUtility.find(bundle, path + "/" + iconName);
	return ImageDescriptor.createFromURL(fullPathString);
    }

}
