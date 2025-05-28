package com.example.progettowordageddon.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HomeController {
    @FXML
    private HBox navbar;

    public void initialize() {
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

}