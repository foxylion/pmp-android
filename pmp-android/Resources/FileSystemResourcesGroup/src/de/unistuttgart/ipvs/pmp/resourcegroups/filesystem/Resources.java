package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.ExternalFileAccess.Directories;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.ExternalFileAccessResource;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.GenericFileAccessResource;

public class Resources {
	
	public static final String GENERIC = "gen";
	public static final String EXTERNAL_MUSIC = "ext_music";
	public static final String EXTERNAL_PODCASTS = "ext_podcasts";
	public static final String EXTERNAL_RINGTONES = "ext_ringones";
	public static final String EXTERNAL_ALARMS = "ext_alarms";
	public static final String EXTERNAL_NOTIFICATIONS = "ext_notifications";
	public static final String EXTERNAL_PICTURES = "ext_pictures";
	public static final String EXTERNAL_MOVIES = "ext_movies";
	public static final String EXTERNAL_DOWNLOAD = "ext_download";
	
	private final GenericFileAccessResource generic = new GenericFileAccessResource();
	private final ExternalFileAccessResource extMusic = new ExternalFileAccessResource(Directories.MUSIC);
	private final ExternalFileAccessResource extPodcasts = new ExternalFileAccessResource(Directories.PODCASTS);
	private final ExternalFileAccessResource extRingtones = new ExternalFileAccessResource(Directories.RINGTONES);
	private final ExternalFileAccessResource extAlarms = new ExternalFileAccessResource(Directories.ALARMS);
	private final ExternalFileAccessResource extNotifications = new ExternalFileAccessResource(Directories.NOTIFICATIONS);
	private final ExternalFileAccessResource extPictures = new ExternalFileAccessResource(Directories.PICTURES);
	private final ExternalFileAccessResource extMovies = new ExternalFileAccessResource(Directories.MOVIES);
	private final ExternalFileAccessResource extDownload = new ExternalFileAccessResource(Directories.DOWNLOAD);
	
	public void addToResourceGroup(ResourceGroup rg) {
		rg.registerResource(GENERIC, generic);
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
