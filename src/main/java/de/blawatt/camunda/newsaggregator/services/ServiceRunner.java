package de.blawatt.camunda.newsaggregator.services;

public class ServiceRunner {

    public static void main(String[] args) {
        (new RSSMessageExtractService()).run();
        (new RSSMessageHandlerService()).run();
        (new RSSInsertNewService()).run();
        (new RSSUpdateExistingService()).run();
        (new RSSUpdateTagsService()).run();
    }

}
