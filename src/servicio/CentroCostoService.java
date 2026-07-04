package servicio;

import dao.CentroCostoDAO;
import modelo.CentroCosto;

import java.util.List;

public class CentroCostoService {

    private final CentroCostoDAO dao = new CentroCostoDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService(); // Reutilizamos tu servicio anterior

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private CentroCosto registroPendiente = null;

    public String prepararAdicionar(String codigo, String nombre) {
        codigo = normalizarTexto(codigo).toUpperCase();
        nombre = normalizarTexto(nombre);

        if (!codigoValido(codigo)) {
            return "Código no válido. Debe tener 4 caracteres (Ej: A001).";
        }
        if (!nombreValido(nombre)) {
            return "Nombre no válido. Debe tener entre 1 y 60 caracteres.";
        }
        if (dao.existe(codigo)) {
            return "Ya existe un centro de costo con el código " + codigo + ".";
        }
        if (estadoService.buscarPorCodigo("A") == null) {
            return "Error: No existe el estado 'A' en GZZ_ESTADO_REGISTRO.";
        }

        registroPendiente = new CentroCosto(codigo, nombre, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String codigo, String nuevoNombre) {
        codigo = normalizarTexto(codigo).toUpperCase();
        nuevoNombre = normalizarTexto(nuevoNombre);

        if (!codigoValido(codigo)) return "Código no válido.";
        if (!nombreValido(nuevoNombre)) return "Nombre no válido.";

        CentroCosto encontrado = dao.buscarPorCodigo(codigo);
        if (encontrado == null) return "No existe el código " + codigo + ".";

        registroPendiente = new CentroCosto(encontrado.getCenCosCod(), nuevoNombre, encontrado.getCenCosEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararInactivar(String codigo) {
        codigo = normalizarTexto(codigo).toUpperCase();
        CentroCosto encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) return "No existe el código " + codigo + ".";
        if (estadoService.buscarPorCodigo("I") == null) return "Error: No existe el estado 'I'.";

        registroPendiente = new CentroCosto(encontrado.getCenCosCod(), encontrado.getCenCosNom(), "I");
        operacionPendiente = "INACTIVAR";
        estRegFlaAct = 1;

        return "Operación INACTIVAR preparada. Presione Actualizar.";
    }

    public String prepararReactivar(String codigo) {
        codigo = normalizarTexto(codigo).toUpperCase();
        CentroCosto encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) return "No existe el código " + codigo + ".";
        if (estadoService.buscarPorCodigo("A") == null) return "Error: No existe el estado 'A'.";

        registroPendiente = new CentroCosto(encontrado.getCenCosCod(), encontrado.getCenCosNom(), "A");
        operacionPendiente = "REACTIVAR";
        estRegFlaAct = 1;

        return "Operación REACTIVAR preparada. Presione Actualizar.";
    }

    public String prepararEliminar(String codigo) {
        codigo = normalizarTexto(codigo).toUpperCase();
        CentroCosto encontrado = dao.buscarPorCodigo(codigo);

        if (encontrado == null) return "No existe el código " + codigo + ".";
        if (estadoService.buscarPorCodigo("*") == null) return "Error: No existe el estado '*'.";

        registroPendiente = new CentroCosto(encontrado.getCenCosCod(), encontrado.getCenCosNom(), "*");
        operacionPendiente = "ELIMINAR";
        estRegFlaAct = 1;

        return "Operación ELIMINAR preparada. Presione Actualizar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null || operacionPendiente.isEmpty()) {
            return "No se ha seleccionado un comando para actualizar.";
        }

        boolean correcto = false;
        switch (operacionPendiente) {
            case "ADICIONAR":
                correcto = dao.insertar(registroPendiente);
                break;
            case "MODIFICAR":
                correcto = dao.modificarNombre(registroPendiente.getCenCosCod(), registroPendiente.getCenCosNom());
                break;
            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(registroPendiente.getCenCosCod(), registroPendiente.getCenCosEstReg());
                break;
        }

        if (correcto) {
            String mensaje = "Operación " + operacionPendiente + " realizada correctamente.";
            limpiarOperacion();
            return mensaje;
        } else {
            return "No se pudo realizar la operación. Verifique la base de datos.";
        }
    }

    public String cancelar() {
        limpiarOperacion();
        return "Operación cancelada.";
    }

    public List<CentroCosto> obtenerListado() {
        return dao.listarTodos();
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private boolean codigoValido(String codigo) {
        return codigo.length() > 0 && codigo.length() <= 4;
    }

    private boolean nombreValido(String nombre) {
        return !nombre.isEmpty() && nombre.length() <= 60;
    }
}