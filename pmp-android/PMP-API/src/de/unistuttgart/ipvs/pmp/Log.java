package de.unistuttgart.ipvs.pmp;

/**
 * Internal {@link android.util.Log} class for the project. Logging is so much more simple.
 * 
 * @author Jakob Jarosch
 */
public class Log {
    
    private static final boolean useClassNameAsPrefix = false;
    private static final boolean useMethodNameAsPrefix = false;
    private static final boolean useLineNumberAsPrefix = false;
    
    private static String sufix = "-Unknown";
    
    
    public static void setTagSufix(String sufix) {
        if (sufix == null) {
            Log.sufix = "-Unknown";
        } else {
            Log.sufix = "-" + sufix;
        }
    }
    
    
    /**
     * @see android.util.Log#d(String, String)
     */
    public static void d(String message) {
        android.util.Log.d(Constants.LOG_NAME + Log.sufix, prefix() + message);
    }
    
    
    /**
     * @see android.util.Log#d(String, String, Throwable)
     */
    public static void d(String message, Throwable t) {
        android.util.Log.d(Constants.LOG_NAME + Log.sufix, prefix() + message, t);
    }
    
    
    /**
     * @see android.util.Log#e(String, String)
     */
    public static void e(String message) {
        android.util.Log.e(Constants.LOG_NAME + Log.sufix, prefix(true) + message);
    }
    
    
    /**
     * @see android.util.Log#e(String, String, Throwable)
     */
    public static void e(String message, Throwable t) {
        android.util.Log.e(Constants.LOG_NAME + Log.sufix, prefix(true) + message, t);
    }
    
    
    /**
     * @see android.util.Log#i(String, String)
     */
    public static void i(String message) {
        android.util.Log.i(Constants.LOG_NAME + Log.sufix, prefix() + message);
    }
    
    
    /**
     * @see android.util.Log#i(String, String, Throwable)
     */
    public static void i(String message, Throwable t) {
        android.util.Log.i(Constants.LOG_NAME + Log.sufix, prefix() + message, t);
    }
    
    
    /**
     * @see android.util.Log#v(String, String)
     */
    public static void v(String message) {
        android.util.Log.v(Constants.LOG_NAME + Log.sufix, prefix() + message);
    }
    
    
    /**
     * @see android.util.Log#v(String, String, Throwable)
     */
    public static void v(String message, Throwable t) {
        android.util.Log.v(Constants.LOG_NAME + Log.sufix, prefix() + message, t);
    }
    
    
    /**
     * @see android.util.Log#w(String, String)
     */
    public static void w(String message) {
        android.util.Log.w(Constants.LOG_NAME + Log.sufix, prefix() + message);
    }
    
    
    /**
     * @see android.util.Log#w(String, String, Throwable)
     */
    public static void w(String message, Throwable t) {
        android.util.Log.w(Constants.LOG_NAME + Log.sufix, prefix() + message, t);
    }
    
    
    private static String prefix() {
        return prefix(false);
    }
    
    
    private static String prefix(boolean forcePrefix) {
        String prefix = "";
        if (useClassNameAsPrefix || useMethodNameAsPrefix || useLineNumberAsPrefix || forcePrefix) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (useClassNameAsPrefix || forcePrefix) {
                prefix += stackTraceElements[4].getClassName();
            }
            if (useMethodNameAsPrefix || forcePrefix) {
                prefix += "#" + stackTraceElements[4].getMethodName() + "()";
            }
            if (useLineNumberAsPrefix || forcePrefix) {
                prefix += ":" + stackTraceElements[4].getLineNumber();
            }
            if (prefix.length() > 0) {
                prefix += " ";
            }
        }
        return prefix;
    }
}
