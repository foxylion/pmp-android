package de.unistuttgart.ipvs.pmp.test.api.resource.privacysetting;

import android.test.InstrumentationTestCase;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;

public class BooleanPrivacySettingTest extends InstrumentationTestCase {
    
    public static final String RG_PACKAGE = "rg.package";
    public static final ResourceGroup rg = new ResourceGroup(RG_PACKAGE, null) {
    };
    
    
    public void testPermits() throws Exception {
        BooleanPrivacySetting bps = new BooleanPrivacySetting();
        
        assertTrue(bps.permits(false, false));
        assertFalse(bps.permits("false", "true"));
        assertTrue(bps.permits(Boolean.TRUE, Boolean.FALSE));
        
        assertFalse(bps.permits(null, "true"));
        assertTrue(bps.permits("false", (String) null));
    }
    
    
    public void testGetHumanReadableValue() throws Exception {
        BooleanPrivacySetting bps = new BooleanPrivacySetting();
        
        assertEquals(Boolean.TRUE.toString(), bps.getHumanReadableValue(Boolean.TRUE.toString()));
    }
    
    
    public void testAssignResourceGroup() throws Exception {
        BooleanPrivacySetting bps = new BooleanPrivacySetting();
        bps.assignResourceGroup(rg, "");
        
        assertEquals(rg, bps.getResourceGroup());
    }
    
}
