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
 * Here are listed all possible types of AISIssues
 * 
 * @author Marcus Vetter
 * 
 */
public enum AISIssueType {
    
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
    
    SF_IDENTIFIER_MISSING,
    SF_IDENTIFIER_OCCURRED_TOO_OFTEN,
    SF_NO_SF_EXISTS,
    SF_AT_LEAST_TWO_SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE,
    
    RRG_IDENTIFIER_MISSING,
    RRG_IDENTIFIER_OCCURRED_TOO_OFTEN,
    RRG_MINREVISION_MISSING,
    RRG_MINREVISION_INVALID,
    RRG_NO_RRG_EXISTS,
    
    RPS_IDENTIFIER_MISSING,
    RPS_IDENTIFIER_OCCURRED_TOO_OFTEN,
    RPS_NO_RPS_EXISTS
    
}
