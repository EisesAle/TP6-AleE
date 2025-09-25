package resol.controlador;

import resol.dao.EmpleadoDAO;
import resol.dto.EmpleadoDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmpleadoControlador {
    private EmpleadoDAO dao = new EmpleadoDAO();

    public void agregarEmpleado(String nombre, LocalDate fechaNac, LocalDate fechaContr, String cargo, String localidad) throws SQLException, ReglaNegocioException {
        EmpleadoDTO e = new EmpleadoDTO();
        e.setNombre_completo(nombre);
        e.setFechaNacimiento(fechaNac);
        e.setFechaContratacion(fechaContr);
        e.setCargo(cargo);
        e.setLocalidad(localidad);
        dao.insertar(e);
    }

    public List<EmpleadoDTO> listarEmpleados() throws SQLException {
        return dao.listar();
    }

    public EmpleadoDTO buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public void actualizar(EmpleadoDTO e) throws SQLException, ReglaNegocioException {
        dao.actualizar(e);
    }

    public int calcularAntiguedad(EmpleadoDTO e) {
        return dao.calcularAntiguedad(e);
    }

    public double calcularBonificacion(EmpleadoDTO e) {
        return dao.calcularBonificacionPorAntiguedad(e);
    }
}
