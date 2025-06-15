package com.example.batchdemo.batchdemo.model;

public class StudentCsv {

    private Integer Id;
    private String firstName;
    private String lastName;
    private String email;

    public StudentCsv() {
    }

    public Integer getId() {
        return Id;
    }
    public void setId(Integer id) {
        Id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "StudentCsv [Id=" + Id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + "]";
    }
}
