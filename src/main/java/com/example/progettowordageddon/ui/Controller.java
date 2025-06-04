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
 * @brief Il controller principale per la gestione della UI.
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
     * Inizializza la barra di navigazione usando
     * {@link initNavbar} e {@link setLogoutIcon}.
     */
    public void initialize() {
        initNavbar();
        setLogoutIcon();
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
            Sessione.schermata = nomeSchermata;
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * @brief Imposta l'icona del bottone di logout.
     *
     * @note Questo metodo non va utilizzato da HomeController,
     *       che deve fare l'Override del metodo initialize
     *
     * Esempio:
     * ```java
     * @Override
     * public void initialize() {
     *     initNavbar();
     * }
     * ```
     */
    private void setLogoutIcon() {
        try {
            B_logout.setGraphic(caricaImmagine("Logout.png", 20, 20));
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
    protected void initNavbar() {
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
     * @brief Carica un'immagine dalla cartella Assets e la trasforma in un'icona
     *
     * @param nome Nome del file (compresa l'estensione)
     * @param width Larghezza dell'icona
     * @param height Altezza dell'icona
     * @return Icona utilizzabile
     */
    protected ImageView caricaImmagine(String nome, int width, int height) {
        ImageView icona = new ImageView(getClass().getResource("/com/example/progettowordageddon/ui/Assets/" + nome).toExternalForm());
        icona.setFitWidth(width);
        icona.setFitHeight(height);
        return icona;
    }

}