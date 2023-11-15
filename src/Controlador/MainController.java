package Controlador;

import Metodos.ColaListaCircular;
import Metodos.ModeloDatos;
import Metodos.PilaListaCircular;
import Metodos.Producto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author coez
 */
public class MainController implements Initializable {
    
    

       
    
    
    @FXML
    private Label label;
    @FXML
    private Button Salir;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfNombre;
    @FXML
    private DatePicker tfFechaLote;
    @FXML
    private DatePicker tfFechaVence;
    @FXML
    private TextField tfPrecio;
    @FXML
    private TableView<Producto> Tabla;



    @FXML
    private ComboBox<String> BoxBuscarPor;
    @FXML
    private TextField tfBuscarPor;
    @FXML
    private Button btnPromedio;
    @FXML
    private Button btnMyMPromedio;
    @FXML
    private Button btnMyMPrecio;
    @FXML
    private Button btnActualizarLista;
    
    
    
   // Variable miembro para la lista observable de productos
    private ObservableList<Producto> productos;
    @FXML
    private TableColumn<Producto, String> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, String> fechaLoteColumn;
    @FXML
    private TableColumn<Producto, String> fechaVenceColumn;
    @FXML
    private TableColumn<Producto, Integer> precioColumn;
    @FXML
    private Button btnAgregar;
    @FXML
    private ComboBox<String> BoxMeses;
    @FXML
    private Button btnListarPorMes;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnAgregarCola;
    @FXML
    private Button Añadir;
    @FXML
    private Button bttVerCarrito;
    @FXML
    private Label ttitulo;
    
    
     public MainController() {
        // Puedes inicializar algunos valores predeterminados aquí si es necesario
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
// Configura las columnas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nomProducto"));
        fechaLoteColumn.setCellValueFactory(new PropertyValueFactory<>("fechaLote"));
        fechaVenceColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVence"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioU"));

        // Inicializa la lista observable de productos
        productos = FXCollections.observableArrayList();

        // Asocia la lista de productos a la tabla
        Tabla.setItems(productos);
        
        modeloDatos = new ModeloDatos();

        
  ObservableList<String> opciones = FXCollections.observableArrayList(
    "idProducto", "nomProducto", "FechaLote", "FechaVence", "precioU"
);
BoxBuscarPor.setItems(opciones);
BoxBuscarPor.setValue("Buscar Por");

// Agrega este código en el método initialize
Tabla.setRowFactory(tv -> {
    TableRow<Producto> row = new TableRow<>();
    row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
            Producto productoSeleccionado = row.getItem();
            mostrarDatosEnCamposEdicion(productoSeleccionado);
        }
    });
    return row;
});



ObservableList<String> opcionesMeses = FXCollections.observableArrayList(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
);
BoxMeses.setItems(opcionesMeses);
BoxMeses.setValue("Enero"); // Puedes establecer el valor predeterminado si lo deseas

    }
       
    
    PilaListaCircular pila = new PilaListaCircular();
        

 
   @FXML
private void handleAgregar(ActionEvent event) {
    try {
        if (idExistente()) {
            mostrarAlerta("Agregar Producto", "La ID ya existe. Ingresa una ID única.");
            return;
        }

        if (camposIncompletos()) {
            mostrarAlerta("Agregar Producto", "Por favor, ingresa toda la información del producto.");
            return;
        }

        Producto producto = crearProducto();

        pila.apilar(producto);
        productos.add(producto);
        limpiarCampos();
        guardarProductoEnArchivo(producto);

        mostrarAlerta("Producto Agregado", "El producto ha sido agregado y guardado en la pila y archivo.");
    } catch (IOException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "Ocurrió un error al agregar el producto a la pila y al archivo.");
    } catch (NumberFormatException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "Error al convertir el precio a un número.");
    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error", "Ocurrió un error inesperado.");
    }
}


private boolean idExistente() {
    String nuevaId = tfID.getText();
    return productos.stream().anyMatch(producto -> producto.getIdProducto().equals(nuevaId));
}

