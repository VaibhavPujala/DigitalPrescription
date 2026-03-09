package com.digitalprescription.business.model;

public class Doctor extends User {
    private String specialty;

    public Doctor() {}
    
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
