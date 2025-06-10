package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;

/**
 * @class PannelloControlloController
 * @brief Il controller per la gestione della schermata "PannelloControllo".
 * @ingroup ui
 *
 * Gestisce la navigazione verso le varie funzionalità amministrative
 * come la gestione delle stopwords, degli utenti admin e dei documenti.
 */
public class PannelloControlloController extends Controller {

    /**
     * @brief Metodo eseguito al click sul pulsante "Gestione Stopwords".
     *
     * Porta l'utente alla schermata per la gestione delle stopwords.
     */
    @FXML
    public void gestioneStopwordsClicked() {
        Logger.log("Cliccato il pulsante: GESTIONE STOPWORDS");
        cambiaSchermata("GestioneStopwords");
    }

    /**
     * @brief Metodo eseguito al click sul pulsante "Gestione Admin".
     *
     * Porta l'utente alla schermata per la gestione degli utenti amministratori.
     */
    @FXML
    public void gestioneAdminClicked() {
        Logger.log("Cliccato il pulsante: GESTIONE ADMIN");
        cambiaSchermata("GestioneAdmin");
    }

    /**
     * @brief Metodo eseguito al click sul pulsante "Gestione Documenti".
     *
     * Porta l'utente alla schermata per la gestione dei documenti.
     */
    @FXML
    public void gestioneDocumentiClicked() {
        Logger.log("Cliccato il pulsante: GESTIONE DOCUMENTI");
        cambiaSchermata("GestioneDocumenti");
    }
}
