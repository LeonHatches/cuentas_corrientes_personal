package servicio;

import dao.ActaDAO;
import modelo.Acta;
import java.util.List;

public class ActaService {

    private final ActaDAO dao = new ActaDAO();
    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private Acta registroPendiente = null;

    public String prepararAdicionar(int actCod, String actRef, int actFec) {
        if (actCod <= 0) return "El código del acta es inválido.";
        if (dao.existe(actCod)) return "Error: El código de acta ya existe en el sistema.";

        registroPendiente = new Acta(actCod, actRef, actFec, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;
        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int actCod, String actRef, int actFec) {
        Acta enc = dao.buscarPorCodigo(actCod);
        if (enc == null) return "No existe el acta a modificar.";

        registroPendiente = new Acta(actCod, actRef, actFec, enc.getActEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;
        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int actCod) { return prepararCambioEstado(actCod, "*", "ELIMINAR"); }
    public String prepararInactivar(int actCod) { return prepararCambioEstado(actCod, "I", "INACTIVAR"); }
    public String prepararReactivar(int actCod) { return prepararCambioEstado(actCod, "A", "REACTIVAR"); }

    private String prepararCambioEstado(int actCod, String est, String op) {
        Acta enc = dao.buscarPorCodigo(actCod);
        if (enc == null) return "No existe el acta seleccionada.";

        registroPendiente = new Acta(enc.getActCod(), enc.getActRef(), enc.getActFec(), est);
        operacionPendiente = op;
        estRegFlaAct = 1;
        return "Operación " + op + " lista. Presione Actualizar para grabar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null) return "No hay operación pendiente.";
        boolean correcto = false;
        switch (operacionPendiente) {
            case "ADICIONAR": correcto = dao.insertar(registroPendiente); break;
            case "MODIFICAR": correcto = dao.modificarDatos(registroPendiente); break;
            default: correcto = dao.cambiarEstadoRegistro(registroPendiente.getActCod(), registroPendiente.getActEstReg());
        }
        if (correcto) { limpiarOperacion(); return "Operación exitosa."; }
        return "Error al intentar guardar en la base de datos.";
    }

    public String cancelar() { limpiarOperacion(); return "Operación cancelada."; }
    public List<Acta> obtenerListado() { return dao.listarTodos(); }
    private void limpiarOperacion() { estRegFlaAct = 0; operacionPendiente = ""; registroPendiente = null; }
}