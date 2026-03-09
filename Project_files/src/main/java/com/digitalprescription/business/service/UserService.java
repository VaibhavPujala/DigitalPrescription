package com.digitalprescription.business.service;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserService {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "digitalprescription";
    private static final String COLLECTION_NAME = "users";
    
    private MongoCollection<Document> usersCollection;
    
    public UserService() {
        try {
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            usersCollection = database.getCollection(COLLECTION_NAME);
            System.out.println("✅ MongoDB Connected!");
        } catch (Exception e) {
            System.err.println("❌ MongoDB Connection Failed: " + e.getMessage());
        }
    }
    
    public boolean saveUser(String username, String password, String role) {
        try {
            Document user = new Document()
                .append("username", username)
                .append("password", password)  // TODO: Hash in production
                .append("role", role)
                .append("createdAt", java.time.LocalDateTime.now().toString());
            
            usersCollection.insertOne(user);
            System.out.println("🆕 SAVED TO MONGODB: " + username);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Save failed: " + e.getMessage());
            return false;
        }
    }
    
    public boolean authenticateUser(String username, String password) {
        try {
            Document query = new Document("username", username)
                .append("password", password);
            return usersCollection.countDocuments(query) > 0;
        } catch (Exception e) {
            System.err.println("❌ Auth failed: " + e.getMessage());
            return false;
        }
    }
    
    public String getUserRole(String username) {
        try {
            Document query = new Document("username", username);
            Document user = usersCollection.find(query).first();
            return user != null ? user.getString("role") : "patient";
        } catch (Exception e) {
            return "patient";
        }
    }
}
