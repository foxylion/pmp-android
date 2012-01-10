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
package de.unistuttgart.ipvs.pmp.util.xml;

/**
 * This class is used to create parser exceptions with a type and details.
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLParserException extends RuntimeException {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = 5368379350530757117L;
    
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
         * A name with the same locale already exists
         */
        NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
        
        /**
         * A description with the same locale already exists
         */
        DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
        
        /**
         * A privacy setting with the same identifier already exists within the
         * same required resource group
         */
        PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
        
        /**
         * A service feature with the same identifier already exists
         */
        SERVICE_FEATURE_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
        
        /**
         * A required Resourcegroup with the same identifier already exists
         */
        REQUIRED_RESOUCEGROUP_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
        
        /**
         * The name of the root node is invalid
         */
        BAD_ROOT_NODE_NAME,
        
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
    
    /**
     * Type of the parser exception
     */
    private Type type;
    
    /**
     * Details of the exception
     */
    private String details;
    
    /**
     * The throwable
     */
    private Throwable throwable;
    
    
    /**
     * @see XMLParserException#XMLParserException(Type, String, Throwable)
     */
    public XMLParserException(Type type, String details) {
        this(type, details, null);
    }
    
    
    /**
     * Constructor
     * 
     * @param type
     *            of the exception
     * @param details
     *            of the exception
     */
    public XMLParserException(Type type, String details, Throwable throwable) {
        this.type = type;
        this.details = details;
        this.throwable = throwable;
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
     * Get the details of the exception
     * 
     * @return details of the exception
     */
    public String getDetails() {
        return this.details;
    }
    
    
    @Override
    public String getMessage() {
        // please use this instead of details, because only this way the details will actually be visible in the stack trace.
        return getDetails();
    }
    
    
    /**
     * Get the throwable
     * 
     * @return the throwable
     */
    public Throwable getParent() {
        return this.throwable;
    }
}
