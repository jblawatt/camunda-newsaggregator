package de.blawatt.camunda.newsaggregator.services;

import de.blawatt.camunda.rss.FeedMessage;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class RSSUpdateTagsService extends AbstractServiceBase {

    public RSSUpdateTagsService() {
        super("http://localhost:8080/engine-rest", "update-tags",  100000L);
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");
        Document document = null;
        try {
            document = Jsoup.connect(entry.id).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(document != null) {
            var result = document.select("meta[name=\"keywords\"]");
            for(Element elem : result) {
                String value = elem.attr("content");
                String[] keywords = value.split(",");
                for(String keyword: keywords) {
                    LOGGER.info(String.format("%s = %s", entry.id, keyword));
                }
            }
        }
        LOGGER.info(String.format("Updated Entry %s", entry.id));
        externalTaskService.complete(externalTask);;
    }
}
