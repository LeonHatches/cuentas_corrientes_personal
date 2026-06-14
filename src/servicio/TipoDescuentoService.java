package servicio;

import dao.TipoDescuentoDAO;
import modelo.TipoDescuento;

import java.util.List;

public class TipoDescuentoService {

    private final TipoDescuentoDAO dao = new TipoDescuentoDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private TipoDescuento registroPendiente = null;

    public String prepararAdicionar(String codigo, String nombre) {
        codigo = normalizarCodigo(codigo);
        nombre = normalizarTexto(nombre);

        if (!codigoValido(codigo)) {
            return "Código no válido. Use 'B' (Beneficio) o 'S' (Servicio).";
        }

        if (!nombreValido(nombre)) {
            return "Nombre no válido. Debe tener entre 1 y 40 caracteres.";
        }

        if (dao.existe(codigo)) {
            return "Ya existe un registro con el código " + codigo + ".";
        }

        registroPendiente = new TipoDescuento(codigo, nombre, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String codigo, String nuevoNombre) {
        codigo = normalizarCodigo(codigo);
        nuevoNombre = normalizarTexto(nuevoNombre);

        if (!codigoValido(codigo)) {
            return "Código no válido.";
        }

        if (!nombreValido(nuevoNombre)) {
            return "Nombre no válido. Debe tener entre 1 y 40 caracteres.";
        }

        TipoDescuento encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        registroPendiente = new TipoDescuento(
                encontrado.getTipDesCod(),
                nuevoNombre,
                encontrado.getTipDesEstReg()
        );

        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(String codigo) {
        return prepararCambioEstado(codigo, "*", "ELIMINAR");
    }

    public String prepararInactivar(String codigo) {
        return prepararCambioEstado(codigo, "I", "INACTIVAR");
    }

    public String prepararReactivar(String codigo) {
        return prepararCambioEstado(codigo, "A", "REACTIVAR");
    }

    private String prepararCambioEstado(String codigo, String nuevoEstadoRegistro, String operacion) {
        codigo = normalizarCodigo(codigo);

        if (!codigoValido(codigo)) {
            return "Código no válido.";
        }

        TipoDescuento encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        registroPendiente = new TipoDescuento(
                encontrado.getTipDesCod(),
                encontrado.getTipDesNom(),
                nuevoEstadoRegistro
        );

        operacionPendiente = operacion;
        estRegFlaAct = 1;

        return "Operación " + operacion + " preparada. Presione Actualizar para grabar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null || operacionPendiente.isEmpty()) {
            return "No hay ninguna operación pendiente para actualizar.";
        }

        boolean correcto = false;

        switch (operacionPendiente) {
            case "ADICIONAR":
                correcto = dao.insertar(registroPendiente);
                break;

            case "MODIFICAR":
                correcto = dao.modificarNombre(
                        registroPendiente.getTipDesCod(),
                        registroPendiente.getTipDesNom()
                );
                break;

            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(
                        registroPendiente.getTipDesCod(),
                        registroPendiente.getTipDesEstReg()
                );
                break;

            default:
                return "Operación no reconocida.";
        }

        if (correcto) {
            String mensaje = "Operación " + operacionPendiente + " realizada con éxito.";
            limpiarOperacion();
            return mensaje;
        } else {
            return "Ocurrió un error. Verifique los datos o cancele la operación.";
        }
    }

    public String cancelar() {
        limpiarOperacion();
        return "Se canceló la operación pendiente.";
    }

    public List<TipoDescuento> obtenerListado() {
        return dao.listarTodos();
    }

    public TipoDescuento buscarPorCodigo(String codigo) {
        return dao.buscarPorCodigo(normalizarCodigo(codigo));
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }

    private String normalizarCodigo(String codigo) {
        if (codigo == null) return "";
        return codigo.trim().toUpperCase();
    }

    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        return texto.trim();
    }

    private boolean codigoValido(String codigo) {
        return "B".equals(codigo) || "S".equals(codigo);
    }

    private boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isBlank() && nombre.length() <= 40;
    }
}