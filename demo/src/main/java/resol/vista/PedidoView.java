package resol.vista;

import resol.controlador.PedidoControlador;
import resol.dto.*;

import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PedidoView {
    private PedidoControlador controlador = new PedidoControlador();
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuPedidos() {
        System.out.println("=== GESTIÓN PEDIDOS ===");
        System.out.println("1. Listar pedidos");
        System.out.println("2. Crear pedido");
        System.out.println("3. Listar pedidos por cliente");
        System.out.println("4. Cambiar estado de pedido");
        System.out.println("5. Volver");
        System.out.print("Opción: ");
        return Integer.parseInt(sc.nextLine());
    }

    public void ejecutar() {
        while (true) {
            int op = mostrarMenuPedidos();
            try {
                switch (op) {
                    case 1:
                        List<PedidoDTO> all = controlador.listarTodos();
                        for (PedidoDTO p : all) {
                            System.out.printf("%d | %s | %s | %.2f | Cliente: %s\n",
                                    p.getId_pedido(), p.getFecha_pedido(), p.getEstado(),
                                    p.getMonto_total(), p.getCliente().getNombre_completo());
                        }
                        break;
                    case 2:
                        PedidoDTO nuevo = new PedidoDTO();
                        ClienteDTO cli = new ClienteDTO();
                        EmpleadoDTO emp = new EmpleadoDTO();
                        List<DetallePedidoDTO> detalles = new ArrayList<>();

                        System.out.print("ID Cliente: "); cli.setId_cliente(Integer.parseInt(sc.nextLine()));
                        nuevo.setCliente(cli);
                        System.out.print("ID Empleado: "); emp.setId_empleado(Integer.parseInt(sc.nextLine()));
                        nuevo.setEmpleado(emp);

                        System.out.print("Fecha pedido (YYYY-MM-DD): "); nuevo.setFecha_pedido(LocalDate.parse(sc.nextLine()));
                        System.out.print("Estado inicial: "); nuevo.setEstado(sc.nextLine());

                        double total = 0.0;
                        while (true) {
                            System.out.print("Agregar detalle? (s/n): ");
                            if (!sc.nextLine().equalsIgnoreCase("s")) break;
                            DetallePedidoDTO det = new DetallePedidoDTO();
                            System.out.print("ID producto: "); det.setId_producto(Integer.parseInt(sc.nextLine()));
                            System.out.print("Cantidad: "); det.setCantidad(Integer.parseInt(sc.nextLine()));
                            System.out.print("Precio unitario: "); det.setPrecioUnitario(Double.parseDouble(sc.nextLine()));
                            det.setSubtotal(det.getCantidad() * det.getPrecioUnitario());
                            total += det.getSubtotal();
                            detalles.add(det);
                        }
                        nuevo.setDetalle(detalles);
                        nuevo.setMonto_total(total);

                        controlador.altaPedido(nuevo);
                        System.out.println("Pedido creado. ID: " + nuevo.getId_pedido());
                        break;
                    case 3:
                        System.out.print("ID cliente: "); int idc = Integer.parseInt(sc.nextLine());
                        List<PedidoDTO> listCli = controlador.listarPedidosPorCliente(idc);
                        for (PedidoDTO ped : listCli) {
                            System.out.printf("%d | %s | %s | %.2f\n",
                                    ped.getId_pedido(), ped.getFecha_pedido(), ped.getEstado(), ped.getMonto_total());
                        }
                        break;
                    case 4:
                        System.out.print("ID pedido: "); int idp = Integer.parseInt(sc.nextLine());
                        System.out.print("Nuevo estado: "); String nuevoEstado = sc.nextLine();
                        LocalDate fEnv = null, fEnt = null;
                        if (nuevoEstado.equalsIgnoreCase("Enviado")) {
                            System.out.print("Fecha envío (YYYY-MM-DD): "); fEnv = LocalDate.parse(sc.nextLine());
                        } else if (nuevoEstado.equalsIgnoreCase("Entregado")) {
                            System.out.print("Fecha envío (YYYY-MM-DD): "); fEnv = LocalDate.parse(sc.nextLine());
                            System.out.print("Fecha entrega (YYYY-MM-DD): "); fEnt = LocalDate.parse(sc.nextLine());
                        }
                        controlador.cambiarEstado(idp, nuevoEstado, fEnv, fEnt);
                        System.out.println("Estado actualizado.");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException ex) {
                System.err.println("Error BD: " + ex.getMessage());
            } catch (ReglaNegocioException ex) {
                System.err.println("Regla negocio: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
}
