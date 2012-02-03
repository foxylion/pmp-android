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
package de.unistuttgart.ipvs.pmp.xmlutil.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Tobias Kuhn
 *
 */
public class XMLNode {
    
    public static final int DEFAULT = 0x0;
    public static final int TEXT_VALUE = 0x1;
    public static final int CDATA_VALUE = 0x2;
    
    public static final int NO_CLOSE_TAG = 0x1 << 16;
    //public static final int INSERT_RANDOM_SPACE_DESTRUCTIVE = 0x2 << 16;
    public static final int INSERT_SPACES_BENIGN = 0x4 << 16;
    
    private String name;
    
    private String content;
    
    private List<XMLNode> children;
    
    private List<XMLAttribute> attributes;
    
    private int flags;
    
    
    public XMLNode(String name) {
        this.name = name;
        this.content = null;
        this.children = new ArrayList<XMLNode>();
        this.attributes = new ArrayList<XMLAttribute>();
        this.flags = DEFAULT;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getContent() {
        return this.content;
    }
    
    
    public void setContent(String content) {
        this.content = content;
        if (content == null) {
            this.flags &= ~TEXT_VALUE;
        } else {
            this.flags |= TEXT_VALUE;
        }
    }
    
    
    public void setCDATAContent(String content) {
        // split "]]>" into several CDATA sections
        if (content == null) {
            this.flags &= ~(TEXT_VALUE | CDATA_VALUE);
            this.content = null;
        } else {
            this.flags |= TEXT_VALUE | CDATA_VALUE;
            this.content = content.replace("]]>", "]]]]><![CDATA[>");
        }
    }
    
    
    public int getFlags() {
        return this.flags;
    }
    
    
    public void setFlags(int flags) {
        this.flags = flags;
    }
    
    
    public List<XMLNode> getChildren() {
        return new ArrayList<XMLNode>(this.children);
    }
    
    
    public boolean hasChildren() {
        return this.children.size() > 0;
    }
    
    
    public void addChild(XMLNode child) {
        this.children.add(child);
        this.flags &= ~(TEXT_VALUE | CDATA_VALUE);
    }
    
    
    public boolean removeChild(XMLNode child) {
        return this.children.remove(child);
    }
    
    
    public List<XMLAttribute> getAttributes() {
        return new ArrayList<XMLAttribute>(this.attributes);
    }
    
    
    public boolean hasAttributes() {
        return this.attributes.size() > 0;
    }
    
    
    public void addAttribute(XMLAttribute attribute) {
        this.attributes.add(attribute);
    }
    
}
