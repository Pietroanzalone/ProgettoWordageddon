package com.example.progettowordageddon.ui.controller;

import com.example.progettowordageddon.model.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class Controller {

    @FXML
    private HBox navbar;

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

    protected void cambiaSchermata(String pathFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/progettowordageddon/ui/" + pathFXML));
            Parent root = loader.load();
            Scene scene = navbar.getScene();
            if (scene != null) scene.setRoot(root);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

}