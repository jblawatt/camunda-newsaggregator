package de.blawatt.camunda.newsaggregator.services;


import de.blawatt.camunda.rss.FeedMessage;
import de.blawatt.camunda.rss.RSSFeedParser;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RSSMessageExtractService extends AbstractServiceBase {

    public RSSMessageExtractService() {
        super("http://localhost:8080/engine-rest", "load-feedurl",  100000L);
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        var feedUrl = (String) externalTask.getVariable("feed_url");
        LOGGER.info("GOT URL " + feedUrl);
        var feedParser = new RSSFeedParser(feedUrl);
        List<FeedMessage> entries = new ArrayList<FeedMessage>();
        try {
            entries = feedParser.readFeed().getEntries();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entryList", entries);
        externalTaskService.complete(externalTask, variables);
    }

    public static void main(String[] args) {
        (new RSSMessageExtractService()).run();
    }

}
