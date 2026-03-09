package com.digitalprescription.business.service;

import org.bson.Document;

import com.digitalprescription.data.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

public class AuthenticationService {
    private final MongoCollection<Document> users;

    public AuthenticationService() {
        this.users = DatabaseConnection.getUsers();
    }

    public boolean login(String username, String password) {
        Document userDoc = users.find(eq("username", username)).first();
        if (userDoc != null) {
            String storedPassword = userDoc.getString("password");
            return storedPassword != null && storedPassword.equals(password);
        }
        return false;
    }

    public void registerUser(String username, String password, String role, String name) {
        Document doc = new Document()
            .append("username", username)
            .append("password", password)
            .append("role", role)
            .append("name", name != null ? name : username);
        
        users.insertOne(doc);
    }
}
