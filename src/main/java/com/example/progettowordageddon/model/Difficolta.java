package com.example.progettowordageddon.model;

/**
 * @enum Difficolta
 * @brief Rappresenta il livello di difficoltà associato a un documento o contenuto.
 *
 * Ogni valore enum ha un codice testuale associato rappresentato da simboli ★.
 */
public enum Difficolta {
    /** @brief Livello di difficoltà facile (★). */
    FACILE    ("★"),

    /** @brief Livello di difficoltà media (★★). */
    MEDIA     ("★★"),

    /** @brief Livello di difficoltà difficile (★★★). */
    DIFFICILE ("★★★");

    /** @brief Codice simbolico della difficoltà (es. ★★). */
    private final String codice;

    /**
     * @brief Costruttore dell'enum.
     * @param codice Codice simbolico della difficoltà.
     */
    Difficolta(String codice) {
        this.codice = codice;
    }

    /**
     * @brief Restituisce la rappresentazione testuale della difficoltà.
     * @return Stringa con simboli ★ corrispondente al livello.
     */
    @Override
    public String toString() {
        return codice;
    }
}
