/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;

public class RSSPanel implements IPanel {
    
    private List<String> headlines;
    private List<String> links;
    
    private ListView view;
    
    private Context context;
    
    
    public RSSPanel(Context context) {
        this.context = context;
        
        this.view = new ListView(context);
        
        this.headlines = new ArrayList<String>();
        this.links = new ArrayList<String>();
        
        try {
            URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            try {
                xpp.setInput(url.openConnection().getInputStream(), "UTF_8");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            boolean insideItem = false;
            int eventType = xpp.getEventType();
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem) {
                            this.headlines.add(xpp.nextText()); // extract the
                            // headline
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem) {
                            this.links.add(xpp.nextText()); // extract the link of
                            // article
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }
                
                eventType = xpp.next(); // move to next element
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.view.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, this.headlines));
        this.view.setOnItemClickListener(new OnItemClickListener() {
            
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Uri uri = Uri.parse(RSSPanel.this.links.get(pos));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                RSSPanel.this.context.startActivity(intent);
            }
        });
    }
    
    
    public View getView() {
        return this.view;
    }
    
    
    public String getTitle() {
        return "RSS";
    }
    
    
    public void update() {
    }
    
    
    public void upload(ProgressDialog dialog) {
    }
    
}
