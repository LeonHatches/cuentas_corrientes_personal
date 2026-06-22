package servicio;

import dao.PrestamoDAO;
import modelo.Prestamo;
import java.util.List;

public class PrestamoService {

    private final PrestamoDAO dao = new PrestamoDAO();
    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private Prestamo registroPendiente = null;

    public String prepararAdicionar(int traCod, int tipCod, int sec, int ctaCod, int fec, double mon, int cuo, double monCuo, double monAcuDes) {
        if (dao.existe(traCod, tipCod, sec)) return "Error: La secuencia de préstamo ya existe.";
        if (mon <= 0 || cuo <= 0) return "El monto y las cuotas deben ser mayores a 0.";

        registroPendiente = new Prestamo(traCod, tipCod, sec, ctaCod, fec, mon, cuo, monCuo, monAcuDes, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;
        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int traCod, int tipCod, int sec, int ctaCod, int fec, double mon, int cuo, double monCuo, double monAcuDes) {
        Prestamo enc = dao.buscarPorIds(traCod, tipCod, sec);
        if (enc == null) return "No existe el préstamo a modificar.";

        registroPendiente = new Prestamo(traCod, tipCod, sec, ctaCod, fec, mon, cuo, monCuo, monAcuDes, enc.getPreEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;
        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int tCod, int tpCod, int sec) { return prepararCambioEstado(tCod, tpCod, sec, "*", "ELIMINAR"); }
    public String prepararInactivar(int tCod, int tpCod, int sec) { return prepararCambioEstado(tCod, tpCod, sec, "I", "INACTIVAR"); }
    public String prepararReactivar(int tCod, int tpCod, int sec) { return prepararCambioEstado(tCod, tpCod, sec, "A", "REACTIVAR"); }

    private String prepararCambioEstado(int tCod, int tpCod, int sec, String est, String op) {
        Prestamo enc = dao.buscarPorIds(tCod, tpCod, sec);
        if (enc == null) return "No existe el préstamo seleccionado.";

        registroPendiente = new Prestamo(enc.getPreTraCod(), enc.getPreTipCod(), enc.getPreSec(), enc.getPreCtaCod(), enc.getPreFec(), enc.getPreMon(), enc.getPreCuo(), enc.getPreMonCuo(), enc.getPreMonAcuDes(), est);
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
            default: correcto = dao.cambiarEstadoRegistro(registroPendiente.getPreTraCod(), registroPendiente.getPreTipCod(), registroPendiente.getPreSec(), registroPendiente.getPreEstReg());
        }
        if (correcto) { limpiarOperacion(); return "Operación exitosa."; }
        return "Error al intentar guardar en la base de datos.";
    }

    public String cancelar() { limpiarOperacion(); return "Operación cancelada."; }
    public List<Prestamo> obtenerListado() { return dao.listarTodos(); }
    private void limpiarOperacion() { estRegFlaAct = 0; operacionPendiente = ""; registroPendiente = null; }
}