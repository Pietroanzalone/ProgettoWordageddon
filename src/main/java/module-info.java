module com.example.progettowordageddon {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.progettowordageddon to javafx.fxml;
    exports com.example.progettowordageddon;
    exports com.example.progettowordageddon.ui.controller;
    opens com.example.progettowordageddon.ui.controller to javafx.fxml;
    exports com.example.progettowordageddon.model;
    opens com.example.progettowordageddon.model to javafx.fxml;
}