private boolean camposIncompletos() {
    return tfNombre.getText().isEmpty() || tfFechaLote.getValue() == null || tfFechaVence.getValue() == null || tfPrecio.getText().isEmpty();
}

private Producto crearProducto() {
    String id = tfID.getText();
    String nombre = tfNombre.getText();
    LocalDate fechaLote = tfFechaLote.getValue();
    LocalDate fechaVence = tfFechaVence.getValue();
    float precio = Float.parseFloat(tfPrecio.getText());

    return new Producto(id, nombre, fechaLote, fechaVence, precio);
}

private void limpiarCampos() {
    tfID.clear();
    tfNombre.clear();
    tfFechaLote.setValue(null);
    tfFechaVence.setValue(null);
    tfPrecio.clear();
}

private void guardarProductoEnArchivo(Producto producto) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\coez\\OneDrive\\Documentos\\Ricardo Ucc\\2023 II\\trabajos\\Tienda01\\src\\Archivo\\Productos.txt", true))) {
        writer.write(producto.toString()); // Ajusta esto según la forma en que desees guardar el producto en el archivo.
        writer.newLine();
    }
}

   
    

private float calcularPrecioPromedio() {
    float total = 0;
    for (Producto producto : productos) {
        total += producto.getPrecioU();
    }
    if (productos.isEmpty()) {
        return 0; // Evita la división por cero
    }
    return total / productos.size();
}



 @FXML
private void handlePromedio (ActionEvent event) {
    float promedio = calcularPrecioPromedio();
    // Muestra el precio promedio en una ventana emergente
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Precio Promedio");
    alert.setHeaderText(null);
    alert.setContentText("El precio promedio de los productos es: " + promedio);
    alert.showAndWait();
}




private List<Producto> productosMayoresAlPromedio() {
    float promedio = calcularPrecioPromedio();
    List<Producto> productosMayores = new ArrayList<>();
    for (Producto producto : productos) {
        if (producto.getPrecioU() > promedio) {
            productosMayores.add(producto);
        }
    }
    return productosMayores;
}

private List<Producto> productosMenoresAlPromedio() {
    float promedio = calcularPrecioPromedio();
    List<Producto> productosMenores = new ArrayList<>();
    for (Producto producto : productos) {
        if (producto.getPrecioU() < promedio) {
            productosMenores.add(producto);
        }
    }
    return productosMenores;
}


 @FXML
private void handleMyMPromedio(ActionEvent event) {
    List<Producto> productosMayores = productosMayoresAlPromedio();
    List<Producto> productosMenores = productosMenoresAlPromedio();

    // Muestra los productos en una ventana emergente
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Productos con Precio Mayor/Menor al Promedio");
    alert.setHeaderText(null);

    // Personaliza el contenido de la alerta
    String contenido = "Productos con precio mayor al promedio:\n";
    for (Producto producto : productosMayores) {
        contenido += "ID: " + producto.getIdProducto() + "\n";
        contenido += "Nombre: " + producto.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + producto.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + producto.getFechaVence() + "\n";
        contenido += "Precio: " + producto.getPrecioU() + "\n\n";
    }
    if (productosMayores.isEmpty()) {
        contenido += "No se encontraron productos con precio mayor al promedio.\n\n";
    }

    contenido += "Productos con precio menor al promedio:\n";
    for (Producto producto : productosMenores) {
        contenido += "ID: " + producto.getIdProducto() + "\n";
        contenido += "Nombre: " + producto.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + producto.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + producto.getFechaVence() + "\n";
        contenido += "Precio: " + producto.getPrecioU() + "\n\n";
    }
    if (productosMenores.isEmpty()) {
        contenido += "No se encontraron productos con precio menor al promedio.\n";
    }

    alert.setContentText(contenido);
    alert.showAndWait();
}




private Producto productoDeMayorPrecio() {
    if (productos.isEmpty()) {
        return null;
    }

    Producto mayorPrecio = productos.get(0);
    for (Producto producto : productos) {
        if (producto.getPrecioU() > mayorPrecio.getPrecioU()) {
            mayorPrecio = producto;
        }
    }
    return mayorPrecio;
}

