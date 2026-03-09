package com.digitalprescription.business.service;

import java.util.List;

import com.digitalprescription.business.model.Prescription;
import com.digitalprescription.data.dao.PrescriptionDAO;

public class PrescriptionService {
    private PrescriptionDAO dao = new PrescriptionDAO();

    public boolean createPrescription(Prescription prescription) {
        prescription.setDate(java.time.LocalDate.now().toString());
        prescription.setDoctorId(1); // Hardcoded current doctor
        return dao.createPrescription(prescription);
    }

    public List<Prescription> getPrescriptionsForPatient(int patientId) {
        return dao.getPrescriptionsForPatient(patientId);
    }
}
