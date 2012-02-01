package unittest;

import java.io.IOException;
import java.net.URL;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityFactory;
import de.unistuttgart.ipvs.pmp.xmlutil.app.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rg.RGIS;

public class TestValidXMLFile {
	
	public static void main(String[] args) {
		try {
			AIS ais = XMLUtilityFactory.getAppUtilities().parseAISXML(new URL("http://mvvt.de/ais.xml").openStream());
			XMLUtilityFactory.getAppUtilities().printAIS(ais);
			
			RGIS rgis = XMLUtilityFactory.getRGUtilities().parseRGISXML(new URL("http://mvvt.de/rgis.xml").openStream());
			XMLUtilityFactory.getRGUtilities().printRGIS(rgis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
