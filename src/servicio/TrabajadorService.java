package servicio;

import dao.TrabajadorDAO;
import modelo.Trabajador;
import java.util.List;

public class TrabajadorService {
    private final TrabajadorDAO dao = new TrabajadorDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService();
    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private Trabajador registroPendiente = null;

    public String prepararAdicionar(String strCod, String nom, String strFecIng, String strFecCes, String strFecVac, int empCod, String estCod, String cenCos, int tipCod) {
        if (strCod.isEmpty() || strCod.length() > 6) return "Código inválido. Máximo 6 dígitos.";
        int codigo = Integer.parseInt(strCod);

        if (nom.isEmpty() || nom.length() > 80) return "Nombre inválido (Máx 80 caract).";

        if (strFecIng.length() != 8) return "Fecha de ingreso debe tener formato AAAAMMDD.";
        int fecIng = Integer.parseInt(strFecIng);

        // EXTRAEMOS LA LETRA DEL ESTADO (A o C)
        String estadoTrabajador = estCod.substring(0, 1);
        Integer fecCes = null;

        // REGLA DE NEGOCIO PARA LA FECHA DE CESE
        if (estadoTrabajador.equals("C")) {
            fecCes = parsearFechaOpcional(strFecCes);
            if (fecCes == null) return "Error: Debe ingresar una Fecha de Cese para un trabajador Cesado.";
        }

        Integer fecVac = parsearFechaOpcional(strFecVac);

        if (dao.buscarPorCodigo(codigo) != null) return "Ya existe un trabajador con ese código.";

        registroPendiente = new Trabajador(codigo, nom.trim(), fecIng, fecCes, fecVac, empCod, estadoTrabajador, cenCos, tipCod, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;
        return "Operación ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String strCod, String nom, String strFecIng, String strFecCes, String strFecVac, int empCod, String estCod, String cenCos, int tipCod) {
        int codigo = Integer.parseInt(strCod);
        Trabajador t = dao.buscarPorCodigo(codigo);
        if (t == null) return "El trabajador no existe en la base de datos.";

        int fecIng = Integer.parseInt(strFecIng);

        String estadoTrabajador = estCod.substring(0, 1);
        Integer fecCes = null;

        // REGLA DE NEGOCIO PARA LA FECHA DE CESE
        if (estadoTrabajador.equals("C")) {
            fecCes = parsearFechaOpcional(strFecCes);
            if (fecCes == null) return "Error: Debe ingresar una Fecha de Cese para un trabajador Cesado.";
        }

        Integer fecVac = parsearFechaOpcional(strFecVac);

        registroPendiente = new Trabajador(codigo, nom.trim(), fecIng, fecCes, fecVac, empCod, estadoTrabajador, cenCos, tipCod, t.getTraEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;
        return "Operación MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararInactivar(String strCodigo) { return cambiarEstado(strCodigo, "I", "INACTIVAR"); }
    public String prepararReactivar(String strCodigo) { return cambiarEstado(strCodigo, "A", "REACTIVAR"); }
    public String prepararEliminar(String strCodigo) { return cambiarEstado(strCodigo, "*", "ELIMINAR"); }

    private String cambiarEstado(String strCodigo, String nuevoEstado, String operacion) {
        Trabajador t = dao.buscarPorCodigo(Integer.parseInt(strCodigo));
        if (t == null) return "No existe el código ingresado.";

        registroPendiente = new Trabajador(t.getTraCod(), t.getTraNom(), t.getTraFecIng(), t.getTraFecCes(), t.getTraFecUltSalVac(), t.getTraEmpCod(), t.getTraEstCod(), t.getTraCenCosCod(), t.getTraTipCod(), nuevoEstado);
        operacionPendiente = operacion;
        estRegFlaAct = 1;
        return "Operación " + operacion + " preparada. Presione Actualizar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null) return "No hay operación pendiente.";
        boolean correcto = false;
        switch (operacionPendiente) {
            case "ADICIONAR": correcto = dao.insertar(registroPendiente); break;
            case "MODIFICAR": correcto = dao.modificarDatos(registroPendiente); break;
            case "ELIMINAR": case "INACTIVAR": case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(registroPendiente.getTraCod(), registroPendiente.getTraEstReg()); break;
        }
        if (correcto) { estRegFlaAct = 0; return "Operación exitosa."; }
        return "Error al ejecutar la operación en la BD.";
    }

    public String cancelar() { estRegFlaAct = 0; operacionPendiente = ""; return "Operación cancelada."; }

    public List<Trabajador> obtenerListado() { return dao.listarTodos(); }

    private Integer parsearFechaOpcional(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) return null;
        if (fechaStr.trim().length() != 8) throw new IllegalArgumentException("Formato de fecha inválido.");
        return Integer.parseInt(fechaStr.trim());
    }
}