package de.blawatt.camunda.newsaggregator;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class RSSHandleEntry implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("RSSHandleEntry");

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        var entry = execution.getVariable("entry");
        System.out.println(entry);
    }
}
