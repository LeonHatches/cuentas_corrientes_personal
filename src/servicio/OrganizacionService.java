package servicio;

import dao.OrganizacionDAO;
import dao.TipoOrganizacionDAO;
import modelo.Organizacion;

import java.util.List;

public class OrganizacionService {
    private final OrganizacionDAO dao = new OrganizacionDAO();
    private final TipoOrganizacionDAO tipoDao = new TipoOrganizacionDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService();

    private String operacionPendiente = "";
    private Organizacion registroPendiente = null;

    public String prepararAdicionar(String strCodigo, String nombre, String ruc, String strTipOrgCod) {
        Organizacion organizacion = construirOrganizacion(strCodigo, nombre, ruc, strTipOrgCod, "A");
        if (organizacion == null) return "Datos no validos. Revise codigo, nombre, RUC y tipo organizacion.";
        if (dao.existe(organizacion.getOrgCod())) return "Ya existe una organizacion con el codigo " + organizacion.getOrgCod() + ".";
        if (estadoService.buscarPorCodigo("A") == null) return "Error: No existe el estado 'A'.";

        registroPendiente = organizacion;
        operacionPendiente = "ADICIONAR";
        return "Operacion ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String strCodigo, String nombre, String ruc, String strTipOrgCod) {
        Organizacion organizacion = construirOrganizacion(strCodigo, nombre, ruc, strTipOrgCod, "A");
        if (organizacion == null) return "Datos no validos. Revise codigo, nombre, RUC y tipo organizacion.";

        Organizacion encontrada = dao.buscarPorCodigo(organizacion.getOrgCod());
        if (encontrada == null) return "No existe el codigo " + organizacion.getOrgCod() + ".";

        organizacion.setEstReg(encontrada.getEstReg());
        registroPendiente = organizacion;
        operacionPendiente = "MODIFICAR";
        return "Operacion MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(String strCodigo) { return cambiarEstadoPreparar(strCodigo, "*", "ELIMINAR"); }
    public String prepararInactivar(String strCodigo) { return cambiarEstadoPreparar(strCodigo, "I", "INACTIVAR"); }
    public String prepararReactivar(String strCodigo) { return cambiarEstadoPreparar(strCodigo, "A", "REACTIVAR"); }

    private String cambiarEstadoPreparar(String strCodigo, String nuevoEstado, String operacion) {
        Integer codigo = parseEntero(strCodigo, 1, 9999);
        if (codigo == null) return "Codigo no valido.";

        Organizacion encontrada = dao.buscarPorCodigo(codigo);
        if (encontrada == null) return "No existe el codigo " + codigo + ".";
        if (estadoService.buscarPorCodigo(nuevoEstado) == null) return "Error: No existe el estado '" + nuevoEstado + "'.";

        encontrada.setEstReg(nuevoEstado);
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
                correcto = dao.cambiarEstadoRegistro(registroPendiente.getOrgCod(), registroPendiente.getEstReg()); break;
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

    public List<Organizacion> obtenerListado() {
        return dao.listarTodos();
    }

    private Organizacion construirOrganizacion(String strCodigo, String nombre, String ruc, String strTipOrgCod, String estado) {
        Integer codigo = parseEntero(strCodigo, 1, 9999);
        Integer tipOrgCod = parseEntero(strTipOrgCod, 1, 99);
        nombre = normalizar(nombre);
        ruc = normalizar(ruc);

        if (codigo == null || tipOrgCod == null) return null;
        if (nombre.isEmpty() || nombre.length() > 80) return null;
        if (!ruc.matches("\\d{11}")) return null;
        if (tipoDao.buscarPorCodigo(tipOrgCod) == null) return null;

        return new Organizacion(codigo, nombre, ruc, tipOrgCod, estado);
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
}
