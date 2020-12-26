package de.blawatt.camunda.rss;

public class FeedMessageWrapper {

    protected boolean isNew = false;
    protected boolean isUpdated = false;
    protected FeedMessage message;

    public FeedMessageWrapper(FeedMessage message, Boolean isNew, Boolean isUpdated) {
        this.message = message;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
    }

    public FeedMessage getFeedMessage() {
        return message;
    }

    public void setFeedMessage(FeedMessage message) {
        this.message = message;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public boolean isUnchanged() {
        return !isNew && !isUpdated;
    }

    public boolean isUpdated() {
        return this.isUpdated;
    }

}
