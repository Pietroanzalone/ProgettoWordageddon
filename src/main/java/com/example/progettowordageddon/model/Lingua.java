package com.example.progettowordageddon.model;

/// @class Lingua
/// @brief Rappresenta le lingue supportate nei documenti testuali.
/// @ingroup model
///
/// Ogni lingua è associata a un codice di abbreviazione a tre lettere.
public enum Lingua implements Comparable<Lingua> {
    /// L%ingua italiana.
    ITALIANO ("ITA"),

    /// L%ingua inglese.
    INGLESE  ("ENG"),

    /// L%ingua francese.
    FRANCESE ("FRA"),

    /// L%ingua spagnola.
    SPAGNOLO ("ESP");

    /// Codice abbreviazione della lingua.
    private final String codice;

    /// @brief Costruttore.
    /// @param codice Codice associato alla lingua.
    Lingua(String codice) {
        this.codice = codice;
    }

    /// @brief Restituisce la rappresentazione in forma di codice della
    ///        lingua.
    /// @return Codice di tre lettere della lingua.
    public String getCodice() {
        return codice;
    }

}