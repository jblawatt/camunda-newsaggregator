package de.blawatt.camunda.newsaggregator.services;

import de.blawatt.camunda.mongo.MongoCollectionProvider;
import de.blawatt.camunda.mongo.MongoFeedSerializer;
import de.blawatt.camunda.rss.FeedMessage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

public class RSSUpdateExistingService extends AbstractServiceBase {

    public RSSUpdateExistingService() {
        super("http://localhost:8080/engine-rest", "update-message", 100000L);
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");

        DBCollection collection = null;
        try {
            collection = MongoCollectionProvider.getCollection("entries");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            externalTaskService.handleFailure(externalTask, e.getMessage(), sw.toString(), 0, 0);
        }
        if (collection != null) {
            DBObject dbObject = MongoFeedSerializer.serialize(entry);
            DBObject dbQueryObject = new BasicDBObject();
            dbQueryObject.put("id", entry.id);
            collection.update(dbQueryObject, dbObject);
            externalTaskService.complete(externalTask);
        }

    }

//     @Override
//     protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
//         FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");
//         Connection connection = null;
//         String statement = "UPDATE feed_entries SET title=?, author=?, published=?, updated=?, summary=?, content=? " +
//                 "WHERE id=?";

//         try {
//             connection = getConnection();
//         } catch (ClassNotFoundException e) {
//             e.printStackTrace();
//         } catch (
//                 SQLException throwables) {
//             throwables.printStackTrace();
//         }

//         PreparedStatement preparedStatement = null;

//         try {
//             preparedStatement = connection.prepareStatement(statement);
//         } catch (SQLException throwables) {
//             throwables.printStackTrace();
//         }

//         try {
//             preparedStatement.setString(1, entry.title);
//             preparedStatement.setString(2, entry.author);
//             preparedStatement.setString(3, entry.published);
//             preparedStatement.setString(4, entry.updated);
//             preparedStatement.setString(5, entry.summary);
//             preparedStatement.setString(6, entry.content);
// //            preparedStatement.setString(7, entry.source);
//             preparedStatement.setString(7, entry.id);
//         } catch (SQLException throwables) {
//             throwables.printStackTrace();
//         }

//         try {
//             preparedStatement.execute();
//         } catch (SQLException throwables) {
//             throwables.printStackTrace();
//         }

//         LOGGER.info(String.format("Updated Entry %s", entry.id));

//         externalTaskService.complete(externalTask);

//     }
}