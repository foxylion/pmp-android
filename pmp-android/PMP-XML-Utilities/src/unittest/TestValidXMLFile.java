/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package unittest;

import java.io.IOException;
import java.net.URL;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

public class TestValidXMLFile {
	
	public static void main(String[] args) {
		try {
			AIS ais = XMLUtilityProxy.parseAISXML(new URL("http://mvvt.de/ais.xml").openStream());
			XMLUtilityProxy.printAIS(ais);
			
			RGIS rgis = XMLUtilityProxy.parseRGISXML(new URL("http://mvvt.de/rgis.xml").openStream());
			XMLUtilityProxy.printRGIS(rgis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
