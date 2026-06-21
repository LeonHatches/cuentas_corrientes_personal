package servicio;

import dao.ConvenioDAO;
import dao.DescuentoDAO;
import modelo.Descuento;

import java.math.BigDecimal;
import java.util.List;

public class DescuentoService {
    private final DescuentoDAO dao = new DescuentoDAO();
    private final ConvenioDAO convenioDao = new ConvenioDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService();

    private String operacionPendiente = "";
    private Descuento registroPendiente = null;

    public String prepararAdicionar(String emp, String org, String tipDes, String conSec, String desSec,
                                    String ctaCod, String fec, String monTot, String numCuo,
                                    String cuoDes, String monCuo, String monCuoAcu) {
        Descuento descuento = construirDescuento(emp, org, tipDes, conSec, desSec, ctaCod, fec, monTot, numCuo, cuoDes, monCuo, monCuoAcu, "A");
        if (descuento == null) return "Datos no validos. Revise clave, fecha, cuenta, cuotas y montos.";
        if (dao.existe(descuento.getDesConEmpCod(), descuento.getDesConOrgCod(), descuento.getDesConTipDesCod(),
                descuento.getDesConSec(), descuento.getDesSec())) return "Ya existe un descuento con esa clave.";
        if (estadoService.buscarPorCodigo("A") == null) return "Error: No existe el estado 'A'.";

        registroPendiente = descuento;
        operacionPendiente = "ADICIONAR";
        return "Operacion ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String emp, String org, String tipDes, String conSec, String desSec,
                                    String ctaCod, String fec, String monTot, String numCuo,
                                    String cuoDes, String monCuo, String monCuoAcu) {
        Descuento descuento = construirDescuento(emp, org, tipDes, conSec, desSec, ctaCod, fec, monTot, numCuo, cuoDes, monCuo, monCuoAcu, "A");
        if (descuento == null) return "Datos no validos. Revise clave, fecha, cuenta, cuotas y montos.";

        Descuento encontrada = dao.buscarPorCodigo(descuento.getDesConEmpCod(), descuento.getDesConOrgCod(),
                descuento.getDesConTipDesCod(), descuento.getDesConSec(), descuento.getDesSec());
        if (encontrada == null) return "No existe el descuento indicado.";

        descuento.setDesEstReg(encontrada.getDesEstReg());
        registroPendiente = descuento;
        operacionPendiente = "MODIFICAR";
        return "Operacion MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(String emp, String org, String tipDes, String conSec, String desSec) { return cambiarEstado(emp, org, tipDes, conSec, desSec, "*", "ELIMINAR"); }
    public String prepararInactivar(String emp, String org, String tipDes, String conSec, String desSec) { return cambiarEstado(emp, org, tipDes, conSec, desSec, "I", "INACTIVAR"); }
    public String prepararReactivar(String emp, String org, String tipDes, String conSec, String desSec) { return cambiarEstado(emp, org, tipDes, conSec, desSec, "A", "REACTIVAR"); }

    private String cambiarEstado(String emp, String org, String tipDes, String conSec, String desSec, String nuevoEstado, String operacion) {
        ClaveDescuento clave = construirClave(emp, org, tipDes, conSec, desSec);
        if (clave == null) return "Clave no valida.";

        Descuento encontrada = dao.buscarPorCodigo(clave.empCod, clave.orgCod, clave.tipDesCod, clave.conSec, clave.desSec);
        if (encontrada == null) return "No existe el descuento indicado.";
        if (estadoService.buscarPorCodigo(nuevoEstado) == null) return "Error: No existe el estado '" + nuevoEstado + "'.";

        encontrada.setDesEstReg(nuevoEstado);
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
                correcto = dao.cambiarEstadoRegistro(registroPendiente.getDesConEmpCod(), registroPendiente.getDesConOrgCod(),
                        registroPendiente.getDesConTipDesCod(), registroPendiente.getDesConSec(), registroPendiente.getDesSec(),
                        registroPendiente.getDesEstReg());
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

    public List<Descuento> obtenerListado() {
        return dao.listarTodos();
    }

    private Descuento construirDescuento(String emp, String org, String tipDes, String conSec, String desSec,
                                         String ctaCod, String fec, String monTot, String numCuo,
                                         String cuoDes, String monCuo, String monCuoAcu, String estado) {
        ClaveDescuento clave = construirClave(emp, org, tipDes, conSec, desSec);
        Integer cuenta = parseEntero(ctaCod, 0, 99999999);
        Integer fecha = parseFecha(fec);
        Integer numeroCuotas = parseEntero(numCuo, 0, 9);
        Integer cuotaDescontada = parseEntero(cuoDes, 0, 9);
        BigDecimal montoTotal = parseDecimal(monTot);
        BigDecimal montoCuota = parseDecimal(monCuo);
        BigDecimal montoAcumulado = parseDecimal(monCuoAcu);

        if (clave == null || cuenta == null || fecha == null || numeroCuotas == null || cuotaDescontada == null) return null;
        if (montoTotal == null || montoCuota == null || montoAcumulado == null) return null;
        if (convenioDao.buscarPorCodigo(clave.empCod, clave.orgCod, clave.tipDesCod, clave.conSec) == null) return null;

        return new Descuento(clave.empCod, clave.orgCod, clave.tipDesCod, clave.conSec, clave.desSec, cuenta, fecha,
                montoTotal, numeroCuotas, cuotaDescontada, montoCuota, montoAcumulado, estado);
    }

    private ClaveDescuento construirClave(String emp, String org, String tipDes, String conSec, String desSec) {
        Integer empCod = parseEntero(emp, 0, 99);
        Integer orgCod = parseEntero(org, 0, 9999);
        Integer convenioSec = parseEntero(conSec, 0, 99);
        Integer descuentoSec = parseEntero(desSec, 0, 99);
        tipDes = normalizar(tipDes);
        if (empCod == null || orgCod == null || convenioSec == null || descuentoSec == null || tipDes.length() != 1) return null;
        return new ClaveDescuento(empCod, orgCod, tipDes, convenioSec, descuentoSec);
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

    private Integer parseFecha(String texto) {
        String valor = normalizar(texto);
        if (!valor.matches("\\d{8}")) return null;
        return Integer.parseInt(valor);
    }

    private BigDecimal parseDecimal(String texto) {
        try {
            return new BigDecimal(normalizar(texto));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static class ClaveDescuento {
        int empCod;
        int orgCod;
        String tipDesCod;
        int conSec;
        int desSec;

        ClaveDescuento(int empCod, int orgCod, String tipDesCod, int conSec, int desSec) {
            this.empCod = empCod;
            this.orgCod = orgCod;
            this.tipDesCod = tipDesCod;
            this.conSec = conSec;
            this.desSec = desSec;
        }
    }
}
