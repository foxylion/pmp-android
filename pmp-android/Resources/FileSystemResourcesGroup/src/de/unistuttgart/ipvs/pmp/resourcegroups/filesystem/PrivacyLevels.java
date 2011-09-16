package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.util.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;

public class PrivacyLevels {

	public static final String GENERIC_READ = "gen_r";
	public static final String GENERIC_WRITE = "gen_w";
	public static final String GENERIC_LIST = "gen_l";
	public static final String GENERIC_DELETE = "gen_d";

	public static final String EXTERNAL_MUSIC_READ = "ext_music_r";
	public static final String EXTERNAL_MUSIC_WRITE = "ext_music_w";
	public static final String EXTERNAL_MUSIC_LIST = "ext_music_l";
	public static final String EXTERNAL_MUSIC_DELETE = "ext_music_d";

	public static final String EXTERNAL_PODCASTS_READ = "ext_podcasts_r";
	public static final String EXTERNAL_PODCASTS_WRITE = "ext_podcasts_w";
	public static final String EXTERNAL_PODCASTS_LIST = "ext_podcasts_l";
	public static final String EXTERNAL_PODCASTS_DELETE = "ext_podcast_d";

	public static final String EXTERNAL_RINGTONES_READ = "ext_ringtones_r";
	public static final String EXTERNAL_RINGTONES_WRITE = "ext_ringtones_w";
	public static final String EXTERNAL_RINGTONES_LIST = "ext_ringtones_l";
	public static final String EXTERNAL_RINGTONES_DELETE = "ext_ringtones_d";

	public static final String EXTERNAL_ALARMS_READ = "ext_alarms_r";
	public static final String EXTERNAL_ALARMS_WRITE = "ext_alarms_w";
	public static final String EXTERNAL_ALARMS_LIST = "ext_alarms_l";
	public static final String EXTERNAL_ALARMS_DELETE = "ext_alarms_d";

	public static final String EXTERNAL_NOTIFICATIONS_READ = "ext_notifications_r";
	public static final String EXTERNAL_NOTIFICATIONS_WRITE = "ext_notifications_w";
	public static final String EXTERNAL_NOTIFICATIONS_LIST = "ext_notifications_l";
	public static final String EXTERNAL_NOTIFICATIONS_DELETE = "ext_notifications_d";

	public static final String EXTERNAL_PICTURES_READ = "ext_pictures_r";
	public static final String EXTERNAL_PICTURES_WRITE = "ext_pictures_w";
	public static final String EXTERNAL_PICTURES_LIST = "ext_pictures_l";
	public static final String EXTERNAL_PICTURES_DELETE = "ext_pictures_d";

	public static final String EXTERNAL_MOVIES_READ = "ext_movies_r";
	public static final String EXTERNAL_MOVIES_WRITE = "ext_movies_w";
	public static final String EXTERNAL_MOVIES_LIST = "ext_movies_l";
	public static final String EXTERNAL_MOVIES_DELETE = "ext_movies_d";

	public static final String EXTERNAL_DOWNLOAD_READ = "ext_download_r";
	public static final String EXTERNAL_DOWNLOAD_WRITE = "ext_download_w";
	public static final String EXTERNAL_DOWNLOAD_LIST = "ext_download_l";
	public static final String EXTERNAL_DOWNLOAD_DELETE = "ext_download_d";

	private static PrivacyLevel genericRead;
	private static PrivacyLevel genericWrite;
	private static PrivacyLevel genericList;
	private static PrivacyLevel genericDelete;

	private static PrivacyLevel extMusicRead;
	private static PrivacyLevel extMusicWrite;
	private static PrivacyLevel extMusicList;
	private static PrivacyLevel extMusicDelete;

	private static PrivacyLevel extPodcastsRead;
	private static PrivacyLevel extPodcastsWrite;
	private static PrivacyLevel extPodcastsList;
	private static PrivacyLevel extPodcastsDelete;

	private static PrivacyLevel extRingtonesRead;
	private static PrivacyLevel extRingtonesWrite;
	private static PrivacyLevel extRingtonesList;
	private static PrivacyLevel extRingtonesDelete;

	private static PrivacyLevel extAlarmsRead;
	private static PrivacyLevel extAlarmsWrite;
	private static PrivacyLevel extAlarmsList;
	private static PrivacyLevel extAlarmsDelete;

