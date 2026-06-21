package servicio;

import dao.PrestamoDAO;
import dao.TrabajadorDAO;
import dao.TipoPrestamoDAO;
import dao.EstadoRegistroDAO;
import modelo.Prestamo;
import modelo.Trabajador;

import java.util.List;

public class PrestamoService {

    private final PrestamoDAO dao = new PrestamoDAO();

    // Importamos los DAO referenciales y maestros para verificar integridad
    private final TrabajadorDAO trabajadorDao = new TrabajadorDAO();
    private final TipoPrestamoDAO tipoPrestamoDao = new TipoPrestamoDAO();
    private final EstadoRegistroDAO estadoDao = new EstadoRegistroDAO();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private Prestamo registroPendiente = null;

    public String prepararAdicionar(int traCod, int tipCod, String strFecPre, double mnt, int canCuo, double mntCuo) {

        // 1. Validar Fecha (AAAAMMDD)
        if (strFecPre == null || strFecPre.length() != 8) {
            return "La fecha del préstamo debe tener formato AAAAMMDD.";
        }
        int fecPre;
        try {
            fecPre = Integer.parseInt(strFecPre);
        } catch (NumberFormatException e) {
            return "La fecha del préstamo debe ser numérica.";
        }

        // 2. Validar reglas matemáticas de negocio
        if (mnt <= 0 || canCuo <= 0 || mntCuo <= 0) {
            return "El monto, la cantidad de cuotas y el monto de la cuota deben ser mayores a 0.";
        }

        // 3. Validar existencia de llaves foráneas y Reglas de Negocio
        Trabajador trabajador = trabajadorDao.buscarPorCodigo(traCod);
        if (trabajador == null) {
            return "Error: El trabajador con código " + traCod + " no existe.";
        }

        // REGLA: No se puede dar préstamo a un trabajador que no esté Activo (ej. "C" Cesado)
        if (!"A".equals(trabajador.getTraEstCod())) {
            return "Error de Negocio: No se puede asignar un préstamo a un trabajador que no se encuentra Activo en la empresa.";
        }

        if (!tipoPrestamoDao.existe(tipCod)) {
            return "Error: El tipo de préstamo " + tipCod + " no existe.";
        }

        if (!estadoDao.existe("A")) {
            return "Error: No existe el estado 'A' en la tabla GZZ_ESTADO_REGISTRO.";
        }

        // 4. Validar que no se duplique la Llave Primaria
        if (dao.existe(traCod, tipCod, fecPre)) {
            return "Ya existe este tipo de préstamo para este trabajador en la fecha indicada.";
        }

        registroPendiente = new Prestamo(traCod, tipCod, fecPre, mnt, canCuo, mntCuo, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR lista. Presione Actualizar para grabar.";
    }

    public String prepararModificar(int traCod, int tipCod, int fecPre, double mnt, int canCuo, double mntCuo) {
        if (mnt <= 0 || canCuo <= 0 || mntCuo <= 0) {
            return "Los montos y la cantidad de cuotas deben ser mayores a cero.";
        }

        Prestamo encontrado = dao.buscarPorIds(traCod, tipCod, fecPre);
        if (encontrado == null) {
            return "No existe el préstamo a modificar.";
        }

        registroPendiente = new Prestamo(traCod, tipCod, fecPre, mnt, canCuo, mntCuo, encontrado.getPreEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR lista. Presione Actualizar para grabar.";
    }

    public String prepararEliminar(int traCod, int tipCod, int fecPre) {
        return prepararCambioEstado(traCod, tipCod, fecPre, "*", "ELIMINAR");
    }

    public String prepararInactivar(int traCod, int tipCod, int fecPre) {
        return prepararCambioEstado(traCod, tipCod, fecPre, "I", "INACTIVAR");
    }

    public String prepararReactivar(int traCod, int tipCod, int fecPre) {
        return prepararCambioEstado(traCod, tipCod, fecPre, "A", "REACTIVAR");
    }

    private String prepararCambioEstado(int traCod, int tipCod, int fecPre, String nuevoEstado, String operacion) {
        Prestamo encontrado = dao.buscarPorIds(traCod, tipCod, fecPre);
        if (encontrado == null) {
            return "No existe el préstamo seleccionado.";
        }

        if (!estadoDao.existe(nuevoEstado)) {
            return "Error: El estado '" + nuevoEstado + "' no está registrado en el sistema.";
        }

        registroPendiente = new Prestamo(
                encontrado.getPreTraCod(), encontrado.getPreTipCod(), encontrado.getPreFecPre(),
                encontrado.getPreMnt(), encontrado.getPreCanCuo(), encontrado.getPreMntCuo(), nuevoEstado
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
                    registroPendiente.getPreTraCod(),
                    registroPendiente.getPreTipCod(),
                    registroPendiente.getPreFecPre(),
                    registroPendiente.getPreEstReg()
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

    public List<Prestamo> obtenerListado() {
        return dao.listarTodos();
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }
}