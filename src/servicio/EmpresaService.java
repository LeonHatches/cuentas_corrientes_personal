package servicio;

import dao.EmpresaDAO;
import modelo.Empresa;

import java.util.List;

public class EmpresaService {

    private final EmpresaDAO dao = new EmpresaDAO();
    private final EstadoRegistroService estadoService = new EstadoRegistroService();

    private int estRegFlaAct = 0;
    private String operacionPendiente = "";
    private Empresa registroPendiente = null;

    public String prepararAdicionar(String strCodigo, String razonSocial, String ruc) {
        razonSocial = normalizarTexto(razonSocial);
        ruc = normalizarTexto(ruc);

        if (!esNumeroValido(strCodigo)) {
            return "Código no válido. Debe ser un número (Ej: 1, 2, 10).";
        }
        int codigo = Integer.parseInt(strCodigo);

        if (!razonSocialValida(razonSocial)) {
            return "Razón Social no válida. Debe tener entre 1 y 80 caracteres.";
        }
        if (!rucValido(ruc)) {
            return "RUC no válido. Debe tener exactamente 11 dígitos numéricos.";
        }
        if (dao.existe(codigo)) {
            return "Ya existe una empresa con el código " + codigo + ".";
        }
        if (estadoService.buscarPorCodigo("A") == null) {
            return "Error: No existe el estado 'A' en GZZ_ESTADO_REGISTRO.";
        }

        registroPendiente = new Empresa(codigo, razonSocial, ruc, "A");
        operacionPendiente = "ADICIONAR";
        estRegFlaAct = 1;

        return "Operación ADICIONAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararModificar(String strCodigo, String nuevaRazonSocial, String nuevoRuc) {
        nuevaRazonSocial = normalizarTexto(nuevaRazonSocial);
        nuevoRuc = normalizarTexto(nuevoRuc);

        if (!esNumeroValido(strCodigo)) return "Código no válido. Debe ser numérico.";
        int codigo = Integer.parseInt(strCodigo);

        if (!razonSocialValida(nuevaRazonSocial)) return "Razón Social no válida.";
        if (!rucValido(nuevoRuc)) return "RUC no válido. Debe tener 11 dígitos.";

        Empresa encontrada = dao.buscarPorCodigo(codigo);
        if (encontrada == null) return "No existe el código " + codigo + ".";

        registroPendiente = new Empresa(encontrada.getEmpCod(), nuevaRazonSocial, nuevoRuc, encontrada.getEmpEstReg());
        operacionPendiente = "MODIFICAR";
        estRegFlaAct = 1;

        return "Operación MODIFICAR preparada. Presione Actualizar para grabar.";
    }

    public String prepararInactivar(String strCodigo) {
        return cambiarEstadoPreparar(strCodigo, "I", "INACTIVAR");
    }

    public String prepararReactivar(String strCodigo) {
        return cambiarEstadoPreparar(strCodigo, "A", "REACTIVAR");
    }

    public String prepararEliminar(String strCodigo) {
        return cambiarEstadoPreparar(strCodigo, "*", "ELIMINAR");
    }

    private String cambiarEstadoPreparar(String strCodigo, String nuevoEstado, String nombreOperacion) {
        if (!esNumeroValido(strCodigo)) return "Código no válido. Debe ser numérico.";
        int codigo = Integer.parseInt(strCodigo);

        Empresa encontrada = dao.buscarPorCodigo(codigo);
        if (encontrada == null) return "No existe el código " + codigo + ".";
        if (estadoService.buscarPorCodigo(nuevoEstado) == null) return "Error: No existe el estado '" + nuevoEstado + "'.";

        registroPendiente = new Empresa(encontrada.getEmpCod(), encontrada.getEmpRazSoc(), encontrada.getEmpRuc(), nuevoEstado);
        operacionPendiente = nombreOperacion;
        estRegFlaAct = 1;

        return "Operación " + nombreOperacion + " preparada. Presione Actualizar.";
    }

    public String actualizar() {
        if (estRegFlaAct == 0 || registroPendiente == null || operacionPendiente.isEmpty()) {
            return "No se ha seleccionado un comando para actualizar.";
        }

        boolean correcto = false;
        switch (operacionPendiente) {
            case "ADICIONAR":
                correcto = dao.insertar(registroPendiente);
                break;
            case "MODIFICAR":
                correcto = dao.modificarDatos(registroPendiente.getEmpCod(), registroPendiente.getEmpRazSoc(), registroPendiente.getEmpRuc());
                break;
            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                correcto = dao.cambiarEstadoRegistro(registroPendiente.getEmpCod(), registroPendiente.getEmpEstReg());
                break;
        }

        if (correcto) {
            String mensaje = "Operación " + operacionPendiente + " realizada correctamente.";
            limpiarOperacion();
            return mensaje;
        } else {
            return "No se pudo realizar la operación. Verifique la base de datos.";
        }
    }

    public String cancelar() {
        limpiarOperacion();
        return "Operación cancelada.";
    }

    public List<Empresa> obtenerListado() {
        return dao.listarTodos();
    }

    private void limpiarOperacion() {
        estRegFlaAct = 0;
        operacionPendiente = "";
        registroPendiente = null;
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private boolean esNumeroValido(String strNumero) {
        if (strNumero == null || strNumero.trim().isEmpty()) return false;
        try {
            int num = Integer.parseInt(strNumero.trim());
            return num >= 0 && num <= 99; // Tinyint(2) limit
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean razonSocialValida(String nombre) {
        return !nombre.isEmpty() && nombre.length() <= 80;
    }

    private boolean rucValido(String ruc) {
        return ruc != null && ruc.matches("\\d{11}");
    }
}