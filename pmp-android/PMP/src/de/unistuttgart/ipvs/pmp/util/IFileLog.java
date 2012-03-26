package de.unistuttgart.ipvs.pmp.util;

import java.util.logging.Level;

import de.unistuttgart.ipvs.pmp.Log;

/**
 * Interface that implements logging to a file.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IFileLog {
    
    /**
     * Logs a new record to the log file.
     * 
     * @param origin
     *            origin class for the logging
     * @param granularity
     *            granularity as defined per the constants.
     * @param level
     *            the {@link Level} of the log
     * @param message
     *            the message
     * @param params
     *            the formatting parameters for message
     */
    public void log(Object origin, int granularity, Level level, String message, Object... params);
    
    
    /**
     * Normal logging with guaranteed calls to {@link Log}, no matter whether it will go in the FileLog or not.
     * 
     * @see FileLog#log(int, Level, String, Object...)
     * @param exception
     *            possible exception to log
     */
    public void logWithForward(Object origin, Throwable exception, int granularity, Level level, String message,
            Object... params);
}
