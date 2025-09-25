package resol.vista;

import resol.controlador.EmpleadoControlador;
import resol.dto.EmpleadoDTO;
import resol.excepciones.ReglaNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EmpleadoView {
    private EmpleadoControlador controlador = new EmpleadoControlador();
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuEmpleados() {
        System.out.println("=== GESTION EMPLEADOS ===");
        System.out.println("1. Listar empleados");
        System.out.println("2. Agregar empleado");
        System.out.println("3. Buscar empleado por ID");
        System.out.println("4. Eliminar empleado");
        System.out.println("5. Volver");
        System.out.print("Opción: ");
        return Integer.parseInt(sc.nextLine());
    }

    public void ejecutar() {
        while (true) {
            int op = mostrarMenuEmpleados();
            try {
                switch (op) {
                    case 1:
                        List<EmpleadoDTO> lista = controlador.listarEmpleados();
                        System.out.println("ID | Nombre | Antiguedad | Bonificación%");
                        for (EmpleadoDTO e : lista) {
                            int antig = controlador.calcularAntiguedad(e);
                            double bono = controlador.calcularBonificacion(e);
                            System.out.printf("%d | %s | %d | %.2f%%\n", e.getId_empleado(), e.getNombre_completo(), antig, bono);
                        }
                        break;
                    case 2:
                        System.out.print("Nombre: "); String nombre = sc.nextLine();
                        System.out.print("Fecha nacimiento (YYYY-MM-DD): "); LocalDate fn = LocalDate.parse(sc.nextLine());
                        System.out.print("Fecha contratacion (YYYY-MM-DD): "); LocalDate fc = LocalDate.parse(sc.nextLine());
                        System.out.print("Cargo: "); String cargo = sc.nextLine();
                        System.out.print("Localidad: "); String loc = sc.nextLine();
                        controlador.agregarEmpleado(nombre, fn, fc, cargo, loc);
                        System.out.println("Empleado agregado.");
                        break;
                    case 3:
                        System.out.print("ID: "); int id = Integer.parseInt(sc.nextLine());
                        EmpleadoDTO e = controlador.buscar(id);
                        if (e != null) {
                            System.out.println("Nombre: " + e.getNombre_completo());
                            System.out.println("Cargo: " + e.getCargo());
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
