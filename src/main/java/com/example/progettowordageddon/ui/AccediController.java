package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.database.UtentiDAO;
import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * @class AccediController
 * @brief Il controller per la gestione della schermata "Accedi".
 * @ingroup ui
 *
 * Questa classe definisce la logica di visualizzazione
 * dei messaggi di errore, l'abilitazione e la disabilitazione
 * dei pulsanti in base allo stato dei campi, e gestisce le
 * azioni da eseguire quanto l'utente interagisce con
 * l'interfaccia.
 */
public class AccediController extends Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private TextField T_username;

    @FXML
    private Label L_usernameWarn;

    @FXML
    private PasswordField P_password;

    @FXML
    private Label L_passwordWarn;

    @FXML
    private Button B_accedi;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza la barra di navigazione usando
     * {@link impostaNavbar} e {@link impostaIconaLogout}.
     */
    @Override
    public void initialize() {
        impostaNavbar();
        impostaIconaLogout();

        logicaUsername();
        logicaPassword();
        logicaPulsanteAccesso();
    }

    /**
     * @brief Definisce la logica di comparsa del
     *        messaggio di errore sotto il campo
     *        "Username".
     *
     * Questo messaggio compare quando il campo
     * "Username" è vuoto o l'username inserito
     * non è presente nel database.
     */
    private void logicaUsername() {
        L_usernameWarn.setVisible(false);
        T_username.textProperty().addListener((observable, oldValue, newValue) ->
                L_usernameWarn.setVisible(
                        !newValue.isEmpty()
                                && !UtentiDAO.contiene(newValue)
                )
        );
    }

    /**
     * @brief Definisce la logica di comparsa del
     *        messaggio di errore sotto il campo
     *        "Password".
     *
     * Questo messaggio compare quando il campo
     * "Password" è vuoto o la password inserita
     * non coincide con quella presente nel database.
     */
    private void logicaPassword() {
        L_passwordWarn.setVisible(false);
        P_password.textProperty().addListener((observable, oldValue, newValue) -> {
            if (T_username.getText().isEmpty() || L_usernameWarn.isVisible()) {
                L_passwordWarn.setVisible(false);
                return;
            }
            String hash = Utente.hashPassword(newValue);
            var u = UtentiDAO.get(T_username.getText());
            if (u == null) {
                mostraErrore(
                        "Errore SQL",
                        "Errore di caricamento dell'utente selezionato"
                ).showAndWait();
                L_passwordWarn.setVisible(true);
                return;
            }
            L_passwordWarn.setVisible(!u.getPassword().equals(hash));
        });
    }

    /**
     * @brief Definisce la logica di attivazione del
     *        pulsante "Accedi".
     *
     * Questo pulsante è attivo solo quando i campi
     * "Username" e "Password" non sono vuoti e non
     * compare nessun messaggio di errore.
     */
    private void logicaPulsanteAccesso() {
        B_accedi.disableProperty().bind(
                T_username.textProperty().isEmpty()
                        .or(P_password.textProperty().isEmpty())
                        .or(L_usernameWarn.visibleProperty())
                        .or(L_passwordWarn.visibleProperty())
        );
    }

    /**
     * @brief Metodo chiamato quando l'utente
     *        interagisce col pulsante "ACCEDI"
     *
     * Autentica l'utente eseguendo l'accesso e
     * salva le informazioni all'interno della
     * Sessione, poi passa alla schermata "Utente".
     */
    @FXML
    public void accediClicked() {
        Logger.log("Cliccato il pulsante: ACCEDI");
        Main.sessione.setUtente(UtentiDAO.get(T_username.getText()));
        cambiaSchermata("Utente");
    }

}