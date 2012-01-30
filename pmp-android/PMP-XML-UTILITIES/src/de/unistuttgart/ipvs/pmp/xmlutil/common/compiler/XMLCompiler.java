package de.unistuttgart.ipvs.pmp.xmlutil.common.compiler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
