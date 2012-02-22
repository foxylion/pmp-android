/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS
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
package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.InvalidRequestResponse;

/**
 * The {@link DefaultHandler} can be used when no other handler matches the request.
 * It returns always a {@link InvalidRequestResponse}.
 * 
 * @author Jakob Jarosch
 */
public class DefaultHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        return new InvalidRequestResponse("");
    }
    
}
