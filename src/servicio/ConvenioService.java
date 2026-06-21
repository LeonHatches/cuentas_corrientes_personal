package servicio;

import dao.EmpresaDAO;
import dao.OrganizacionDAO;
import dao.TipoDescuentoDAO;
import dao.ConvenioDAO;
import modelo.Convenio;

import java.util.List;

public class ConvenioService {
    private final ConvenioDAO dao = new ConvenioDAO();
    private final EmpresaDAO empresaDao = new EmpresaDAO();
    private final OrganizacionDAO organizacionDao = new OrganizacionDAO();
    private final TipoDescuentoDAO tipoDescuentoDao = new TipoDescuentoDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService();

    private String operacionPendiente = "";
    private Convenio registroPendiente = null;

    public String prepararAdicionar(String emp, String org, String tipDes, String sec, String des, String actRef, String actFec, String con) {
        Convenio convenio = construirConvenio(emp, org, tipDes, sec, des, actRef, actFec, con, "A");
        if (convenio == null) return "Datos no validos. Revise codigos, descripcion, acto y contenido.";
        if (dao.existe(convenio.getConEmpCod(), convenio.getConOrgCod(), convenio.getConTipDesCod(), convenio.getConSec())) {
            return "Ya existe un convenio con esa clave.";
        }
        if (estadoService.buscarPorCodigo("A") == null) return "Error: No existe el estado 'A'.";

        registroPendiente = convenio;
        operacionPendiente = "ADICIONAR";
        return "Operacion ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String emp, String org, String tipDes, String sec, String des, String actRef, String actFec, String con) {
        Convenio convenio = construirConvenio(emp, org, tipDes, sec, des, actRef, actFec, con, "A");
        if (convenio == null) return "Datos no validos. Revise codigos, descripcion, acto y contenido.";

        Convenio encontrada = dao.buscarPorCodigo(convenio.getConEmpCod(), convenio.getConOrgCod(), convenio.getConTipDesCod(), convenio.getConSec());
        if (encontrada == null) return "No existe el convenio indicado.";

        convenio.setConEstReg(encontrada.getConEstReg());
        registroPendiente = convenio;
        operacionPendiente = "MODIFICAR";
        return "Operacion MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(String emp, String org, String tipDes, String sec) { return cambiarEstado(emp, org, tipDes, sec, "*", "ELIMINAR"); }
    public String prepararInactivar(String emp, String org, String tipDes, String sec) { return cambiarEstado(emp, org, tipDes, sec, "I", "INACTIVAR"); }
    public String prepararReactivar(String emp, String org, String tipDes, String sec) { return cambiarEstado(emp, org, tipDes, sec, "A", "REACTIVAR"); }

    private String cambiarEstado(String emp, String org, String tipDes, String sec, String nuevoEstado, String operacion) {
        Integer empCod = parseEntero(emp, 0, 99);
        Integer orgCod = parseEntero(org, 0, 9999);
        Integer conSec = parseEntero(sec, 0, 99);
        tipDes = normalizar(tipDes);
        if (empCod == null || orgCod == null || conSec == null || tipDes.length() != 1) return "Clave no valida.";

        Convenio encontrada = dao.buscarPorCodigo(empCod, orgCod, tipDes, conSec);
        if (encontrada == null) return "No existe el convenio indicado.";
        if (estadoService.buscarPorCodigo(nuevoEstado) == null) return "Error: No existe el estado '" + nuevoEstado + "'.";

        encontrada.setConEstReg(nuevoEstado);
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
                correcto = dao.cambiarEstadoRegistro(registroPendiente.getConEmpCod(), registroPendiente.getConOrgCod(),
                        registroPendiente.getConTipDesCod(), registroPendiente.getConSec(), registroPendiente.getConEstReg());
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

    public List<Convenio> obtenerListado() {
        return dao.listarTodos();
    }

    private Convenio construirConvenio(String emp, String org, String tipDes, String sec, String des, String actRef, String actFec, String con, String estado) {
        Integer empCod = parseEntero(emp, 0, 99);
        Integer orgCod = parseEntero(org, 0, 9999);
        Integer conSec = parseEntero(sec, 0, 99);
        Integer fecha = parseFecha(actFec);
        tipDes = normalizar(tipDes);
        des = normalizar(des);
        actRef = normalizar(actRef);
        con = normalizar(con);

        if (empCod == null || orgCod == null || conSec == null || fecha == null) return null;
        if (tipDes.length() != 1 || des.isEmpty() || des.length() > 120) return null;
        if (actRef.isEmpty() || actRef.length() > 30 || con.isEmpty() || con.length() > 200) return null;
        if (empresaDao.buscarPorCodigo(empCod) == null) return null;
        if (organizacionDao.buscarPorCodigo(orgCod) == null) return null;
        if (tipoDescuentoDao.buscarPorCodigo(tipDes) == null) return null;

        return new Convenio(empCod, orgCod, tipDes, conSec, des, actRef, fecha, con, estado);
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
}
