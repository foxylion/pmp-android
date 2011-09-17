package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.content.Context;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;

/**
 * Defines all privacy-levels used by the resources for accessing the file
 * system
 * 
 * @author Patrick Strobel
 * @version 0.3.0
 * 
 */
public class PrivacyLevels {

	public static final String GENERIC_READ = "gen_r";
	public static final String GENERIC_WRITE = "gen_w";
	public static final String GENERIC_LIST = "gen_l";
	public static final String GENERIC_DELETE = "gen_d";
	public static final String GENERIC_MAKE_DIRS = "gen_mkdirs";

	/**
	 * Privacy-level for reading all files stored on the SD-Card including files and directories controlled by the
	 * other privacy-levels (e.g. data in the music directory). This means, even if
	 * <code>EXTERNAL_MUSIC_READ</code> is set to false and
	 * <code>EXTERNAL_BASE_DIR_READ</code> is enabled, the access to
	 * <code>Music/</code> is allowed.
	 */
	public static final String EXTERNAL_BASE_DIR_READ = "ext_base_r";
	/**
	 * Privacy-level for writing files everywhere on the external SD-Card.
	 * @see EXTERNAL_BASE_DIR_READ
	 */
	public static final String EXTERNAL_BASE_DIR_WRITE = "ext_base_w";
	public static final String EXTERNAL_BASE_DIR_LIST = "ext_base_l";
	public static final String EXTERNAL_BASE_DIR_DELETE = "ext_base_d";
	public static final String EXTERNAL_BASE_DIR_MAKE_DIRS = "ext_base_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Music/</code> directory
	 */
	public static final String EXTERNAL_MUSIC_READ = "ext_music_r";
	public static final String EXTERNAL_MUSIC_WRITE = "ext_music_w";
	public static final String EXTERNAL_MUSIC_LIST = "ext_music_l";
	public static final String EXTERNAL_MUSIC_DELETE = "ext_music_d";
	public static final String EXTERNAL_MUSIC_MAKE_DIRS = "ext_music_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Podcasts/</code> directory
	 */
	public static final String EXTERNAL_PODCASTS_READ = "ext_podcasts_r";
	public static final String EXTERNAL_PODCASTS_WRITE = "ext_podcasts_w";
	public static final String EXTERNAL_PODCASTS_LIST = "ext_podcasts_l";
	public static final String EXTERNAL_PODCASTS_DELETE = "ext_podcasts_d";
	public static final String EXTERNAL_PODCASTS_MAKE_DIRS = "ext_podcasts_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Ringtones/</code> directory
	 */
	public static final String EXTERNAL_RINGTONES_READ = "ext_ringtones_r";
	public static final String EXTERNAL_RINGTONES_WRITE = "ext_ringtones_w";
	public static final String EXTERNAL_RINGTONES_LIST = "ext_ringtones_l";
	public static final String EXTERNAL_RINGTONES_DELETE = "ext_ringtones_d";
	public static final String EXTERNAL_RINGTONES_MAKE_DIRS = "ext_ringtones_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Alarms/</code> directory
	 */
	public static final String EXTERNAL_ALARMS_READ = "ext_alarms_r";
	public static final String EXTERNAL_ALARMS_WRITE = "ext_alarms_w";
	public static final String EXTERNAL_ALARMS_LIST = "ext_alarms_l";
	public static final String EXTERNAL_ALARMS_DELETE = "ext_alarms_d";
	public static final String EXTERNAL_ALARMS_MAKE_DIRS = "ext_alarms_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Notifications/</code> directory
	 */
	public static final String EXTERNAL_NOTIFICATIONS_READ = "ext_notifications_r";
	public static final String EXTERNAL_NOTIFICATIONS_WRITE = "ext_notifications_w";
	public static final String EXTERNAL_NOTIFICATIONS_LIST = "ext_notifications_l";
	public static final String EXTERNAL_NOTIFICATIONS_DELETE = "ext_notifications_d";
	public static final String EXTERNAL_NOTIFICATIONS_MAKE_DIRS = "ext_notifications_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Pictures/</code> directory
	 */
	public static final String EXTERNAL_PICTURES_READ = "ext_pictures_r";
	public static final String EXTERNAL_PICTURES_WRITE = "ext_pictures_w";
	public static final String EXTERNAL_PICTURES_LIST = "ext_pictures_l";
	public static final String EXTERNAL_PICTURES_DELETE = "ext_pictures_d";
	public static final String EXTERNAL_PICTURES_MAKE_DIRS = "ext_pictures_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Movies/</code> directory
	 */
	public static final String EXTERNAL_MOVIES_READ = "ext_movies_r";
	public static final String EXTERNAL_MOVIES_WRITE = "ext_movies_w";
	public static final String EXTERNAL_MOVIES_LIST = "ext_movies_l";
	public static final String EXTERNAL_MOVIES_DELETE = "ext_movies_d";
	public static final String EXTERNAL_MOVIES_MAKE_DIRS = "ext_movies_mkdirs";

