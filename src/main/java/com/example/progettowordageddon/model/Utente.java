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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Utente u)
            return username.equals(u.username);
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

}