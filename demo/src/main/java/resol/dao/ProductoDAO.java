package resol.dao;

import resol.dto.ProductoDTO;
import resol.conexion.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public void insertar(ProductoDTO p) throws SQLException, IllegalArgumentException {
        if (p.getPrecioUnidad() <= 0) throw new IllegalArgumentException("Precio debe ser > 0");
        if (p.getStock() < 0) throw new IllegalArgumentException("Stock debe ser >= 0");

        String sql = "INSERT INTO producto(nombre, unidades_en_existencia, nivel_nuevo_pedido, precio_unidad, suspendido) VALUES(?,?,?,?,?)";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getStock());
            ps.setInt(3, p.getNivelNuevoPedido());
            ps.setDouble(4, p.getPrecioUnidad());
            ps.setInt(5, p.isDisponible() ? 0 : 1);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId_producto(rs.getInt(1));
            }
        }
    }

    public void actualizar(ProductoDTO p) throws SQLException {
        String sql = "UPDATE producto SET nombre=?, unidades_en_existencia=?, nivel_nuevo_pedido=?, precio_unidad=?, suspendido=? WHERE id_producto=?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getStock());
            ps.setInt(3, p.getNivelNuevoPedido());
            ps.setDouble(4, p.getPrecioUnidad());
            ps.setInt(5, p.isDisponible() ? 0 : 1);
            ps.setInt(6, p.getId_producto());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public ProductoDAO.ProductoWrapper buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM producto WHERE id_producto=?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProductoDTO p = new ProductoDTO();
                    p.setId_producto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setStock(rs.getInt("unidades_en_existencia"));
                    p.setNivelNuevoPedido(rs.getInt("nivel_nuevo_pedido"));
                    p.setPrecioUnidad(rs.getDouble("precio_unidad"));
                    p.setDisponible(rs.getInt("suspendido") == 0);
                    p.setNecesitaReposicion(p.getStock() < p.getNivelNuevoPedido());
                    return new ProductoWrapper(p);
                }
            }
        }
        return null;
    }

    public List<ProductoDTO> listarTodos() throws SQLException {
        List<ProductoDTO> res = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductoDTO p = new ProductoDTO();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("unidades_en_existencia"));
                p.setNivelNuevoPedido(rs.getInt("nivel_nuevo_pedido"));
                p.setPrecioUnidad(rs.getDouble("precio_unidad"));
                p.setDisponible(rs.getInt("suspendido") == 0);
                p.setNecesitaReposicion(p.getStock() < p.getNivelNuevoPedido());
                res.add(p);
            }
        }
        return res;
    }

    public List<ProductoDTO> listarDisponibles() throws SQLException {
        List<ProductoDTO> res = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE suspendido = 0";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductoDTO p = new ProductoDTO();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("unidades_en_existencia"));
                p.setNivelNuevoPedido(rs.getInt("nivel_nuevo_pedido"));
                p.setPrecioUnidad(rs.getDouble("precio_unidad"));
                p.setDisponible(true);
                p.setNecesitaReposicion(p.getStock() < p.getNivelNuevoPedido());
                res.add(p);
            }
        }
        return res;
    }

    public List<ProductoDTO> listarNecesitanReposicion() throws SQLException {
        List<ProductoDTO> res = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE unidades_en_existencia < nivel_nuevo_pedido";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductoDTO p = new ProductoDTO();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("unidades_en_existencia"));
                p.setNivelNuevoPedido(rs.getInt("nivel_nuevo_pedido"));
                p.setPrecioUnidad(rs.getDouble("precio_unidad"));
                p.setDisponible(rs.getInt("suspendido") == 0);
                p.setNecesitaReposicion(true);
                res.add(p);
            }
        }
        return res;
    }

    public static class ProductoWrapper {
        public final ProductoDTO producto;
        public ProductoWrapper(ProductoDTO producto) { this.producto = producto; }
    }
}
