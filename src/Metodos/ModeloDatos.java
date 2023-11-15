package Metodos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModeloDatos {

    private List<Producto> productos = new ArrayList<>();
    private List<Producto> productosEnCarrito = new ArrayList<>();
    private Carrito carrito = new Carrito();  // Inicializa el carrito en la declaración

    // Otros atributos y métodos según sea necesario

    public void agregarProducto(Producto producto) {
        if (producto != null) {
            productos.add(producto);
            // Notificar a las vistas sobre el cambio si es necesario
        }
    }

    public void agregarProductoAlCarrito(Producto producto) {
        if (producto != null) {
            productosEnCarrito.add(producto);
            // Notificar a las vistas sobre el cambio si es necesario
        }
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
        // Notificar a las vistas sobre el cambio si es necesario
    }

    public void eliminarProductoDelCarrito(Producto producto) {
        productosEnCarrito.remove(producto);
        // Notificar a las vistas sobre el cambio si es necesario
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public List<Producto> getProductosEnCarrito() {
        return Collections.unmodifiableList(productosEnCarrito);
    }

    public Carrito getCarrito() {
        return carrito;
    }

    

    // Métodos adicionales según sea necesario

    public static class Carrito {

        // Contenido del carrito, si es necesario
    }
}
