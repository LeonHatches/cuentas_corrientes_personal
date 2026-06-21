package servicio;

import dao.DescuentoDAO;
import dao.DescuentoMovDAO;
import dao.TipoMovimientoDAO;
import modelo.DescuentoMov;

import java.math.BigDecimal;
import java.util.List;

public class DescuentoMovService {
    private final DescuentoMovDAO dao = new DescuentoMovDAO();
    private final DescuentoDAO descuentoDao = new DescuentoDAO();
    private final TipoMovimientoDAO tipoMovimientoDao = new TipoMovimientoDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService();

    private String operacionPendiente = "";
    private DescuentoMov registroPendiente = null;

    public String prepararAdicionar(String emp, String org, String tipDes, String conSec, String desSec, String movSec,
                                    String anio, String mes, String plaNum, String tipMov, String monto) {
        DescuentoMov mov = construirMovimiento(emp, org, tipDes, conSec, desSec, movSec, anio, mes, plaNum, tipMov, monto, "A");
        if (mov == null) return "Datos no validos. Revise clave, periodo, tipo movimiento y monto.";
        if (dao.existe(mov.getDesMovConEmpCod(), mov.getDesMovConOrgCod(), mov.getDesMovConTipDesCod(),
                mov.getDesMovConSec(), mov.getDesMovDesSec(), mov.getDesMovSec(), mov.getDesMovPlaAnio(),
                mov.getDesMovPlaMes(), mov.getDesMovPlaNum())) return "Ya existe un movimiento con esa clave.";
        if (estadoService.buscarPorCodigo("A") == null) return "Error: No existe el estado 'A'.";

        registroPendiente = mov;
        operacionPendiente = "ADICIONAR";
        return "Operacion ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String emp, String org, String tipDes, String conSec, String desSec, String movSec,
                                    String anio, String mes, String plaNum, String tipMov, String monto) {
        DescuentoMov mov = construirMovimiento(emp, org, tipDes, conSec, desSec, movSec, anio, mes, plaNum, tipMov, monto, "A");
        if (mov == null) return "Datos no validos. Revise clave, periodo, tipo movimiento y monto.";

        DescuentoMov encontrada = buscar(mov);
        if (encontrada == null) return "No existe el movimiento indicado.";

        mov.setDesMovEstReg(encontrada.getDesMovEstReg());
        registroPendiente = mov;
        operacionPendiente = "MODIFICAR";
        return "Operacion MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(String emp, String org, String tipDes, String conSec, String desSec, String movSec, String anio, String mes, String plaNum) {
        return cambiarEstado(emp, org, tipDes, conSec, desSec, movSec, anio, mes, plaNum, "*", "ELIMINAR");
    }

    public String prepararInactivar(String emp, String org, String tipDes, String conSec, String desSec, String movSec, String anio, String mes, String plaNum) {
        return cambiarEstado(emp, org, tipDes, conSec, desSec, movSec, anio, mes, plaNum, "I", "INACTIVAR");
    }

    public String prepararReactivar(String emp, String org, String tipDes, String conSec, String desSec, String movSec, String anio, String mes, String plaNum) {
        return cambiarEstado(emp, org, tipDes, conSec, desSec, movSec, anio, mes, plaNum, "A", "REACTIVAR");
    }

    private String cambiarEstado(String emp, String org, String tipDes, String conSec, String desSec, String movSec,
                                 String anio, String mes, String plaNum, String nuevoEstado, String operacion) {
        DescuentoMov clave = construirMovimiento(emp, org, tipDes, conSec, desSec, movSec, anio, mes, plaNum, "X", "0", nuevoEstado);
        if (clave == null) return "Clave no valida.";

        DescuentoMov encontrada = buscar(clave);
        if (encontrada == null) return "No existe el movimiento indicado.";
        if (estadoService.buscarPorCodigo(nuevoEstado) == null) return "Error: No existe el estado '" + nuevoEstado + "'.";

        encontrada.setDesMovEstReg(nuevoEstado);
        registroPendiente = encontrada;
        operacionPendiente = operacion;
        return "Operacion " + operacion + " preparada. Presione Actualizar.";
    }

    public String actualizar() {
        if (registroPendiente == null || operacionPendiente.isEmpty()) return "No se ha seleccionado un comando para actualizar.";

        boolean correcto = false;
        switch (operacionPendiente) {
            case "ADICIONAR": correcto = dao.insertar(registroPendiente); break;
            case "MODIFICAR": correcto = dao.modificarDatos(registroPendiente); break;
            case "ELIMINAR": case "INACTIVAR": case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(registroPendiente, registroPendiente.getDesMovEstReg());
                break;
        }

        if (correcto) {
            String mensaje = "Operacion " + operacionPendiente + " realizada correctamente.";
            limpiarOperacion();
            return mensaje;
        }
        return "No se pudo realizar la operacion. Verifique la base de datos.";
    }

    public String cancelar() {
        limpiarOperacion();
        return "Operacion cancelada.";
    }

    public List<DescuentoMov> obtenerListado() {
        return dao.listarTodos();
    }

    private DescuentoMov construirMovimiento(String emp, String org, String tipDes, String conSec, String desSec, String movSec,
                                             String anio, String mes, String plaNum, String tipMov, String monto, String estado) {
        Integer empCod = parseEntero(emp, 0, 99);
        Integer orgCod = parseEntero(org, 0, 9999);
        Integer convenioSec = parseEntero(conSec, 0, 99);
        Integer descuentoSec = parseEntero(desSec, 0, 99);
        Integer movimientoSec = parseEntero(movSec, 0, 99);
        Integer planillaAnio = parseEntero(anio, 0, 9999);
        Integer planillaMes = parseEntero(mes, 1, 12);
        Integer planillaNum = parseEntero(plaNum, 0, 9999);
        BigDecimal montoMov = parseDecimal(monto);
        tipDes = normalizar(tipDes);
        tipMov = normalizar(tipMov);

        if (empCod == null || orgCod == null || convenioSec == null || descuentoSec == null || movimientoSec == null) return null;
        if (planillaAnio == null || planillaMes == null || planillaNum == null || montoMov == null) return null;
        if (tipDes.length() != 1 || tipMov.length() != 1) return null;
        if (descuentoDao.buscarPorCodigo(empCod, orgCod, tipDes, convenioSec, descuentoSec) == null) return null;
        if (!"X".equals(tipMov) && tipoMovimientoDao.buscarPorCodigo(tipMov) == null) return null;

        return new DescuentoMov(empCod, orgCod, tipDes, convenioSec, descuentoSec, movimientoSec,
                planillaAnio, planillaMes, planillaNum, tipMov, montoMov, estado);
    }

    private DescuentoMov buscar(DescuentoMov mov) {
        return dao.buscarPorCodigo(mov.getDesMovConEmpCod(), mov.getDesMovConOrgCod(), mov.getDesMovConTipDesCod(),
                mov.getDesMovConSec(), mov.getDesMovDesSec(), mov.getDesMovSec(), mov.getDesMovPlaAnio(),
                mov.getDesMovPlaMes(), mov.getDesMovPlaNum());
    }

    private void limpiarOperacion() {
        operacionPendiente = "";
        registroPendiente = null;
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private Integer parseEntero(String texto, int min, int max) {
        try {
            int valor = Integer.parseInt(normalizar(texto));
            return valor >= min && valor <= max ? valor : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseDecimal(String texto) {
        try {
            return new BigDecimal(normalizar(texto));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
