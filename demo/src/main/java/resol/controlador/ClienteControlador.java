package resol.controlador;

import resol.dao.ClienteDAO;
import resol.dto.ClienteDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.util.List;

public class ClienteControlador {
    private ClienteDAO dao = new ClienteDAO();

    public void agregarCliente(ClienteDTO c) throws SQLException, ReglaNegocioException {
        dao.insertar(c);
    }

    public List<ClienteDTO> listarClientes() throws SQLException {
        return dao.listar();
    }

    public ClienteDTO buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public void actualizar(ClienteDTO c) throws SQLException, ReglaNegocioException {
        dao.actualizar(c);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }
}
