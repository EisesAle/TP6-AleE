package resol.controlador;

import resol.dto.ProductoDTO;
import resol.modelo.ProductoModelo;

import java.sql.SQLException;
import java.util.List;

public class ProductoControlador {
    private ProductoModelo modelo = new ProductoModelo();

    public void agregarProducto(ProductoDTO p) throws SQLException {
        modelo.agregarProducto(p);
    }

    public List<ProductoDTO> listarDisponibles() throws SQLException {
        return modelo.listarDisponibles();
    }

    public List<ProductoDTO> listarNecesitanReposicion() throws SQLException {
        return modelo.listarNecesitanReposicion();
    }
}
