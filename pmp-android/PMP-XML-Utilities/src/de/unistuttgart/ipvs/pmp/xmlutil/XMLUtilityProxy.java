/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
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
package de.unistuttgart.ipvs.pmp.xmlutil;

/**
 * This proxy provides XML utilities for Apps, Resourcegroups and Presets
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLUtilityProxy {
    
    /**
     * The util classes {@link AppUtil}, {@link RGUtil} and {@link PresetUtil}
     */
    private static AppUtil appUtil = new AppUtil();
    private static RGUtil rgUtil = new RGUtil();
    private static PresetUtil presetUtil = new PresetUtil();
    
    
    /**
     * Protect the constructor. Use static methods.
     */
    private XMLUtilityProxy() {
    }
    
    
    /**
     * Get the {@link AppUtil}
     * 
     * @return {@link AppUtil}
     */
    public static AppUtil getAppUtil() {
        return appUtil;
    }
    
    
    /**
     * Get the {@link RGUtil}
     * 
     * @return {@link RGUtil}
     */
    public static RGUtil getRGUtil() {
        return rgUtil;
    }
    
    
    /**
     * Get the {@link PresetUtil}
     * 
     * @return {@link PresetUtil}
     */
    public static PresetUtil getPresetUtil() {
        return presetUtil;
    }
    
}
