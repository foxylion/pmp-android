/*
 * Copyright 2012 pmp-android development team
 * Project: RSSFeedResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSParser extends DefaultHandler {
    
    private final static String TAG_ITEM = "item";
    private final static String[] XMLTAGS = { "title", "link", "pubDate", "description" };
    
    private RSSItem currentItem = null;
    private List<RSSItem> rssItemList = new ArrayList<RSSItem>();
    private int currentIndex = -1;
    private boolean isParsing = false;
    private StringBuilder builder = new StringBuilder();
    
    
    public RSSParser(List<RSSItem> list) {
        super();
        this.rssItemList = list;
    }
    
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        
        if (this.isParsing && -1 != this.currentIndex && null != this.builder) {
            this.builder.append(ch, start, length);
        }
    }
    
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        if (localName.equalsIgnoreCase(TAG_ITEM)) {
            this.currentItem = new RSSItem();
            this.currentIndex = -1;
            this.isParsing = true;
            
            this.rssItemList.add(this.currentItem);
        } else {
            this.currentIndex = itemIndexFromString(localName);
            
            this.builder = null;
            
            if (-1 != this.currentIndex) {
                this.builder = new StringBuilder();
            }
        }
    }
    
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        
        if (localName.equalsIgnoreCase(TAG_ITEM)) {
            this.isParsing = false;
        } else if (this.currentIndex != -1) {
            if (this.isParsing) {
                switch (this.currentIndex) {
                    case 0:
                        this.currentItem.title = this.builder.toString();
                        break;
                    case 1:
                        this.currentItem.link = this.builder.toString();
                        break;
                    case 2:
                        this.currentItem.date = this.builder.toString();
                        break;
                    case 3:
                        this.currentItem.description = this.builder.toString();
                        break;
                }
            }
        }
    }
    
    
    private int itemIndexFromString(String tagname) {
        int itemindex = -1;
        
        for (int index = 0; index < XMLTAGS.length; ++index) {
            if (tagname.equalsIgnoreCase(XMLTAGS[index])) {
                itemindex = index;
                
                break;
            }
        }
        
        return itemindex;
    }
}
