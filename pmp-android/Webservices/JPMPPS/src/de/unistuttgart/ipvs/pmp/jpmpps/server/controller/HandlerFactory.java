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

public class HandlerFactory {
    
    private static HandlerFactory instance;
    
    private Map<Class<? extends AbstractRequest>, IRequestHandler> handlerMap = new HashMap<Class<? extends AbstractRequest>, IRequestHandler>();
    
    private IRequestHandler defaultHander = new DefaultHandler();
    
    private HandlerFactory() {
        handlerMap.put(RequestCommunicationEnd.class, new CommunicationEndHandler());
        handlerMap.put(RequestResourceGroups.class, new ResourceGroupsHandler());
        handlerMap.put(RequestResourceGroupPackage.class, new ResourceGroupPackageHandler());
        handlerMap.put(RequestRGIS.class, new RGISHandler());
        handlerMap.put(RequestPresetSetSave.class, new PresetSetSaveHandler());
        handlerMap.put(RequestPresetSetLoad.class, new PresetSetLoadHandler());
    }
    
    
    public static HandlerFactory getInstance() {
        if(instance == null) {
            instance = new HandlerFactory();
        }
        return instance;
    }
    
    public void handle(ConnectionController controller, AbstractRequest request) {
        IRequestHandler handler = null;
        
        if (request == null) {
            handler = defaultHander;
        } else {
            handler = handlerMap.get(request.getClass());
        }
        
        if(handler == null) {
            handler = defaultHander;
        }
        
        AbstractResponse response = null;
        
        try {
            response = handler.process(request);
        } catch(IllegalArgumentException e) {
            controller.log("Something went wrong while processing the request", e);
            response = new InternalServerErrorResponse();
        }
        if (response == null) {
            controller.endCommunication();
        } else {
            controller.writeResponse(response);
        }
    }
}
