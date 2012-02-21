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
package de.unistuttgart.ipvs.pmp.jpmpps.server.controller;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetLoad;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetSave;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestRGIS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroupPackage;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.InternalServerErrorResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.CommunicationEndHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.DefaultHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.IRequestHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.PresetSetLoadHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.PresetSetSaveHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.RGISHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.ResourceGroupPackageHandler;
import de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler.ResourceGroupsHandler;

/**
 * The {@link HandlerFactory} provides access to all handlers used for answering a request done by a client.
 * 
 * @author Jakob Jarosch
 * 
 */
public class HandlerFactory {
    
    /**
     * Instance of the {@link HandlerFactory}.
     */
    private static HandlerFactory instance;
    
    /**
     * Map for storing instances of all {@link IRequestHandler}s.
     */
    private Map<Class<? extends AbstractRequest>, IRequestHandler> handlerMap = new HashMap<Class<? extends AbstractRequest>, IRequestHandler>();
    
    /**
     * The default handler which is invoked when no other compatible {@link IRequestHandler} was found.
     */
    private IRequestHandler defaultHander = new DefaultHandler();
    
    /**
     * {@link HandlerFactory} constructor.
     */
    private HandlerFactory() {
        /* Register all handlers. */
        this.handlerMap.put(RequestCommunicationEnd.class, new CommunicationEndHandler());
        this.handlerMap.put(RequestResourceGroups.class, new ResourceGroupsHandler());
        this.handlerMap.put(RequestResourceGroupPackage.class, new ResourceGroupPackageHandler());
        this.handlerMap.put(RequestRGIS.class, new RGISHandler());
        this.handlerMap.put(RequestPresetSetSave.class, new PresetSetSaveHandler());
        this.handlerMap.put(RequestPresetSetLoad.class, new PresetSetLoadHandler());
    }
    
    /**
     * @return Returns the instance of {@link HandlerFactory}.
     */
    public static HandlerFactory getInstance() {
        if (instance == null) {
            instance = new HandlerFactory();
        }
        
        return instance;
    }
    
    
    /**
     * Handles a new incoming request.
     * 
     * @param controller {@link ConnectionController} which is responsible for the request.
     * @param request Request which should be handled.
     */
    public void handle(ConnectionController controller, AbstractRequest request) {
        IRequestHandler handler = null;
        
        if (request == null) {
            /* No request means default handler. */
            handler = this.defaultHander;
        } else {
            /* Try to find the correct handler from the map */
            handler = this.handlerMap.get(request.getClass());
        }
        
        /* If there is still no handler use the default handler. */
        if (handler == null) {
            handler = this.defaultHander;
        }
        
        AbstractResponse response = null;
        
        /* Try to generate a response. */
        try {
            response = handler.process(request);
        } catch (IllegalArgumentException e) {
            controller.log("Something went wrong while processing the request", e);
            response = new InternalServerErrorResponse();
        }
        
        /* When the response is null that is an indication for a communication end. */
        if (response == null) {
            controller.endCommunication();
        } else {
            controller.writeResponse(response);
        }
    }
}
