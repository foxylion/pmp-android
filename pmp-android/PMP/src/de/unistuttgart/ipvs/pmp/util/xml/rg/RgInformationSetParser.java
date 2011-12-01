package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.InputStream;

public class RgInformationSetParser {
    
    /**
     * This method creates an resourcegroup information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return resourcegroup information set
     */
    public static RgInformationSet createRgInformationSet(InputStream xmlStream) {
        return new XMLParser(xmlStream).parse();
    }

}
