package resol.dao;

import resol.dto.*;
import resol.excepciones.ReglaNegocioException;
import resol.conexion.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    public void altaPedido(PedidoDTO pedido) throws SQLException, ReglaNegocioException {
        validarPedido(pedido);

        String sqlPedido = "INSERT INTO pedido(fecha_pedido, fecha_envio, fecha_entrega, estado, monto_total, id_cliente, id_empleado) VALUES(?,?,?,?,?,?,?)";
        String sqlDetalle = "INSERT INTO detalle_pedido(id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES(?,?,?,?,?)";

        Connection c = null;
        try {
            c = Conexion.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement psPedido = c.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setDate(1, Date.valueOf(pedido.getFecha_pedido()));
                if (pedido.getFecha_envio() != null) psPedido.setDate(2, Date.valueOf(pedido.getFecha_envio()));
                else psPedido.setNull(2, Types.DATE);
                if (pedido.getFecha_entrega() != null) psPedido.setDate(3, Date.valueOf(pedido.getFecha_entrega()));
                else psPedido.setNull(3, Types.DATE);
                psPedido.setString(4, pedido.getEstado());
                psPedido.setDouble(5, pedido.getMonto_total());
                psPedido.setInt(6, pedido.getCliente().getId_cliente());
                psPedido.setInt(7, pedido.getEmpleado().getId_empleado());

                psPedido.executeUpdate();
                try (ResultSet rs = psPedido.getGeneratedKeys()) {
                    if (rs.next()) pedido.setId_pedido(rs.getInt(1));
                }
            }

            try (PreparedStatement psDet = c.prepareStatement(sqlDetalle)) {
                for (DetallePedidoDTO det : pedido.getDetalle()) {
                    psDet.setInt(1, pedido.getId_pedido());
                    psDet.setInt(2, det.getId_producto());
                    psDet.setInt(3, det.getCantidad());
                    psDet.setDouble(4, det.getPrecioUnitario());
                    psDet.setDouble(5, det.getSubtotal());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            c.commit();
        } catch (SQLException ex) {
            if (c != null) c.rollback();
            throw ex;
        } finally {
            if (c != null) c.setAutoCommit(true);
            if (c != null) c.close();
        }
    }

    private void validarPedido(PedidoDTO p) throws ReglaNegocioException {
        if (p.getMonto_total() <= 0) throw new ReglaNegocioException("Monto total debe ser mayor a 0.");
        if (p.getFecha_entrega() != null && p.getFecha_pedido().isAfter(p.getFecha_entrega()))
            throw new ReglaNegocioException("Fecha de pedido debe ser menor o igual a fecha de entrega.");
        if (p.getFecha_envio() == null && !"Pendiente".equalsIgnoreCase(p.getEstado()))
            throw new ReglaNegocioException("Si fecha_envio es NULL, estado debe ser Pendiente.");
        if (p.getFecha_envio() != null && p.getFecha_entrega() == null && !"Enviado".equalsIgnoreCase(p.getEstado()))
            throw new ReglaNegocioException("Si hay fecha_envio y no fecha_entrega, estado debe ser Enviado.");
        if (p.getFecha_entrega() != null && !"Entregado".equalsIgnoreCase(p.getEstado()))
            throw new ReglaNegocioException("Si hay fecha_entrega, estado debe ser Entregado.");
    }

    public PedidoDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.*, c.nombre as cliente_nombre, c.nombre_empresa, c.tipo_empresa, c.localidad as cliente_localidad, " +
                "e.nombre as empleado_nombre, e.cargo as empleado_cargo " +
                "FROM pedido p " +
                "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                "JOIN empleado e ON p.id_empleado = e.id_empleado " +
                "WHERE p.id_pedido = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PedidoDTO p = new PedidoDTO();
                    p.setId_pedido(rs.getInt("id_pedido"));
                    p.setFecha_pedido(rs.getDate("fecha_pedido").toLocalDate());
                    Date dEnv = rs.getDate("fecha_envio");
                    p.setFecha_envio(dEnv != null ? dEnv.toLocalDate() : null);
                    Date dEnt = rs.getDate("fecha_entrega");
                    p.setFecha_entrega(dEnt != null ? dEnt.toLocalDate() : null);
                    p.setEstado(rs.getString("estado"));
                    p.setMonto_total(rs.getDouble("monto_total"));

                    ClienteDTO c = new ClienteDTO();
                    c.setId_cliente(rs.getInt("id_cliente"));
                    c.setNombre_completo(rs.getString("cliente_nombre"));
                    c.setNombre_empresa(rs.getString("nombre_empresa"));
                    c.setTipo_empresa(rs.getString("tipo_empresa"));
                    c.setLocalidad(rs.getString("cliente_localidad"));
                    p.setCliente(c);

                    EmpleadoDTO e = new EmpleadoDTO();
                    e.setId_empleado(rs.getInt("id_empleado"));
                    e.setNombre_completo(rs.getString("empleado_nombre"));
                    e.setCargo(rs.getString("empleado_cargo"));
                    p.setEmpleado(e);

                    p.setDetalle(listarDetallePorPedido(p.getId_pedido()));
                    return p;
                }
            }
        }
        return null;
    }

    public List<PedidoDTO> listarTodos() throws SQLException {
        List<PedidoDTO> res = new ArrayList<>();
        String sql = "SELECT id_pedido FROM pedido";
        try (Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PedidoDTO p = buscarPorId(rs.getInt("id_pedido"));
                if (p != null) res.add(p);
            }
        }
        return res;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void actualizar(PedidoDTO p) throws SQLException, ReglaNegocioException {
        validarPedido(p);
        String sql = "UPDATE pedido SET fecha_pedido=?, fecha_envio=?, fecha_entrega=?, estado=?, monto_total=?, id_cliente=?, id_empleado=? WHERE id_pedido=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(p.getFecha_pedido()));
            if (p.getFecha_envio() != null) ps.setDate(2, Date.valueOf(p.getFecha_envio()));
            else ps.setNull(2, Types.DATE);
            if (p.getFecha_entrega() != null) ps.setDate(3, Date.valueOf(p.getFecha_entrega()));
            else ps.setNull(3, Types.DATE);
            ps.setString(4, p.getEstado());
            ps.setDouble(5, p.getMonto_total());
            ps.setInt(6, p.getCliente().getId_cliente());
            ps.setInt(7, p.getEmpleado().getId_empleado());
            ps.setInt(8, p.getId_pedido());
            ps.executeUpdate();
        }
    }

    public List<PedidoDTO> listarPedidosPorCliente(int idCliente) throws SQLException {
        List<PedidoDTO> res = new ArrayList<>();
        String sql = "SELECT id_pedido FROM pedido WHERE id_cliente = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidoDTO p = buscarPorId(rs.getInt("id_pedido"));
                    if (p != null) res.add(p);
                }
            }
        }
        return res;
    }

    public List<DetallePedidoDTO> listarDetallePorPedido(int idPedido) throws SQLException {
        List<DetallePedidoDTO> res = new ArrayList<>();
        String sql = "SELECT d.*, pr.nombre as nombre_producto FROM detalle_pedido d JOIN producto pr ON d.id_producto = pr.id_producto WHERE d.id_pedido = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedidoDTO det = new DetallePedidoDTO();
                    det.setId_detalle(rs.getInt("id_detalle"));
                    det.setId_producto(rs.getInt("id_producto"));
                    det.setNombreProducto(rs.getString("nombre_producto"));
                    det.setCantidad(rs.getInt("cantidad"));
                    det.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    det.setSubtotal(rs.getDouble("subtotal"));
                    res.add(det);
                }
            }
        }
        return res;
    }

    public void cambiarEstado(int idPedido, String nuevoEstado, LocalDate fechaEnvio, LocalDate fechaEntrega) throws SQLException, ReglaNegocioException {
        PedidoDTO p = buscarPorId(idPedido);
        if (p == null) throw new ReglaNegocioException("Pedido no existe.");

        if (fechaEnvio != null) p.setFecha_envio(fechaEnvio);
        if (fechaEntrega != null) p.setFecha_entrega(fechaEntrega);
        p.setEstado(nuevoEstado);

        validarPedido(p);
        actualizar(p);
    }
}
