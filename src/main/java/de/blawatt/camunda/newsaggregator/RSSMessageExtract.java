package de.blawatt.camunda.newsaggregator;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import de.blawatt.camunda.rss.Feed;
import de.blawatt.camunda.rss.RSSFeedParser;

public class RSSMessageExtract implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("RSSMessageExtract");

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String feedUrl = (String) execution.getVariable("feed_url");

        LOGGER.info(String.format("GOT FEED URL: %s", feedUrl));

        RSSFeedParser feedParser = new RSSFeedParser(feedUrl);

        Feed f = feedParser.readFeed();

        execution.setVariable("entryList", f.getEntries());


    }

}
