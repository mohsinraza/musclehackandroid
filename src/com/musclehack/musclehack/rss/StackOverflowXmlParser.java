package com.musclehack.musclehack.rss;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class StackOverflowXmlParser {
	// We don't use namespaces
	private static final String ns = null;
   
	public List parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}
	
	private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List entries = new ArrayList();

		//parser.require(XmlPullParser.START_TAG, ns, "channel");
		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equalsIgnoreCase("channel")){
				continue;
			}
			// Starts by looking for the entry tag
			if (name.equalsIgnoreCase("item")) {
				entries.add(readItem(parser));
			} else {
				skip(parser);
			}
		}  
		return entries;
	}
	public static class RssItem {
		public final String title;
		public final String link;
		public final String description;
		public final String dateTimeString;
		protected String shortDescription;
		protected String imageUrl;

		private RssItem(String title,
						String link,
						String description,
						String dateTimeString) {
			this.title = title;
			this.description = description;
			this.link = link;
			this.dateTimeString = dateTimeString;
			this.evalInfos();
		}
		
		protected void evalInfos(){
			String[] elements = this.description.split("src=\"");
			this.imageUrl = elements[1];
			elements = this.imageUrl.split("\" class");
			this.imageUrl = elements[0];
			elements = this.description.split("</a>");
			this.shortDescription = elements[1];
			elements = this.shortDescription.split("<img");
			this.shortDescription = elements[0];
			int nDescriptionChar = 150;
			if(this.shortDescription.length() > nDescriptionChar){
				this.shortDescription = this.shortDescription.substring(0, nDescriptionChar);
			}
			this.shortDescription += "...";
		}
		
		public String getShortDescription(){
			return this.shortDescription;
		}
		
		public String getImageUrl(){
			return this.imageUrl;
		}
		
		public Date getDateTime(){
			Date dateTime = RssItem.getDateTime(this.dateTimeString);
			return dateTime;
		}

		static public Date getDateTime(String dateString){
			ParsePosition pos = new ParsePosition(0);
			SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss +SSSS", Locale.US);
			//Wed, 15 May 2013 13:01:44 +0000
			Date dateTime = simpledateformat.parse(dateString, pos);
			return dateTime;
		}
	}

	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private RssItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "item");
		String title = null;
		String link = null;
		String description = null;
		String dateTimeString = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = this.readTitle(parser);
			} else if (name.equals("description")) {
				description = this.readDescription(parser);
			} else if (name.equals("link")) {
				link = this.readLink(parser);
			} else if (name.equals("pubDate")) {
				dateTimeString = this.readPubDate(parser);
			} else {
				skip(parser);
			}
		}
		return new RssItem(title, link, description, dateTimeString);
	}
		// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}
	  
	// Processes link tags in the feed.
	private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "link");
		String link = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "link");
		return link;
	}
		// Processes summary tags in the feed.
	private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "description");
		return description;
	}
	
	private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "pubDate");
		String pubDate = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "pubDate");
		return pubDate;
	}
		// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	 }
}

