package com.digitalprescription.business.model;

import java.util.Date;

public class Patient extends User {
    private Date dateOfBirth;

    public Patient() {}
    
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}
