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
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);

		if (isParsing && -1 != currentIndex && null != builder) {
			builder.append(ch, start, length);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		if (localName.equalsIgnoreCase(TAG_ITEM)) {
			currentItem = new RSSItem();
			currentIndex = -1;
			isParsing = true;

			rssItemList.add(currentItem);
		} else {
			currentIndex = itemIndexFromString(localName);

			builder = null;

			if (-1 != currentIndex)
				builder = new StringBuilder();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);

		if (localName.equalsIgnoreCase(TAG_ITEM)) {
			isParsing = false;
		} else if (currentIndex != -1) {
			if (isParsing) {
				switch (currentIndex) {
				case 0:
					currentItem.title = builder.toString();
					break;
				case 1:
					currentItem.link = builder.toString();
					break;
				case 2:
					currentItem.date = builder.toString();
					break;
				case 3:
					currentItem.description = builder.toString();
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