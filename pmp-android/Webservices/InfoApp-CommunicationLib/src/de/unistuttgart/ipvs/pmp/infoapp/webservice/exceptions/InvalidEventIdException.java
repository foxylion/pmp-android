/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions;

import java.io.IOException;

/**
 * This exception will be thrown whenever a server request failed (JSON-result contains '"successful" : false'}
 * with a "invalid_event_id". See webservice specification for details. A copy of the message returned my the server
 * (JSON-result field 'msg') is stored in the exceptions message.
 * 
 * @author Patrick Strobel
 */
public class InvalidEventIdException extends IOException {
    
    private static final long serialVersionUID = -2092222093133784651L;
    
    
    public InvalidEventIdException(String s) {
        super(s);
    }
}
