package de.blawatt.camunda.newsaggregator.services;

import de.blawatt.camunda.mongo.MongoCollectionProvider;
import de.blawatt.camunda.mongo.MongoFeedSerializer;
import de.blawatt.camunda.rss.FeedMessage;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class RSSInsertNewService extends AbstractServiceBase {

    public RSSInsertNewService() {
        super("http://localhost:8080/engine-rest", "insert-message",  100000L);
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");

        DBCollection collection = null;
        try {
            // FIXME: zentrale settings
            collection = MongoCollectionProvider.getCollection("entries");
        } catch (UnknownHostException e) {            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            externalTaskService.handleFailure(externalTask, e.getMessage(), sw.toString(), 0, 0);
        }

        if (collection != null) {            
            DBObject dbObject = MongoFeedSerializer.serialize(entry);
            collection.insert(dbObject);
            externalTaskService.complete(externalTask);
        } 

    }
}
