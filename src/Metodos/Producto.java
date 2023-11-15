package Metodos;

import java.io.Serializable;
import java.time.LocalDate;

public class Producto implements Serializable{
    private String idProducto;
    private String nomProducto;
    private LocalDate fechaLote;
    private LocalDate fechaVence;
    private float precioU;

    public Producto(String idProducto, String nomProducto, LocalDate fechaLote, LocalDate fechaVence, float precioU) {
        this.idProducto = idProducto;
        this.nomProducto = nomProducto;
        this.fechaLote = fechaLote;
        this.fechaVence = fechaVence;
        this.precioU = precioU;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public LocalDate getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(LocalDate fechaLote) {
        this.fechaLote = fechaLote;
    }

    public LocalDate getFechaVence() {
        return fechaVence;
    }

    public void setFechaVence(LocalDate fechaVence) {
        this.fechaVence = fechaVence;
    }

    public float getPrecioU() {
        return precioU;
    }

    public void setPrecioU(float precioU) {
        this.precioU = precioU;
    }
    
 @Override
public String toString() {
    return String.format("ID: %-3s | Nombre: %-3s | Fecha de Lote: %-3s | Fecha de Vencimiento: %-3s | Precio Unitario: %.2f",
            idProducto, nomProducto, fechaLote, fechaVence, precioU);
}


}