private Producto productoDeMenorPrecio() {
    if (productos.isEmpty()) {
        return null;
    }

    Producto menorPrecio = productos.get(0);
    for (Producto producto : productos) {
        if (producto.getPrecioU() < menorPrecio.getPrecioU()) {
            menorPrecio = producto;
        }
    }
    return menorPrecio;
}


@FXML
private void handleMyMPrecio(ActionEvent event) {
    Producto mayorPrecio = productoDeMayorPrecio();
    Producto menorPrecio = productoDeMenorPrecio();

    // Muestra los productos en una ventana emergente
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Productos de Mayor y Menor Precio");
    alert.setHeaderText(null);

    // Personaliza el contenido de la alerta
    String contenido = "Producto de mayor precio:\n";
    if (mayorPrecio != null) {
        contenido += "ID: " + mayorPrecio.getIdProducto() + "\n";
        contenido += "Nombre: " + mayorPrecio.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + mayorPrecio.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + mayorPrecio.getFechaVence() + "\n";
        contenido += "Precio: " + mayorPrecio.getPrecioU() + "\n";
    } else {
        contenido += "No se encontró un producto de mayor precio.\n";
    }

    contenido += "\nProducto de menor precio:\n";
    if (menorPrecio != null) {
        contenido += "ID: " + menorPrecio.getIdProducto() + "\n";
        contenido += "Nombre: " + menorPrecio.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + menorPrecio.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + menorPrecio.getFechaVence() + "\n";
        contenido += "Precio: " + menorPrecio.getPrecioU() + "\n";
    } else {
        contenido += "No se encontró un producto de menor precio.\n";
    }

    alert.setContentText(contenido);
    alert.showAndWait();
}


private void actualizarTabla() {
   Tabla.getItems().clear();
    // Agrega todos los productos a la tabla
    Tabla.getItems().addAll(productos);
}

 @FXML
private void handleActualizarLista(ActionEvent event) {
    // Implementación del método
    actualizarTabla();
}


    
    @FXML
     private void handleSalir(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }

@FXML
private void handleBuscarPor(ActionEvent event) {
    String campoSeleccionado = (String) BoxBuscarPor.getValue();
    String criterio = tfBuscarPor.getText().toLowerCase(); // Convertir a minúsculas para la comparación

    ObservableList<Producto> productosEncontrados = FXCollections.observableArrayList();

    for (Producto producto : productos) {
        String valorCampo = "";
        switch (campoSeleccionado) {
            case "idProducto":
                valorCampo = producto.getIdProducto();
                break;
            case "nomProducto":
                valorCampo = producto.getNomProducto();
                break;
            case "FechaLote":
                valorCampo = producto.getFechaLote().toString();
                break;
            case "FechaVence":
                valorCampo = producto.getFechaVence().toString();
                break;
            case "precioU":
                valorCampo = String.valueOf(producto.getPrecioU());
                break;
        }

        if (valorCampo.toLowerCase().contains(criterio)) {
            productosEncontrados.add(producto);
        }
    }

    // Actualiza la tabla con los productos encontrados
    Tabla.setItems(productosEncontrados);
}


    @FXML
private void handleListarPorMes(ActionEvent event) {
    // Obtén el mes seleccionado del ComboBox
    String mesSeleccionado = BoxMeses.getValue();

    // Crea una lista para almacenar los productos que coinciden con el mes
    ObservableList<Producto> productosEncontrados = FXCollections.observableArrayList();

    for (Producto producto : productos) {
        // Obtiene el mes de la fecha de lote y fecha de vencimiento
        int mesLote = producto.getFechaLote().getMonthValue();
        int mesVencimiento = producto.getFechaVence().getMonthValue();

        // Compara si alguno de los meses coincide con el mes seleccionado
        if (mesLote == obtenerNumeroMes(mesSeleccionado) || mesVencimiento == obtenerNumeroMes(mesSeleccionado)) {
            productosEncontrados.add(producto);
        }
    }

    // Actualiza la tabla con los productos encontrados
    Tabla.setItems(productosEncontrados);
}

