package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.UtentiDAO;
import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AccediController extends Controller {

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

    @Override
    public void initialize() {
        super.initialize();
        setLogoutIcon();
        logicaUsername();
        logicaPassword();
        logicaPulsanteAccesso();
    }

    public void accediClicked() {
        Logger.log("Cliccato il pulsante: ACCEDI");
        Sessione.utente = UtentiDAO.get(T_username.getText());
        cambiaSchermata("Utente");
    }

    private void logicaUsername() {
        L_usernameWarn.setVisible(false);
        T_username.textProperty().addListener((observable, oldValue, newValue) ->
            L_usernameWarn.setVisible(
                !newValue.isEmpty()
                && !UtentiDAO.contiene(newValue)
            )
        );
    }

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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("SQL Exception");
                alert.setContentText(null);
                alert.showAndWait();
                L_passwordWarn.setVisible(true);
                return;
            }
            L_passwordWarn.setVisible(!u.getPassword().equals(hash));
        });
    }

    private void logicaPulsanteAccesso() {
        B_accedi.disableProperty().bind(
            T_username.textProperty().isEmpty()
            .or(P_password.textProperty().isEmpty())
            .or(L_usernameWarn.visibleProperty())
            .or(L_passwordWarn.visibleProperty())
        );
    }

}