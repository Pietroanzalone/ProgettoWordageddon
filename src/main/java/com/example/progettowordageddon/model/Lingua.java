package com.example.progettowordageddon.model;

/// @class Lingua
/// @brief Rappresenta le lingue supportate nei documenti testuali.
/// @ingroup model
///
/// Ogni lingua è associata a un codice di abbreviazione a tre lettere.
public enum Lingua implements Comparable<Lingua> {
    /// L%ingua italiana.
    ITALIANO,

    /// L%ingua inglese.
    INGLESE,

    /// L%ingua francese.
    FRANCESE,

    /// L%ingua spagnola.
    SPAGNOLO

}