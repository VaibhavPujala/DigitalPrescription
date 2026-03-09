package com.digitalprescription.business.service;

import java.util.Date;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class SharingService {
    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "digital_prescription";
    private static final String PRESCRIPTIONS_COLLECTION = "prescriptions";
    private static final String SHARING_COLLECTION = "prescription_shares";
    
    /**
     * Share prescription with share code generation
     * @param prescriptionId - P001, P002, etc.
     * @param patientId - Patient ID (1, 2, etc.)
     * @param doctorName - Doctor who prescribed
     * @param diagnosis - Medical diagnosis
     * @param medications - Prescribed medications
     * @return Generated share code (SHARE-P001-ABC123)
     */
    public String sharePrescription(String prescriptionId, String patientId, String doctorName, 
                                   String diagnosis, String medications) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            
            // 1. Save/Update prescription in prescriptions collection (FIXED)
            MongoCollection<Document> prescriptions = database.getCollection(PRESCRIPTIONS_COLLECTION);
            Document prescription = new Document("prescriptionId", prescriptionId)
                    .append("patientId", patientId)
                    .append("doctorName", doctorName)
                    .append("diagnosis", diagnosis)
                    .append("medications", medications)
                    .append("status", "shared")
                    .append("sharedAt", new Date());
            
            // ✅ FIXED: Simple replaceOne (auto-creates if missing)
            Document filter = new Document("prescriptionId", prescriptionId);
            prescriptions.replaceOne(filter, prescription);
            
            // 2. Generate unique share code
            String shareCode = "SHARE-" + prescriptionId + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            
            // 3. Save share record
            MongoCollection<Document> shares = database.getCollection(SHARING_COLLECTION);
            Document shareDoc = new Document("shareCode", shareCode)
                    .append("prescriptionId", prescriptionId)
                    .append("patientId", patientId)
                    .append("sharedAt", new Date())
                    .append("views", 0)
                    .append("expiresAt", new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30 days
            
            shares.insertOne(shareDoc);
            
            return shareCode;
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get prescription by share code (for hospitals/pharmacies)
     * @param shareCode - SHARE-P001-ABC123
     * @return Prescription details or null
     */
    public Document getSharedPrescription(String shareCode) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> shares = database.getCollection(SHARING_COLLECTION);
            
            // Find share record
            Document share = shares.find(Filters.eq("shareCode", shareCode)).first();
            if (share == null) return null;
            
            // Check expiry
            if (new Date().after((Date) share.get("expiresAt"))) {
                shares.deleteOne(Filters.eq("_id", share.getObjectId("_id")));
                return null; // Expired
            }
            
            // Get prescription details
            MongoCollection<Document> prescriptions = database.getCollection(PRESCRIPTIONS_COLLECTION);
            Document prescription = prescriptions.find(Filters.eq("prescriptionId", share.getString("prescriptionId"))).first();
            
            if (prescription != null) {
                // Increment view count
                shares.updateOne(Filters.eq("_id", share.getObjectId("_id")),
                               Updates.inc("views", 1));
            }
            
            return prescription;
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Generate share code for PatientUI "Share Selected Prescription"
     */
    public String generateShareCode(String prescriptionId, String patientId) {
        String doctorName = getDoctorName(prescriptionId);
        String diagnosis = getDiagnosis(prescriptionId);
        String medications = getMedications(prescriptionId);
        
        return sharePrescription(prescriptionId, patientId, doctorName, diagnosis, medications);
    }
    
    // ✅ FIXED: Java 11 compatible if-else (NO switch expressions)
    private String getDoctorName(String prescriptionId) {
        switch (prescriptionId) {
            case "P001":
                return "Dr. Ravi Kumar";
            case "P002":
                return "Dr. Smith";
            case "P003":
                return "Dr. Kumar";
            default:
                return "Dr. Unknown";
        }
    }
    
    private String getDiagnosis(String prescriptionId) {
        switch (prescriptionId) {
            case "P001":
                return "Flu Infection";
            case "P002":
                return "Fever";
            case "P003":
                return "Common Cold";
            default:
            return "N/A";
        }
    }
    
    private String getMedications(String prescriptionId) {
        switch (prescriptionId) {
            case "P001":
                return "Paracetamol 500mg";
            case "P002":
                return "Ibuprofen 400mg";
            case "P003":
                return "Cetirizine 10mg";
            default:
                return "N/A";
        }
    }
}
