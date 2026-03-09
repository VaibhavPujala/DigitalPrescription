package com.digitalprescription.business.service;

import java.util.ArrayList;
import java.util.List;

import com.digitalprescription.business.model.Prescription;

public class HistoryService {
    
    public List<Prescription> getPatientCompleteHistory(int patientId) {
        System.out.println("📋 Patient history for: " + patientId);
        return new ArrayList<>();
    }
    
    // ADD THIS METHOD - FIXES THE ERROR
    public List<Prescription> getDoctorPrescriptions(int doctorId) {
        System.out.println("📋 Doctor prescriptions for: " + doctorId);
        return new ArrayList<>();
    }
}
