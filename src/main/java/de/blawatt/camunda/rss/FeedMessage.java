package de.blawatt.camunda.rss;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedMessage {
    public String author;
    public String link;
    public String title;
    public String published;
    public String updated;
    public String id;
    public String summary;
    public String content;
    // public String source;
}
