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
package de.unistuttgart.ipvs.pmp.resource.privacylevel;

import java.util.HashMap;

import android.test.AndroidTestCase;
import de.unistuttgart.ipvs.pmp.Constants;

/**
 * A test specifically for {@link BooleanPrivacyLevel} down to {@link PrivacySetting}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class RGBooleanPrivacyLevelTest extends AndroidTestCase {
    
    /**
     * App idents, NO_APP_IDENT should not be an app
     */
    private static final String APP_1_IDENT = "APP_1";
    private static final String APP_2_IDENT = "APP_2";
    private static final String NO_APP_IDENT = "NO_APP";
    
    /**
     * Not a boolean value
     */
    private static final String NON_BOOLEAN_VALUE = "MAYBE";
    
    private HashMap<String, String> hm;
    private BooleanPrivacyLevel bpl;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.bpl = new BooleanPrivacyLevel();
        this.hm = new HashMap<String, String>();
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testParseValueString() throws PrivacyLevelValueException {
        assertEquals(Boolean.TRUE, this.bpl.parseValue(Boolean.TRUE.toString()));
    }
    
    
    /**
     * Tests whether parseValue() correctly throws exceptions
     */
    public void testParseValueStringFaulty() {
        try {
            this.bpl.parseValue(NON_BOOLEAN_VALUE);
            fail();
        } catch (PrivacyLevelValueException plve) {
        }
    }
    
    
    public void testGetHumanReadableValue() throws PrivacyLevelValueException {
        assertEquals(Boolean.FALSE.toString(),
                this.bpl.getHumanReadableValue(Constants.DEFAULT_LOCALE, Boolean.FALSE.toString()));
    }
    
    
    /**
     * Tests whether testGetHumanReadableValue() throws exceptions
     */
    public void testGetHumanReadableValueFaulty() {
        try {
            this.bpl.getHumanReadableValue(Constants.DEFAULT_LOCALE, NON_BOOLEAN_VALUE);
            fail();
        } catch (PrivacyLevelValueException plve) {
        }
    }
    
    
    public void testPermitsStringString() throws PrivacyLevelValueException {
        assertTrue(this.bpl.permits(Boolean.TRUE.toString(), Boolean.FALSE.toString()));
    }
    
    
    /**
     * Tests wheter permits() throws exceptions
     */
    public void testPermitsStringStringFaulty() {
        try {
            this.bpl.permits(Boolean.TRUE.toString(), NON_BOOLEAN_VALUE);
            fail();
        } catch (PrivacyLevelValueException plve) {
        }
    }
    
    
    public void testPermitsStringT() throws PrivacyLevelValueException {
        this.hm.put(APP_1_IDENT, Boolean.TRUE.toString());
        this.hm.put(APP_2_IDENT, Boolean.FALSE.toString());
        
        this.bpl.setValues(this.hm);
        
        assertTrue(this.bpl.permits(APP_1_IDENT, true));
        assertFalse(this.bpl.permits(APP_2_IDENT, true));
        assertFalse(this.bpl.permits(NO_APP_IDENT, true));
    }
    
    
    public void testSetValues() throws PrivacyLevelValueException {
        this.hm.put(APP_1_IDENT, Boolean.TRUE.toString());
        this.hm.put(APP_2_IDENT, Boolean.FALSE.toString());
        
        this.bpl.setValues(this.hm);
        
        assertEquals(Boolean.TRUE, this.bpl.getValue(APP_1_IDENT));
        assertEquals(Boolean.FALSE, this.bpl.getValue(APP_2_IDENT));
        assertNull(this.bpl.getValue(NO_APP_IDENT));
    }
    
    
    /**
     * Tests whether setValues() throws exceptions
     */
    public void testSetValuesFaulty() {
        this.hm.put(APP_1_IDENT, Boolean.TRUE.toString());
        this.hm.put(APP_2_IDENT, NON_BOOLEAN_VALUE);
        
        try {
            this.bpl.setValues(this.hm);
            fail();
        } catch (PrivacyLevelValueException plve) {
        }
    }
    
}
