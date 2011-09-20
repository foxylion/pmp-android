package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.content.Context;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;

/**
 * Defines all privacy-levels used by the resources for accessing the file system
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
     * Privacy-level for reading all files stored on the SD-Card including files and directories controlled by the other
     * privacy-levels (e.g. data in the music directory). This means, even if <code>EXTERNAL_MUSIC_READ</code> is set to
     * false and <code>EXTERNAL_BASE_DIR_READ</code> is enabled, the access to <code>Music/</code> is allowed.
     */
    public static final String EXTERNAL_BASE_DIR_READ = "ext_base_r";
    /**
     * Privacy-level for writing files everywhere on the external SD-Card.
     * 
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
    
    private BooleanPrivacyLevel genericRead;
    private BooleanPrivacyLevel genericWrite;
    private BooleanPrivacyLevel genericList;
    private BooleanPrivacyLevel genericDelete;
    private BooleanPrivacyLevel genericMakeDirs;
    
    private BooleanPrivacyLevel extBaseDirRead;
    private BooleanPrivacyLevel extBaseDirWrite;
    private BooleanPrivacyLevel extBaseDirList;
    private BooleanPrivacyLevel extBaseDirDelete;
    private BooleanPrivacyLevel extBaseDirMakeDirs;
    
    private BooleanPrivacyLevel extMusicRead;
    private BooleanPrivacyLevel extMusicWrite;
    private BooleanPrivacyLevel extMusicList;
    private BooleanPrivacyLevel extMusicDelete;
    private BooleanPrivacyLevel extMusicMakeDirs;
    
    private BooleanPrivacyLevel extPodcastsRead;
    private BooleanPrivacyLevel extPodcastsWrite;
    private BooleanPrivacyLevel extPodcastsList;
    private BooleanPrivacyLevel extPodcastsDelete;
    private BooleanPrivacyLevel extPodcastsMakeDirs;
    
    private BooleanPrivacyLevel extRingtonesRead;
    private BooleanPrivacyLevel extRingtonesWrite;
    private BooleanPrivacyLevel extRingtonesList;
    private BooleanPrivacyLevel extRingtonesDelete;
    private BooleanPrivacyLevel extRingtonesMakeDirs;
    
    private BooleanPrivacyLevel extAlarmsRead;
    private BooleanPrivacyLevel extAlarmsWrite;
    private BooleanPrivacyLevel extAlarmsList;
    private BooleanPrivacyLevel extAlarmsDelete;
    private BooleanPrivacyLevel extAlarmsMakeDirs;
    
    private BooleanPrivacyLevel extNotificationsRead;
    private BooleanPrivacyLevel extNotificationsWrite;
    private BooleanPrivacyLevel extNotificationsList;
    private BooleanPrivacyLevel extNotificationsDelete;
    private BooleanPrivacyLevel extNotificationsMakeDirs;
    
    private BooleanPrivacyLevel extPicturesRead;
    private BooleanPrivacyLevel extPicturesWrite;
    private BooleanPrivacyLevel extPicturesList;
    private BooleanPrivacyLevel extPicturesDelete;
    private BooleanPrivacyLevel extPicturesMakeDirs;
    
    private BooleanPrivacyLevel extMoviesRead;
    private BooleanPrivacyLevel extMoviesWrite;
    private BooleanPrivacyLevel extMoviesList;
    private BooleanPrivacyLevel extMoviesDelete;
    private BooleanPrivacyLevel extMoviesMakeDirs;
    
    private BooleanPrivacyLevel extDownloadRead;
    private BooleanPrivacyLevel extDownloadWrite;
    private BooleanPrivacyLevel extDownloadList;
    private BooleanPrivacyLevel extDownloadDelete;
    private BooleanPrivacyLevel extDownloadMakeDirs;
    
    
    /**
     * This creates all privacy-levels for reading, writing, deleting and listing files in different locations.
     * 
     * @throws Exception
     *             Thrown,
     */
    public PrivacyLevels(Context c) throws Exception {
        
        // Read localized strings for the basic operations
        String read = c.getResources().getString(R.string.read);
        String write = c.getResources().getString(R.string.write);
        String list = c.getResources().getString(R.string.list);
        String delete = c.getResources().getString(R.string.delete);
        String mkdirs = c.getResources().getString(R.string.mkdirs);
        
        // Creates privacy-levels for reading, writing, deleting and listing
        // files.
        // We do not need a specialized implementation of our privacy-levels,
        // so we are using the implementation provided by "BooleanPrivacyLevel".
        
        // Generic file access
        String generic = c.getResources().getString(R.string.pl_generic);
        String generic_warning = c.getResources().getString(R.string.pl_generic_warning);
        this.genericRead = new BooleanPrivacyLevel(generic + ": " + read, c.getResources().getString(
                R.string.pl_generic_read_desc)
                + " " + generic_warning);
        this.genericWrite = new BooleanPrivacyLevel(generic + ": " + write, c.getResources().getString(
                R.string.pl_generic_write_desc)
                + " " + generic_warning);
        this.genericList = new BooleanPrivacyLevel(generic + ": " + list, c.getResources().getString(
                R.string.pl_generic_list_desc)
                + " " + generic_warning);
        this.genericDelete = new BooleanPrivacyLevel(generic + ": " + delete, c.getResources().getString(
                R.string.pl_generic_delete_desc)
                + " " + generic_warning);
        this.genericMakeDirs = new BooleanPrivacyLevel(generic + ": " + mkdirs, c.getResources().getString(
                R.string.pl_generic_mkdirs_desc)
                + " " + generic_warning);
        
        // External base-dir access
        String extBaseDir = c.getResources().getString(R.string.pl_external_base_dir);
        String extBaseDirNotice = c.getResources().getString(R.string.pl_external_base_dir_notice);
        this.extBaseDirRead = new BooleanPrivacyLevel(extBaseDir + ": " + read, c.getResources().getString(
                R.string.pl_external_base_dir_read_desc)
                + " " + extBaseDirNotice);
        this.extBaseDirWrite = new BooleanPrivacyLevel(extBaseDir + ": " + write, c.getResources().getString(
                R.string.pl_external_base_dir_write_desc)
                + " " + extBaseDirNotice);
        this.extBaseDirList = new BooleanPrivacyLevel(extBaseDir + ": " + list, c.getResources().getString(
                R.string.pl_external_base_dir_list_desc)
                + " " + extBaseDirNotice);
        this.extBaseDirDelete = new BooleanPrivacyLevel(extBaseDir + ": " + delete, c.getResources().getString(
                R.string.pl_external_base_dir_delete_desc)
                + " " + extBaseDirNotice);
        this.extBaseDirMakeDirs = new BooleanPrivacyLevel(extBaseDir + ": " + delete, c.getResources().getString(
                R.string.pl_external_base_dir_mkdirs_desc)
                + " " + extBaseDirNotice);
        
        // External music-dir access
        this.extMusicRead = getExternalReadPl(c, R.string.music);
        this.extMusicWrite = getExternalWritePl(c, R.string.music);
        this.extMusicList = getExternalListPl(c, R.string.music);
        this.extMusicDelete = getExternalDeletePl(c, R.string.music);
        this.extMusicMakeDirs = getExternalMkdirsPl(c, R.string.mkdirs);
        
        // External podcasts-dir access
        this.extPodcastsRead = getExternalReadPl(c, R.string.podcasts);
        this.extPodcastsWrite = getExternalWritePl(c, R.string.podcasts);
        this.extPodcastsList = getExternalListPl(c, R.string.podcasts);
        this.extPodcastsDelete = getExternalDeletePl(c, R.string.podcasts);
        this.extPodcastsMakeDirs = getExternalMkdirsPl(c, R.string.podcasts);
        
        // External ringtones-dir access
        this.extRingtonesRead = getExternalReadPl(c, R.string.ringtones);
        this.extRingtonesWrite = getExternalWritePl(c, R.string.ringtones);
        this.extRingtonesList = getExternalListPl(c, R.string.ringtones);
        this.extRingtonesDelete = getExternalDeletePl(c, R.string.ringtones);
        this.extRingtonesMakeDirs = getExternalMkdirsPl(c, R.string.ringtones);
        
        // External alarm-dir access
        this.extAlarmsRead = getExternalReadPl(c, R.string.alarms);
        this.extAlarmsWrite = getExternalWritePl(c, R.string.alarms);
        this.extAlarmsList = getExternalListPl(c, R.string.alarms);
        this.extAlarmsDelete = getExternalDeletePl(c, R.string.alarms);
        this.extAlarmsMakeDirs = getExternalMkdirsPl(c, R.string.alarms);
        
        // External notifications-dir access
        this.extNotificationsRead = getExternalReadPl(c, R.string.notifications);
        this.extNotificationsWrite = getExternalWritePl(c, R.string.notifications);
        this.extNotificationsList = getExternalListPl(c, R.string.notifications);
        this.extNotificationsDelete = getExternalDeletePl(c, R.string.notifications);
        this.extNotificationsMakeDirs = getExternalMkdirsPl(c, R.string.notifications);
        
        // External pictures-dir access
        this.extPicturesRead = getExternalReadPl(c, R.string.pictures);
        this.extPicturesWrite = getExternalWritePl(c, R.string.pictures);
        this.extPicturesList = getExternalListPl(c, R.string.pictures);
        this.extPicturesDelete = getExternalDeletePl(c, R.string.pictures);
        this.extPicturesMakeDirs = getExternalMkdirsPl(c, R.string.pictures);
        
        // External movies-dir access
        this.extMoviesRead = getExternalReadPl(c, R.string.movies);
        this.extMoviesWrite = getExternalWritePl(c, R.string.movies);
        this.extMoviesList = getExternalListPl(c, R.string.movies);
        this.extMoviesDelete = getExternalDeletePl(c, R.string.movies);
        this.extMoviesMakeDirs = getExternalMkdirsPl(c, R.string.movies);
        
        // External download-dir access
        this.extDownloadRead = getExternalReadPl(c, R.string.download);
        this.extDownloadWrite = getExternalWritePl(c, R.string.download);
        this.extDownloadList = getExternalListPl(c, R.string.download);
        this.extDownloadDelete = getExternalDeletePl(c, R.string.download);
        this.extDownloadMakeDirs = getExternalMkdirsPl(c, R.string.download);
        
    }
    
    
    /**
     * Creates a PL using strings from the resource file for reading data.
     * 
     * @param c
     *            App context
     * @param stringTypeId
     *            Resource id used for the type of the external directory (music, podcasts, ...)
     * @return Generates PL
     */
    private BooleanPrivacyLevel getExternalReadPl(Context c, int stringTypeId) {
        String read = c.getResources().getString(R.string.read);
        return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + read,
                getResourceWithReplacedDir(c, R.string.pl_external_read_desc, stringTypeId));
    }
    
    
    /**
     * Creates a PL using strings from the resource file for writing data.
     * 
     * @param c
     *            App context
     * @param stringTypeId
     *            Resource id used for the type of the external directory (music, podcasts, ...)
     * @return Generates PL
     */
    private BooleanPrivacyLevel getExternalWritePl(Context c, int stringTypeId) {
        String write = c.getResources().getString(R.string.write);
        return new BooleanPrivacyLevel(
                getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + write,
                getResourceWithReplacedDir(c, R.string.pl_external_write_desc, stringTypeId));
    }
    
    
    /**
     * Creates a PL using strings from the resource file for listing files/dirs.
     * 
     * @param c
     *            App context
     * @param stringTypeId
     *            Resource id used for the type of the external directory (music, podcasts, ...)
     * @return Generates PL
     */
    private BooleanPrivacyLevel getExternalListPl(Context c, int stringTypeId) {
        String list = c.getResources().getString(R.string.list);
        return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": " + list,
                getResourceWithReplacedDir(c, R.string.pl_external_list_desc, stringTypeId));
    }
    
    
    /**
     * Creates a PL using strings from the resource file for deleting files/dirs.
     * 
     * @param c
     *            App context
     * @param stringTypeId
     *            Resource id used for the type of the external directory (music, podcasts, ...)
     * @return Generates PL
     */
    private BooleanPrivacyLevel getExternalDeletePl(Context c, int stringTypeId) {
        String delete = c.getResources().getString(R.string.delete);
        return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": "
                + delete, getResourceWithReplacedDir(c, R.string.pl_external_delete_desc, stringTypeId));
    }
    
    
    /**
     * Creates a PL using strings from the resource file mkdirs.
     * 
     * @param c
     *            App context
     * @param stringTypeId
     *            Resource id used for the type of the external directory (music, podcasts, ...)
     * @return Generates PL
     */
    private BooleanPrivacyLevel getExternalMkdirsPl(Context c, int stringTypeId) {
        String mkdirs = c.getResources().getString(R.string.mkdirs);
        return new BooleanPrivacyLevel(getResourceWithReplacedDir(c, R.string.pl_external, stringTypeId) + ": "
                + mkdirs, getResourceWithReplacedDir(c, R.string.pl_external_mkdirs_desc, stringTypeId));
    }
    
    
    /**
     * Loads a source string and replaces <code>{dir}</code> with the replacement string
     * 
     * @param c
     *            Application context
     * @param sourceId
     *            ID of the source string
     * @param replacementId
     *            ID of the replacement string
     * @return Generated string
     */
    private String getResourceWithReplacedDir(Context c, int sourceId, int replacementId) {
        String raw = c.getResources().getString(sourceId);
        String replacement = c.getResources().getString(replacementId);
        return raw.replace("{dir}", replacement);
    }
    
    
    /**
     * Adds all generated privacy-levels to a resource-group
     * 
     * @param rg
     *            Resource-group to which the privacy-levels should be added
     */
    public void addToResourceGroup(ResourceGroup rg) {
        rg.registerPrivacyLevel(GENERIC_READ, this.genericRead);
        rg.registerPrivacyLevel(GENERIC_WRITE, this.genericWrite);
        rg.registerPrivacyLevel(GENERIC_LIST, this.genericList);
        rg.registerPrivacyLevel(GENERIC_DELETE, this.genericDelete);
        rg.registerPrivacyLevel(GENERIC_MAKE_DIRS, this.genericMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_READ, this.extBaseDirRead);
        rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_WRITE, this.extBaseDirWrite);
        rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_LIST, this.extBaseDirList);
        rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_DELETE, this.extBaseDirDelete);
        rg.registerPrivacyLevel(EXTERNAL_BASE_DIR_MAKE_DIRS, this.extBaseDirMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_MUSIC_READ, this.extMusicRead);
        rg.registerPrivacyLevel(EXTERNAL_MUSIC_WRITE, this.extMusicWrite);
        rg.registerPrivacyLevel(EXTERNAL_MUSIC_LIST, this.extMusicList);
        rg.registerPrivacyLevel(EXTERNAL_MUSIC_DELETE, this.extMusicDelete);
        rg.registerPrivacyLevel(EXTERNAL_MUSIC_MAKE_DIRS, this.extMusicMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_PODCASTS_READ, this.extPodcastsRead);
        rg.registerPrivacyLevel(EXTERNAL_PODCASTS_WRITE, this.extPodcastsWrite);
        rg.registerPrivacyLevel(EXTERNAL_PODCASTS_LIST, this.extPodcastsList);
        rg.registerPrivacyLevel(EXTERNAL_PODCASTS_DELETE, this.extPodcastsDelete);
        rg.registerPrivacyLevel(EXTERNAL_PODCASTS_MAKE_DIRS, this.extPodcastsMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_RINGTONES_READ, this.extRingtonesRead);
        rg.registerPrivacyLevel(EXTERNAL_RINGTONES_WRITE, this.extRingtonesWrite);
        rg.registerPrivacyLevel(EXTERNAL_RINGTONES_LIST, this.extRingtonesList);
        rg.registerPrivacyLevel(EXTERNAL_RINGTONES_DELETE, this.extRingtonesDelete);
        rg.registerPrivacyLevel(EXTERNAL_RINGTONES_MAKE_DIRS, this.extRingtonesMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_ALARMS_READ, this.extAlarmsRead);
        rg.registerPrivacyLevel(EXTERNAL_ALARMS_WRITE, this.extAlarmsWrite);
        rg.registerPrivacyLevel(EXTERNAL_ALARMS_LIST, this.extAlarmsList);
        rg.registerPrivacyLevel(EXTERNAL_ALARMS_DELETE, this.extAlarmsDelete);
        rg.registerPrivacyLevel(EXTERNAL_ALARMS_MAKE_DIRS, this.extAlarmsMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_READ, this.extNotificationsRead);
        rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_WRITE, this.extNotificationsWrite);
        rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_LIST, this.extNotificationsList);
        rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_DELETE, this.extNotificationsDelete);
        rg.registerPrivacyLevel(EXTERNAL_NOTIFICATIONS_MAKE_DIRS, this.extNotificationsMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_PICTURES_READ, this.extPicturesRead);
        rg.registerPrivacyLevel(EXTERNAL_PICTURES_WRITE, this.extPicturesWrite);
        rg.registerPrivacyLevel(EXTERNAL_PICTURES_LIST, this.extPicturesList);
        rg.registerPrivacyLevel(EXTERNAL_PICTURES_DELETE, this.extPicturesDelete);
        rg.registerPrivacyLevel(EXTERNAL_PICTURES_MAKE_DIRS, this.extPicturesMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_MOVIES_READ, this.extMoviesRead);
        rg.registerPrivacyLevel(EXTERNAL_MOVIES_WRITE, this.extMoviesWrite);
        rg.registerPrivacyLevel(EXTERNAL_MOVIES_LIST, this.extMoviesList);
        rg.registerPrivacyLevel(EXTERNAL_MOVIES_DELETE, this.extMoviesDelete);
        rg.registerPrivacyLevel(EXTERNAL_MOVIES_MAKE_DIRS, this.extMoviesMakeDirs);
        
        rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_READ, this.extDownloadRead);
        rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_WRITE, this.extDownloadWrite);
        rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_LIST, this.extDownloadList);
        rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_DELETE, this.extDownloadDelete);
        rg.registerPrivacyLevel(EXTERNAL_DOWNLOAD_MAKE_DIRS, this.extDownloadMakeDirs);
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
    public static boolean privacyLevelSet(String privacyLevelName, String app, Resource resource) {
        if (privacyLevelName == null) {
            Log.e("PrivacyLevels", "Name of the privacy-level cannot be null");
            return false;
        }
        
        BooleanPrivacyLevel privacyLevel = (BooleanPrivacyLevel) resource.getPrivacyLevel(privacyLevelName);
        return privacyLevel.permits(app, true);
    }
    
}
