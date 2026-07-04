package servicio;

import dao.TipoCuentaCorrienteDAO;
import modelo.TipoCuentaCorriente;

import java.util.List;

public class TipoCuentaCorrienteService {

    private final TipoCuentaCorrienteDAO dao = new TipoCuentaCorrienteDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private TipoCuentaCorriente registroPendiente = null;

    public String prepararAdicionar(int codigo, String nombre) {
        nombre = normalizarTexto(nombre);

        if (!codigoValido(codigo)) {
            return "Código inválido. Debe ser un número entre 1 y 99.";
        }

        if (!nombreValido(nombre)) {
            return "Nombre inválido. Debe tener entre 1 y 60 caracteres.";
        }

        if (dao.existe(codigo)) {
            return "El código " + codigo + " ya existe.";
        }

        registroPendiente = new TipoCuentaCorriente(codigo, nombre, "A");
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
            return "Nombre inválido. Debe tener entre 1 y 60 caracteres.";
        }

        TipoCuentaCorriente encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No se encontró el código " + codigo + ".";
        }

        registroPendiente = new TipoCuentaCorriente(
                encontrado.getTipCtaCod(),
                nuevoNombre,
                encontrado.getTipCtaEstReg()
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

        TipoCuentaCorriente encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No se encontró el código " + codigo + ".";
        }

        registroPendiente = new TipoCuentaCorriente(
                encontrado.getTipCtaCod(),
                encontrado.getTipCtaNom(),
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
                        registroPendiente.getTipCtaCod(),
                        registroPendiente.getTipCtaNom()
                );
                break;

            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(
                        registroPendiente.getTipCtaCod(),
                        registroPendiente.getTipCtaEstReg()
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

    public List<TipoCuentaCorriente> obtenerListado() {
        return dao.listarTodos();
    }

    public TipoCuentaCorriente buscarPorCodigo(int codigo) {
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
        return codigo == 1 || codigo == 2 || codigo == 3 || codigo == 99;
    }

    private boolean nombreValido(String nombre) {
        // Se cambió el límite a 60 según la nueva estructura de la tabla
        return nombre != null && !nombre.isBlank() && nombre.length() <= 60;
    }
}
