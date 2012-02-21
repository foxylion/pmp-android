/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
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
package de.unistuttgart.ipvs.pmp.xmlutil.compiler.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class XMLCompiler {
    
    private XMLCompiler() {
    }
    
    
    public static InputStream compileStream(XMLNode mainNode) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(compile(mainNode).getBytes("UTF-8"));
    }
    
    
    public static String compile(XMLNode mainNode) {
        StringBuilder sb = new StringBuilder();
        
        appendNode(mainNode, sb);
        
        return prettyFormat(sb.toString());
        
    }
    
    
    private static String prettyFormat(String input) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 3);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
            
        } catch (TransformerException e) {
            return input;
        }
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
                return;
                
            } else {
                // children node
                sb.append(String.format("%s>", benignSpaces ? " " : ""));
                
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
    }
    
}
