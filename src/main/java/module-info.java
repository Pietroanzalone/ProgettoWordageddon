module com.example.progettowordageddon {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.progettowordageddon to javafx.fxml;
    exports com.example.progettowordageddon;
}