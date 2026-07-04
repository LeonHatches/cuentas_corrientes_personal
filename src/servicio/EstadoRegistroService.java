package servicio;

import dao.EstadoRegistroDAO;
import modelo.EstadoRegistro;

import java.util.List;

public class EstadoRegistroService {

    private final EstadoRegistroDAO dao = new EstadoRegistroDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private EstadoRegistro registroPendiente = null;

    public String prepararAdicionar(String codigo, String nombre) {
        codigo = normalizarCodigo(codigo);
        nombre = normalizarTexto(nombre);

        if (!codigoValido(codigo)) {
            return "Código no válido. Solo se permite A, I o *.";
        }

        if (!nombreValido(nombre)) {
            return "Nombre no válido. Debe tener entre 1 y 40 caracteres.";
        }

        if (dao.existe(codigo)) {
            return "Ya existe un registro con el código " + codigo + ".";
        }

        String estadoRegistro;

        if ("A".equals(codigo) && !dao.existe("A")) {
            estadoRegistro = null;
        } else {
            if (!dao.existe("A")) {
                return "Primero debe existir el estado A = Activo.";
            }
            estadoRegistro = "A";
        }

        registroPendiente = new EstadoRegistro(codigo, nombre, estadoRegistro);
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String codigo, String nuevoNombre) {
        codigo = normalizarCodigo(codigo);
        nuevoNombre = normalizarTexto(nuevoNombre);

        if (!codigoValido(codigo)) {
            return "Código no válido. Solo se permite A, I o *.";
        }

        if (!nombreValido(nuevoNombre)) {
            return "Nombre no válido. Debe tener entre 1 y 40 caracteres.";
        }

        EstadoRegistro encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        registroPendiente = new EstadoRegistro(
                encontrado.getEstReg(),
                nuevoNombre,
                encontrado.getEstRegEstReg()
        );

        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(String codigo) {
        codigo = normalizarCodigo(codigo);

        if (!codigoValido(codigo)) {
            return "Código no válido. Solo se permite A, I o *.";
        }

        EstadoRegistro encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        if (!dao.existe("*")) {
            return "Primero debe existir el estado * = Eliminado.";
        }

        registroPendiente = new EstadoRegistro(
                encontrado.getEstReg(),
                encontrado.getEstRegNom(),
                "*"
        );

        operacionPendiente = "ELIMINAR";
        estRegFlaAct = 1;

        return "Operación ELIMINAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararInactivar(String codigo) {
        codigo = normalizarCodigo(codigo);

        if (!codigoValido(codigo)) {
            return "Código no válido. Solo se permite A, I o *.";
        }

        EstadoRegistro encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        if (!dao.existe("I")) {
            return "Primero debe existir el estado I = Inactivo.";
        }

        registroPendiente = new EstadoRegistro(
                encontrado.getEstReg(),
                encontrado.getEstRegNom(),
                "I"
        );

        operacionPendiente = "INACTIVAR";
        estRegFlaAct = 1;

        return "Operación INACTIVAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararReactivar(String codigo) {
        codigo = normalizarCodigo(codigo);

        if (!codigoValido(codigo)) {
            return "Código no válido. Solo se permite A, I o *.";
        }

        EstadoRegistro encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) {
            return "No existe el código " + codigo + ".";
        }

        if (!dao.existe("A")) {
            return "Primero debe existir el estado A = Activo.";
        }

        registroPendiente = new EstadoRegistro(
                encontrado.getEstReg(),
                encontrado.getEstRegNom(),
                "A"
        );

        operacionPendiente = "REACTIVAR";
        estRegFlaAct = 1;

        return "Operación REACTIVAR preparada. Presione Actualizar para grabar.";
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
                        registroPendiente.getEstReg(),
                        registroPendiente.getEstRegNom()
                );
                break;

            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(
                        registroPendiente.getEstReg(),
                        registroPendiente.getEstRegEstReg()
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

    public List<EstadoRegistro> obtenerListado() {
        return dao.listarTodos();
    }

    public EstadoRegistro buscarPorCodigo(String codigo) {
        return dao.buscarPorCodigo(normalizarCodigo(codigo));
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }

    private String normalizarCodigo(String codigo) {
        if (codigo == null) {
            return "";
        }

        codigo = codigo.trim();

        if ("*".equals(codigo)) {
            return "*";
        }

        return codigo.toUpperCase();
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    private boolean codigoValido(String codigo) {
        return "A".equals(codigo) || "I".equals(codigo) || "*".equals(codigo);
    }

    private boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isBlank() && nombre.length() <= 40;
    }
}