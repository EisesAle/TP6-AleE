package resol.vista;

import resol.controlador.PedidoControlador;
import resol.controlador.ClienteControlador;
import resol.controlador.EmpleadoControlador;
import resol.controlador.ProductoControlador;
import resol.dto.*;

import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PedidoView {
    private PedidoControlador controlador = new PedidoControlador();
    private ClienteControlador clienteCtrl = new ClienteControlador();
    private EmpleadoControlador empleadoCtrl = new EmpleadoControlador();
    private ProductoControlador productoCtrl = new ProductoControlador();
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuPedidos() {
        System.out.println("=== GESTION PEDIDOS ===");
        System.out.println("1. Listar pedidos");
        System.out.println("2. Crear pedido");
        System.out.println("3. Buscar pedido por ID");
        System.out.println("4. Listar pedidos por cliente");
        System.out.println("5. Cambiar estado de pedido");
        System.out.println("6. Eliminar pedido");
        System.out.println("7. Volver");
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
                            System.out.printf("%d | Fecha: %s | Estado: %s | Monto: %.2f | Cliente: %s\n",
                                    p.getId_pedido(), p.getFecha_pedido(), p.getEstado(), p.getMonto_total(), p.getCliente().getNombre_completo());
                        }
                        break;
                    case 2:
                        PedidoDTO nuevo = new PedidoDTO();
                        System.out.print("ID Cliente: "); int idCli = Integer.parseInt(sc.nextLine());
                        ClienteDTO cli = clienteCtrl.buscar(idCli);
                        if (cli == null) { System.out.println("Cliente no encontrado."); break; }
                        System.out.print("ID Empleado: "); int idEmp = Integer.parseInt(sc.nextLine());
                        EmpleadoDTO emp = empleadoCtrl.buscar(idEmp);
                        if (emp == null) { System.out.println("Empleado no encontrado."); break; }

                        nuevo.setCliente(cli);
                        nuevo.setEmpleado(emp);

                        System.out.print("Fecha pedido (YYYY-MM-DD): "); nuevo.setFecha_pedido(LocalDate.parse(sc.nextLine()));
                        System.out.print("Estado inicial (Pendiente/Enviado/Entregado): "); nuevo.setEstado(sc.nextLine());

                        List<DetallePedidoDTO> detalles = new ArrayList<>();
                        double total = 0.0;
                        while (true) {
                            System.out.print("Agregar detalle? (s/n): ");
                            String r = sc.nextLine();
                            if (!r.equalsIgnoreCase("s")) break;
                            System.out.print("ID producto: "); int idProd = Integer.parseInt(sc.nextLine());
                            ProductoDTO prod = productoCtrl.buscarPorId(idProd).producto;
                            if (prod == null) { System.out.println("Producto no existe."); continue; }
                            System.out.print("Cantidad: "); int cant = Integer.parseInt(sc.nextLine());
                            double precio = prod.getPrecioUnidad();
                            double sub = precio * cant;
                            DetallePedidoDTO det = new DetallePedidoDTO();
                            det.setId_producto(idProd);
                            det.setCantidad(cant);
                            det.setPrecioUnitario(precio);
                            det.setSubtotal(sub);
                            det.setNombreProducto(prod.getNombre());
                            detalles.add(det);
                            total += sub;
                        }
                        nuevo.setDetalle(detalles);
                        nuevo.setMonto_total(total);

                        controlador.altaPedido(nuevo);
                        System.out.println("Pedido creado. ID: " + nuevo.getId_pedido());
                        break;
                    case 3:
                        System.out.print("ID pedido: "); int id = Integer.parseInt(sc.nextLine());
                        PedidoDTO p = controlador.buscar(id);
                        if (p == null) System.out.println("No encontrado.");
                        else {
                            System.out.printf("Pedido %d | Fecha: %s | Estado: %s | Monto: %.2f\n", p.getId_pedido(), p.getFecha_pedido(), p.getEstado(), p.getMonto_total());
                            System.out.println("Cliente: " + p.getCliente().getNombre_completo());
                            System.out.println("Empleado: " + p.getEmpleado().getNombre_completo());
                            System.out.println("Detalle:");
                            for (DetallePedidoDTO d : p.getDetalle()) {
                                System.out.println(" - " + d.getNombreProducto() + " x" + d.getCantidad() + " = " + d.getSubtotal());
                            }
                        }
                        break;
                    case 4:
                        System.out.print("ID cliente: "); int idc = Integer.parseInt(sc.nextLine());
                        List<PedidoDTO> listCli = controlador.listarPedidosPorCliente(idc);
                        for (PedidoDTO ped : listCli) {
                            System.out.printf("%d | Fecha: %s | Estado: %s | Monto: %.2f\n", ped.getId_pedido(), ped.getFecha_pedido(), ped.getEstado(), ped.getMonto_total());
                        }
                        break;
                    case 5:
                        System.out.print("ID pedido: "); int idp = Integer.parseInt(sc.nextLine());
                        System.out.print("Nuevo estado (Pendiente/Enviado/Entregado): "); String nuevoEstado = sc.nextLine();
                        LocalDate fEnv = null, fEnt = null;
                        if (nuevoEstado.equalsIgnoreCase("Enviado")) {
                            System.out.print("Fecha envio (YYYY-MM-DD): "); fEnv = LocalDate.parse(sc.nextLine());
                        } else if (nuevoEstado.equalsIgnoreCase("Entregado")) {
                            System.out.print("Fecha envio (YYYY-MM-DD): "); fEnv = LocalDate.parse(sc.nextLine());
                            System.out.print("Fecha entrega (YYYY-MM-DD): "); fEnt = LocalDate.parse(sc.nextLine());
                        }
                        controlador.cambiarEstado(idp, nuevoEstado, fEnv, fEnt);
                        System.out.println("Estado actualizado.");
                        break;
                    case 6:
                        System.out.print("ID a eliminar: "); int iddel = Integer.parseInt(sc.nextLine());
                        controlador.eliminar(iddel);
                        System.out.println("Eliminado si existía.");
                        break;
                    case 7:
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
