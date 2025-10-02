package resol.modelo;

import resol.dao.ProductoDAO;
import resol.dto.ProductoDTO;

import java.sql.SQLException;
import java.util.List;

public class ProductoModelo {
    private ProductoDAO dao = new ProductoDAO();

    public void agregarProducto(ProductoDTO p) throws SQLException {
        if (p.getPrecioUnidad() <= 0) throw new IllegalArgumentException("El precio debe ser mayor a 0");
        if (p.getStock() < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        dao.insertar(p);
    }

    public List<ProductoDTO> listarDisponibles() throws SQLException {
        return dao.listarDisponibles();
    }

    public List<ProductoDTO> listarNecesitanReposicion() throws SQLException {
        return dao.listarNecesitanReposicion();
    }
}
