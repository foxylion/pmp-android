package de.unistuttgart.ipvs.pmp.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.test.AndroidTestCase;

public class ResourceGroupAccessTest extends AndroidTestCase {
    
    private static final String APP_IDENT = "APP_IDENT";
    private static final byte[] APP_PKEY = new byte[] { 0x7f, 0x42 };
    
    private static final String PL_1_IDENT = "PL_1_IDENT";
    private static final String PL_1_VALUE = "PL_1_VALUE";
    private static final String PL_2_IDENT = "PL_2_TNEDI";
    private static final String PL_2_VALUE = "PL_1_EULAV";
    private static final String NO_PL_IDENT = "NO_PL_IDENT";
    
    private ResourceGroupAccessHeader rgah = new ResourceGroupAccessHeader(APP_IDENT, APP_PKEY);
    private ResourceGroupAccess rga;
    private Map<String, String> privacyLvls = new HashMap<String, String>();
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        this.privacyLvls.put(PL_1_IDENT, PL_1_VALUE);
        this.privacyLvls.put(PL_2_IDENT, PL_2_VALUE);
        
        this.rgah = null;
        this.rga = null;
    }
    
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testResourceGroupAccess() {
        this.rgah = new ResourceGroupAccessHeader(APP_IDENT, APP_PKEY);
        this.rga = new ResourceGroupAccess(rgah, privacyLvls);
    }
    
    
    public void testGetHeader() {
        testResourceGroupAccess();
        assertEquals(APP_IDENT, rga.getHeader().getIdentifier());
        assertEquals(APP_PKEY.length, rga.getHeader().getPublicKey().length);
        for (int i = 0; i < APP_PKEY.length; i++) {
            assertEquals(APP_PKEY[i], rga.getHeader().getPublicKey()[i]);    
        }
    }
    
    
    public void testGetPrivacyLevelValue() {
        testResourceGroupAccess();
        assertEquals(PL_1_VALUE, rga.getPrivacyLevelValue(PL_1_IDENT));
        assertEquals(PL_2_VALUE, rga.getPrivacyLevelValue(PL_2_IDENT));
    }
    
    /**
     * Tests whether getPrivacyLevelValue() throws exceptions
     */
    public void testGetPrivacyLevelValueFaulty() {
        testResourceGroupAccess();
        assertNull(this.rga.getPrivacyLevelValue(NO_PL_IDENT));
    }
    
    
    public void testGetPrivacyLevelValues() {
        testResourceGroupAccess();
        assertEquals(2, this.rga.getPrivacyLevelValues().size());
        for (Entry<String, String> e : rga.getPrivacyLevelValues().entrySet()) {
            assertTrue(entryEquals(e, PL_1_IDENT, PL_1_VALUE) || entryEquals(e, PL_2_IDENT, PL_2_VALUE));
        }
    }
    
    private boolean entryEquals(Entry<String, String> e, String key, String value) {
        return key.equals(e.getKey()) && value.equals(e.getValue());
    }
    
}
