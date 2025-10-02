package resol.modelo;

import resol.dao.EmpleadoDAO;
import resol.dto.EmpleadoDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class EmpleadoModelo {
    private EmpleadoDAO dao = new EmpleadoDAO();

    public void agregarEmpleado(EmpleadoDTO e) throws SQLException, ReglaNegocioException {
        if (e.getFechaContratacion().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("Fecha de contratación no puede ser futura.");
        }
        if (Period.between(e.getFechaNacimiento(), LocalDate.now()).getYears() < 16) {
            throw new ReglaNegocioException("El empleado debe ser mayor de 16 años.");
        }
        dao.insertar(e);
    }

    public List<EmpleadoDTO> listar() throws SQLException {
        return dao.listar();
    }

    public double calcularBonificacion(EmpleadoDTO e) {
        int años = Period.between(e.getFechaContratacion(), LocalDate.now()).getYears();
        if (años <= 1) return 0.0;
        if (años <= 4) return 5.0;
        if (años <= 9) return 10.0;
        return 15.0;
    }
}
