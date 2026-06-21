package servicio;

import dao.TipoTrabajadorDAO;
import modelo.TipoTrabajador;

import java.util.List;

public class TipoTrabajadorService {

    private final TipoTrabajadorDAO dao = new TipoTrabajadorDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private TipoTrabajador registroPendiente = null;

    public String prepararAdicionar(int codigo, String nombre) {
        nombre = normalizarTexto(nombre);

        if (!codigoValido(codigo)) {
            return "Código no válido. Debe ser un número de 1 dígito.";
        }

        if (!nombreValido(nombre)) {
            return "Nombre no válido. Debe tener entre 1 y 40 caracteres.";
        }

        if (dao.existe(codigo)) {
            return "Ya existe un registro con el código " + codigo + ".";
        }

        // Por defecto, un nuevo registro nace como Activo ("A")
        registroPendiente = new TipoTrabajador(codigo, nombre, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int codigo, String nuevoNombre) {
        nuevoNombre = normalizarTexto(nuevoNombre);

        if (!codigoValido(codigo)) {
            return "Código no válido.";
        }

        if (!nombreValido(nuevoNombre)) {
            return "Nombre no válido. Debe tener entre 1 y 40 caracteres.";
        }

        TipoTrabajador encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        registroPendiente = new TipoTrabajador(
                encontrado.getTipTraCod(),
                nuevoNombre,
                encontrado.getTipTraEstReg()
        );

        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int codigo) {
        return prepararCambioEstado(codigo, "*", "ELIMINAR");
    }

    public String prepararInactivar(int codigo) {
        return prepararCambioEstado(codigo, "I", "INACTIVAR");
    }

    public String prepararReactivar(int codigo) {
        return prepararCambioEstado(codigo, "A", "REACTIVAR");
    }

    private String prepararCambioEstado(int codigo, String nuevoEstado, String operacion) {
        if (!codigoValido(codigo)) {
            return "Código no válido.";
        }

        TipoTrabajador encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        registroPendiente = new TipoTrabajador(
                encontrado.getTipTraCod(),
                encontrado.getTipTraNom(),
                nuevoEstado
        );

        operacionPendiente = operacion;
        estRegFlaAct = 1;

        return "Operación " + operacion + " preparada. Presione Actualizar para grabar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null || operacionPendiente.isEmpty()) {
            return "No se ha seleccionado un comando para actualizar un registro de la BD.";
        }

        boolean correcto = false;

        switch (operacionPendiente) {
            case "ADICIONAR":
                correcto = dao.insertar(registroPendiente);
                break;

            case "MODIFICAR":
                correcto = dao.modificarNombre(
                        registroPendiente.getTipTraCod(),
                        registroPendiente.getTipTraNom()
                );
                break;

            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(
                        registroPendiente.getTipTraCod(),
                        registroPendiente.getTipTraEstReg()
                );
                break;

            default:
                return "Operación no reconocida.";
        }

        if (correcto) {
            String mensaje = "Operación " + operacionPendiente + " realizada correctamente.";
            limpiarOperacion();
            return mensaje;
        } else {
            return "No se pudo realizar la operación. Puede corregir o cancelar.";
        }
    }

    public String cancelar() {
        limpiarOperacion();
        return "Operación cancelada. Se limpió la operación pendiente.";
    }

    public List<TipoTrabajador> obtenerListado() {
        return dao.listarTodos();
    }

    public TipoTrabajador buscarPorCodigo(int codigo) {
        return dao.buscarPorCodigo(codigo);
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    private boolean codigoValido(int codigo) {
        return codigo == 1 || codigo == 2 || codigo == 3;
    }

    private boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isBlank() && nombre.length() <= 40;
    }
}
