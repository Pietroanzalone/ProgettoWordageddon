package com.example.progettowordageddon.ui.controller;

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

public class Controller {

    @FXML
    private HBox navbar;

    @FXML
    private Button B_logout;

    public void initialize() {
        initNavbar();
    }

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

    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void logout() {
        Sessione.utente = new Utente(null, false);
        cambiaSchermata("Home");
    }

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