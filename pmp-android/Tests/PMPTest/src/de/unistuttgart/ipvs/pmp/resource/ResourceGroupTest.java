/*
 * Copyright 2011 pmp-android development team
 * Project: PMPTest
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
package de.unistuttgart.ipvs.pmp.resource;

import java.util.HashMap;

import android.os.IBinder;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;
import de.unistuttgart.ipvs.pmp.service.NullServiceStubImpl;

public class ResourceGroupTest extends InstrumentationTestCase {
    
    protected static final String SERVICE_ANDROID_NAME = "RG_SERVICE_ANDROID_NAME";
    
    protected static final String RG_DEFAULT_NAME = "RG_NAME";
    protected static final String RG_DEFAULT_DESC = "RG_DESC";
    
    private static final String RESOURCE_IDENTIFIER = "RG_RESOURCE";
    private static final String NO_RESOURCE_IDENTIFIER = "NO_RESOURCE";
    
    private static final String APP_IDENTIFIER = "APP";
    private static final String NO_APP_IDENTIFIER = "NO_APP";
    
    private static final String PL_IDENTIFIER = "RG_PL";
    private static final String PL_NAME = "RG_PL_NAME";
    private static final String PL_DESC = "RG_PL_DESC";
    private static final String PL_VALUE = "TRUE";
    private static final String NO_PL_DENTIFIER = "NO_PL";
    
    private ResourceGroup rg;
    private Resource res;
    private BooleanPrivacyLevel bpl;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.res = new Resource() {
            
            @Override
            public IBinder getAndroidInterface(String appIdentifier) {
                return new NullServiceStubImpl("Test class");
            }
        };
        
        this.bpl = new BooleanPrivacyLevel(PL_NAME, PL_DESC);
        
        this.rg = new ResourceGroup(new MockContext()) {
            
            @Override
            public void onRegistrationSuccess() {
                // IPC call
            }
            
            
            @Override
            public void onRegistrationFailed(String message) {
                // IPC call
            }
            
            
            @Override
            protected String getServiceAndroidName() {
                return SERVICE_ANDROID_NAME;
            }
            
            
            @Override
            public String getName(String locale) {
                return RG_DEFAULT_NAME;
            }
            
            
            @Override
            public String getDescription(String locale) {
                return RG_DEFAULT_DESC;
            }
        };
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testGetSignee() {
        assertNotNull(this.rg.getSignee());
        assertNotNull(this.rg.getSignee().getLocalPublicKey());
        assertEquals(PMPComponentType.RESOURCE_GROUP, this.rg.getSignee().getType());
        assertEquals(SERVICE_ANDROID_NAME, this.rg.getSignee().getIdentifier());
    }
    
    
    public void testRegisterResource() {
        this.rg.registerResource(RESOURCE_IDENTIFIER, this.res);
        assertEquals(1, this.rg.getResources().size());
        assertEquals(this.res, this.rg.getResource(RESOURCE_IDENTIFIER));
    }
    
    
    /**
     * Test whether getResource() returns null
     */
    public void testGetResource() {
        testRegisterResource();
        assertNull(this.rg.getResource(NO_RESOURCE_IDENTIFIER));
    }
    
    
    public void testRegisterPrivacyLevel() {
        this.rg.registerPrivacyLevel(PL_IDENTIFIER, this.bpl);
        assertEquals(1, this.rg.getPrivacyLevels().size());
        assertEquals(this.bpl, this.rg.getPrivacyLevel(PL_IDENTIFIER));
    }
    
    
    /**
     * Test whether getPrivacyLevel() returns null
     */
    public void testGetPrivacyLevel() {
        testRegisterPrivacyLevel();
        assertNull(this.rg.getPrivacyLevel(NO_PL_DENTIFIER));
    }
    
    
    public void testUpdateAccess() throws PrivacyLevelValueException {
        testRegisterPrivacyLevel();
        HashMap<String, String> values = new HashMap<String, String>();
        values.put(APP_IDENTIFIER, PL_VALUE);
        try {
            this.rg.updateAccess(PL_IDENTIFIER, values);
        } catch (UnsupportedOperationException uoe) {
            // we do not care when persistence files are not written, we just care about the privacy levels values
            // TODO: test persistence files sometime
        }
        
        assertEquals(Boolean.TRUE, this.rg.getPrivacyLevel(PL_IDENTIFIER).getValue(APP_IDENTIFIER));
        assertNull(this.rg.getPrivacyLevel(PL_IDENTIFIER).getValue(NO_APP_IDENTIFIER));
    }
    
}
