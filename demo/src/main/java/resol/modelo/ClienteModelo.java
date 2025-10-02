package resol.modelo;

import resol.dao.ClienteDAO;
import resol.dto.ClienteDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteModelo {
    private ClienteDAO dao = new ClienteDAO();

    public void agregarCliente(ClienteDTO c) throws SQLException, ReglaNegocioException {
        if (c.getFecha_alta().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("La fecha de alta no puede ser futura.");
        }
        dao.insertar(c);
    }

    public List<ClienteDTO> listar() throws SQLException {
        return dao.listar();
    }
}
