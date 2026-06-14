package servicio;

import dao.TipoPrestamoDAO;
import modelo.TipoPrestamo;

import java.util.List;

public class TipoPrestamoService {

    private final TipoPrestamoDAO dao = new TipoPrestamoDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private TipoPrestamo registroPendiente = null;

    public String prepararAdicionar(int codigo, String nombre) {
        nombre = normalizarTexto(nombre);

        if (!codigoValido(codigo)) {
            return "Código inválido. Debe ser un número entre 1 y 99.";
        }

        if (!nombreValido(nombre)) {
            return "Nombre inválido. Debe tener entre 1 y 40 caracteres.";
        }

        if (dao.existe(codigo)) {
            return "El código " + codigo + " ya existe.";
        }

        registroPendiente = new TipoPrestamo(codigo, nombre, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR lista. Presione Actualizar para guardar.";
    }

    public String prepararModificar(int codigo, String nuevoNombre) {
        nuevoNombre = normalizarTexto(nuevoNombre);

        if (!codigoValido(codigo)) {
            return "Código inválido.";
        }

        if (!nombreValido(nuevoNombre)) {
            return "Nombre inválido. Debe tener entre 1 y 40 caracteres.";
        }

        TipoPrestamo encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No se encontró el código " + codigo + ".";
        }

        registroPendiente = new TipoPrestamo(
                encontrado.getTipPreCod(),
                nuevoNombre,
                encontrado.getTipPreEstReg()
        );

        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR lista. Presione Actualizar para guardar.";
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
            return "Código inválido.";
        }

        TipoPrestamo encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No se encontró el código " + codigo + ".";
        }

        registroPendiente = new TipoPrestamo(
                encontrado.getTipPreCod(),
                encontrado.getTipPreNom(),
                nuevoEstado
        );

        operacionPendiente = operacion;
        estRegFlaAct = 1;

        return "Operación " + operacion + " lista. Presione Actualizar para guardar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null || operacionPendiente.isEmpty()) {
            return "No hay ninguna operación pendiente.";
        }

        boolean correcto = false;

        switch (operacionPendiente) {
            case "ADICIONAR":
                correcto = dao.insertar(registroPendiente);
                break;

            case "MODIFICAR":
                correcto = dao.modificarNombre(
                        registroPendiente.getTipPreCod(),
                        registroPendiente.getTipPreNom()
                );
                break;

            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(
                        registroPendiente.getTipPreCod(),
                        registroPendiente.getTipPreEstReg()
                );
                break;

            default:
                return "Operación desconocida.";
        }

        if (correcto) {
            String mensaje = "Operación " + operacionPendiente + " exitosa.";
            limpiarOperacion();
            return mensaje;
        } else {
            return "Error al intentar guardar. Verifique los datos o cancele.";
        }
    }

    public String cancelar() {
        limpiarOperacion();
        return "Operación cancelada.";
    }

    public List<TipoPrestamo> obtenerListado() {
        return dao.listarTodos();
    }

    public TipoPrestamo buscarPorCodigo(int codigo) {
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
        // Al ser N de longitud 2, se aceptan valores del 1 al 99
        return codigo > 0 && codigo <= 99;
    }

    private boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isBlank() && nombre.length() <= 40;
    }
}