package de.unistuttgart.ipvs.pmp.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

public class FileLog extends FileHandler {
    
    /**
     * Installation, uninstallation of RGs;
     * Registration, unregistration of Apps
     */
    public static final int GRANULARITY_COMPONENT_CHANGES = 1 << 1;
    
    /**
     * Changes of SFs (Simple Mode);
     * Changes of Presets, PSs or Contexts (Expert Mode)
     */
    public static final int GRANULARITY_SETTING_CHANGES = 1 << 2;
    
    /**
     * ContextCondition changes;
     * PS changes caused by that
     */
    public static final int GRANULARITY_CONTEXT_CHANGES = 1 << 3;
    
    /**
     * SF requests by Apps;
     * PS values by RGs
     */
    public static final int GRANULARITY_SETTING_REQUESTS = 1 << 4;
    
    private static volatile FileLog instance;
    
    private static final File LOG_FILE_DIR = PMPApplication.getContext().getDir("log", Context.MODE_PRIVATE);
    private static final String LOG_FILE_NAME = LOG_FILE_DIR.getAbsolutePath() + "/log.xml";
    
    private static final String TAG = "FileLog";
    
    
    private FileLog() throws IOException {
        super(LOG_FILE_NAME, true);
    }
    
    
    public static FileLog getInstance() {
        if (instance == null) {
            try {
                instance = new FileLog();
            } catch (IOException e) {
                Log.e(TAG, "Could make log to " + LOG_FILE_NAME, e);
            }
        }
        return instance;
    }
    
    
    public void log(int granularity, Level level, String message, Object... params) {
        if ((PMPPreferences.getInstance().getLoggingGranularity() & granularity) > 0) {
            LogRecord lr = new LogRecord(level, String.format(message, params));
            publish(lr);
        }
    }
    
}
