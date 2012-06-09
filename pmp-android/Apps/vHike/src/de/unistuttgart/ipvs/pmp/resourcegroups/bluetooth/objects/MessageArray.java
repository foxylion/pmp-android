/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

public class MessageArray implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1670918836332795158L;
    List<String> messages;
    
    
    public MessageArray(List<String> messages) {
        this.messages = messages;
    }
    
    
    public List<String> getMessages() {
        return this.messages;
    }
}
