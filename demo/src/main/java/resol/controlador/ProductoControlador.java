package resol.controlador;

import resol.dao.ProductoDAO;
import resol.dto.ProductoDTO;

import java.sql.SQLException;
import java.util.List;

public class ProductoControlador {
    private ProductoDAO dao = new ProductoDAO();

    public void agregarProducto(ProductoDTO p) throws SQLException {
        dao.insertar(p);
    }

    public List<ProductoDTO> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<ProductoDTO> listarDisponibles() throws SQLException {
        return dao.listarDisponibles();
    }

    public List<ProductoDTO> listarNecesitanReposicion() throws SQLException {
        return dao.listarNecesitanReposicion();
    }

    public ProductoDAO.ProductoWrapper buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public void actualizar(ProductoDTO p) throws SQLException {
        dao.actualizar(p);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }
}
