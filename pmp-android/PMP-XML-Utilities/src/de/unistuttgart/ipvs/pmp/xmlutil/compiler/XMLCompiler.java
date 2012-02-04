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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class XMLCompiler {
    
    private static final String LINE_BREAK = System.getProperty("line.separator");
    
    
    private XMLCompiler() {
    }
    
    
    public static InputStream compileStream(XMLNode mainNode) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(compile(mainNode).getBytes("UTF-8"));
    }
    
    
    public static String compile(XMLNode mainNode) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append(LINE_BREAK);
        appendNode(mainNode, sb);
        
        return sb.toString();
    }
    
    
    private static void appendNode(XMLNode node, StringBuilder sb) {
        
        boolean textNode = (XMLNode.TEXT_VALUE & node.getFlags()) != 0;
        boolean cdataNode = (XMLNode.CDATA_VALUE & node.getFlags()) != 0;
        
        boolean noClose = (XMLNode.NO_CLOSE_TAG & node.getFlags()) != 0;
        boolean benignSpaces = (XMLNode.INSERT_SPACES_BENIGN & node.getFlags()) != 0;
        
        sb.append("<");
        sb.append(node.getName());
        
        // write attributes
        for (XMLAttribute xmla : node.getAttributes()) {
            sb.append(" ");
            sb.append(xmla.getName());
            sb.append(String.format("%s=%<s\"", benignSpaces ? " " : ""));
            sb.append(xmla.getValue());
            sb.append(String.format("\"%s", benignSpaces ? " " : ""));
        }
        
        if (textNode) {
            // text node
            sb.append(">");
            sb.append(String.format("%s%s%s", cdataNode ? "<![CDATA[" : "", node.getContent(), cdataNode ? "]]>" : ""));
            
        } else {
            if (!node.hasChildren()) {
                // empty node
                sb.append(String.format("%s%s>", benignSpaces ? " " : "", noClose ? "" : "/"));
                sb.append(LINE_BREAK);
                return;
                
            } else {
                // children node
                sb.append(String.format("%s>", benignSpaces ? " " : ""));
                sb.append(LINE_BREAK);
                
                for (XMLNode xmln : node.getChildren()) {
                    appendNode(xmln, sb);
                }
                
            }
        }
        
        // if we land here, we have to close the node
        if (!noClose) {
            sb.append("</");
            sb.append(node.getName());
            sb.append(">");
        }
        sb.append(LINE_BREAK);
    }
}
