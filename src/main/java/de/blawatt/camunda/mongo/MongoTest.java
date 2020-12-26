package de.blawatt.camunda.mongo;

import com.mongodb.*;

import java.net.UnknownHostException;

public class MongoTest {

    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient(new MongoClientURI("monogdb://localhost:27017"));
        DB database = client.getDB("feeds");
        DBCollection collection = database.getCollection("feeds");
        DBObject entry = new BasicDBObject()
                .append("id", "foobar");

        collection.insert(entry);

        System.out.println(entry.get("id_"));

    }

}
