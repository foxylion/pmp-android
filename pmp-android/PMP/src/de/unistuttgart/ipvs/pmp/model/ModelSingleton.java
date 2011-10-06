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
package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.model.implementations.ModelImpl;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;

/**
 * {@link ModelSingleton} provides an instance of the {@link IModel} for direct access to the model data.
 * 
 * @author Jakob Jarosch
 */
public class ModelSingleton {
    
    /**
     * The instance.
     */
    private static final ModelSingleton instance = new ModelSingleton();
    
    
    /**
     * Private constructor, prevents direct object creation.
     */
    private ModelSingleton() {
        
    }
    
    
    /**
     * @return Returns the instance of the {@link ModelSingleton} class.
     */
    public static ModelSingleton getInstance() {
        return instance;
    }
    
    
    /**
     * @return Returns an instance of the {@link IModel} interface.
     */
    public IModel getModel() {
        return new ModelImpl();
    }
}
