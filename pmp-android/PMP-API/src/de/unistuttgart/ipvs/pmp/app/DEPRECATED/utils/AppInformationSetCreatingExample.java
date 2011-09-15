package de.unistuttgart.ipvs.pmp.app.DEPRECATED.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSetParser;

/**
 * This activity creates a app information set with the given xml file. This is
 * just an example.
 * 
 * @author Marcus Vetter
 */
@Deprecated
public class AppInformationSetCreatingExample extends Activity {

    /**
     * Called, when activity is started.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	URL xmlURL = null;

	try {
	    // The URL of the XML file
	    xmlURL = new URL("http://marcus.mvvt.de/AppExample.xml");
	} catch (MalformedURLException e) {
	    Log.e(e.getMessage());
	}

	// Create the app information set
	AppInformationSet ais = null;
	try {
	    ais = AppInformationSetParser.createAppInformationSet(xmlURL.openStream());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// Print the app information set
	AppInformationSetParser.printAppInformationSet(ais);

    }

}