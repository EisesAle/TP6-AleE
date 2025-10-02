package resol.controlador;

import resol.dto.EmpleadoDTO;
import resol.excepciones.ReglaNegocioException;
import resol.modelo.EmpleadoModelo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmpleadoControlador {
    private EmpleadoModelo modelo = new EmpleadoModelo();

    public void agregarEmpleado(String nombre, LocalDate fechaNac, LocalDate fechaContr, String cargo, String localidad)
            throws SQLException, ReglaNegocioException {
        EmpleadoDTO e = new EmpleadoDTO();
        e.setNombre_completo(nombre);
        e.setFechaNacimiento(fechaNac);
        e.setFechaContratacion(fechaContr);
        e.setCargo(cargo);
        e.setLocalidad(localidad);
        modelo.agregarEmpleado(e);
    }

    public List<EmpleadoDTO> listarEmpleados() throws SQLException {
        return modelo.listar();
    }

    public double calcularBonificacion(EmpleadoDTO e) {
        return modelo.calcularBonificacion(e);
    }
}
