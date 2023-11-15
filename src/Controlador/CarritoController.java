package Controlador;

import Metodos.ModeloDatos;
import Metodos.Producto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CarritoController implements Initializable {
    private ModeloDatos modeloDatos;
    
    @FXML
    private TableColumn<Producto, String> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, Integer> precioColumn;
    @FXML
    private Button btnGenerarFactura;
    @FXML
    private TextField TotalPagar;
    @FXML
    private TableView<Producto> TablaCarrito;
    @FXML
    private TableColumn<Producto, String> fechaLoteColumn;
    @FXML
    private TableColumn<Producto, String> fechaVenceColumn;
    @FXML
    private Button btnEliminar;

 public void inicializarModelo(ModeloDatos modeloDatos) {
        this.modeloDatos = modeloDatos;
    }
    
    
    // Constructor sin argumentos
    public CarritoController() {
        // Puedes inicializar algunos valores predeterminados aquí si es necesario
    }

    public CarritoController(ModeloDatos modeloDatos) {
        this.modeloDatos = modeloDatos;
        inicializarTablaCarrito();
    }

    private void inicializarTablaCarrito() {
        // Configura las celdas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nomProducto"));
        fechaLoteColumn.setCellValueFactory(new PropertyValueFactory<>("fechaLote"));
        fechaVenceColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVence"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioU"));
  
    }
    
   
    @FXML
    private void MetodoGeneralFactura(ActionEvent event) {
        // Verifica si el modeloDatos es nulo
        if (modeloDatos == null) {
            mostrarAlerta("Error", "modeloDatos no inicializado en CarritoController.");
            return;
        }

        // Obtiene la lista de productos en el carrito desde el modelo de datos
        List<Producto> productosEnCarrito = modeloDatos.getProductosEnCarrito();

        // Verifica si hay productos en el carrito
        if (productosEnCarrito.isEmpty()) {
            mostrarAlerta("Generar Factura", "No hay productos en el carrito para generar la factura.");
            return;
        }

        // Genera la estructura de la factura y la muestra como una alerta
        generarFactura(productosEnCarrito);
    }

   private void generarFactura(List<Producto> productos) {
        // Crea el contenido de la factura
        StringBuilder facturaContent = new StringBuilder();
        facturaContent.append("FACTURA\n");
        facturaContent.append("------------------------------\n");

        for (Producto producto : productos) {
            facturaContent.append("ID: ").append(producto.getIdProducto()).append("\n");
            facturaContent.append("Nombre: ").append(producto.getNomProducto()).append("\n");
            facturaContent.append("Fecha de Lote: ").append(producto.getFechaLote()).append("\n");
            facturaContent.append("Fecha de Vencimiento: ").append(producto.getFechaVence()).append("\n");
            facturaContent.append("Precio Unitario: ").append(producto.getPrecioU()).append("\n");
            facturaContent.append("------------------------------\n");
        }

        float totalAPagar = calcularTotalAPagar(productos);
        facturaContent.append("Total a Pagar: ").append(totalAPagar).append("\n");
        facturaContent.append("------------------------------\n");
        facturaContent.append("¡Gracias por su compra!");

        // Muestra la factura como una alerta
        mostrarAlerta("Factura", facturaContent.toString());
    }

   
   private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }


   @FXML
private void MetodoTotalAPagar(ActionEvent event) {
    // Verifica si el modeloDatos es nulo
    if (modeloDatos == null) {
        System.err.println("Error: modeloDatos no inicializado en CarritoController.");
        return;
    }

    // Obtiene la lista de productos en el carrito desde el modelo de datos
    List<Producto> productosEnCarrito = modeloDatos.getProductosEnCarrito();

    // Verifica si hay productos en el carrito
    if (productosEnCarrito.isEmpty()) {
        mostrarAlerta("Total a Pagar", "No hay productos en el carrito.");
        return;
    }

    // Calcula el total a pagar considerando solo el precio de los productos
    float totalAPagar = calcularTotalAPagar(productosEnCarrito);

    // Muestra el total a pagar en la casilla correspondiente
    TotalPagar.setText(String.format("%.2f", totalAPagar));
}

private float calcularTotalAPagar(List<Producto> productos) {
    // Implementa la lógica para calcular el total a pagar
    float total = 0;
    for (Producto producto : productos) {
        total += producto.getPrecioU();
    }
    return total;
}

   

    @Override
public void initialize(URL location, ResourceBundle resources) {
    try {
                inicializarTablaCarrito();

        // Cualquier lógica de inicialización necesaria
    } catch (Exception e) {
        e.printStackTrace();
        // Puedes mostrar una alerta al usuario o realizar otro manejo de errores aquí
    }
}

    @FXML
private void handleEliminarProducto(ActionEvent event) {
    // Verifica si el modeloDatos es nulo
    if (modeloDatos == null) {
        mostrarAlerta("Error", "modeloDatos no inicializado en CarritoController.");
        return;
    }
    // Obtiene el producto seleccionado en la tabla del carrito
    Producto productoSeleccionado = TablaCarrito.getSelectionModel().getSelectedItem();

    // Verifica si se seleccionó un producto
    if (productoSeleccionado != null) {
        // Elimina el producto del carrito en el modelo de datos
        modeloDatos.eliminarProductoDelCarrito(productoSeleccionado);

        // Actualiza la tabla del carrito en la interfaz Carrito
        actualizarTablaCarrito();

        // Muestra una alerta indicando que el producto ha sido eliminado
        mostrarAlerta("Producto Eliminado", "El producto ha sido eliminado del carrito.");
    } else {
        // Muestra una alerta si no se selecciona ningún producto
        mostrarAlerta("Eliminar Producto", "Por favor, selecciona un producto de la tabla del carrito.");
    }
}

public void actualizarTablaCarrito() {
    Platform.runLater(() -> {
        // Verifica si el modeloDatos es nulo
        if (modeloDatos == null) {
            System.out.println("Error: modeloDatos no inicializado en CarritoController.");
            return;
        }

        // Obtiene la lista de productos en el carrito desde el modelo de datos
        List<Producto> productosEnCarrito = modeloDatos.getProductosEnCarrito();

        // Muestra algunos mensajes de depuración
        System.out.println("Número de productos en el carrito: " + productosEnCarrito.size());

        // Actualiza directamente la lista observable asociada a la tabla
        ObservableList<Producto> observableProductosEnCarrito = FXCollections.observableArrayList(productosEnCarrito);
        TablaCarrito.setItems(observableProductosEnCarrito);

        // Muestra un mensaje de depuración indicando que la tabla se ha actualizado
        System.out.println("Tabla del carrito actualizada correctamente.");
    });
}


}