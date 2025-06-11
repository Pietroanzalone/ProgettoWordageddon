package com.example.progettowordageddon;

import java.io.IOException;

import com.example.progettowordageddon.model.Sessione;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/// @anchor mainClass
/// @class Main
/// @brief Il punto di ingresso dell'applicazione Wordageddon.
///
/// La classe Main estende
/// [Application](https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html)
/// e contiene il metodo `main` per l'avvio dell'applicazione
/// e il metodo `start` che inizializza
/// l'interfaccia grafica caricando il file FXML principale.
public class Main extends Application {

    /// @brief Dati della sessione corrente (utente loggato, schermata attiva...).
    public static Sessione sessione;

    /// @brief Metodo principale che lancia l'applicazione JavaFX.
    ///
    /// @param args Argomenti da linea di comando (ignorati).
    public static void main(String[] args) {
        launch();
    }

    /// @cond DOXY_SKIP
    static {
        sessione = new Sessione();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ui/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("WORDAGEDDON");
        stage.setScene(scene);
        stage.show();
    }
    /// @endcond

}