// Método para obtener el número de mes a partir del nombre
private int obtenerNumeroMes(String nombreMes) {
    switch (nombreMes) {
        case "Enero":
            return 1;
        case "Febrero":
            return 2;
        case "Marzo":
            return 3;
        case "Abril":
            return 4;
        case "Mayo":
            return 5;
        case "Junio":
            return 6;
        case "Julio":
            return 7;
        case "Agosto":
            return 8;
        case "Septiembre":
            return 9;
        case "Octubre":
            return 10;
        case "Noviembre":
            return 11;
        case "Diciembre":
            return 12;
        default:
            return 0; // Valor predeterminado si no se encuentra el mes
    }
}

    @FXML
private void handleEliminarProducto(ActionEvent event) {
    // Obtén el producto seleccionado en la tabla
    Producto productoSeleccionado = Tabla.getSelectionModel().getSelectedItem();

    if (productoSeleccionado != null) {
        // Muestra un diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Producto");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar este producto?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Elimina el producto de la lista observable
            productos.remove(productoSeleccionado);
        }
    } else {
        // Muestra un mensaje si no se selecciona un producto
        mostrarAlerta("Eliminar Producto", "Por favor, selecciona un producto de la tabla.");
    }
}

// Método para mostrar una alerta
private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}



private void mostrarDatosEnCamposEdicion(Producto producto) {
    tfID.setText(producto.getIdProducto());
    tfNombre.setText(producto.getNomProducto());
    tfFechaLote.setValue(producto.getFechaLote());
    tfFechaVence.setValue(producto.getFechaVence());
    tfPrecio.setText(String.valueOf(producto.getPrecioU()));
}



@FXML
private void handleGuardarProducto(ActionEvent event) {
    // Obtiene el producto seleccionado en la tabla
    Producto productoSeleccionado = Tabla.getSelectionModel().getSelectedItem();

    if (productoSeleccionado != null) {
        // Verifica si el campo de precio no está vacío
        if (!tfPrecio.getText().isEmpty()) {
            // Actualiza los datos del producto seleccionado con los datos de los campos de edición
            productoSeleccionado.setIdProducto(tfID.getText());
            productoSeleccionado.setNomProducto(tfNombre.getText());
            productoSeleccionado.setFechaLote(tfFechaLote.getValue());
            productoSeleccionado.setFechaVence(tfFechaVence.getValue());

            try {
                // Intenta parsear el contenido del campo de precio
                float precio = Float.parseFloat(tfPrecio.getText());
                productoSeleccionado.setPrecioU(precio);
            } catch (NumberFormatException e) {
                // Muestra una alerta si no se puede parsear el contenido del campo de precio
                mostrarAlerta("Guardar Producto", "Por favor, ingresa un valor numérico válido en el campo de precio.");
                return; // Sale del método si hay un error
            }

            // Limpia los campos de edición después de guardar los cambios
            tfID.clear();
            tfNombre.clear();
            tfFechaLote.setValue(null);
            tfFechaVence.setValue(null);
            tfPrecio.clear();

            // Actualiza solo la fila del producto modificado en la tabla
            Tabla.refresh();
        } else {
            mostrarAlerta("Guardar Producto", "Por favor, ingresa un valor numérico en el campo de precio.");
        }
    } else {
        mostrarAlerta("Guardar Producto", "Por favor, selecciona un producto de la tabla.");
    }
}


    // Método auxiliar para obtener el controlador CarritoController
private CarritoController obtenerControladorCarrito() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
    try {
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Verificar si el controlador se inicializó correctamente
        if (loader.getController() instanceof CarritoController) {
            return loader.getController();
        } else {
            System.err.println("Error al cargar el controlador del carrito.");
            return null;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}



public class GestorArchivo {

private static final String RUTA_ARCHIVO = "C:\\Users\\coez\\OneDrive\\Documentos\\Ricardo Ucc\\2023 II\\trabajos\\Tienda01\\src\\Archivo\\Productos.txt";

    public static void guardarProducto(Producto producto) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            // Escribe la información del producto en el archivo
            writer.write(producto.toString());
            writer.newLine();
        }
    }
    public static List<String> cargarProductos() throws IOException {
        // Lee todas las líneas del archivo y las retorna como una lista de cadenas
        return Files.readAllLines(Paths.get(RUTA_ARCHIVO));
    }
}


