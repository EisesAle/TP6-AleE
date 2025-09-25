package resol;

import resol.vista.*;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmpleadoView empleadoView = new EmpleadoView();
        ProductoView productoView = new ProductoView();
        ClienteView clienteView = new ClienteView();
        PedidoView pedidoView = new PedidoView();

        while (true) {
            System.out.println("=== SISTEMA TP6 ===");
            System.out.println("1. Gestión Empleados");
            System.out.println("2. Gestión Productos");
            System.out.println("3. Gestión Clientes");
            System.out.println("4. Gestión Pedidos");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            String linea = sc.nextLine();
            int op = 0;
            try { op = Integer.parseInt(linea); } catch (Exception ignored) {}
            switch (op) {
                case 1: empleadoView.ejecutar(); break;
                case 2: productoView.ejecutar(); break;
                case 3: clienteView.ejecutar(); break;
                case 4: pedidoView.ejecutar(); break;
                case 5:
                    System.out.println("Saliendo..."); System.exit(0); break;
                default:
                    System.out.println("Opción inválida.");
            }
sc.close(); }
    }
}
