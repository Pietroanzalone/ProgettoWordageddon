package com.example.progettowordageddon.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @class Utente
 * @brief Rappresenta un utente del sistema Wordageddon.
 *
 * La classe gestisce le credenziali dell'utente, incluso l'hashing della password
 * tramite SHA-256 senza l'uso di dipendenze esterne. Fornisce anche metodi
 * per verificare le credenziali e rappresentare l'utente come stringa.
 */
public class Utente {

    /** Nome utente univoco. */
    private final String username;

    /** Password dell'utente. Se hashedPassword è true,
     * è memorizzata in forma hash. */
    private String password;

    /** Flag che indica se la password è stata già sottoposta ad hashing. */
    private boolean hashedPassword;

    /** Flag che indica se l'utente ha privilegi di amministratore. */
    private final boolean isAdmin;

    /**
     * Costruttore completo per la classe Utente.
     *
     * @param username Nome utente.
     * @param password Password in chiaro o hash.
     * @param hashedPassword True se la password è già in forma hash.
     * @param isAdmin True se l'utente è amministratore.
     */
    public Utente(String username, String password, boolean hashedPassword, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.isAdmin = isAdmin;
    }

    /**
     * Restituisce il nome utente.
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password.
     * @return Password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Verifica se la password è già hashata.
     * @return True se la password è hashata.
     */
    public boolean isHashedPassword() {
        return hashedPassword;
    }

    /**
     * Verifica se l'utente è un amministratore.
     * @return True se l'utente ha privilegi di admin.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Applica l'hashing SHA-256 alla password, se non già hashata.
     * Ignora la richiesta se la password è già hashata.
     */
    public void hash() {
        if (hashedPassword)
            return;

        password = hashPassword(password);
        hashedPassword = true;
    }

    /**
     * Applica l'hashing SHA-256 a una stringa.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    /**
     * Converte un array di byte in una stringa esadecimale.
     * @param hash Array di byte.
     * @return Stringa esadecimale.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash)
            hexString.append(String.format("%02x", b));
        return hexString.toString();
    }

    /**
     * Verifica se una password in chiaro corrisponde all'hash memorizzato.
     * @param password Password in chiaro da verificare.
     * @return True se la password è corretta.
     */
    public boolean verificaPassword(String password) {
        if (!isHashedPassword()) return false;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String hashed = bytesToHex(hash);
            return hashed.equals(this.password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    /**
     * Confronta due oggetti Utente in base all'username.
     * @param obj Oggetto da confrontare.
     * @return True se gli username coincidono.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Utente u)
            return username.equals(u.username);
        return false;
    }

    /**
     * Calcola l'hash code basato sull'username.
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Rappresenta l'utente come stringa.
     * Se è admin, aggiunge un asterisco.
     * @return Stringa rappresentativa dell'utente.
     */
    @Override
    public String toString() {
        if (isAdmin) return username + " (*)";
        return username;
    }

}