package consola;

import modelo.EstadoRegistro;
import servicio.EstadoRegistroService;

import java.util.List;
import java.util.Scanner;

public class EstadoRegistroMenu {

    private final EstadoRegistroService service = new EstadoRegistroService();
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    adicionar();
                    break;

                case 2:
                    modificar();
                    break;

                case 3:
                    eliminar();
                    break;

                case 4:
                    inactivar();
                    break;

                case 5:
                    reactivar();
                    break;

                case 6:
                    actualizar();
                    break;

                case 7:
                    cancelar();
                    break;

                case 8:
                    listar();
                    break;

                case 9:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 9);
    }

    private void mostrarMenu() {
        System.out.println("\n==============================================");
        System.out.println(" MANTENIMIENTO GZZ_ESTADO_REGISTRO");
        System.out.println("==============================================");
        System.out.println("1. Adicionar");
        System.out.println("2. Modificar");
        System.out.println("3. Eliminar lógico");
        System.out.println("4. Inactivar");
        System.out.println("5. Reactivar");
        System.out.println("6. Actualizar");
        System.out.println("7. Cancelar");
        System.out.println("8. Listar / Consultar");
        System.out.println("9. Salir");
        System.out.println("==============================================");
    }

    private void adicionar() {
        System.out.println("\n--- ADICIONAR ESTADO REGISTRO ---");
        System.out.println("Códigos permitidos: A, I, *");

        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");

        String mensaje = service.prepararAdicionar(codigo, nombre);
        System.out.println(mensaje);
    }

    private void modificar() {
        System.out.println("\n--- MODIFICAR ESTADO REGISTRO ---");

        String codigo = leerTexto("Código a modificar: ");
        String nuevoNombre = leerTexto("Nuevo nombre: ");

        String mensaje = service.prepararModificar(codigo, nuevoNombre);
        System.out.println(mensaje);
    }

    private void eliminar() {
        System.out.println("\n--- ELIMINAR LÓGICO ESTADO REGISTRO ---");

        String codigo = leerTexto("Código a eliminar lógicamente: ");

        String mensaje = service.prepararEliminar(codigo);
        System.out.println(mensaje);
    }

    private void inactivar() {
        System.out.println("\n--- INACTIVAR ESTADO REGISTRO ---");

        String codigo = leerTexto("Código a inactivar: ");

        String mensaje = service.prepararInactivar(codigo);
        System.out.println(mensaje);
    }

    private void reactivar() {
        System.out.println("\n--- REACTIVAR ESTADO REGISTRO ---");

        String codigo = leerTexto("Código a reactivar: ");

        String mensaje = service.prepararReactivar(codigo);
        System.out.println(mensaje);
    }

    private void actualizar() {
        System.out.println("\n--- ACTUALIZAR BD ---");

        String mensaje = service.actualizar();
        System.out.println(mensaje);

        listar();
    }

    private void cancelar() {
        System.out.println("\n--- CANCELAR OPERACIÓN ---");

        String mensaje = service.cancelar();
        System.out.println(mensaje);
    }

    private void listar() {
        System.out.println("\n--- LISTADO GZZ_ESTADO_REGISTRO ---");

        List<EstadoRegistro> lista = service.obtenerListado();

        if (lista.isEmpty()) {
            System.out.println("No hay registros.");
            return;
        }

        System.out.printf("%-10s %-40s %-15s%n", "Código", "Nombre", "Estado Reg.");
        System.out.println("----------------------------------------------------------------");

        for (EstadoRegistro estado : lista) {
            System.out.printf(
                    "%-10s %-40s %-15s%n",
                    estado.getEstReg(),
                    estado.getEstRegNom(),
                    estado.getEstRegEstReg()
            );
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = sc.nextLine();
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            }
        }
    }
}