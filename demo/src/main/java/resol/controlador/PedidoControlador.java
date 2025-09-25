package resol.controlador;

import resol.dao.PedidoDAO;
import resol.dto.PedidoDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoControlador {
    private PedidoDAO dao = new PedidoDAO();

    public void altaPedido(PedidoDTO p) throws SQLException, ReglaNegocioException {
        dao.altaPedido(p);
    }

    public PedidoDTO buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<PedidoDTO> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<PedidoDTO> listarPedidosPorCliente(int idCliente) throws SQLException {
        return dao.listarPedidosPorCliente(idCliente);
    }

    public void cambiarEstado(int idPedido, String nuevoEstado, LocalDate fechaEnvio, LocalDate fechaEntrega) throws SQLException, ReglaNegocioException {
        dao.cambiarEstado(idPedido, nuevoEstado, fechaEnvio, fechaEntrega);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }
}
