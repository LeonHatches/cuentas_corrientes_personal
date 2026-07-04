package servicio;

import dao.CuentaCorrienteDAO;
import dao.TrabajadorDAO;
import dao.TipoCuentaCorrienteDAO;
import dao.EstadoRegistroDAO;
import modelo.CuentaCorriente;
import modelo.Trabajador;

import java.util.List;

public class CuentaCorrienteService {

    private final CuentaCorrienteDAO dao = new CuentaCorrienteDAO();

    // DAO para validaciones referenciales y maestras
    private final TrabajadorDAO trabajadorDao = new TrabajadorDAO();
    private final TipoCuentaCorrienteDAO tipoCtaDao = new TipoCuentaCorrienteDAO();
    private final EstadoRegistroDAO estadoDao = new EstadoRegistroDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private CuentaCorriente registroPendiente = null;

    public String prepararAdicionar(int traCod, int ctaCod, int sec, String strFec, String docRfe, double mnt) {

        // 1. Validar Secuencia y Monto
        if (sec <= 0) {
            return "La secuencia debe ser un número mayor a 0.";
        }
        if (mnt <= 0) {
            return "El monto debe ser mayor a 0.";
        }

        // 2. Validar Fecha (AAAAMMDD)
        if (strFec == null || strFec.length() != 8) {
            return "La fecha debe tener formato AAAAMMDD.";
        }
        int fec;
        try {
            fec = Integer.parseInt(strFec);
        } catch (NumberFormatException e) {
            return "La fecha debe ser estrictamente numérica.";
        }

        // 3. Validar Documento de Referencia
        if (docRfe == null || docRfe.trim().isEmpty() || docRfe.length() > 15) {
            return "El documento de referencia es obligatorio y no debe exceder 15 caracteres.";
        }

        // 4. Validar integridad de Llaves Foráneas (Reglas de Negocio)
        Trabajador trabajador = trabajadorDao.buscarPorCodigo(traCod);
        if (trabajador == null) {
            return "Error: El trabajador con código " + traCod + " no existe.";
        }

        // REGLA: El trabajador debe estar activo
        if (!"A".equals(trabajador.getTraEstCod())) {
            return "Error: No se pueden registrar movimientos a un trabajador inactivo o cesado.";
        }

        if (!tipoCtaDao.existe(ctaCod)) {
            return "Error: El tipo de cuenta corriente (" + ctaCod + ") no está registrado.";
        }

        if (!estadoDao.existe("A")) {
            return "Error: No existe el estado 'A' en la tabla GZZ_ESTADO_REGISTRO.";
        }

        // 5. Verificar duplicidad de Llave Primaria
        if (dao.existe(traCod, ctaCod, sec)) {
            return "Ya existe un registro con esa misma secuencia para este trabajador y cuenta.";
        }

        registroPendiente = new CuentaCorriente(traCod, ctaCod, sec, fec, docRfe.trim(), mnt, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int traCod, int ctaCod, int sec, int fec, String docRfe, double mnt) {
        if (mnt <= 0) {
            return "El monto debe ser mayor a cero.";
        }
        if (docRfe == null || docRfe.trim().isEmpty() || docRfe.length() > 15) {
            return "El documento de referencia es obligatorio y máximo de 15 caracteres.";
        }

        CuentaCorriente encontrado = dao.buscarPorIds(traCod, ctaCod, sec);
        if (encontrado == null) {
            return "No existe el registro de cuenta corriente a modificar.";
        }

        registroPendiente = new CuentaCorriente(traCod, ctaCod, sec, fec, docRfe.trim(), mnt, encontrado.getCtaCorEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int traCod, int ctaCod, int sec) {
        return prepararCambioEstado(traCod, ctaCod, sec, "*", "ELIMINAR");
    }

    public String prepararInactivar(int traCod, int ctaCod, int sec) {
        return prepararCambioEstado(traCod, ctaCod, sec, "I", "INACTIVAR");
    }

    public String prepararReactivar(int traCod, int ctaCod, int sec) {
        return prepararCambioEstado(traCod, ctaCod, sec, "A", "REACTIVAR");
    }

    private String prepararCambioEstado(int traCod, int ctaCod, int sec, String nuevoEstado, String operacion) {
        CuentaCorriente encontrado = dao.buscarPorIds(traCod, ctaCod, sec);
        if (encontrado == null) {
            return "No existe el registro seleccionado.";
        }

        if (!estadoDao.existe(nuevoEstado)) {
            return "Error: El estado '" + nuevoEstado + "' no está registrado en el sistema.";
        }

        registroPendiente = new CuentaCorriente(
                encontrado.getCtaCorTraCod(), encontrado.getCtaCorCtaCod(), encontrado.getCtaCorSec(),
                encontrado.getCtaCorFec(), encontrado.getCtaCorDocRfe(), encontrado.getCtaCorMnt(), nuevoEstado
        );

        operacionPendiente = operacion;
        estRegFlaAct = 1;

        return "Operación " + operacion + " lista. Presione Actualizar para grabar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null || operacionPendiente.isEmpty()) {
            return "No hay operación pendiente.";
        }

        boolean correcto = switch (operacionPendiente) {
            case "ADICIONAR" -> dao.insertar(registroPendiente);
            case "MODIFICAR" -> dao.modificarDatos(registroPendiente);
            case "ELIMINAR", "INACTIVAR", "REACTIVAR" -> dao.cambiarEstadoRegistro(
                    registroPendiente.getCtaCorTraCod(),
                    registroPendiente.getCtaCorCtaCod(),
                    registroPendiente.getCtaCorSec(),
                    registroPendiente.getCtaCorEstReg()
            );
            default -> false;
        };

        if (correcto) {
            String mensaje = "Operación " + operacionPendiente + " exitosa.";
            limpiarOperacion();
            return mensaje;
        } else {
            return "Error al intentar guardar en la base de datos.";
        }
    }

    public String cancelar() {
        limpiarOperacion();
        return "Operación cancelada.";
    }

    public List<CuentaCorriente> obtenerListado() {
        return dao.listarTodos();
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }
}