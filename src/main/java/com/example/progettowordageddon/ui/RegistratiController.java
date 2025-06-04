package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.UtentiDAO;
import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class RegistratiController extends Controller {

    @FXML
    private TextField T_username;

    @FXML
    private Label L_usernameWarn;

    @FXML
    private PasswordField P_password;

    @FXML
    private PasswordField P_confermaPassword;

    @FXML
    private Label L_passwordWarn;

    @FXML
    private Button B_registrati;

    @Override
    public void initialize() {
        impostaNavbar();
        impostaIconaLogout();

        logicaUsername();
        logicaPassword();
        logicaPulsanteRegistrazione();
    }

    @FXML
    public void registratiClicked() {
        Logger.log("Cliccato il pulsante: REGISTRATI");
        try {
            Sessione.utente = new Utente(
                    T_username.getText(),
                    P_password.getText(),
                    false,
                    false
            );
            Sessione.utente.hash();
            UtentiDAO.aggiungi(Sessione.utente);
            cambiaSchermata("Utente");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("SQL Exception");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void logicaUsername() {
        L_usernameWarn.setVisible(false);
        T_username.textProperty().addListener((observable, oldValue, newValue) ->
            L_usernameWarn.setVisible(UtentiDAO.contiene(newValue))
        );
    }

    private void logicaPassword() {
        L_passwordWarn.visibleProperty().bind(
            P_password.textProperty()
            .isEqualTo(P_confermaPassword.textProperty())
            .not()
        );
    }

    private void logicaPulsanteRegistrazione() {
        B_registrati.disableProperty().bind(
            T_username.textProperty().isEmpty()
            .or(P_password.textProperty().isEmpty())
            .or(L_usernameWarn.visibleProperty())
            .or(L_passwordWarn.visibleProperty())
        );
    }

}