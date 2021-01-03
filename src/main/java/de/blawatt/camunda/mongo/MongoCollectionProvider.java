package de.blawatt.camunda.mongo;

import java.net.UnknownHostException;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoCollectionProvider {
    public static DBCollection getCollection(String name) throws UnknownHostException {
        var client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        var database = client.getDB("feeds");
        var collection = database.getCollection(name);
        return collection;
    }
}
