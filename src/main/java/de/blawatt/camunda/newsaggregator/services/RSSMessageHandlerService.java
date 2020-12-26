package de.blawatt.camunda.newsaggregator.services;

import de.blawatt.camunda.rss.Feed;
import de.blawatt.camunda.rss.FeedMessage;
import de.blawatt.camunda.rss.FeedMessageWrapper;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.sql.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RSSMessageHandlerService extends AbstractServiceBase {
    public RSSMessageHandlerService() {
        super("http://localhost:8080/engine-rest", "handle-item",  100000L);
    }

    public  FeedMessageWrapper createWrapper(FeedMessage message) throws SQLException, ClassNotFoundException {
        var con = getConnection();
        FeedMessageWrapper result = null;
        PreparedStatement statement = con.prepareStatement("SELECT updated FROM feed_entries WHERE id = ?");
        statement.setString(1, message.id);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            result = new FeedMessageWrapper(message, true, false);
        } else {
            String dbvalue = resultSet.getString("updated");
            boolean isUpdated = !dbvalue.equals(message.updated);
            result = new FeedMessageWrapper(message, false, isUpdated);
        }
        resultSet.close();
        con.close();

        return result;
    }

    @Override
    protected void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        FeedMessage entry = (FeedMessage) externalTask.getVariable("entry");
        LOGGER.info(String.format("ENTRY %s", entry));
        FeedMessageWrapper entryWrapper = null;
        try {
             entryWrapper = createWrapper(entry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entryIsNew", entryWrapper.isNew());
        variables.put("entryIsUpdated", entryWrapper.isUpdated());
        variables.put("entryIsUnchanged", entryWrapper.isUnchanged());
        externalTaskService.complete(externalTask, variables);
    }

    public static void main (String[] args) {
        (new RSSMessageHandlerService()).run();
    }
}
