package resol.dto;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    private int id_pedido;
    private LocalDate fecha_pedido;
    private LocalDate fecha_envio;
    private LocalDate fecha_entrega;
    private String estado;
    private double monto_total;

    private ClienteDTO cliente;
    private EmpleadoDTO empleado;
    private List<DetallePedidoDTO> detalle;

    public int getId_pedido() { return id_pedido; }
    public void setId_pedido(int id_pedido) { this.id_pedido = id_pedido; }

    public LocalDate getFecha_pedido() { return fecha_pedido; }
    public void setFecha_pedido(LocalDate fecha_pedido) { this.fecha_pedido = fecha_pedido; }

    public LocalDate getFecha_envio() { return fecha_envio; }
    public void setFecha_envio(LocalDate fecha_envio) { this.fecha_envio = fecha_envio; }

    public LocalDate getFecha_entrega() { return fecha_entrega; }
    public void setFecha_entrega(LocalDate fecha_entrega) { this.fecha_entrega = fecha_entrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getMonto_total() { return monto_total; }
    public void setMonto_total(double monto_total) { this.monto_total = monto_total; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public EmpleadoDTO getEmpleado() { return empleado; }
    public void setEmpleado(EmpleadoDTO empleado) { this.empleado = empleado; }

    public List<DetallePedidoDTO> getDetalle() { return detalle; }
    public void setDetalle(List<DetallePedidoDTO> detalle) { this.detalle = detalle; }
}
