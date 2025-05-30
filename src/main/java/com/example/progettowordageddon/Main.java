package com.example.progettowordageddon;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * @class Main
 * @brief Punto di ingresso dell'applicazione Wordageddon.
 *
 * La classe Main estende
 * [Application](https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html)
 * e contiene il metodo {@code main} per l'avvio dell'applicazione
 * e il metodo {@code start} che inizializza
 * l'interfaccia grafica caricando il file FXML principale.
 */
public class Main extends Application {

    /**
     * Metodo principale che lancia l'applicazione JavaFX.
     *
     * @param args Argomenti da linea di comando (non utilizzati).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Inizializza lo stage principale dell'applicazione.
     *
     * Carica l'interfaccia grafica definita nel file {@code Home.fxml},
     * imposta lo stile della finestra come undecorated e mostra la scena.
     *
     * @param stage Lo stage principale fornito da JavaFX.
     * @throws IOException Se il file FXML non viene caricato correttamente.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ui/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("WORDAGEDDON");
        stage.setScene(scene);
        stage.show();
    }

}