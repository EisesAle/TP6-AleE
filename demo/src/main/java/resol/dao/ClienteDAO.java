package resol.dao;

import resol.dto.ClienteDTO;
import resol.excepciones.ReglaNegocioException;
import resol.conexion.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void insertar(ClienteDTO c) throws SQLException, ReglaNegocioException {
        if (c.getFecha_alta().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("Fecha de alta no puede ser futura.");
        }
        String sql = "INSERT INTO cliente(nombre, nombre_empresa, tipo_empresa, localidad, fecha_alta) VALUES(?,?,?,?,?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre_completo());
            ps.setString(2, c.getNombre_empresa());
            ps.setString(3, c.getTipo_empresa());
            ps.setString(4, c.getLocalidad());
            ps.setDate(5, Date.valueOf(c.getFecha_alta()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId_cliente(rs.getInt(1));
            }
        }
    }

    public void actualizar(ClienteDTO c) throws SQLException, ReglaNegocioException {
        if (c.getFecha_alta().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("Fecha de alta no puede ser futura.");
        }
        String sql = "UPDATE cliente SET nombre=?, nombre_empresa=?, tipo_empresa=?, localidad=?, fecha_alta=? WHERE id_cliente=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre_completo());
            ps.setString(2, c.getNombre_empresa());
            ps.setString(3, c.getTipo_empresa());
            ps.setString(4, c.getLocalidad());
            ps.setDate(5, Date.valueOf(c.getFecha_alta()));
            ps.setInt(6, c.getId_cliente());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public ClienteDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteDTO c = new ClienteDTO();
                    c.setId_cliente(rs.getInt("id_cliente"));
                    c.setNombre_completo(rs.getString("nombre"));
                    c.setNombre_empresa(rs.getString("nombre_empresa"));
                    c.setTipo_empresa(rs.getString("tipo_empresa"));
                    c.setLocalidad(rs.getString("localidad"));
                    c.setFecha_alta(rs.getDate("fecha_alta").toLocalDate());
                    return c;
                }
            }
        }
        return null;
    }

    public List<ClienteDTO> listar() throws SQLException {
        List<ClienteDTO> res = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClienteDTO c = new ClienteDTO();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNombre_completo(rs.getString("nombre"));
                c.setNombre_empresa(rs.getString("nombre_empresa"));
                c.setTipo_empresa(rs.getString("tipo_empresa"));
                c.setLocalidad(rs.getString("localidad"));
                c.setFecha_alta(rs.getDate("fecha_alta").toLocalDate());
                res.add(c);
            }
        }
        return res;
    }
}
