package resol.dao;

import resol.dto.EmpleadoDTO;
import resol.excepciones.ReglaNegocioException;
import resol.conexion.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    public void insertar(EmpleadoDTO e) throws SQLException, ReglaNegocioException {
        LocalDate hoy = LocalDate.now();
        if (e.getFechaContratacion().isAfter(hoy)) {
            throw new ReglaNegocioException("Fecha de contratación no puede ser futura.");
        }
        if (Period.between(e.getFechaNacimiento(), hoy).getYears() < 16) {
            throw new ReglaNegocioException("El empleado debe ser mayor o igual a 16 años.");
        }

        String sql = "INSERT INTO empleado(nombre, fecha_nacimiento, fecha_contratacion, cargo, localidad) VALUES(?,?,?,?,?)";
        try (Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre_completo());
            ps.setDate(2, Date.valueOf(e.getFechaNacimiento()));
            ps.setDate(3, Date.valueOf(e.getFechaContratacion()));
            ps.setString(4, e.getCargo());
            ps.setString(5, e.getLocalidad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId_empleado(rs.getInt(1));
            }
        }
    }

    public void actualizar(EmpleadoDTO e) throws SQLException, ReglaNegocioException {
        LocalDate hoy = LocalDate.now();
        if (e.getFechaContratacion().isAfter(hoy)) {
            throw new ReglaNegocioException("Fecha de contratación no puede ser futura.");
        }
        if (Period.between(e.getFechaNacimiento(), hoy).getYears() < 16) {
            throw new ReglaNegocioException("El empleado debe ser mayor o igual a 16 años.");
        }

        String sql = "UPDATE empleado SET nombre=?, fecha_nacimiento=?, fecha_contratacion=?, cargo=?, localidad=? WHERE id_empleado=?";
        try (Connection c = Conexion.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNombre_completo());
            ps.setDate(2, Date.valueOf(e.getFechaNacimiento()));
            ps.setDate(3, Date.valueOf(e.getFechaContratacion()));
            ps.setString(4, e.getCargo());
            ps.setString(5, e.getLocalidad());
            ps.setInt(6, e.getId_empleado());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public EmpleadoDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpleadoDTO e = new EmpleadoDTO();
                    e.setId_empleado(rs.getInt("id_empleado"));
                    e.setNombre_completo(rs.getString("nombre"));
                    e.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                    e.setFechaContratacion(rs.getDate("fecha_contratacion").toLocalDate());
                    e.setCargo(rs.getString("cargo"));
                    e.setLocalidad(rs.getString("localidad"));
                    return e;
                }
            }
        }
        return null;
    }

    public List<EmpleadoDTO> listar() throws SQLException {
        List<EmpleadoDTO> res = new ArrayList<>();
        String sql = "SELECT * FROM empleado";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EmpleadoDTO e = new EmpleadoDTO();
                e.setId_empleado(rs.getInt("id_empleado"));
                e.setNombre_completo(rs.getString("nombre"));
                e.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                e.setFechaContratacion(rs.getDate("fecha_contratacion").toLocalDate());
                e.setCargo(rs.getString("cargo"));
                e.setLocalidad(rs.getString("localidad"));
                res.add(e);
            }
        }
        return res;
    }

    public int calcularAntiguedad(EmpleadoDTO e) {
        return Period.between(e.getFechaContratacion(), LocalDate.now()).getYears();
    }

    public double calcularBonificacionPorAntiguedad(EmpleadoDTO e) {
        int años = calcularAntiguedad(e);
        if (años <= 1) return 0.0;
        if (años <= 4) return 5.0;
        if (años <= 9) return 10.0;
        return 15.0;
    }
}
