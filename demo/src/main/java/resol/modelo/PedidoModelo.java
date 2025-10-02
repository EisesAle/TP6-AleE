package resol.modelo;

import resol.dao.PedidoDAO;
import resol.dto.PedidoDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoModelo {
    private PedidoDAO dao = new PedidoDAO();

    public void altaPedido(PedidoDTO p) throws SQLException, ReglaNegocioException {
        if (p.getMonto_total() <= 0) {
            throw new ReglaNegocioException("El monto total debe ser mayor a 0.");
        }
        if (p.getFecha_entrega() != null && p.getFecha_pedido().isAfter(p.getFecha_entrega())) {
            throw new ReglaNegocioException("La fecha de pedido no puede ser mayor que la fecha de entrega.");
        }
        dao.altaPedido(p);
    }

    public void cambiarEstado(int idPedido, String nuevoEstado, LocalDate fechaEnvio, LocalDate fechaEntrega) throws SQLException, ReglaNegocioException {
        dao.cambiarEstado(idPedido, nuevoEstado, fechaEnvio, fechaEntrega);
    }

    public List<PedidoDTO> listar() throws SQLException {
        return dao.listarTodos();
    }

    public List<PedidoDTO> listarPorCliente(int idCliente) throws SQLException {
        return dao.listarPedidosPorCliente(idCliente);
    }
}