	/**
	 * Privacy-level for reading all files stored in the external <code>Download/</code> directory
	 */
	public static final String EXTERNAL_DOWNLOAD_READ = "ext_download_r";
	public static final String EXTERNAL_DOWNLOAD_WRITE = "ext_download_w";
	public static final String EXTERNAL_DOWNLOAD_LIST = "ext_download_l";
	public static final String EXTERNAL_DOWNLOAD_DELETE = "ext_download_d";
	public static final String EXTERNAL_DOWNLOAD_MAKE_DIRS = "ext_download_mkdirs";

	
	private static PrivacyLevel genericRead;
	private static PrivacyLevel genericWrite;
	private static PrivacyLevel genericList;
	private static PrivacyLevel genericDelete;
	private static PrivacyLevel genericMakeDirs;

	private static PrivacyLevel extBaseDirRead;
	private static PrivacyLevel extBaseDirWrite;
	private static PrivacyLevel extBaseDirList;
	private static PrivacyLevel extBaseDirDelete;
	private static PrivacyLevel extBaseDirMakeDirs;
	
	private static PrivacyLevel extMusicRead;
	private static PrivacyLevel extMusicWrite;
	private static PrivacyLevel extMusicList;
	private static PrivacyLevel extMusicDelete;
	private static PrivacyLevel extMusicMakeDirs;

	private static PrivacyLevel extPodcastsRead;
	private static PrivacyLevel extPodcastsWrite;
	private static PrivacyLevel extPodcastsList;
	private static PrivacyLevel extPodcastsDelete;
	private static PrivacyLevel extPodcastsMakeDirs;

	private static PrivacyLevel extRingtonesRead;
	private static PrivacyLevel extRingtonesWrite;
	private static PrivacyLevel extRingtonesList;
	private static PrivacyLevel extRingtonesDelete;
	private static PrivacyLevel extRingtonesMakeDirs;

	private static PrivacyLevel extAlarmsRead;
	private static PrivacyLevel extAlarmsWrite;
	private static PrivacyLevel extAlarmsList;
	private static PrivacyLevel extAlarmsDelete;
	private static PrivacyLevel extAlarmsMakeDirs;

	private static PrivacyLevel extNotificationsRead;
	private static PrivacyLevel extNotificationsWrite;
	private static PrivacyLevel extNotificationsList;
	private static PrivacyLevel extNotificationsDelete;
	private static PrivacyLevel extNotificationsMakeDirs;

	private static PrivacyLevel extPicturesRead;
	private static PrivacyLevel extPicturesWrite;
	private static PrivacyLevel extPicturesList;
	private static PrivacyLevel extPicturesDelete;
	private static PrivacyLevel extPicturesMakeDirs;

	private static PrivacyLevel extMoviesRead;
	private static PrivacyLevel extMoviesWrite;
	private static PrivacyLevel extMoviesList;
	private static PrivacyLevel extMoviesDelete;
	private static PrivacyLevel extMoviesMakeDirs;

	private static PrivacyLevel extDownloadRead;
	private static PrivacyLevel extDownloadWrite;
	private static PrivacyLevel extDownloadList;
	private static PrivacyLevel extDownloadDelete;
	private static PrivacyLevel extDownloadMakeDirs;

