package unittest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

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
        
        for (IRGISPrivacySetting ps : rgis.getPrivacySettings()) {
            for (ILocalizedString description : ps.getDescriptions()) {
                LocalizedString changeDescription = new LocalizedString();
                changeDescription.setLocale(description.getLocale());
                changeDescription.setString(description.getString());
                ps.addChangeDescription(changeDescription);
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
