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
package de.unistuttgart.ipvs.pmp.xmlutil.common.exception;

/**
 * This class is used to create parser exceptions with a type and message.
 * 
 * @author Marcus Vetter
 * 
 */
public class ParserException extends AbstractException {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 5368379350530757117L;
    
    /**
     * Type of the parser exception
     */
    private Type type;
    
    
    /**
     * Constructor
     * 
     * @param type
     *            Type of the exception
     * @param message
     *            Message of the exception
     * @param throwable
     */
    public ParserException(Type type, String message, Throwable throwable) {
        super(message, throwable);
        this.type = type;
    }
    
    
    /**
     * Constructor
     * 
     * @param type
     *            Type of the exception
     * @param message
     *            Message of the exception
     */
    public ParserException(Type type, String message) {
        this(type, message, null);
    }
    
    
    /**
     * Get the type of the exception
     * 
     * @return type of the exception
     */
    public Type getType() {
        return this.type;
    }
    
    /**
     * Possible types a parser exception can have.
     * 
     * @author Marcus Vetter
     */
    public enum Type {
        /**
         * A required node is missing
         */
        NODE_MISSING,
        
        /**
         * A node occurred too often
         */
        NODE_OCCURRED_TOO_OFTEN,
        
        /** A locale attribute is missing */
        LOCALE_MISSING,
        
        /**
         * A given locale attribute is invalid
         */
        LOCALE_INVALID,
        
        /**
         * The identifier is missing
         */
        IDENTIFIER_MISSING,
        
        /**
         * A value is missing
         */
        VALUE_MISSING,
        
        /**
         * Service Features are missing
         */
        SERVICE_FEATURE_MISSING,
        
        /**
         * Required Resourcegroup is missing
         */
        REQUIRED_RESOURCE_GROUP_MISSING,
        
        /**
         * Privacy Setting is missing
         */
        PRIVACY_SETTING_MISSING,
        
        /**
         * The name of the root node is invalid
         */
        BAD_ROOT_NODE_NAME,
        
        /**
         * The parser found an unexpected node
         */
        UNEXPECTED_NODE,
        
        /**
         * The parser found at least two Service Features, which address the
         * same required ResouceGroups and the same Privacy Settings with keys
         * and values
         */
        AT_LEAST_TWO_SFS_ADDRESS_SAME_RRGS_AND_PSS,
        
        /**
         * The parser crashed, unknown reason
         */
        CONFIGURATION_EXCEPTION,
        
        /**
         * The SAX-parser crashed, unknown reason
         */
        SAX_EXCEPTION,
        
        /**
         * Something with the input does not work
         */
        IO_EXCEPTION,
        
        /**
         * The XML input stream was null
         */
        NULL_XML_STREAM;
    }
    
}
