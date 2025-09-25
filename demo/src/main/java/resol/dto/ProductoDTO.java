package resol.dto;

public class ProductoDTO {
    private int id_producto;
    private String nombre;
    private int stock;
    private boolean disponible;
    private boolean necesitaReposicion;
    private int nivelNuevoPedido;
    private double precioUnidad;

    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public boolean isNecesitaReposicion() { return necesitaReposicion; }
    public void setNecesitaReposicion(boolean necesitaReposicion) { this.necesitaReposicion = necesitaReposicion; }

    public int getNivelNuevoPedido() { return nivelNuevoPedido; }
    public void setNivelNuevoPedido(int nivelNuevoPedido) { this.nivelNuevoPedido = nivelNuevoPedido; }

    public double getPrecioUnidad() { return precioUnidad; }
    public void setPrecioUnidad(double precioUnidad) { this.precioUnidad = precioUnidad; }
}
