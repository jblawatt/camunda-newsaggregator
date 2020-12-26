package de.blawatt.camunda.newsaggregator.services;

import de.blawatt.camunda.rss.Feed;
import de.blawatt.camunda.rss.FeedMessage;
import de.blawatt.camunda.rss.FeedMessageWrapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RSSInsertNewService extends AbstractServiceBase {

    public RSSInsertNewService() {
        super("http://localhost:8080/engine-rest", "insert-message",  100000L);
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");

        Connection connection = null;
        String statement = "INSERT INTO feed_entries(id, title, author, published, updated, summary, content) " +
                                 "VALUES ( ?, ?, ?, ?, ?, ?, ? )";

        try {
            connection = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        PreparedStatement preparedStatement = null;

        try {
             preparedStatement = connection.prepareStatement(statement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            preparedStatement.setString(1, entry.id);
            preparedStatement.setString(2, entry.title);
            preparedStatement.setString(3, entry.author);
            preparedStatement.setString(4, entry.published);
            preparedStatement.setString(5, entry.updated);
            preparedStatement.setString(6, entry.summary);
            preparedStatement.setString(7, entry.content);
//            preparedStatement.setString(8, entry.source);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        LOGGER.info(String.format("Inserted Entry %s", entry.id));

        externalTaskService.complete(externalTask);

    }
}
