module com.example.sistemanatacionapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.sistemanatacionapp to javafx.fxml;
    opens com.example.sistemanatacionapp.controller to javafx.fxml;
    opens com.example.sistemanatacionapp.model to javafx.base;
    exports com.example.sistemanatacionapp;
}