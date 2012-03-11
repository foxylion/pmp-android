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

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetSet extends IssueLocation implements Serializable, IPresetSet {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -3603193651021108354L;
    
    /**
     * List of {@link IPreset}s
     */
    private List<IPreset> presets = new ArrayList<IPreset>();
    
    
    @Override
    public List<IPreset> getPresets() {
        return this.presets;
    }
    
    
    @Override
    public void addPreset(IPreset preset) {
        this.presets.add(preset);
    }
    
    
    @Override
    public void removePreset(IPreset preset) {
        this.presets.remove(preset);
    }
}
