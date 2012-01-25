package de.unistuttgart.ipvs.pmp.test.api.resource;

import android.content.Context;
import android.os.IBinder;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;

public class ResourceGroupTest extends InstrumentationTestCase {
    
    public static final String RG_PACKAGE = "take.me.to.the.bonuslevel";
    
    public static final String R1_ID = "R1";
    public static final String R2_ID = "R2";
    
    public static final String PS1_ID = "PS1";
    public static final String PS2_ID = "PS2";
    
    public static final String APP1_ID = "APP1";
    
    public static final Resource r1 = new Resource() {
        
        @Override
        public IBinder getAndroidInterface(String appIdentifier) {
            throw new RuntimeException(R1_ID);
        }
    };
    
    public static final Resource r2 = new Resource() {
        
        @Override
        public IBinder getAndroidInterface(String appIdentifier) {
            throw new RuntimeException(R2_ID);
        }
    };
    
    public static final AbstractPrivacySetting<?> ps1 = new BooleanPrivacySetting();
    public static final AbstractPrivacySetting<?> ps2 = new BooleanPrivacySetting();
    
    public static final Context mockContext = new MockContext();
    
    
    public void testRegisterResource() throws Exception {
        ResourceGroup rg = new ResourceGroup(RG_PACKAGE, null) {
        };
        
        rg.registerResource(R1_ID, r1);
        
        assertEquals(1, rg.getResources().size());
        assertEquals(R1_ID, rg.getResources().get(0));
        
        assertEquals(r1, rg.getResource(R1_ID));
        assertNull(rg.getResource(R2_ID));
    }
    
    
    public void testRegisterPrivacySetting() throws Exception {
        ResourceGroup rg = new ResourceGroup(RG_PACKAGE, null) {
        };
        
        rg.registerPrivacySetting(PS1_ID, ps1);
        
        assertEquals(1, rg.getPrivacySettings().size());
        assertEquals(PS1_ID, rg.getPrivacySettings().get(0));
        
        assertEquals(ps1, rg.getPrivacySetting(PS1_ID));
        assertNull(rg.getPrivacySetting(PS2_ID));
    }
    
    
    public void testRGPackage() throws Exception {
        ResourceGroup rg = new ResourceGroup(RG_PACKAGE, null) {
        };
        assertEquals(RG_PACKAGE, rg.getRgPackage());
    }
    
    
    public void testPMPConnectionInterface() throws Exception {
        ResourceGroup rg = new ResourceGroup(RG_PACKAGE, new IPMPConnectionInterface() {
            
            public String getPrivacySettingValue(String rgPackage, String psIdentifier, String appPackage) {
                return String.format("%s-%s-%s", rgPackage, psIdentifier, appPackage);
            }
            
            
            public Context getContext(String rgPackage) {
                return mockContext;
            }
            
            
            public void requestTransmission(String rgPackage, String resource) {
            }
        }) {
        };
        
        assertEquals(mockContext, rg.getContext());
        String expected = String.format("%s-%s-%s", RG_PACKAGE, PS1_ID, APP1_ID);
        assertEquals(expected, rg.getPMPPrivacySettingValue(PS1_ID, APP1_ID));
    }
    
}