	/**
	 * This creates all privacy-levels for reading, writing, deleting and
	 * listing files in different locations.
	 * 
	 * @throws Exception
	 *             Thrown,
	 */
	public PrivacyLevels(Context c) throws Exception {
		String read = c.getResources().getString(R.string.read);
		String write = c.getResources().getString(R.string.write);
		String list = c.getResources().getString(R.string.list);
		String delete = c.getResources().getString(R.string.delete);
		String mkdirs = c.getResources().getString(R.string.mkdirs);
		
		// Creates privacy-levels for reading, writing, deleting and listing
		// files.
		// We do not need a specialized implementation of our privacy-levels,
		// so we are using the implementation provided by "SimplePrivacyLevel".
		// As we want to use the privacy-levels' names and descriptions
		// stored in the Andorid resource-file (values/string.xml), we
		// set the name and description parameters to "null".
		String generic = c.getResources().getString(R.string.pl_generic);
		String generic_warning = c.getResources().getString(R.string.pl_generic_warning);
		genericRead = new BooleanPrivacyLevel(generic + ": " + read,
				c.getResources().getString(R.string.pl_generic_read_desc) + " " + generic_warning);
		genericWrite = new BooleanPrivacyLevel(generic + ": " + write,
				c.getResources().getString(R.string.pl_generic_write_desc) + " " + generic_warning);
		genericList = new BooleanPrivacyLevel(generic + ": " + list,
				c.getResources().getString(R.string.pl_generic_list_desc) + " " + generic_warning);
		genericDelete = new BooleanPrivacyLevel(generic + ": " + delete,
				c.getResources().getString(R.string.pl_generic_delete_desc) + " " + generic_warning);
		genericMakeDirs = new BooleanPrivacyLevel(generic + ": " + mkdirs,
				c.getResources().getString(R.string.pl_generic_mkdirs_desc) + " " + generic_warning);

		// External base-dir access
		String extBaseDir = c.getResources().getString(R.string.pl_external_base_dir);
		String extBaseDirNotice = c.getResources().getString(R.string.pl_external_base_dir_notice);
		extBaseDirRead = new BooleanPrivacyLevel(extBaseDir + ": " + read,
				c.getResources().getString(R.string.pl_external_base_dir_read_desc) + " " + extBaseDirNotice);
		extBaseDirWrite = new BooleanPrivacyLevel(extBaseDir + ": " + write,
				c.getResources().getString(R.string.pl_external_base_dir_write_desc) + " " + extBaseDirNotice);
		extBaseDirList = new BooleanPrivacyLevel(extBaseDir + ": " + list,
				c.getResources().getString(R.string.pl_external_base_dir_list_desc) + " " + extBaseDirNotice);
		extBaseDirDelete = new BooleanPrivacyLevel(extBaseDir + ": " + delete,
				c.getResources().getString(R.string.pl_external_base_dir_delete_desc) + " " + extBaseDirNotice);
		extBaseDirMakeDirs = new BooleanPrivacyLevel(extBaseDir + ": " + delete,
				c.getResources().getString(R.string.pl_external_base_dir_mkdirs_desc) + " " + extBaseDirNotice);


		extMusicRead = getExternalReadPl(c, R.string.music);
		extMusicWrite = getExternalWritePl(c, R.string.music);
		extMusicList = getExternalListPl(c, R.string.music);
		extMusicDelete = getExternalDeletePl(c, R.string.delete);
		extMusicMakeDirs = getExternalMkdirsPl(c, R.string.mkdirs);

		extPodcastsRead = getExternalReadPl(c, R.string.podcasts);
		extPodcastsWrite = getExternalWritePl(c, R.string.podcasts);
		extPodcastsList = getExternalListPl(c, R.string.podcasts);
		extPodcastsDelete = getExternalDeletePl(c, R.string.podcasts);
		extPodcastsMakeDirs = getExternalMkdirsPl(c, R.string.podcasts);

		extRingtonesRead = getExternalReadPl(c, R.string.ringtones);
		extRingtonesWrite = getExternalWritePl(c, R.string.ringtones);
		extRingtonesList = getExternalListPl(c, R.string.ringtones);
		extRingtonesDelete = getExternalDeletePl(c, R.string.ringtones);
		extRingtonesMakeDirs = getExternalMkdirsPl(c, R.string.ringtones);

		extAlarmsRead = getExternalReadPl(c, R.string.alarms);
		extAlarmsWrite = getExternalWritePl(c, R.string.alarms);
		extAlarmsList = getExternalListPl(c, R.string.alarms);
		extAlarmsDelete = getExternalDeletePl(c, R.string.alarms);
		extAlarmsMakeDirs = getExternalMkdirsPl(c, R.string.alarms);
		
		extNotificationsRead = getExternalReadPl(c, R.string.notifications);
		extNotificationsWrite = getExternalWritePl(c, R.string.notifications);
		extNotificationsList = getExternalListPl(c, R.string.notifications);
		extNotificationsDelete = getExternalDeletePl(c, R.string.notifications);
		extNotificationsMakeDirs = getExternalMkdirsPl(c, R.string.notifications);

		extPicturesRead = getExternalReadPl(c, R.string.pictures);
		extPicturesWrite = getExternalWritePl(c, R.string.pictures);
		extPicturesList = getExternalListPl(c, R.string.pictures);
		extPicturesDelete = getExternalDeletePl(c, R.string.pictures);
		extPicturesMakeDirs = getExternalMkdirsPl(c, R.string.pictures);
		
		extMoviesRead = getExternalReadPl(c, R.string.movies);
		extMoviesWrite = getExternalWritePl(c, R.string.movies);
		extMoviesList = getExternalListPl(c, R.string.movies);
		extMoviesDelete = getExternalDeletePl(c, R.string.movies);
		extMoviesMakeDirs = getExternalMkdirsPl(c, R.string.movies);
		
		extDownloadRead = getExternalReadPl(c, R.string.download);
		extDownloadWrite = getExternalWritePl(c, R.string.download);
		extDownloadList = getExternalListPl(c, R.string.download);
		extDownloadDelete = getExternalDeletePl(c, R.string.download);
		extDownloadMakeDirs = getExternalMkdirsPl(c, R.string.download);

	}
	
