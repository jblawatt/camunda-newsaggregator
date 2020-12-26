package de.blawatt.camunda.rss;

import java.util.List;
import java.util.ArrayList;

public class Feed {
    final String id;
    final String title;
    final String link;
    final String language;
    final String copyright;
    final String updated;

    final List<FeedMessage> entries = new ArrayList<FeedMessage>();

    public Feed(String id, String title, String link, String language, String copyright, String updated) {
        this.title = title;
        this.link = link;
        this.language = language;
        this.copyright = copyright;
        this.updated = updated;
        this.id = id;
    }

    public List<FeedMessage> getEntries() {
        return this.entries;
    }
}
