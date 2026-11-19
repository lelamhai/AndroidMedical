package com.example.androidmedical.model;

public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;

    public RegisterRequest(String email, String password, String firstName, String lastName, String gender) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