	/**
	 * Creates a PL using strings from the resource file for reading data.
	 * @param c				App context
	 * @param stringTypeId	Resource id used for the type of the external directory (music, podcasts, ...)
	 * @return	Generates PL
	 */
	private BooleanPrivacyLevel getExternalReadPl(Context c, int stringTypeId) {
		String read = c.getResources().getString(R.string.read);
		return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + read,
				getResourceWithReplacedDir(c, R.string.pl_external_read_desc, stringTypeId));
	}
	
	/**
	 * Creates a PL using strings from the resource file for writing data.
	 * @param c				App context
	 * @param stringTypeId	Resource id used for the type of the external directory (music, podcasts, ...)
	 * @return	Generates PL
	 */
	private BooleanPrivacyLevel getExternalWritePl(Context c, int stringTypeId) {
		String write = c.getResources().getString(R.string.write);
		return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + write,
				getResourceWithReplacedDir(c, R.string.pl_external_write_desc, stringTypeId));
	}
	
	/**
	 * Creates a PL using strings from the resource file for listing files/dirs.
	 * @param c				App context
	 * @param stringTypeId	Resource id used for the type of the external directory (music, podcasts, ...)
	 * @return	Generates PL
	 */
	private BooleanPrivacyLevel getExternalListPl(Context c, int stringTypeId) {
		String list = c.getResources().getString(R.string.list);
		return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + list,
				getResourceWithReplacedDir(c, R.string.pl_external_list_desc, stringTypeId));
	}
	
	/**
	 * Creates a PL using strings from the resource file for deleting files/dirs.
	 * @param c				App context
	 * @param stringTypeId	Resource id used for the type of the external directory (music, podcasts, ...)
	 * @return	Generates PL
	 */
	private BooleanPrivacyLevel getExternalDeletePl(Context c, int stringTypeId) {
		String delete = c.getResources().getString(R.string.delete);
		return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + delete,
				getResourceWithReplacedDir(c, R.string.pl_external_delete_desc, stringTypeId));
	}
	
	/**
	 * Creates a PL using strings from the resource file mkdirs.
	 * @param c				App context
	 * @param stringTypeId	Resource id used for the type of the external directory (music, podcasts, ...)
	 * @return	Generates PL
	 */
	private BooleanPrivacyLevel getExternalMkdirsPl(Context c, int stringTypeId) {
		String mkdirs = c.getResources().getString(R.string.mkdirs);
		return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + mkdirs,
				getResourceWithReplacedDir(c, R.string.pl_external_mkdirs_desc, stringTypeId));
	}
	
	/**
	 * Loads a source string and replaces <code>{dir}</code> with the replacement string
	 * @param c				Application context
	 * @param sourceId		ID of the source string
	 * @param replacementId	ID of the replacement string
	 * @return	Generated string
	 */
	private String getResourceWithReplacedDir(Context c, int sourceId, int replacementId) {
		String raw = c.getResources().getString(sourceId);
		String replacement = c.getResources().getString(replacementId);
		return raw.replace("{dir}", replacement);
	}

	/**
	 * Adds all generated pirvacy-levels to a resource-group
	 * 
	 * @param rg
	 *            Resource-group to which the privacy-levels should be added
	 */
	public void addToResourceGroup(ResourceGroup rg) {
		rg.registerPrivacyLevel(GENERIC_READ, genericRead);
		rg.registerPrivacyLevel(GENERIC_WRITE, genericWrite);
		rg.registerPrivacyLevel(GENERIC_LIST, genericList);
		rg.registerPrivacyLevel(GENERIC_DELETE, genericDelete);
		rg.registerPrivacyLevel(GENERIC_MAKE_DIRS, genericMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_READ, extBaseDirRead);
		rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_WRITE, extBaseDirWrite);
		rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_LIST, extBaseDirList);
		rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_DELETE, extBaseDirDelete);
		rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_MAKE_DIRS, extBaseDirMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_MUSIC_READ, extMusicRead);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_WRITE, extMusicWrite);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_LIST, extMusicList);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_DELETE, extMusicDelete);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_MAKE_DIRS, extMusicMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_READ, extPodcastsRead);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_WRITE, extPodcastsWrite);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_LIST, extPodcastsList);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_DELETE, extPodcastsDelete);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_MAKE_DIRS, extPodcastsMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_READ, extRingtonesRead);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_WRITE, extRingtonesWrite);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_LIST, extRingtonesList);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_DELETE, extRingtonesDelete);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_MAKE_DIRS, extRingtonesMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_ALARMS_READ, extAlarmsRead);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_WRITE, extAlarmsWrite);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_LIST, extAlarmsList);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_DELETE, extAlarmsDelete);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_MAKE_DIRS, extAlarmsMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_READ,
				extNotificationsRead);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_WRITE,
				extNotificationsWrite);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_LIST,
				extNotificationsList);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_DELETE,
				extNotificationsDelete);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_MAKE_DIRS,
				extNotificationsMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_PICTURES_READ, extPicturesRead);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_WRITE, extPicturesWrite);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_LIST, extPicturesList);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_DELETE, extPicturesDelete);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_MAKE_DIRS, extPicturesMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_MOVIES_READ, extMoviesRead);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_WRITE, extMoviesWrite);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_LIST, extMoviesList);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_DELETE, extMoviesDelete);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_MAKE_DIRS, extMoviesMakeDirs);

		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_READ, extDownloadRead);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_WRITE, extDownloadWrite);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_LIST, extDownloadList);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_DELETE, extDownloadDelete);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_MAKE_DIRS, extDownloadMakeDirs);
	}

	/**
	 * Checks if a specific privacy-level is set for an application
	 * 
	 * @param privacyLevelName
	 *            The privacy-level to check
	 * @param app
	 *            The app to check
	 * @param resource
	 *            Resource
	 * @return True, if privacy-level is set for this application
	 */
	public static boolean privacyLevelSet(String privacyLevelName, String app,
			Resource resource) {
		if (privacyLevelName == null) {
			Log.e("PrivacyLevels", "Name of the privacy-level cannot be null");
			return false;
		}

		String setPrivacyLevel = resource.getPrivacyLevelValue(app,
				privacyLevelName);
		return BooleanPrivacyLevel.valueOf(setPrivacyLevel);
	}

}
