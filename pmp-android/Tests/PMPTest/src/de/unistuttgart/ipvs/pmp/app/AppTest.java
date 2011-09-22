package de.unistuttgart.ipvs.pmp.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;

public class AppTest extends InstrumentationTestCase {
    
    protected static final String SERVICE_ANDROID_NAME = "APP_SERVICE_ANDROID_NAME";
    
    private App app;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        this.app = new App() {
            
            @Override
            public void setActiveServiceLevel(int level) {
                // called by IPC  
            }
            
            
            @Override
            public void onRegistrationSuccess() {
                // called by IPC
            }
            
            
            @Override
            public void onRegistrationFailed(String message) {
                // called by IPC
            }
            
            
            @Override
            protected InputStream getXMLInputStream() {
                try {
                    return getInstrumentation().getContext().getAssets().open("xml-parser-test/WellFormedFile.xml");
                } catch (IOException e) {
                    fail("App.getXMLInputStream() did not exist");
                    return null;
                }
            }
            
            
            @Override
            protected String getServiceAndroidName() {
                return SERVICE_ANDROID_NAME;
            }
            
            
            @Override
            public Context getApplicationContext() {
                // mockup
                return new MockContext();
            }
        };
        this.app.onCreate();
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testGetSignee() {
        assertNotNull(this.app.getSignee());
        assertNotNull(this.app.getSignee().getLocalPublicKey());
        assertEquals(PMPComponentType.APP, this.app.getSignee().getType());
        assertEquals(SERVICE_ANDROID_NAME, this.app.getSignee().getIdentifier());
    }
    
    
    public void testGetInfoSet() {
        assertNotNull(this.app.getInfoSet());
        
        AppInformationSet info = this.app.getInfoSet();
        
        /*
         * COPY PASTA FROM XMLParserTest ...
         */
        
        // App names
        Map<Locale, String> appNames = info.getNames();
        assertEquals("Number of names", 3, appNames.size());
        assertEquals("German app-name", "KalenderDe", appNames.get("de"));
        assertEquals("English app-name", "Calendar", appNames.get("en"));
        assertEquals("Swedish app-name", "KalenderSv", appNames.get("sv"));
        
        // App descriptions
        Map<Locale, String> appDescs = info.getDescriptions();
        assertEquals("Number of descriptions", 3, appNames.size());
        assertEquals(
                "German app-description",
                "Die App wird verwendet um die Privacy Manangement Platform zu testen. Sie stellt einfache Funktionalitäten eines Kalenders zur Verfügung.",
                appDescs.get("de"));
        assertEquals(
                "English app-description",
                "This App is used to test the privacy management platform. It provides simple calendar functionalities.",
                appDescs.get("en"));
        assertEquals("Swedish app-description",
                "App används för att testa plattformen privatliv Manangement. Det ger enkel kalender funktioner.",
                appDescs.get("sv"));
        
        /*
         * ... EOF COPYPASTA 
         */
    }
    
}
