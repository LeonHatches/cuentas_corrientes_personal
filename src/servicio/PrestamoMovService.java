package servicio;

import dao.PrestamoMovDAO;
import dao.PrestamoDAO;
import dao.EstadoRegistroDAO;
import modelo.PrestamoMov;

import java.util.List;

public class PrestamoMovService {

    private final PrestamoMovDAO dao = new PrestamoMovDAO();

    // DAO para validar integridad
    private final PrestamoDAO prestamoDao = new PrestamoDAO();
    private final EstadoRegistroDAO estadoDao = new EstadoRegistroDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private PrestamoMov registroPendiente = null;

    public String prepararAdicionar(int traCod, int tipCod, int fecPre, int numCuo, String strFecPag, double mntCuo) {

        // 1. Validar Fecha de Pago (AAAAMMDD)
        if (strFecPag == null || strFecPag.length() != 8) {
            return "La fecha de pago debe tener formato AAAAMMDD.";
        }
        int fecPag;
        try {
            fecPag = Integer.parseInt(strFecPag);
        } catch (NumberFormatException e) {
            return "La fecha de pago debe ser numérica.";
        }

        // REGLA DE NEGOCIO: La fecha de pago no puede ser menor a la fecha del préstamo
        if (fecPag < fecPre) {
            return "Error: La fecha de pago no puede ser anterior a la fecha del préstamo.";
        }

        // 2. Validar números
        if (numCuo <= 0) {
            return "El número de cuota debe ser mayor a 0.";
        }
        if (mntCuo <= 0) {
            return "El monto pagado debe ser mayor a 0.";
        }

        // 3. Validar existencia del Préstamo (Cabecera) y Estado
        if (!prestamoDao.existe(traCod, tipCod, fecPre)) {
            return "Error: El préstamo principal al que intenta asociar la cuota no existe.";
        }
        if (!estadoDao.existe("A")) {
            return "Error: No existe el estado 'A' en la tabla GZZ_ESTADO_REGISTRO.";
        }

        // 4. Validar que la cuota no haya sido registrada antes
        if (dao.existe(traCod, tipCod, fecPre, numCuo)) {
            return "Ya existe registrada la cuota número " + numCuo + " para este préstamo.";
        }

        registroPendiente = new PrestamoMov(traCod, tipCod, fecPre, numCuo, fecPag, mntCuo, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int traCod, int tipCod, int fecPre, int numCuo, int fecPag, double mntCuo) {
        if (mntCuo <= 0) {
            return "El monto de la cuota debe ser mayor a cero.";
        }
        if (fecPag < fecPre) {
            return "Error: La fecha de pago no puede ser anterior a la fecha del préstamo.";
        }

        PrestamoMov encontrado = dao.buscarPorIds(traCod, tipCod, fecPre, numCuo);
        if (encontrado == null) {
            return "No existe el movimiento de préstamo a modificar.";
        }

        registroPendiente = new PrestamoMov(traCod, tipCod, fecPre, numCuo, fecPag, mntCuo, encontrado.getPreMovEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int traCod, int tipCod, int fecPre, int numCuo) {
        return prepararCambioEstado(traCod, tipCod, fecPre, numCuo, "*", "ELIMINAR");
    }

    public String prepararInactivar(int traCod, int tipCod, int fecPre, int numCuo) {
        return prepararCambioEstado(traCod, tipCod, fecPre, numCuo, "I", "INACTIVAR");
    }

    public String prepararReactivar(int traCod, int tipCod, int fecPre, int numCuo) {
        return prepararCambioEstado(traCod, tipCod, fecPre, numCuo, "A", "REACTIVAR");
    }

    private String prepararCambioEstado(int traCod, int tipCod, int fecPre, int numCuo, String nuevoEstado, String operacion) {
        PrestamoMov encontrado = dao.buscarPorIds(traCod, tipCod, fecPre, numCuo);
        if (encontrado == null) {
            return "No existe la cuota seleccionada.";
        }

        if (!estadoDao.existe(nuevoEstado)) {
            return "Error: El estado '" + nuevoEstado + "' no está registrado en el sistema.";
        }

        registroPendiente = new PrestamoMov(
                encontrado.getPreMovTraCod(), encontrado.getPreMovTipCod(),
                encontrado.getPreMovFecPre(), encontrado.getPreMovNumCuo(),
                encontrado.getPreMovFecPag(), encontrado.getPreMovMntCuo(), nuevoEstado
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
                    registroPendiente.getPreMovTraCod(),
                    registroPendiente.getPreMovTipCod(),
                    registroPendiente.getPreMovFecPre(),
                    registroPendiente.getPreMovNumCuo(),
                    registroPendiente.getPreMovEstReg()
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

    public List<PrestamoMov> obtenerListado() {
        return dao.listarTodos();
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }
}