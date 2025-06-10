package com.example.progettowordageddon.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @class Utente
 * @brief Un utente dell'applicazione.
 * @ingroup model
 *
 * La classe gestisce le credenziali dell'utente, incluso l'hashing della password
 * tramite [SHA-256](https://it.wikipedia.org/wiki/Secure_Hash_Algorithm) senza
 * l'uso di dipendenze esterne. Fornisce anche metodi per verificare le credenziali
 * e rappresentare l'utente come stringa.
 */
public class Utente implements Serializable {

    /** @brief Username univoco. */
    private final String username;

    /** @brief Password dell'utente.
     *
     * Se `hashedPassword` è `true`, è memorizzata in forma hash. */
    private String password;

    /** @brief Flag che indica se la password è stata già sottoposta ad hashing. */
    private boolean hashedPassword;

    /** @brief Flag che indica se l'utente ha privilegi di amministratore. */
    private final boolean isAdmin;

    /** @brief Costruttore completo per la classe Utente.
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

    /** @brief Restituisce il nome utente.
     * @return L'username dell'utente.
     */
    public String getUsername() {
        return username;
    }

    /** @brief Restituisce la password.
     *
     * Se hashedPassword è true, la password
     * restituita è criptata.
     * @return La password dell'utente.
     */
    public String getPassword() {
        return password;
    }

    /** @brief Verifica se la password è già hashata.
     * @return True se la password è hashata.
     */
    public boolean isHashedPassword() {
        return hashedPassword;
    }

    /** @brief Verifica se l'utente è un amministratore.
     * @return True se l'utente ha privilegi di admin.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /** @brief Applica l'hashing [SHA-256](https://it.wikipedia.org/wiki/Secure_Hash_Algorithm)
     * alla password, se non già hashata. Ignora la richiesta se la password è già hashata.
     *
     * @see hashPassword
     */
    public void hash() {
        if (hashedPassword)
            return;

        password = hashPassword(password);
        hashedPassword = true;
    }

    /** @brief Applica l'hashing [SHA-256](https://it.wikipedia.org/wiki/Secure_Hash_Algorithm)
     *         a una password.
     * @param password La password da hashare.
     * @return La password hashata
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

    /** \cond DOXY_SKIP */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash)
            hexString.append(String.format("%02x", b));
        return hexString.toString();
    }
    /** \endcond */

    /** @brief Confronta due oggetti Utente in base all'username.
     * @param obj Oggetto da confrontare.
     * @return True se gli username coincidono.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Utente u)
            return username.equals(u.username);
        return false;
    }

    /** @brief Calcola l'hash code basato sull'username.
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /** @brief Rappresenta l'utente come stringa.
     *
     * Se è admin, aggiunge un asterisco.
     * @return Stringa rappresentativa dell'utente.
     */
    @Override
    public String toString() {
        if (isAdmin) return username + " (*)";
        return username;
    }

}