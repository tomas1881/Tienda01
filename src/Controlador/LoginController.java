package Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Button login;
    @FXML
    private TextField usuario;
    @FXML
    private TextField contra;
    @FXML
    private Pane main_area;
    @FXML
    private Button loginn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void login() {
        String usuarioInput = usuario.getText();
        String contraInput = contra.getText();

        // Verificar si el usuario y la contraseña son correctos
        if ("ucc".equals(usuarioInput.toLowerCase()) && "2023".equals(contraInput)) {
            // Cargar la interfaz Main
            abrirInterfazMain();
        } else {
            // Mostrar un mensaje de error
            mostrarAlerta("Error de inicio de sesión", "Usuario o contraseña incorrectos");
        }
    }

    private void salir() {
        System.exit(0);
    }

    private void abrirInterfazMain() {
        try {
            // Cargar el archivo FXML de la interfaz Main
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interfaz/Main.fxml"));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Obtener el controlador de la interfaz Main
            MainController mainController = loader.getController();

            // Configurar la escena actual en el escenario principal
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Cerrar la ventana de inicio de sesión (opcional)
            Stage loginStage = (Stage) loginn.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loginAccount(MouseEvent event) {
        // You can add additional logic for mouse event if needed
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
