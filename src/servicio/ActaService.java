package servicio;

import dao.ActaDAO;
import dao.TrabajadorDAO;
import dao.CentroCostoDAO;
import dao.EstadoRegistroDAO;
import modelo.Acta;
import modelo.Trabajador;

import java.util.List;

public class ActaService {

    private final ActaDAO dao = new ActaDAO();

    // DAO referenciales y maestros
    private final TrabajadorDAO trabajadorDao = new TrabajadorDAO();
    private final CentroCostoDAO centroCostoDao = new CentroCostoDAO();
    private final EstadoRegistroDAO estadoDao = new EstadoRegistroDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private Acta registroPendiente = null;

    public String prepararAdicionar(int traCod, String cenCosCod, String strFec, String descripcion) {

        // 1. Validar Centro de Costo (Texto)
        if (cenCosCod == null || cenCosCod.trim().isEmpty() || cenCosCod.length() > 4) {
            return "El código del Centro de Costo es obligatorio y no debe exceder 4 caracteres.";
        }
        cenCosCod = cenCosCod.trim().toUpperCase();

        // 2. Validar Fecha (AAAAMMDD)
        if (strFec == null || strFec.length() != 8) {
            return "La fecha del acta debe tener formato AAAAMMDD.";
        }
        int fec;
        try {
            fec = Integer.parseInt(strFec);
        } catch (NumberFormatException e) {
            return "La fecha debe ser numérica.";
        }

        // 3. Validar Descripción
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción del acta es obligatoria.";
        }
        if (descripcion.length() > 200) {
            return "La descripción no debe exceder los 200 caracteres.";
        }

        // 4. Validar existencia de llaves foráneas (Maestras y Referenciales)
        Trabajador trabajador = trabajadorDao.buscarPorCodigo(traCod);
        if (trabajador == null) {
            return "Error: El trabajador con código " + traCod + " no existe.";
        }

        // REGLA: El trabajador debe estar activo
        if (!"A".equals(trabajador.getTraEstCod())) {
            return "Error: No se pueden emitir actas para trabajadores inactivos o cesados.";
        }

        if (!centroCostoDao.existe(cenCosCod)) {
            return "Error: El centro de costo '" + cenCosCod + "' no existe.";
        }

        if (!estadoDao.existe("A")) {
            return "Error: No existe el estado 'A' en la tabla GZZ_ESTADO_REGISTRO.";
        }

        // 5. Verificar duplicidad de Llave Primaria
        if (dao.existe(traCod, cenCosCod, fec)) {
            return "Ya existe un acta registrada para este trabajador y centro de costo en esta fecha.";
        }

        registroPendiente = new Acta(traCod, cenCosCod, fec, descripcion.trim(), "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int traCod, String cenCosCod, int fec, String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty() || descripcion.length() > 200) {
            return "La descripción es obligatoria y no debe superar los 200 caracteres.";
        }

        Acta encontrada = dao.buscarPorIds(traCod, cenCosCod, fec);
        if (encontrada == null) {
            return "No existe el acta que intenta modificar.";
        }

        registroPendiente = new Acta(traCod, cenCosCod, fec, descripcion.trim(), encontrada.getActEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int traCod, String cenCosCod, int fec) {
        return prepararCambioEstado(traCod, cenCosCod, fec, "*", "ELIMINAR");
    }

    public String prepararInactivar(int traCod, String cenCosCod, int fec) {
        return prepararCambioEstado(traCod, cenCosCod, fec, "I", "INACTIVAR");
    }

    public String prepararReactivar(int traCod, String cenCosCod, int fec) {
        return prepararCambioEstado(traCod, cenCosCod, fec, "A", "REACTIVAR");
    }

    private String prepararCambioEstado(int traCod, String cenCosCod, int fec, String nuevoEstado, String operacion) {
        Acta encontrada = dao.buscarPorIds(traCod, cenCosCod, fec);
        if (encontrada == null) {
            return "No existe el acta seleccionada.";
        }

        if (!estadoDao.existe(nuevoEstado)) {
            return "Error: El estado '" + nuevoEstado + "' no está registrado en el sistema.";
        }

        registroPendiente = new Acta(
                encontrada.getActTraCod(), encontrada.getActCenCosCod(),
                encontrada.getActFec(), encontrada.getActDes(), nuevoEstado
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
                    registroPendiente.getActTraCod(),
                    registroPendiente.getActCenCosCod(),
                    registroPendiente.getActFec(),
                    registroPendiente.getActEstReg()
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

    public List<Acta> obtenerListado() {
        return dao.listarTodos();
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }
}