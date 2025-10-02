package resol.vista;

import resol.controlador.ProductoControlador;
import resol.dto.ProductoDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductoView {
    private ProductoControlador controlador = new ProductoControlador();
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuProductos() {
        System.out.println("=== GESTIÓN PRODUCTOS ===");
        System.out.println("1. Listar disponibles");
        System.out.println("2. Listar que necesitan reposición");
        System.out.println("3. Agregar producto");
        System.out.println("4. Volver");
        System.out.print("Opción: ");
        return Integer.parseInt(sc.nextLine());
    }

    public void ejecutar() {
        while (true) {
            int op = mostrarMenuProductos();
            try {
                switch (op) {
                    case 1:
                        List<ProductoDTO> disp = controlador.listarDisponibles();
                        disp.forEach(p -> System.out.printf("%d - %s | stock:%d\n", p.getId_producto(), p.getNombre(), p.getStock()));
                        break;
                    case 2:
                        List<ProductoDTO> rep = controlador.listarNecesitanReposicion();
                        rep.forEach(p -> System.out.printf("%d - %s | stock:%d necesita reposición\n", p.getId_producto(), p.getNombre(), p.getStock()));
                        break;
                    case 3:
                        ProductoDTO p = new ProductoDTO();
                        System.out.print("Nombre: "); p.setNombre(sc.nextLine());
                        System.out.print("Stock: "); p.setStock(Integer.parseInt(sc.nextLine()));
                        System.out.print("Nivel nuevo pedido: "); p.setNivelNuevoPedido(Integer.parseInt(sc.nextLine()));
                        System.out.print("Precio unidad: "); p.setPrecioUnidad(Double.parseDouble(sc.nextLine()));
                        System.out.print("Disponible? (true/false): "); p.setDisponible(Boolean.parseBoolean(sc.nextLine()));
                        controlador.agregarProducto(p);
                        System.out.println("Producto agregado.");
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException ex) {
                System.err.println("Error de BD: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
}
