package de.blawatt.camunda.newsaggregator.services;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class AbstractServiceBase {

    protected final String baseUrl;
    protected final String topic;
    protected final Long lockDuration;

    protected final Logger LOGGER;

    public AbstractServiceBase(String baseUrl, String topic, Long lockDuration) {
        this.LOGGER = Logger.getLogger(this.getClass().getName());
        this.baseUrl = baseUrl;
        this.topic = topic;
        this.lockDuration = lockDuration;
    }

    protected ExternalTaskClient createClient() {
        return ExternalTaskClient.create()
            .baseUrl(baseUrl)
            .asyncResponseTimeout(10000)
            .build();
    }

    public void run() {
        this.createClient()
                .subscribe(topic)
                .lockDuration(lockDuration)
                .handler(this::execute)
                .open();
        System.out.println(String.format("Running Service %s ...", this.getClass().getName()));
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:postgresql://172.17.51.125:5432/feeds";
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(connectionString, "feeds", "feeds");
    }

    protected abstract void execute(ExternalTask externalTask, ExternalTaskService externalTaskService);

}
