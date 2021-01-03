package de.blawatt.camunda.mongo;

import com.mongodb.*;

import java.net.UnknownHostException;

public class MongoTest {

    public static void main(String[] args) throws UnknownHostException {
        
        
        MongoClient client = new MongoClient("127.0.0.1", 27017);
        
        DB database = client.getDB("feeds");
        DBCollection collection = database.getCollection("entries");
        
        var cursor = collection.find();
        while (cursor.hasNext()) {
            var item = cursor.next();
            System.out.println(item.get("id_"));
        }


    }

}
