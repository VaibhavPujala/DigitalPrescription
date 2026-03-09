package com.digitalprescription.data;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    private static MongoDatabase database;

    private DatabaseConnection() {}

    public static MongoDatabase getDatabase() {
        if (database == null) {
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("digitalprescription");
            // Ensure collections exist
            database.createCollection("prescriptions");
            database.createCollection("users");
        }
        return database;
    }

    public static MongoCollection<Document> getPrescriptions() {
        return getDatabase().getCollection("prescriptions");
    }

    public static MongoCollection<Document> getUsers() {
        return getDatabase().getCollection("users");
    }
}