	private static PrivacyLevel extNotificationsRead;
	private static PrivacyLevel extNotificationsWrite;
	private static PrivacyLevel extNotificationsList;
	private static PrivacyLevel extNotificationsDelete;

	private static PrivacyLevel extPicturesRead;
	private static PrivacyLevel extPicturesWrite;
	private static PrivacyLevel extPicturesList;
	private static PrivacyLevel extPicturesDelete;

	private static PrivacyLevel extMoviesRead;
	private static PrivacyLevel extMoviesWrite;
	private static PrivacyLevel extMoviesList;
	private static PrivacyLevel extMoviesDelete;

	private static PrivacyLevel extDownloadRead;
	private static PrivacyLevel extDownloadWrite;
	private static PrivacyLevel extDownloadList;
	private static PrivacyLevel extDownloadDelete;

	/**
	 * This creates all privacy-levels for reading, writing, deleting and
	 * listing files in different locations.
	 * 
	 * @throws Exception
	 *             Thrown,
	 */
	public PrivacyLevels() throws Exception {
		// Creates privacy-levels for reading, writing, deleting and listing
		// files.
		// We do not need a specialized implementation of our privacy-levels,
		// so we are using the implementation provided by "SimplePrivacyLevel".
		// As we want to use the privacy-levels' names and descriptions
		// stored in the Andorid resource-file (values/string.xml), we
		// set the name and description parameters to "null".
		genericRead = new BooleanPrivacyLevel("Generic read",
				"Allows the application to read every file stored on the device");
		genericWrite = new BooleanPrivacyLevel("Generic write",
				"Allows the application to write files everywhere on the device");
		genericList = new BooleanPrivacyLevel("Generic list",
				"Allows the application to receive a list of all files"
						+ "stored in every a directory of on the device");
		genericDelete = new BooleanPrivacyLevel("Generic delete",
				"Allows the application to delete every file stored on the device");

		extMusicRead = new BooleanPrivacyLevel("External music read",
				"Allows the application to read files stored in the external music directory");
		extMusicWrite = new BooleanPrivacyLevel("External music write",
				"Allows the application to write files in the external music directory");
		extMusicList = new BooleanPrivacyLevel(
				"External music list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external music directory");
		extMusicDelete = new BooleanPrivacyLevel("External music delete",
				"Allows the application to delete files stored in the external music directory");

		extPodcastsRead = new BooleanPrivacyLevel("External podcast read",
				"Allows the application to read files stored in the external podcast directory");
		extPodcastsWrite = new BooleanPrivacyLevel("External podcast write",
				"Allows the application to write files in the external podcast directory");
		extPodcastsList = new BooleanPrivacyLevel(
				"External podcast list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external podcast directory");
		extPodcastsDelete = new BooleanPrivacyLevel(
				"External podcast delete",
				"Allows the application to delete files stored in the external podcast directory");

		extRingtonesRead = new BooleanPrivacyLevel(
				"External ringtone read",
				"Allows the application to read files stored in the external ringtone directory");
		extRingtonesWrite = new BooleanPrivacyLevel("External ringtone write",
				"Allows the application to write files in the external ringtone directory");
		extRingtonesList = new BooleanPrivacyLevel(
				"External ringtone list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external podcast directory");
		extRingtonesDelete = new BooleanPrivacyLevel(
				"External ringtone delete",
				"Allows the application to delete files stored in the external ringtone directory");
		
		extAlarmsRead = new BooleanPrivacyLevel(
				"External alarm read",
				"Allows the application to read files stored in the external alarm directory");
		extAlarmsWrite = new BooleanPrivacyLevel("External alarm write",
				"Allows the application to write files in the external alarm directory");
		extAlarmsList = new BooleanPrivacyLevel(
				"External alarm list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external alarm directory");
		extAlarmsDelete = new BooleanPrivacyLevel(
				"External alarm delete",
				"Allows the application to delete files stored in the external alarm directory");

		extNotificationsRead = new BooleanPrivacyLevel(
				"External notifications read",
				"Allows the application to read files stored in the external notification directory");
		extNotificationsWrite = new BooleanPrivacyLevel("External alarm write",
				"Allows the application to write files in the external notification directory");
		extNotificationsList = new BooleanPrivacyLevel(
				"External notifications list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external notification directory");
		extNotificationsDelete = new BooleanPrivacyLevel(
				"External notifications delete",
				"Allows the application to delete files stored in the external notification directory");

		extPicturesRead = new BooleanPrivacyLevel(
				"External pictures read",
				"Allows the application to read files stored in the external picture directory");
		extPicturesWrite = new BooleanPrivacyLevel("External pictures write",
				"Allows the application to write files in the external picture directory");
		extPicturesList = new BooleanPrivacyLevel(
				"External pictures list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external picture directory");
		extPicturesDelete = new BooleanPrivacyLevel(
				"External pictures delete",
				"Allows the application to delete files stored in the external picture directory");

		extMoviesRead = new BooleanPrivacyLevel(
				"External movies read",
				"Allows the application to read files stored in the external movie directory");
		extMoviesWrite = new BooleanPrivacyLevel("External movies write",
				"Allows the application to write files in the external movie directory");
		extMoviesList = new BooleanPrivacyLevel(
				"External movies list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external movie directory");
		extMoviesDelete = new BooleanPrivacyLevel(
				"External movies delete",
				"Allows the application to delete files stored in the external movie directory");

		extDownloadRead = new BooleanPrivacyLevel(
				"External download read",
				"Allows the application to read files stored in the external download directory");
		extDownloadWrite = new BooleanPrivacyLevel("External download write",
				"Allows the application to write files in the external download directory");
		extDownloadList = new BooleanPrivacyLevel(
				"External download list",
				"Allows the application to receive a list of files"
						+ "stored in a directory in the external download directory");
		extDownloadDelete = new BooleanPrivacyLevel(
				"External download delete",
				"Allows the application to delete files stored in the external download directory");

	}

