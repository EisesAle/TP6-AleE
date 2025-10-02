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
        System.out.println("=== GESTIÓN EMPLEADOS ===");
        System.out.println("1. Listar empleados");
        System.out.println("2. Agregar empleado");
        System.out.println("3. Volver");
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
                        System.out.println("ID | Nombre | Bonificación%");
                        for (EmpleadoDTO e : lista) {
                            double bono = controlador.calcularBonificacion(e);
                            System.out.printf("%d | %s | %.2f%%\n",
                                    e.getId_empleado(), e.getNombre_completo(), bono);
                        }
                        break;
                    case 2:
                        System.out.print("Nombre: "); String nombre = sc.nextLine();
                        System.out.print("Fecha nacimiento (YYYY-MM-DD): "); LocalDate fn = LocalDate.parse(sc.nextLine());
                        System.out.print("Fecha contratación (YYYY-MM-DD): "); LocalDate fc = LocalDate.parse(sc.nextLine());
                        System.out.print("Cargo: "); String cargo = sc.nextLine();
                        System.out.print("Localidad: "); String loc = sc.nextLine();
                        controlador.agregarEmpleado(nombre, fn, fc, cargo, loc);
                        System.out.println("Empleado agregado.");
                        break;
                    case 3:
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
