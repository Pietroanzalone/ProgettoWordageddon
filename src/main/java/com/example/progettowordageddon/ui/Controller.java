package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.*;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * @class Controller
 * @brief Controller principale per la gestione della UI.
 *
 * Gestisce la navbar, il logout e il cambio schermata.
 */
public class Controller {

    /** Barra di navigazione principale */
    @FXML
    private HBox navbar;

    /** Pulsante di ritorno alla schermata Home */
    @FXML
    private Button B_logout;

    /**
     * @brief Inizializza la navbar.
     * Metodo chiamato automaticamente all'inizializzazione del controller.
     */
    public void initialize() {
        initNavbar();
    }

    /**
     * @brief Inizializza la navbar impostando un listener per la trasparenza in base alla posizione del mouse.
     * La navbar diventa trasparente quando il mouse si sposta sotto la sua area.
     */
    private void initNavbar() {
        Platform.runLater(() -> {
            Scene scene = navbar.getScene();
            if (scene != null)
                scene.setOnMouseMoved(event -> {
                    double mouseY = event.getSceneY();
                    double navbarBottomY = navbar.localToScene(navbar.getBoundsInLocal()).getMaxY();

                    if (mouseY > navbarBottomY) {
                        if (navbar.getOpacity() > 0)
                            navbar.setOpacity(navbar.getOpacity() - 0.1);
                    } else
                        navbar.setOpacity(1);
                });
        });
    }

    /**
     * @brief Esce dall'applicazione.
     * Metodo associato al pulsante rosso in alto a destra.
     */
    @FXML
    private void exit() {
        Platform.exit();
    }

    /**
     * @brief Ritorna alla schermata Home.
     * Resetta la sessione e cambia schermata alla Home.
     */
    @FXML
    private void logout() {
        Sessione.utente = new Utente(null, null, true, false);
        cambiaSchermata("Home");
    }

    /**
     * @brief Cambia la schermata visualizzata caricando il file FXML indicato.
     *
     * @param nomeSchermata Nome della schermata (file FXML senza estensione) da caricare.
     */
    protected void cambiaSchermata(String nomeSchermata) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/progettowordageddon/ui/" + nomeSchermata + ".fxml"));
            Parent root = loader.load();
            Scene scene = navbar.getScene();
            if (scene != null) scene.setRoot(root);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * @brief Imposta l'icona del bottone di logout.
     *
     * Questo metodo va utilizzato da tutti i controller,
     * escluso HomeController dato che non ha il pulsante
     * "Logout" al suo interno.
     *
     * Esempio:
     * @verbatim
     @Override
     public void initialize() {
         super.initialize();
         setLogoutIcon();
     } @endverbatim
     */
    protected void setLogoutIcon() {
        try {
            ImageView icon = new ImageView(getClass().getResource("/com/example/progettowordageddon/ui/Assets/Logout.png").toExternalForm());
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            B_logout.setGraphic(icon);
            B_logout.setText("");
        } catch (NullPointerException e) {
            Logger.error(e.getMessage());
        }
    }

}