	public void addToResourceGroup(ResourceGroup rg) {
		rg.registerPrivacyLevel(GENERIC_READ, genericRead);
		rg.registerPrivacyLevel(GENERIC_WRITE, genericWrite);
		rg.registerPrivacyLevel(GENERIC_LIST, genericList);
		rg.registerPrivacyLevel(GENERIC_DELETE, genericDelete);

		rg.registerPrivacyLevel(EXTERNAL_MUSIC_READ, extMusicRead);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_WRITE, extMusicWrite);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_LIST, extMusicList);
		rg.registerPrivacyLevel(EXTERNAL_MUSIC_DELETE, extMusicDelete);

		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_READ, extPodcastsRead);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_WRITE, extPodcastsWrite);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_LIST, extPodcastsList);
		rg.registerPrivacyLevel(EXTERNAL_PODCASTS_DELETE, extPodcastsDelete);

		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_READ, extRingtonesRead);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_WRITE, extRingtonesWrite);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_LIST, extRingtonesList);
		rg.registerPrivacyLevel(EXTERNAL_RINGTONES_DELETE, extRingtonesDelete);

		rg.registerPrivacyLevel(EXTERNAL_ALARMS_READ, extAlarmsRead);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_WRITE, extAlarmsWrite);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_LIST, extAlarmsList);
		rg.registerPrivacyLevel(EXTERNAL_ALARMS_DELETE, extAlarmsDelete);

		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_READ,
				extNotificationsRead);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_WRITE,
				extNotificationsWrite);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_LIST,
				extNotificationsList);
		rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_DELETE,
				extNotificationsDelete);

		rg.registerPrivacyLevel(EXTERNAL_PICTURES_READ, extPicturesRead);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_WRITE, extPicturesWrite);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_LIST, extPicturesList);
		rg.registerPrivacyLevel(EXTERNAL_PICTURES_DELETE, extPicturesDelete);

		rg.registerPrivacyLevel(EXTERNAL_MOVIES_READ, extMoviesRead);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_WRITE, extMoviesWrite);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_LIST, extMoviesList);
		rg.registerPrivacyLevel(EXTERNAL_MOVIES_DELETE, extMoviesDelete);

		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_READ, extDownloadRead);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_WRITE, extDownloadWrite);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_LIST, extDownloadList);
		rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_DELETE, extDownloadDelete);
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
		BooleanPrivacyLevel privacyLevel = (BooleanPrivacyLevel) resource
				.getPrivacyLevel(privacyLevelName);
		String setPrivacyLevel = resource.getPrivacyLevelValue(app,
				privacyLevelName);
		return privacyLevel.parseValue(setPrivacyLevel);
	}

}
