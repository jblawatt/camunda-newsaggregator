package de.blawatt.camunda.newsaggregator;

import java.util.logging.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class Greeter implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("greeter");

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Hello Camunda!");
    }

}
