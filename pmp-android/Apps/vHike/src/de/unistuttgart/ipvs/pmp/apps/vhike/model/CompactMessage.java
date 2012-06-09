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
package de.unistuttgart.ipvs.pmp.apps.vhike.model;

public class CompactMessage {
    
    public int id;
    public CompactUser sender, recipient;
    public boolean isOffer;
    public int status;
    public String message;
    
    
    public CompactMessage(int messageId, CompactUser sender, CompactUser recipient, boolean isOffer,
            String message) {
        this.id = messageId;
        this.sender = sender;
        this.recipient = recipient;
        this.isOffer = isOffer;
        this.message = message;
        
    }
    
    
    @Override
    public String toString() {
        return this.sender.name + (this.isOffer ? " (Request)" : "");
    }
    
    
    public int getId() {
        return this.id;
    }
    
}
