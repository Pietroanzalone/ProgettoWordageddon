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
 * @brief Controllore principale per la gestione della UI.
 *
 * Implementa funzioni di base, come il cambio di schermata e la chiusura
 * dell'applicazione.
 * Inoltre, gestisce interamente la barra di navigazione in alto.
 */
public class Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private HBox navbar;

    @FXML
    private Button B_logout;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza la barra di navigazione usando {@link initNavbar}.
     */
    public void initialize() {
        initNavbar();
    }

    /**
     * @brief Esce dall'applicazione.
     *
     * Chiude la finestra.
     * Metodo associato al pulsante rosso in alto a destra.
     */
    @FXML
    private void chiudi() {
        Platform.exit();
    }

    /**
     * @brief Ritorna alla schermata Home.
     *
     * Resetta la sessione e cambia schermata alla Home.
     * Metodo associato al pulsante giallo in alto a sinistra.
     */
    @FXML
    private void home() {
        Sessione.utente = new Utente(null, null, true, false);
        cambiaSchermata("Home");
    }

    /**
     * @brief Cambia la schermata visualizzata caricando il file FXML indicato.
     *
     * @param nomeSchermata Nome della schermata (file FXML senza estensione) da caricare.
     *
     * @note Questo è il metodo principale da usare per i pulsanti _@FXML_.
     *
     * Esempio:
     * ```java
     * @FXML
     * public void accediClicked() {
     *     cambiaSchermata("Accedi");
     * }
     * ```
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
     * @note Questo metodo va utilizzato da tutti i controller,
     *       escluso HomeController che non ha il pulsante "Logout"
     *       al suo interno.
     *
     * Esempio:
     * ```java
     * @Override
     * public void initialize() {
     *     super.initialize();
     *     setLogoutIcon();
     * }
     * ```
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

    /**
     * @brief Imposta la logica della barra di navigazione in alto.
     *
     * Aggiunge un listener che rende la barra di navigazione trasparente quando il mouse si sposta sotto la sua area.
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

}