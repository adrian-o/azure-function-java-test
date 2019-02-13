package br.com.viavarejo.azure.functions.models;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class EvaluationCollection {

    public String insertOne(String str) {
        System.out.println("mounting URI...");
        MongoClientURI uri = new MongoClientURI("mongodb://IP_HOST:PORT");

        MongoClient mongoClient = null;
        try {
            System.out.println("Creating client...");
            mongoClient = new MongoClient(uri);

            System.out.println("Getting database...");
            // Get database
            MongoDatabase database = mongoClient.getDatabase("viavarejo-db");

            System.out.println("Getting collection...");
            // Get collection
            MongoCollection<Document> collection = database.getCollection("evaluations");

            System.out.println("Mounting document...");
            // Insert documents
            Document document1 = new Document("test", str);

            System.out.println("Inserting document in collection...");
            collection.insertOne(document1);

            // Find fruits by name
            Document queryResult = collection.find(Filters.eq("test", str)).first();
            System.out.println(queryResult.toJson());

            System.out.println( "Completed successfully" );

            return queryResult.toJson();

        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}
