package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.os.RemoteException;

public class RSSFeedAdapter extends IRSSFeedAdapter.Stub {
    
    public RSSFeedAdapter(Context context, RSSFeedResource resource, String appIdentifier) {
        
    }
    
    
    @Override
    public RSSFeed fetch(String uri) throws RemoteException {
        RSSFeed rssFeed = new RSSFeed();
        try {
            rssFeed.url = uri;
            rssFeed.name = uri;
            
            URL url = new URL(uri);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            RSSParser rssParser = new RSSParser(rssFeed.itemList);
            xmlReader.setContentHandler(rssParser);
            InputSource is = new InputSource(url.openStream());
            xmlReader.parse(is);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssFeed;
    };
    
}
