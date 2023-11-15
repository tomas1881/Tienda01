package Metodos;

public class ColaListaCircular {
    private NodoProducto frente;
    private NodoProducto fin;

    public ColaListaCircular() {
        frente = null;
        fin = null;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public void encolar(Producto producto) {
        NodoProducto nuevoNodo = new NodoProducto(producto);
        if (estaVacia()) {
            frente = nuevoNodo;
            fin = nuevoNodo;
            frente.setSiguiente(frente); // Enlazar el frente con él mismo en una cola vacía
        } else {
            fin.setSiguiente(nuevoNodo);
            fin = nuevoNodo;
            fin.setSiguiente(frente); // Enlazar el último elemento con el frente
        }
    }

    public Producto desencolar() {
        if (estaVacia()) {
            return null; // Cola vacía, no se puede desencolar
        }

        NodoProducto nodoDesencolado = frente;
        frente = frente.getSiguiente();

        if (frente == null) {
            fin = null; // Si la cola queda vacía, actualizar el puntero fin
        } else {
            fin.setSiguiente(frente); // Enlazar el último elemento con el frente
        }

        return nodoDesencolado.getProducto();
    }

    public Producto obtenerFrente() {
        if (estaVacia()) {
            return null; // Cola vacía, no hay frente
        }
        return frente.getProducto();
    }
}