package com.digitalprescription.business.model;

import java.time.LocalDate;

public class MedicalRecord {

    private int id;
    private int patientId;
    private String diagnosis;
    private String treatment;
    private LocalDate date;

    // Default Constructor
    public MedicalRecord() {
    }

    // Constructor without id (for inserts)
    public MedicalRecord(int patientId, String diagnosis, String treatment, LocalDate date) {
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
    }

    // Full Constructor
    public MedicalRecord(int id, int patientId, String diagnosis, String treatment, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
    }

    // ===== Getters =====

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public LocalDate getDate() {
        return date;
    }

    // ===== Setters =====

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}