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
package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetSet implements Serializable {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3603193651021108354L;
    
    /**
     * All presets
     */
    private List<Preset> presets = new ArrayList<Preset>();
    
    
    /**
     * Get all Presets
     * 
     * @return list with all Presets
     */
    public List<Preset> getPresets() {
        return presets;
    }
    
    
    /**
     * Add a Preset
     * 
     * @param preset
     *            Preset to add
     */
    public void addPreset(Preset preset) {
        this.presets.add(preset);
    }
    
    
    /**
     * Remove a Preset
     * 
     * @param preset
     *            Preset to remove
     */
    public void removePreset(Preset preset) {
        this.presets.remove(preset);
    }
    
}
