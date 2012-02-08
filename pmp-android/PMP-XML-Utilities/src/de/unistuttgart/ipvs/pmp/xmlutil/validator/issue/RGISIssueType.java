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
package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

/**
 * Here are listed all possible types of RGISIssues
 * 
 * @author Marcus Vetter
 * 
 */
public enum RGISIssueType {
    
    NAME_LOCALE_INVALID,
    NAME_LOCALE_MISSING,
    NAME_LOCALE_OCCURRED_TOO_OFTEN,
    NAME_LOCALE_EN_MISSING,
    NAME_EMPTY,
    
    DESCR_LOCALE_INVALID,
    DESCR_LOCALE_MISSING,
    DESCR_LOCALE_OCCURRED_TOO_OFTEN,
    DESCR_LOCALE_EN_MISSING,
    DESCR_EMPTY,
    
    RGI_IDENTIFIER_MISSING,
    RGI_ICON_MISSING,
    RGI_CLASSNAME_MISSING,
    
    PS_IDENTIFIER_OCCURRED_TOO_OFTEN,
    PS_IDENTIFIER_MISSING,
    PS_VALID_VALUE_DESCRIPTION_MISSING
    
}
