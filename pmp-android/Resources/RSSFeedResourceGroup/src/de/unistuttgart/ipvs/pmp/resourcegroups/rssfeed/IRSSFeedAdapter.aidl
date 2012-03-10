package de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed;

import de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed.RSSFeed;
import de.unistuttgart.ipvs.pmp.resourcegroups.rssfeed.RSSItem;

interface IRSSFeedAdapter {
    RSSFeed fetch(String url);
}