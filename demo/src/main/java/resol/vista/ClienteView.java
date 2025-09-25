package resol.vista;

import resol.controlador.ClienteControlador;
import resol.dto.ClienteDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClienteView {
    private ClienteControlador controlador = new ClienteControlador();
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuClientes() {
        System.out.println("=== GESTION CLIENTES ===");
        System.out.println("1. Listar clientes");
        System.out.println("2. Agregar cliente");
        System.out.println("3. Buscar cliente por ID");
        System.out.println("4. Eliminar cliente");
        System.out.println("5. Volver");
        System.out.print("Opción: ");
        return Integer.parseInt(sc.nextLine());
    }

    public void ejecutar() {
        while (true) {
            int op = mostrarMenuClientes();
            try {
                switch (op) {
                    case 1:
                        List<ClienteDTO> lista = controlador.listarClientes();
                        for (ClienteDTO c : lista) {
                            System.out.printf("%d | %s | Empresa: %s | Tipo: %s | Localidad: %s | FechaAlta: %s\n",
                                    c.getId_cliente(), c.getNombre_completo(), c.getNombre_empresa(), c.getTipo_empresa(), c.getLocalidad(), c.getFecha_alta());
                        }
                        break;
                    case 2:
                        ClienteDTO c = new ClienteDTO();
                        System.out.print("Nombre completo: "); c.setNombre_completo(sc.nextLine());
                        System.out.print("Nombre empresa: "); c.setNombre_empresa(sc.nextLine());
                        System.out.print("Tipo empresa: "); c.setTipo_empresa(sc.nextLine());
                        System.out.print("Localidad: "); c.setLocalidad(sc.nextLine());
                        System.out.print("Fecha de alta (YYYY-MM-DD): "); c.setFecha_alta(LocalDate.parse(sc.nextLine()));
                        controlador.agregarCliente(c);
                        System.out.println("Cliente agregado.");
                        break;
                    case 3:
                        System.out.print("ID: "); int id = Integer.parseInt(sc.nextLine());
                        ClienteDTO found = controlador.buscar(id);
                        if (found != null) {
                            System.out.println("Nombre: " + found.getNombre_completo());
                            System.out.println("Empresa: " + found.getNombre_empresa());
                        } else System.out.println("No encontrado.");
                        break;
                    case 4:
                        System.out.print("ID a eliminar: "); int idDel = Integer.parseInt(sc.nextLine());
                        controlador.eliminar(idDel);
                        System.out.println("Eliminado si existía.");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException ex) {
                System.err.println("Error de BD: " + ex.getMessage());
            } catch (ReglaNegocioException ex) {
                System.err.println("Regla de negocio: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
}
