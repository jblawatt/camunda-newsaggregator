package de.blawatt.camunda.newsaggregator.services;

import de.blawatt.camunda.mongo.MongoCollectionProvider;
import de.blawatt.camunda.rss.FeedMessage;
import de.blawatt.camunda.rss.FeedMessageWrapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import static de.blawatt.camunda.mongo.MongoFeedSerializer.toUnixTimestamp;

public class RSSMessageHandlerService extends AbstractServiceBase {
    public RSSMessageHandlerService() {
        super("http://localhost:8080/engine-rest", "handle-item", 100000L);
    }

    public FeedMessageWrapper createWrapper(FeedMessage entry) throws UnknownHostException {
        
        DBCollection collection = MongoCollectionProvider.getCollection("entries");      
        DBObject dbQueryObject = new BasicDBObject();
        dbQueryObject.put("id", entry.id);
        DBObject result = collection.findOne(dbQueryObject);
        
        if (result == null) {
            return new FeedMessageWrapper(entry, true, false);
        } else {
            long dbvalue =  (long)result.get("updated");
            long itemValue = toUnixTimestamp(entry.updated);
            boolean isUpdated = dbvalue != itemValue;
            return new FeedMessageWrapper(entry, false, isUpdated);
        }
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");
        LOGGER.info(String.format("ENTRY %s", entry));
        FeedMessageWrapper entryWrapper = null;
        try {
             entryWrapper = createWrapper(entry);
        } catch (UnknownHostException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            externalTaskService.handleFailure(externalTask, e.getMessage(), sw.toString(), 0, 0);
        }
        if (entryWrapper != null) {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("entryIsNew", entryWrapper.isNew());
            variables.put("entryIsUpdated", entryWrapper.isUpdated());
            variables.put("entryIsUnchanged", entryWrapper.isUnchanged());
            externalTaskService.complete(externalTask, variables);
        }
    }

    public static void main (String[] args) {
        (new RSSMessageHandlerService()).run();
    }
}
