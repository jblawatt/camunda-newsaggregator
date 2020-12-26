package de.blawatt.camunda.rss;

import javax.print.attribute.AttributeSet;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
// import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class RSSFeedParser {

    static final String TITLE = "title";
    static final String SUMMARY = "summary";
    static final String CONTENT = "content";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "rights";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ENTRY = "entry";
    static final String PUBLISHED = "published";
    static final String UPDATED = "updated";
    static final String ID = "id";

    protected URL _url;

    public RSSFeedParser(URL url) {
        this._url = url;
    }

    public RSSFeedParser(String url) {
        try {
            this._url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected String getElementName(XMLEvent event) {
        return event.asStartElement().getName().getLocalPart();
    }

    public Feed readFeed() throws XMLStreamException {
        Feed feed = null;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream inputStream = read();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        boolean isFeedHeader = true;
        String title = "";
        String link = "";
        String language = "";
        String copyright = "";
        String author = "";
        String published = "";
        String updated = "";
        String summary = "";
        String content = "";
        String id = "";

        while (eventReader.hasNext()) {

            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                String localPart = getElementName(event);
                switch (localPart) {
                    case ENTRY:
                        // beim ersten mal ist es der header und damit erstellen
                        // wir ein neues feed
                        if (isFeedHeader) {
                            isFeedHeader = false;
                            feed = new Feed(id, title, link, language, copyright, updated);
                            event = eventReader.nextEvent();
                        }
                        break;
                    case TITLE:
                        title = getCharData(eventReader);
                        break;
                    case SUMMARY:
                        summary = getCharData(eventReader);
                        break;
                    case CONTENT:
                        content = getCharData(eventReader);
                        break;
                    case LINK:
                        link = getAttributeValue("href", event, eventReader);
                        break;
                    case ID:
                        id = getCharData(eventReader);
                        break;
                    case LANGUAGE:
                        language = getCharData(eventReader);
                        break;
                    case AUTHOR:
                        // FIXME: nested values
                        author = getCharData(eventReader);
                        break;
                    case PUBLISHED:
                        published = getCharData(eventReader);
                        break;
                    case UPDATED:
                        updated = getCharData(eventReader);
                        break;
                    case COPYRIGHT:
                        copyright = getCharData(eventReader);
                        break;
                }
            } else if (event.isEndElement()) {
                if (event.asEndElement().getName().getLocalPart() == ENTRY) {
                    FeedMessage feedMessage = new FeedMessage();
                    feedMessage.author = author;
                    feedMessage.content = content;
                    feedMessage.summary = summary;
                    feedMessage.id = id;
                    feedMessage.title = title;
                    feedMessage.link = link;
                    feedMessage.published = published;
                    feedMessage.updated = updated;
//                    feedMessage.source = _url.toString();
                    // todo: reset field
                    feed.entries.add(feedMessage);
                    event = eventReader.nextEvent();
                    continue;
                }

            }
        }

        return feed;

    }

    protected String getAttributeValue(String attributeName, XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        var element = event.asStartElement();
        var qname = new QName(attributeName);
        var attr = element.getAttributeByName(qname);
        return attr.getValue();
    }

    protected String getCharData(XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        XMLEvent event = eventReader.nextEvent();
        if (event.isCharacters()) {
            result = event.asCharacters().getData();
        }
        if (event.isAttribute()) {
            eventReader.nextEvent();
            return getCharData(eventReader);
        }
        return result;
    }

    protected InputStream read() {
        try {
            return _url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
