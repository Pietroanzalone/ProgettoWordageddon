package com.example.progettowordageddon.model;

/// @class Lingua
/// @brief Rappresenta le lingue supportate nei documenti testuali.
/// @ingroup model
///
/// Ogni lingua è associata a un codice di abbreviazione a tre lettere.
public enum Lingua implements Comparable<Lingua> {
    /// Lingua italiana.
    ITALIANO ("ITA"),

    /// Lingua inglese.
    INGLESE  ("ENG"),

    /// Lingua francese.
    FRANCESE ("FRA"),

    /// Lingua spagnola.
    SPAGNOLO ("ESP");

    private final String codice; ///< Codice abbreviazione della lingua.

    /// @brief Costruttore dell'enumerazione Lingua.
    /// @param codice Codice associato alla lingua.
    Lingua(String codice) {
        this.codice = codice;
    }

    /// @brief Restituisce la rappresentazione in forma di codice della lingua.
    /// @return Codice di tre lettere della lingua.
    public String getCodice() {
        return codice;
    }

}