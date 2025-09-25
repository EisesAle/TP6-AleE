package resol.dto;

import java.time.LocalDate;

public class ClienteDTO {
    private int id_cliente;
    private String nombre_completo;
    private String nombre_empresa;
    private String tipo_empresa;
    private String localidad;
    private LocalDate fecha_alta;

    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }

    public String getNombre_completo() { return nombre_completo; }
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }

    public String getNombre_empresa() { return nombre_empresa; }
    public void setNombre_empresa(String nombre_empresa) { this.nombre_empresa = nombre_empresa; }

    public String getTipo_empresa() { return tipo_empresa; }
    public void setTipo_empresa(String tipo_empresa) { this.tipo_empresa = tipo_empresa; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public LocalDate getFecha_alta() { return fecha_alta; }
    public void setFecha_alta(LocalDate fecha_alta) { this.fecha_alta = fecha_alta; }
}
