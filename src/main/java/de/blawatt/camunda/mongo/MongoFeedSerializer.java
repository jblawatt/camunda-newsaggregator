package de.blawatt.camunda.mongo;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.blawatt.camunda.rss.FeedMessage;

public class MongoFeedSerializer {
    public static DBObject serialize(FeedMessage message) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("id", message.id);
        dbObject.put("title", message.title);
        dbObject.put("link", message.link);
        dbObject.put("published", toUnixTimestamp(message.published));
        dbObject.put("updated", toUnixTimestamp(message.updated));
        dbObject.put("summary", message.summary);
        dbObject.put("content", message.content);
        return dbObject;
    }

    public static long toUnixTimestamp(String dateString) {
        return ZonedDateTime
        .parse(dateString)
        .toLocalDateTime()
        .atZone(ZoneId.systemDefault())
        .toEpochSecond();
    }
}
