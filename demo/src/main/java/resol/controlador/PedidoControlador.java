package resol.controlador;

import resol.dto.PedidoDTO;
import resol.excepciones.ReglaNegocioException;
import resol.modelo.PedidoModelo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoControlador {
    private PedidoModelo modelo = new PedidoModelo();

    public void altaPedido(PedidoDTO p) throws SQLException, ReglaNegocioException {
        modelo.altaPedido(p);
    }

    public List<PedidoDTO> listarTodos() throws SQLException {
        return modelo.listar();
    }

    public List<PedidoDTO> listarPedidosPorCliente(int idCliente) throws SQLException {
        return modelo.listarPorCliente(idCliente);
    }

    public void cambiarEstado(int idPedido, String nuevoEstado, LocalDate fechaEnvio, LocalDate fechaEntrega)
            throws SQLException, ReglaNegocioException {
        modelo.cambiarEstado(idPedido, nuevoEstado, fechaEnvio, fechaEntrega);
    }
}
