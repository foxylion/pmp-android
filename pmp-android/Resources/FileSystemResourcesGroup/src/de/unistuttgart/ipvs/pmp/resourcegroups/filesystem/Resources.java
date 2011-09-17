package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.ExternalFileAccess.Directories;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.ExternalFileAccessResource;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.GenericFileAccessResource;

/**
 * Defines and generates all resource used by the file system resource-group
 * @author Patrick Strobel
 * @version 0.2.0
 *
 */
public class Resources {
	
	/**
	 * Gives access to every file on the Android device including system files. Accessing all
	 * directories might require root-rights. This resource should only be used if it's
	 * special functions are required. For safety and security reasons this resource should not be used
	 * unless special functionality is required.
	 */
	
	public static final String GENERIC = "gen";

	/**
	 * Gives access to all files and directories in the external directory (typically the whole SD-Card)
	 * As this may affect other applications' data, using this resource is not recommended
	 */
	public static final String EXTERNAL_BASE_DIR = "ext_base_dir";
	
	/**
	 * Gives access to the external music directory (<code>Music/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_MUSIC = "ext_music";
	
	/**
	 * Gives access to the external podcasts directory (<code>Podcasts/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_PODCASTS = "ext_podcasts";
	
	/**
	 * Gives access to the external ringtones directory (<code>Ringtones/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_RINGTONES = "ext_ringones";
	
	/**
	 * Gives access to the external alarms directory (<code>Alarms/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_ALARMS = "ext_alarms";
	
	/**
	 * Gives access to the external notifications directory (<code>Notifications/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_NOTIFICATIONS = "ext_notifications";
	
	/**
	 * Gives access to the external pictures directory (<code>Pictures/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_PICTURES = "ext_pictures";
	
	/**
	 * Gives access to the external movies directory (<code>Movies/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_MOVIES = "ext_movies";
	
	/**
	 * Gives access to the external download directory (<code>Download/</code>, typically on the SD-card)
	 */
	public static final String EXTERNAL_DOWNLOAD = "ext_download";
	
	
	private final GenericFileAccessResource generic = new GenericFileAccessResource();
	private final ExternalFileAccessResource extBaseDir = new ExternalFileAccessResource(Directories.BASE_DIR);
	private final ExternalFileAccessResource extMusic = new ExternalFileAccessResource(Directories.MUSIC);
	private final ExternalFileAccessResource extPodcasts = new ExternalFileAccessResource(Directories.PODCASTS);
	private final ExternalFileAccessResource extRingtones = new ExternalFileAccessResource(Directories.RINGTONES);
	private final ExternalFileAccessResource extAlarms = new ExternalFileAccessResource(Directories.ALARMS);
	private final ExternalFileAccessResource extNotifications = new ExternalFileAccessResource(Directories.NOTIFICATIONS);
	private final ExternalFileAccessResource extPictures = new ExternalFileAccessResource(Directories.PICTURES);
	private final ExternalFileAccessResource extMovies = new ExternalFileAccessResource(Directories.MOVIES);
	private final ExternalFileAccessResource extDownload = new ExternalFileAccessResource(Directories.DOWNLOAD);
	
	/**
	 * Adds the created resources to a resource-goup
	 * @param rg Resource-group to which the resource should be added to
	 */
	public void addToResourceGroup(ResourceGroup rg) {
		rg.registerResource(GENERIC, generic);
		rg.registerResource(EXTERNAL_BASE_DIR, extBaseDir);
		rg.registerResource(EXTERNAL_MUSIC, extMusic);
		rg.registerResource(EXTERNAL_PODCASTS, extPodcasts);
		rg.registerResource(EXTERNAL_RINGTONES, extRingtones);
		rg.registerResource(EXTERNAL_ALARMS, extAlarms);
		rg.registerResource(EXTERNAL_NOTIFICATIONS, extNotifications);
		rg.registerResource(EXTERNAL_PICTURES, extPictures);
		rg.registerResource(EXTERNAL_MOVIES, extMovies);
		rg.registerResource(EXTERNAL_DOWNLOAD, extDownload);
	}

}
