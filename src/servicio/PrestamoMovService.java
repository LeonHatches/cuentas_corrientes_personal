package servicio;

import dao.PrestamoMovDAO;
import dao.PrestamoDAO;
import modelo.PrestamoMov;
import modelo.Prestamo;
import java.util.List;

public class PrestamoMovService {

    private final PrestamoMovDAO dao = new PrestamoMovDAO();
    private final PrestamoDAO prestamoDao = new PrestamoDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private PrestamoMov registroPendiente = null;

    public String prepararAdicionar(int traCod, int tipCod, int sec, int anio, int mes, int num, String tipMov, double mon) {
        if (mon <= 0) return "El monto de movimiento debe ser mayor a 0.";
        if (mes < 1 || mes > 12) return "El mes debe estar entre 1 y 12.";
        if (dao.existe(traCod, tipCod, sec, anio, mes, num)) return "Error: La cuota de esta planilla ya existe.";

        registroPendiente = new PrestamoMov(traCod, tipCod, sec, anio, mes, num, tipMov, mon, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;
        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int traCod, int tipCod, int sec, int anio, int mes, int num, String tipMov, double mon) {
        if (mon <= 0) return "El monto de movimiento debe ser mayor a cero.";
        PrestamoMov enc = dao.buscarPorIds(traCod, tipCod, sec, anio, mes, num);
        if (enc == null) return "No existe el movimiento a modificar.";

        registroPendiente = new PrestamoMov(traCod, tipCod, sec, anio, mes, num, tipMov, mon, enc.getPreMovEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;
        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int tCod, int tpCod, int sec, int anio, int mes, int num) { return prepararCambioEstado(tCod, tpCod, sec, anio, mes, num, "*", "ELIMINAR"); }
    public String prepararInactivar(int tCod, int tpCod, int sec, int anio, int mes, int num) { return prepararCambioEstado(tCod, tpCod, sec, anio, mes, num, "I", "INACTIVAR"); }
    public String prepararReactivar(int tCod, int tpCod, int sec, int anio, int mes, int num) { return prepararCambioEstado(tCod, tpCod, sec, anio, mes, num, "A", "REACTIVAR"); }

    private String prepararCambioEstado(int tCod, int tpCod, int sec, int anio, int mes, int num, String est, String op) {
        PrestamoMov enc = dao.buscarPorIds(tCod, tpCod, sec, anio, mes, num);
        if (enc == null) return "No existe la cuota seleccionada.";

        registroPendiente = new PrestamoMov(enc.getPreMovTraCod(), enc.getPreMovTipCod(), enc.getPreMovPreSec(), enc.getPreMovPlaAnio(), enc.getPreMovPlaMes(), enc.getPreMovPlaNum(), enc.getPreMovTipMovCod(), enc.getPreMovMonDes(), est);
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
            default: correcto = dao.cambiarEstadoRegistro(registroPendiente.getPreMovTraCod(), registroPendiente.getPreMovTipCod(), registroPendiente.getPreMovPreSec(), registroPendiente.getPreMovPlaAnio(), registroPendiente.getPreMovPlaMes(), registroPendiente.getPreMovPlaNum(), registroPendiente.getPreMovEstReg());
        }

        if (correcto) {
            sincronizarPrestamoPrincipal(registroPendiente.getPreMovTraCod(), registroPendiente.getPreMovTipCod(), registroPendiente.getPreMovPreSec());
            limpiarOperacion();
            return "Operación exitosa.";
        }
        return "Error al intentar guardar en la base de datos.";
    }

    private void sincronizarPrestamoPrincipal(int traCod, int tipCod, int sec) {
        Prestamo p = prestamoDao.buscarPorIds(traCod, tipCod, sec);
        if (p == null) return;

        double dineroAcumulado = 0.0;

        for (PrestamoMov mov : dao.listarTodos()) {
            if (mov.getPreMovTraCod() == traCod && mov.getPreMovTipCod() == tipCod && mov.getPreMovPreSec() == sec) {
                if (mov.getPreMovEstReg().equals("A")) {
                    dineroAcumulado += mov.getPreMovMonDes();
                }
            }
        }

        p.setPreMonAcuDes(dineroAcumulado);
        prestamoDao.modificarDatos(p);
    }

    public String cancelar() { limpiarOperacion(); return "Operación cancelada."; }
    public List<PrestamoMov> obtenerListado() { return dao.listarTodos(); }
    private void limpiarOperacion() { estRegFlaAct = 0; operacionPendiente = ""; registroPendiente = null; }
}