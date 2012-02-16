package unittest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPSChangeDescription;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Converts an old ResourceGroup RGIS to a new one.
 * 
 * @author Jakob Jarosch
 */
public class Converter {
    
    public static void main(String args[]) throws IOException {
        
        String pathToRepositroy = "D:\\Daten\\Repositories";
        String rgFolderName = "FileSystemResourcesGroup";
        
        RGIS rgis = XMLUtilityProxy.getRGUtil().parse(
                new FileInputStream(pathToRepositroy + "\\pmp-android\\pmp-android\\Resources\\" + rgFolderName
                        + "\\assets\\rgis.xml"));
        
        for (RGISPrivacySetting ps : rgis.getPrivacySettings()) {
            for (Description d : ps.getDescriptions()) {
                RGISPSChangeDescription d2 = new RGISPSChangeDescription();
                d2.setLocale(d.getLocale());
                d2.setChangeDescription(d.getDescription());
                ps.addChangeDescription(d2);
            }
        }
        InputStream is = XMLUtilityProxy.getRGUtil().compile(rgis);
        
        FileOutputStream fos = new FileOutputStream(pathToRepositroy + "\\pmp-android\\pmp-android\\Resources\\"
                + rgFolderName + "\\assets\\rgis.xml");
        byte[] bytes = new byte[1];
        while (is.read(bytes) != -1) {
            fos.write(bytes);
        }
        
        fos.close();
    }
}
