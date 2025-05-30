module com.example.progettowordageddon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.progettowordageddon to javafx.fxml;
    exports com.example.progettowordageddon;
    exports com.example.progettowordageddon.ui;
    opens com.example.progettowordageddon.ui to javafx.fxml;
    exports com.example.progettowordageddon.model;
    opens com.example.progettowordageddon.model to javafx.fxml;
}