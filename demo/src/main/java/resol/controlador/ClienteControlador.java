package resol.controlador;

import resol.dto.ClienteDTO;
import resol.excepciones.ReglaNegocioException;
import resol.modelo.ClienteModelo;

import java.sql.SQLException;
import java.util.List;

public class ClienteControlador {
    private ClienteModelo modelo = new ClienteModelo();

    public void agregarCliente(ClienteDTO c) throws SQLException, ReglaNegocioException {
        modelo.agregarCliente(c);
    }

    public List<ClienteDTO> listarClientes() throws SQLException {
        return modelo.listar();
    }
}
