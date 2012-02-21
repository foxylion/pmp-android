package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.RGISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.revision.RevisionReader;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;

/**
 * Class which holds the information about a resource group (identifier, name,
 * description, revision) in all languages.
 * 
 * @author Jakob Jarosch
 * 
 */
public class ResourceGroup {
    
    /**
     * Path to the {@link ResourceGroup}.
     */
    private File path;
    
    /**
     * Instance of the {@link IRGIS}.
     */
    private IRGIS parsedRGIS = null;
    
    /**
     * The latest known revision of the {@link ResourceGroup}.
     */
    private long revision = Long.MIN_VALUE;
    
    /**
     * The time of last revision check.
     */
    private long lastRevisionUpdate = 0;
    
    
    /**
     * Creates a new ResourceGroup.
     */
    public ResourceGroup(File filename) throws FileNotFoundException {
        this.path = filename;
        
        if (!this.path.isFile()) {
            throw new FileNotFoundException();
        }
    }
    
    
    /**
     * @return Returns the Path to the file.
     */
    public File getPath() {
        return this.path;
    }
    
    
    /**
     * @return Returns a {@link InputStream} for the {@link ResourceGroup}.
     */
    public InputStream getInputStream() {
        try {
            return new FileInputStream(this.path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    /**
     * @return Return an instance of the {@link IRGIS}.
     */
    public IRGIS getRGIS() {
        long currentRevision = getRevision();
        
        /* 
         * When a RGIS is available an the current revision is the newest one,
         * directly return the instance of the RGIS.
         */
        if (this.parsedRGIS != null && this.revision == currentRevision) {
            return this.parsedRGIS;
        }
        
        /* Update the revision */
        this.revision = currentRevision;
        
        /* Start new parsing execution. */
        RGISParser parser = new RGISParser();
        
        try {
            ZipFile zip = new ZipFile(getPath());
            ZipEntry entry = zip.getEntry("assets/rgis.xml");
            if (entry == null) {
                System.out.println("[E] rgis.xml does not exist in package " + getPath().getName() + ".");
                return null;
            }
            InputStream stream = zip.getInputStream(entry);
            this.parsedRGIS = parser.parse(stream);
            
        } catch (IOException e) {
            System.out.println("[E] Failed to load rgis.xml from package " + getPath().toString()
                    + ", skipping. (Error: " + e.getMessage() + ")");
        }
        
        return this.parsedRGIS;
    }
    
    
    /**
     * @return Returns the revision of the {@link ResourceGroup}.
     */
    public long getRevision() {
        long revision = this.revision;
        if (this.lastRevisionUpdate + JPMPPS.REVISION_UPDATE_DELAY < System.currentTimeMillis()) {
            revision = RevisionReader.get().readRevision(this.path);
        }
        
        return revision;
    }
    
    
    /**
     * Returns a localized version of the {@link ResourceGroup}.
     * 
     * @param locale
     *            Locale in which the resource group should be returned.
     * @return A localized version of the {@link ResourceGroup}.
     */
    public LocalizedResourceGroup getLocalized(String locale) {
        LocalizedResourceGroup lrg = new LocalizedResourceGroup();
        
        String name = getRGIS().getNameForLocale(new Locale(locale));
        lrg.setName(name != null ? name : getRGIS().getNameForLocale(Locale.ENGLISH));
        
        String description = getRGIS().getDescriptionForLocale(new Locale(locale));
        lrg.setDescription(description != null ? description : getRGIS().getDescriptionForLocale(Locale.ENGLISH));
        
        lrg.setIdentifier(getRGIS().getIdentifier());
        lrg.setRevision(RevisionReader.get().readRevision(this.path));
        
        return lrg;
    }
}
