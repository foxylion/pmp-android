package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This class provides image and icons used all over this plugin
 * List of available icons: http://vorlesungsfrei.de/kai/index.php?location=eclipse&lang=ger
 * @author Patrick Strobel
 *
 */
public class Images {
	
	public static final Image INFO16;
	public static final Image ERROR16;
	public static final Image FOLDER16;
	public static final Image FILE16;
	
	static {
		ISharedImages si = PlatformUI.getWorkbench().getSharedImages();
		INFO16 = si.getImage(ISharedImages.IMG_OBJS_INFO_TSK);
		ERROR16 = si.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		FOLDER16 = si.getImage(ISharedImages.IMG_OBJ_FOLDER);
		FILE16 = si.getImage(ISharedImages.IMG_OBJ_FILE);
	}

}
