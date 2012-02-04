package de.unistuttgart.ipvs.pmp.xmlutil.revision;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Reader to read in the revision date.
 * 
 * @author Tobias Kuhn
 * 
 */
public class RevisionReader {
    
    /* Singleton stuff */
    
    private static final RevisionReader instance = new RevisionReader();
    
    
    private RevisionReader() {
        
    }
    
    
    public static RevisionReader get() {
        return instance;
    }
    
    
    /* actual implementation */
    
    /**
     * Reads the revision of an apk file.
     * 
     * @param apk
     *            the apk file to read in
     * @return the revision, i.e. the seconds that have passed since 01.01.1970 = UNIX timestamp,
     *         or 04:04:04 04.04.1904 if a file wasn't found
     *         or 23:59:59 31.12.1969 if no time was specified in the APK
     *         or 06:06:06 06.06.1906 if another error occurred
     */
    public long readRevision(File apk) {
        
        long result = -1000;
        
        try {
            ZipFile zipApk = new ZipFile(apk);
            try {
                
                ZipEntry ze = zipApk.getEntry("classes.dex");
                if (ze == null) {
                    return -2074708556000L; // 404'd
                }
                result = Math.max(result, ze.getTime());
                
                ze = zipApk.getEntry("assets/rgis.xml");
                if (ze == null) {
                    return -2074708556000L; // 404'd
                }
                result = Math.max(result, ze.getTime());
                
            } finally {
                zipApk.close();
            }
            
            return result;
            
        } catch (ZipException e) {
            e.printStackTrace();
            return -2006186034000L; // the devil
        } catch (IOException e) {
            e.printStackTrace();
            return -2006186034000L; // the devil
        }
    }
    
    
    /**
     * 
     * @param revision
     *            a revision
     * @return the revision in human readable form
     */
    public String toHumanReadable(long revision) {
        return SimpleDateFormat.getDateTimeInstance().format(new Date(revision));
    }
}
