package com.benjsoft.validationdemo;

public class UserForm {
    private final String username;
    private final String email;
    private final String role;
    private final String birthDate;
    private final String notes;

    public UserForm(String username, String email, String role,
                    String birthDate, String notes) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.birthDate = birthDate;
        this.notes = notes;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getBirthDate() { return birthDate; }
    public String getNotes() { return notes; }
}