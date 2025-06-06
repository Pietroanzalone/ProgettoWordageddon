package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import com.example.progettowordageddon.model.Sessione;
import com.example.progettowordageddon.model.Utente;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
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
    private Button B_exit;

    @FXML
    private Button B_logout;

    @FXML
    private Button B_home;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza la barra di navigazione usando
     * {@link impostaNavbar}, {@link impostaIconaLogout}
     * e {@link impostaIconaHome}.
     *
     *
     * @note Se un controller ha la barra di navigazione che non comprende tutti
     *       e tre i pulsanti, deve assicurarsi di fare l'Override di questo
     *       metodo e usare solo i comandi che gli servono.
     *       Esempio per AccediController che ha solo i pulsanti "Esci" e "Logout":
     *       ```java
     *       @Override
     *       public void initialize() {
     *           impostaNavbar();
     *           impostaIconaLogout();
     *       }
     *       ```
     */
    public void initialize() {
        impostaNavbar();
        impostaIconaLogout();
        impostaIconaHome();
    }

    /**
     * @brief Esce dall'applicazione.
     *
     * Chiude la finestra.
     * Metodo associato al pulsante rosso in alto a destra.
     */
    @FXML
    private void chiudi() {
        Logger.log("Cliccato il pulsante della navbar: CHIUDI");
        Platform.exit();
    }

    /**
     * @brief Ritorna alla schermata iniziale.
     *
     * Resetta la sessione e cambia schermata alla Home.
     * Metodo associato al pulsante giallo in alto a sinistra.
     */
    @FXML
    private void logout() {
        Logger.log("Cliccato il pulsante della navbar: LOGOUT");
        Sessione.utente = new Utente(null, null, true, false);
        cambiaSchermata("Home");
    }

    /**
     * @brief Ritorna alla schermata Home.
     *
     * Annulla le operazioni e torna alla schermata Utente.
     * Metodo associato al pulsante verde in alto.
     */
    @FXML
    private void home() {
        Logger.log("Cliccato il pulsante della navbar: HOME");
        Sessione.quizAttivo = false;
        cambiaSchermata("Utente");
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
            Logger.log("Passo alla schermata " + nomeSchermata);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * @brief Imposta l'icona del pulsante Logout.
     */
    protected void impostaIconaLogout() {
        try {
            B_logout.setGraphic(caricaImmagine("Logout.png", 20, 20));
            B_logout.setText("");
            B_logout.setTooltip(new Tooltip("Logout"));
        } catch (NullPointerException e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * @brief Imposta l'icona del pulsante Home.
     */
    protected void impostaIconaHome() {
        try {
            B_home.setGraphic(caricaImmagine("Home.png", 20, 20));
            B_home.setText("");
            B_home.setTooltip(new Tooltip("Home"));
        } catch (NullPointerException e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * @brief Imposta la logica della barra di navigazione in alto
     *        e del pulsante di chiusura dell'applicazione.
     *
     * Aggiunge un listener che rende la barra di navigazione
     * trasparente quando il mouse si sposta sotto la sua area.
     */
    protected void impostaNavbar() {
        B_exit.setTooltip(new Tooltip("Chiudi"));
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
     * @brief Metodo di utility per caricare un'immagine dalla cartella
     * `Assets` del progetto e la restituisce come oggetto
     * [ImageView](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html).
     *
     * L'immagine viene scalata alle dimensioni specificate tramite i parametri {@code larghezza}
     * e {@code altezza}.
     *
     * @param nome   Il nome del file immagine, inclusa l'estensione (es. "logo.png").
     * @param larghezza  La larghezza desiderata per l'icona in pixel.
     * @param altezza L'altezza desiderata per l'icona in pixel.
     * @return Un oggetto {@code ImageView} contenente l'immagine scalata, oppure {@code null} se
     * il file non viene trovato.
     */
    protected ImageView caricaImmagine(String nome, int larghezza, int altezza) {
        try {
            ImageView icona = new ImageView(getClass().getResource("/com/example/progettowordageddon/ui/Assets/" + nome).toExternalForm());
            icona.setFitWidth(larghezza);
            icona.setFitHeight(altezza);
            return icona;
        } catch (NullPointerException e) {
            Logger.error("File " + nome + " non trovato");
            return null;
        }
    }

}