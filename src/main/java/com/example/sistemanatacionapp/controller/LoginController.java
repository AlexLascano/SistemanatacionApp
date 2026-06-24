package com.example.sistemanatacionapp.controller;

import com.example.sistemanatacionapp.util.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblError;

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        try {
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                lblError.setText("⚠ Por favor completa todos los campos");
                return;
            }

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                // Abrir ventana CRUD
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/crud.fxml"));
                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Sistema de Natación");
                stage.show();
            } else {
                lblError.setText("⚠ Usuario o contraseña incorrectos");
            }

        } catch (Exception e) {
            lblError.setText("Error: " + e.getMessage());
        }
    }
}