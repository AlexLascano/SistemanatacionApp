package com.example.sistemanatacionapp.controller;

import com.example.sistemanatacionapp.model.Participante;
import com.example.sistemanatacionapp.util.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class CrudController {

    @FXML private TextField txtCedula, txtNombre, txtApellido, txtEdad, txtCorreo;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private RadioButton rbMasculino, rbFemenino;
    @FXML private ToggleGroup generoGroup;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblMensaje;

    @FXML private TableView<Participante> tableView;
    @FXML private TableColumn<Participante, String> colCedula, colNombre, colApellido, colCorreo, colCategoria, colGenero;
    @FXML private TableColumn<Participante, Integer> colEdad;

    private Connection connection;

    @FXML
    public void initialize() {
        connection = Conexion.getInstancia().getConnection();

        cmbCategoria.setItems(FXCollections.observableArrayList("Infantil", "Juvenil", "Adulto"));

        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        handleLeer();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) cargarDatos(newVal);
        });
    }

    @FXML
    private void handleCrear() {
        try {
            if (!validar()) return;

            String sql = "INSERT INTO participantes (cedula, nombre, apellido, edad, correo, categoria, genero, observaciones) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtCedula.getText());
            ps.setString(2, txtNombre.getText());
            ps.setString(3, txtApellido.getText());
            ps.setInt(4, Integer.parseInt(txtEdad.getText()));
            ps.setString(5, txtCorreo.getText());
            ps.setString(6, cmbCategoria.getValue());
            ps.setString(7, getGenero());
            ps.setString(8, txtObservaciones.getText());
            ps.executeUpdate();

            lblMensaje.setText("✅ Participante creado exitosamente");
            handleLimpiar();
            handleLeer();

        } catch (SQLIntegrityConstraintViolationException e) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("⚠ La cédula o correo ya están registrados");
        } catch (SQLException e) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleLeer() {
        try {
            ObservableList<Participante> lista = FXCollections.observableArrayList();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM participantes");
            while (rs.next()) {
                lista.add(new Participante(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("edad"),
                        rs.getString("correo"),
                        rs.getString("categoria"),
                        rs.getString("genero"),
                        rs.getString("observaciones")
                ));
            }
            tableView.setItems(lista);
        } catch (SQLException e) {
            lblMensaje.setText("Error al leer: " + e.getMessage());
        }
    }

    @FXML
    private void handleActualizar() {
        try {
            if (!validar()) return;
            String sql = "UPDATE participantes SET nombre=?, apellido=?, edad=?, correo=?, categoria=?, genero=?, observaciones=? WHERE cedula=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellido.getText());
            ps.setInt(3, Integer.parseInt(txtEdad.getText()));
            ps.setString(4, txtCorreo.getText());
            ps.setString(5, cmbCategoria.getValue());
            ps.setString(6, getGenero());
            ps.setString(7, txtObservaciones.getText());
            ps.setString(8, txtCedula.getText());
            ps.executeUpdate();

            lblMensaje.setStyle("-fx-text-fill: #00ff88;");
            lblMensaje.setText("✅ Participante actualizado exitosamente");
            handleLimpiar();
            handleLeer();

        } catch (SQLException e) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        Participante seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("⚠ Selecciona un participante de la tabla");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro?");
        alert.setContentText("Se eliminará a: " + seleccionado.getNombre());

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM participantes WHERE cedula=?");
                ps.setString(1, seleccionado.getCedula());
                ps.executeUpdate();

                lblMensaje.setStyle("-fx-text-fill: #00ff88;");
                lblMensaje.setText("✅ Participante eliminado exitosamente");
                handleLimpiar();
                handleLeer();

            } catch (SQLException e) {
                lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
                lblMensaje.setText("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLimpiar() {
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEdad.clear();
        txtCorreo.clear();
        txtObservaciones.clear();
        cmbCategoria.setValue(null);
        generoGroup.selectToggle(null);
        lblMensaje.setText("");
    }

    private void cargarDatos(Participante p) {
        txtCedula.setText(p.getCedula());
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtEdad.setText(String.valueOf(p.getEdad()));
        txtCorreo.setText(p.getCorreo());
        cmbCategoria.setValue(p.getCategoria());
        if (p.getGenero().equals("Masculino")) rbMasculino.setSelected(true);
        else rbFemenino.setSelected(true);
        txtObservaciones.setText(p.getObservaciones());
    }

    private boolean validar() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtApellido.getText().isEmpty() || txtEdad.getText().isEmpty() ||
                txtCorreo.getText().isEmpty() || cmbCategoria.getValue() == null) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("⚠ Todos los campos son obligatorios");
            return false;
        }
        try {
            int edad = Integer.parseInt(txtEdad.getText());
            if (edad <= 5) {
                lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
                lblMensaje.setText("⚠ La edad debe ser mayor a 5");
                return false;
            }
        } catch (NumberFormatException e) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("⚠ La edad debe ser un número");
            return false;
        }
        if (!txtCorreo.getText().contains("@")) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("⚠ El correo debe contener @");
            return false;
        }
        if (!txtCedula.getText().matches("\\d+")) {
            lblMensaje.setStyle("-fx-text-fill: #ff6b6b;");
            lblMensaje.setText("⚠ La cédula solo debe contener números");
            return false;
        }
        return true;
    }

    private String getGenero() {
        if (rbMasculino.isSelected()) return "Masculino";
        if (rbFemenino.isSelected()) return "Femenino";
        return "";
    }
}