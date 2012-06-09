/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.jpmpps.io.request;

/**
 * Message should be submitted when a list of available {@link ResourceGroup}s
 * is requested.<br/>
 * 
 * <b>Filter options:<b>
 * <ul>
 * <li><b>package:</b> as a prefix before the queried package name, searches only for the package identifier.</li>
 * <li><b>comma:</b> a "," separates to different search requests.
 * </ul>
 * 
 * @author Jakob Jarosch
 */
public class RequestResourceGroups extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private String locale;
    private String filter;
    
    
    /**
     * Creates a new request for the given locale.
     * 
     * @param locale
     *            Locale for the response.
     */
    public RequestResourceGroups(String locale) {
        this(locale, null);
    }
    
    
    /**
     * Creates a new request for the given locale and the given filter.
     * 
     * @param locale
     *            Locale for the response.
     * @param filter
     *            Filter which should be applied on the search.
     */
    public RequestResourceGroups(String locale, String filter) {
        this.locale = locale;
        
        if (locale == null) {
            throw new IllegalArgumentException("locale should never be null");
        }
        
        if (filter == null) {
            filter = "";
        }
        this.filter = filter;
    }
    
    
    /**
     * @return Returns the locale of the request.
     */
    public String getLocale() {
        return this.locale;
    }
    
    
    /**
     * @return Returns the filter of the request.
     */
    public String getFilter() {
        return this.filter;
    }
}
