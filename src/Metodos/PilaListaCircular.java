package Metodos;

public class PilaListaCircular {

    private NodoProducto cima;

    public PilaListaCircular() {
        cima = null;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public void apilar(Producto producto) {
        if (estaVacia()) {
            cima = new NodoProducto(producto);
            cima.setSiguiente(cima);
        } else {
            NodoProducto nuevoNodo = new NodoProducto(producto);
            nuevoNodo.setSiguiente(cima.getSiguiente());
            cima.setSiguiente(nuevoNodo);
        }
    }

    public boolean contieneProductoConID(String id) {
        NodoProducto temp = cima;
        if (temp == null) {
            return false;
        } else {
            do {
                if (temp.getProducto().getIdProducto().equals(id)) {
                    return true;
                }
                temp = temp.getSiguiente();
            } while (temp != cima);
        }
        return false;
    }

    public Producto desapilar() {
        if (estaVacia()) {
            return null; // Pila vacía, no se puede desapilar
        }

        NodoProducto nodoDesapilado = cima;
        if (cima.getSiguiente() == cima) {
            cima = null; // Único nodo en la pila
        } else {
            NodoProducto nodoAnterior = cima;
            while (nodoAnterior.getSiguiente() != cima) {
                nodoAnterior = nodoAnterior.getSiguiente();
            }
            cima = nodoAnterior;
            nodoAnterior.setSiguiente(nodoDesapilado.getSiguiente());
        }

        return nodoDesapilado.getProducto();
    }

    public NodoProducto obtenerCimaNodo() {
        return cima;
    }

  


}
