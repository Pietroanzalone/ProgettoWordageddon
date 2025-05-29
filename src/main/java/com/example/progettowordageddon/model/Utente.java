package com.example.progettowordageddon.model;

public class Utente {
    private final String username;
    private final boolean isAdmin;

    public Utente(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}