@FXML
private void handleAgregarCola(ActionEvent event) throws IOException {
    try {
        // Verificar si la ID ya existe en la lista de productos
        String nuevaId = tfID.getText();
        for (Producto producto : productos) {
            if (producto.getIdProducto().equals(nuevaId)) {
                mostrarAlerta("Agregar Producto", "La ID ya existe. Ingresa una ID única.");
                return;
            }
        }

        // Verificar si se han ingresado otros campos
        if (tfNombre.getText().isEmpty() || tfFechaLote.getValue() == null || tfFechaVence.getValue() == null || tfPrecio.getText().isEmpty()) {
            mostrarAlerta("Agregar Producto", "Por favor, ingresa toda la información del producto.");
            return;
        }

        // Obtén los valores de los campos de entrada
        String id = tfID.getText();
        String nombre = tfNombre.getText();
        LocalDate fechaLote = tfFechaLote.getValue();
        LocalDate fechaVence = tfFechaVence.getValue();
        float precio = Float.parseFloat(tfPrecio.getText());

        // Crea una nueva instancia de Producto
        Producto producto = new Producto(id, nombre, fechaLote, fechaVence, precio);

        // Agrega el producto a la cola
        ColaListaCircular cola = new ColaListaCircular();
        cola.encolar(producto);

        // Agrega el producto al principio de la lista observable
        productos.add(0, producto);

        // Limpia los campos de entrada después de agregar el producto
        tfID.clear();
        tfNombre.clear();
        tfFechaLote.setValue(null);
        tfFechaVence.setValue(null);
        tfPrecio.clear();

        // Guarda el producto en el archivo
        GestorArchivo.guardarProducto(producto);

        mostrarAlerta("Producto Agregado", "El producto ha sido agregado y guardado en la cola y archivo.");
    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error", "Ocurrió un error al agregar el producto a la cola y a la pila.");
    }
}

private ModeloDatos modeloDatos;

    

@FXML
private void handleAñadirCarrito(ActionEvent event) {
    try {
        // Verificar que el modeloDatos y el carrito estén inicializados
        if (modeloDatos == null || modeloDatos.getCarrito() == null) {
            mostrarAlerta("Error", "El modeloDatos o el carrito no están inicializados correctamente.");
            return;
        }

        // Obtener el producto seleccionado en la tabla principal
        Producto productoSeleccionado = Tabla.getSelectionModel().getSelectedItem();

        if (productoSeleccionado != null) {
            // Agregar el producto al carrito en el modelo de datos
            modeloDatos.agregarProductoAlCarrito(productoSeleccionado);

            // Mostrar la alerta de éxito
            mostrarAlerta("Producto Agregado al Carrito", "El producto ha sido añadido al carrito.");
        } else {
            // Mostrar una alerta si no se selecciona ningún producto
            mostrarAlerta("Añadir al Carrito", "Por favor, selecciona un producto de la tabla principal.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error", "Ocurrió un error al añadir el producto al carrito.");
    }
}



@FXML
private void abrirVentanaCarrito() {
    try {
        // Verificar que el modeloDatos y el carrito estén inicializados
        if (modeloDatos == null || modeloDatos.getCarrito() == null) {
            mostrarAlerta("Error", "El modeloDatos o el carrito no están inicializados correctamente.");
            return;
        }

        // Cargar la vista del carrito
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interfaz/Carrito.fxml"));
        Parent root = loader.load();

        // Obtén el controlador del CarritoController
        CarritoController carritoController = loader.getController();
        carritoController.inicializarModelo(modeloDatos); // Asegúrate de inicializar el modelo

        // Configura la nueva escena
        Stage carritoStage = new Stage();
        carritoStage.setScene(new Scene(root));

        // Llama al método actualizarTablaCarrito() después de inicializar el modelo
        carritoController.actualizarTablaCarrito();

        // Mostrar la ventana
        carritoStage.show();
    } catch (IOException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "Ocurrió un error al abrir la ventana del carrito.");
    }
}


}