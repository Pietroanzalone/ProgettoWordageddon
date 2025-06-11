package com.example.progettowordageddon.model;

/// @class Difficolta
/// @brief Rappresenta il livello di difficoltà associato a un documento o
///        contenuto.
/// @ingroup model
///
/// Ogni valore enum ha un codice testuale associato rappresentato da
/// simboli ★.
public enum Difficolta {
    /// @brief Livello di difficoltà facile.
    FACILE    ("★"),

    /// @brief Livello di difficoltà media.
    MEDIA     ("★★"),

    /// @brief Livello di difficoltà difficile.
    DIFFICILE ("★★★");

    /// @brief Codice simbolico della difficoltà (es. ★★).
    private final String codice;

    /// @brief Costruttore dell'enum.
    /// @param codice Codice simbolico della difficoltà.
    Difficolta(String codice) {
        this.codice = codice;
    }

    /// @brief Restituisce la rappresentazione in forma di codice
    /// della difficoltà.
    /// @return Stringa con simboli ★ corrispondente al livello.
    public String getCodice() {
        return codice;
    }

}