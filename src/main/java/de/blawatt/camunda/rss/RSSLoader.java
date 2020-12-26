package de.blawatt.camunda.rss;

import javax.xml.stream.XMLStreamException;

public class RSSLoader {

    public static void main(String[] args) throws XMLStreamException {
        var url = "https://www.heise.de/developer/rss/news-atom.xml";
        var feedParser = new RSSFeedParser(url);
        var feed = feedParser.readFeed();

        System.out.println(feed.title);

        for (FeedMessage message : feed.entries) {
            System.out.println(String.format("## %s\n   %s", message.title, message.link));
        }
    }

}
