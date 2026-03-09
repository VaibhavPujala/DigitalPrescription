package com.digitalprescription.data.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.digitalprescription.business.model.Prescription;
import com.digitalprescription.data.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.InsertOneResult;

public class PrescriptionDAO {
    private MongoCollection<Document> prescriptions = DatabaseConnection.getPrescriptions();

    public boolean createPrescription(Prescription prescription) {
        try {
            Document doc = new Document()
                .append("doctorId", prescription.getDoctorId())
                .append("patientId", prescription.getPatientId())
                .append("medication", prescription.getMedication())
                .append("dosage", prescription.getDosage())
                .append("diagnosis", prescription.getDiagnosis())
                .append("instructions", prescription.getInstructions())
                .append("date", prescription.getDate() != null ? prescription.getDate() : java.time.LocalDate.now().toString());

            InsertOneResult result = prescriptions.insertOne(doc);
            System.out.println("MongoDB Inserted ID: " + result.getInsertedId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prescription> getPrescriptionsForPatient(int patientId) {
        List<Prescription> list = new ArrayList<>();
        // Fetch from MongoDB and convert to Prescription objects
        prescriptions.find(eq("patientId", patientId)).forEach(doc -> {
            Prescription p = new Prescription();
            p.setId(doc.getInteger("_id", 0));
            p.setPatientId(doc.getInteger("patientId"));
            p.setMedication(doc.getString("medication"));
            p.setDosage(doc.getString("dosage"));
            p.setDiagnosis(doc.getString("diagnosis"));
            p.setInstructions(doc.getString("instructions"));
            p.setDate(doc.getString("date"));
            list.add(p);
        });
        return list;
    }
}
