package servicio;

import dao.CuentaCorrienteDAO;
import modelo.CuentaCorriente;
import java.util.List;

public class CuentaCorrienteService {

    private final CuentaCorrienteDAO dao = new CuentaCorrienteDAO();
    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private CuentaCorriente registroPendiente = null;

    public String prepararAdicionar(int ctaCod, int traCod, int tipCtaCod, int fecApe, double salIni, double salAct) {
        if (ctaCod <= 0) return "El código de cuenta corriente es inválido.";
        if (dao.existe(ctaCod)) return "Error: El número de cuenta corriente ya existe en el sistema.";

        registroPendiente = new CuentaCorriente(ctaCod, traCod, tipCtaCod, fecApe, salIni, salAct, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;
        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int ctaCod, int traCod, int tipCtaCod, int fecApe, double salIni, double salAct) {
        CuentaCorriente enc = dao.buscarPorCodigo(ctaCod);
        if (enc == null) return "No existe la cuenta corriente a modificar.";

        registroPendiente = new CuentaCorriente(ctaCod, traCod, tipCtaCod, fecApe, salIni, salAct, enc.getCtaEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;
        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int ctaCod) { return prepararCambioEstado(ctaCod, "*", "ELIMINAR"); }
    public String prepararInactivar(int ctaCod) { return prepararCambioEstado(ctaCod, "I", "INACTIVAR"); }
    public String prepararReactivar(int ctaCod) { return prepararCambioEstado(ctaCod, "A", "REACTIVAR"); }

    private String prepararCambioEstado(int ctaCod, String est, String op) {
        CuentaCorriente enc = dao.buscarPorCodigo(ctaCod);
        if (enc == null) return "No existe la cuenta corriente seleccionada.";

        registroPendiente = new CuentaCorriente(enc.getCtaCod(), enc.getCtaTraCod(), enc.getCtaTipCtaCod(), enc.getCtaFecApe(), enc.getCtaSalIni(), enc.getCtaSalAct(), est);
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
            default: correcto = dao.cambiarEstadoRegistro(registroPendiente.getCtaCod(), registroPendiente.getCtaEstReg());
        }
        if (correcto) { limpiarOperacion(); return "Operación exitosa."; }
        return "Error al intentar guardar en la base de datos.";
    }

    public String cancelar() { limpiarOperacion(); return "Operación cancelada."; }
    public List<CuentaCorriente> obtenerListado() { return dao.listarTodos(); }
    private void limpiarOperacion() { estRegFlaAct = 0; operacionPendiente = ""; registroPendiente = null; }
}