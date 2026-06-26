package vista;

import conexion.ConexionBD;
import modelo.Descuento;
import modelo.Empresa;
import modelo.Organizacion;
import modelo.TipoDescuento;
import servicio.EmpresaService;
import servicio.DescuentoService;
import servicio.OrganizacionService;
import servicio.TipoDescuentoService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DescuentoFrame extends MaestroFrameBase {
    private final DescuentoService service = new DescuentoService();
    private boolean actualizandoCalculados = false;

    public DescuentoFrame() {
        super("R13017 - Descuento",
                "R13017 - MANTENIMIENTO DE DESCUENTO",
                "Datos_del_Descuento",
                "Lista_de_Descuentos",
                new String[][]{
                        {"emp", "Empresa:"},
                        {"org", "Organizacion:"},
                        {"tipDes", "Tipo Descuento:"},
                        {"conSec", "Sec. Convenio:"},
                        {"desSec", "Sec. Descuento:"},
                        {"cta", "Cuenta Corriente:"},
                        {"fec", "Fecha AAAAMMDD:"},
                        {"monTot", "Monto Total:"},
                        {"numCuo", "Num. Cuotas:"},
                        {"cuoDes", "Cuotas Desc.:"},
                        {"monCuo", "Monto Cuota:"},
                        {"monAcu", "Monto Acumulado:"},
                        {"estado", "Estado Registro:"}
                },
                new String[]{"Emp.", "Org.", "Tipo Desc.", "Sec. Conv.", "Sec. Desc.", "Cuenta", "Fecha",
                        "Monto Total", "Num. Cuotas", "Cuotas Desc.", "Monto Cuota", "Monto Acum.", "Estado"});
        configurarCombosDependientes();
        configurarCamposCalculados();
        cargarTabla();
        ajustarAnchosColumnas(65, 65, 95, 95, 95, 95, 105, 110, 105, 105, 110, 120, 85);
    }

    @Override
    protected JComponent crearComponenteCampo(String clave) {
        if ("emp".equals(clave)) {
            JComboBox<String> combo = crearComboBoxCampo(clave);
            List<Empresa> empresas = new EmpresaService().obtenerListado();
            for (Empresa empresa : empresas) {
                if ("A".equals(empresa.getEmpEstReg())) {
                    combo.addItem(String.format("%02d - %s", empresa.getEmpCod(), empresa.getEmpRazSoc()));
                }
            }
            return combo;
        }

        if ("org".equals(clave)) {
            JComboBox<String> combo = crearComboBoxCampo(clave);
            List<Organizacion> organizaciones = new OrganizacionService().obtenerListado();
            for (Organizacion organizacion : organizaciones) {
                if ("A".equals(organizacion.getEstReg())) {
                    combo.addItem(String.format("%04d - %s", organizacion.getOrgCod(), organizacion.getOrgNom()));
                }
            }
            return combo;
        }

        if ("tipDes".equals(clave)) {
            JComboBox<String> combo = crearComboBoxCampo(clave);
            List<TipoDescuento> tipos = new TipoDescuentoService().obtenerListado();
            for (TipoDescuento tipo : tipos) {
                if ("A".equals(tipo.getTipDesEstReg())) {
                    combo.addItem(tipo.getTipDesCod() + " - " + tipo.getTipDesNom());
                }
            }
            return combo;
        }

        if ("conSec".equals(clave)) {
            return crearComboBoxCampo(clave);
        }

        if ("cta".equals(clave)) {
            JComboBox<String> combo = crearComboBoxCampo(clave);
            cargarCuentasCorrientes(combo);
            return combo;
        }

        return super.crearComponenteCampo(clave);
    }

    private void configurarCombosDependientes() {
        JComboBox<String> empresa = combos.get("emp");
        JComboBox<String> organizacion = combos.get("org");
        JComboBox<String> tipoDescuento = combos.get("tipDes");
        if (empresa == null || organizacion == null || tipoDescuento == null) return;

        empresa.addActionListener(e -> cargarSecuenciasConvenio());
        organizacion.addActionListener(e -> cargarSecuenciasConvenio());
        tipoDescuento.addActionListener(e -> cargarSecuenciasConvenio());
        cargarSecuenciasConvenio();
    }

    private void cargarSecuenciasConvenio() {
        JComboBox<String> combo = combos.get("conSec");
        if (combo == null) return;

        combo.removeAllItems();

        String emp = valor("emp");
        String org = valor("org");
        String tipDes = valor("tipDes");

        if (emp.isEmpty() || org.isEmpty() || tipDes.isEmpty()) {
            combo.addItem("Seleccione empresa, organizacion y tipo descuento");
            return;
        }

        String sql = "SELECT ConSec, ConDes FROM R1T_CONVENIO " +
                "WHERE ConEmpCod = ? AND ConOrgCod = ? AND ConTipDesCod = ? AND ConEstReg = 'A' " +
                "ORDER BY ConSec";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(emp));
            ps.setInt(2, Integer.parseInt(org));
            ps.setString(3, tipDes);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    combo.addItem(String.format("%02d - %s", rs.getInt("ConSec"), rs.getString("ConDes")));
                }
            }

            if (combo.getItemCount() == 0) {
                combo.addItem("No hay convenio para la seleccion");
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error al cargar secuencias de convenio: " + e.getMessage());
            combo.addItem("No se pudo cargar convenios");
        }
    }

    private void cargarCuentasCorrientes(JComboBox<String> combo) {
        String sql = "SELECT c.CtaCod, c.CtaTraCod, t.TraNom, c.CtaSalAct " +
                "FROM R1T_CUENTA_CORRIENTE c " +
                "LEFT JOIN R1M_TRABAJADOR t ON t.TraCod = c.CtaTraCod " +
                "WHERE c.CtaEstReg = 'A' ORDER BY c.CtaCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                combo.addItem(String.format("%08d - Trab. %06d - %s - Saldo %.2f",
                        rs.getInt("CtaCod"),
                        rs.getInt("CtaTraCod"),
                        rs.getString("TraNom") == null ? "Sin nombre" : rs.getString("TraNom"),
                        rs.getDouble("CtaSalAct")));
            }

            if (combo.getItemCount() == 0) {
                combo.addItem("No hay cuentas corrientes activas");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar cuentas corrientes: " + e.getMessage());
            combo.addItem("No se pudo cargar cuentas");
        }
    }

    private void configurarCamposCalculados() {
        aplicarCampoCalculado(campos.get("cuoDes"));
        aplicarCampoCalculado(campos.get("monCuo"));
        aplicarCampoCalculado(campos.get("monAcu"));

        DocumentListener recalculador = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { recalcularCamposCalculados(); }

            @Override
            public void removeUpdate(DocumentEvent e) { recalcularCamposCalculados(); }

            @Override
            public void changedUpdate(DocumentEvent e) { recalcularCamposCalculados(); }
        };

        JTextField montoTotal = campos.get("monTot");
        JTextField numeroCuotas = campos.get("numCuo");

        if (montoTotal != null) montoTotal.getDocument().addDocumentListener(recalculador);
        if (numeroCuotas != null) numeroCuotas.getDocument().addDocumentListener(recalculador);

        inicializarCamposCalculados();
    }

    private void aplicarCampoCalculado(JTextField campo) {
        if (campo == null) return;
        campo.setEditable(false);
        campo.setBackground(new Color(242, 248, 240));
    }

    private void inicializarCamposCalculados() {
        actualizandoCalculados = true;
        try {
            JTextField cuotasDescontadas = campos.get("cuoDes");
            JTextField montoAcumulado = campos.get("monAcu");
            if (cuotasDescontadas != null && cuotasDescontadas.getText().trim().isEmpty()) {
                cuotasDescontadas.setText("0");
            }
            if (montoAcumulado != null && montoAcumulado.getText().trim().isEmpty()) {
                montoAcumulado.setText("0.00");
            }
        } finally {
            actualizandoCalculados = false;
        }
        recalcularCamposCalculados();
    }

    private void recalcularCamposCalculados() {
        if (actualizandoCalculados) return;

        JTextField montoTotalCampo = campos.get("monTot");
        JTextField numeroCuotasCampo = campos.get("numCuo");
        JTextField cuotasDescontadasCampo = campos.get("cuoDes");
        JTextField montoCuotaCampo = campos.get("monCuo");
        JTextField montoAcumuladoCampo = campos.get("monAcu");
        if (montoTotalCampo == null || numeroCuotasCampo == null || montoCuotaCampo == null || montoAcumuladoCampo == null) return;

        actualizandoCalculados = true;
        try {
            BigDecimal montoTotal = new BigDecimal(montoTotalCampo.getText().trim());
            int numeroCuotas = Integer.parseInt(numeroCuotasCampo.getText().trim());
            int cuotasDescontadas = cuotasDescontadasCampo == null || cuotasDescontadasCampo.getText().trim().isEmpty()
                    ? 0 : Integer.parseInt(cuotasDescontadasCampo.getText().trim());
            BigDecimal montoAcumulado = new BigDecimal(montoAcumuladoCampo.getText().trim());

            int cuotasPendientes = numeroCuotas - cuotasDescontadas;
            BigDecimal montoPendiente = montoTotal.subtract(montoAcumulado);

            if (numeroCuotas <= 0 || cuotasPendientes <= 0 || montoPendiente.compareTo(BigDecimal.ZERO) <= 0) {
                montoCuotaCampo.setText("");
                return;
            }

            BigDecimal montoCuota = montoPendiente.divide(BigDecimal.valueOf(cuotasPendientes), 2, RoundingMode.HALF_UP);
            montoCuotaCampo.setText(montoCuota.toPlainString());
        } catch (NumberFormatException e) {
            montoCuotaCampo.setText("");
        } finally {
            actualizandoCalculados = false;
        }
    }

    @Override
    protected void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Descuento des : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{
                    des.getDesConEmpCod(),
                    des.getDesConOrgCod(),
                    des.getDesConTipDesCod(),
                    des.getDesConSec(),
                    des.getDesSec(),
                    des.getDesCtaCod(),
                    des.getDesFec(),
                    des.getDesMonTot(),
                    des.getDesNumCuo(),
                    des.getDesCuoDes(),
                    des.getDesMonCuo(),
                    des.getDesMonCuoAcu(),
                    des.getDesEstReg()
            });
        }
    }

    @Override
    protected void cargarDatosSeleccionados() {
        cargarFilaEnCampos("emp", "org", "tipDes", "conSec", "desSec", "cta", "fec", "monTot",
                "numCuo", "cuoDes", "monCuo", "monAcu", "estado");
        recalcularCamposCalculados();
    }

    @Override
    protected String prepararAdicionar() {
        return service.prepararAdicionar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("cta"), valor("fec"), valor("monTot"), valor("numCuo"),
                valor("cuoDes"), valor("monCuo"), valor("monAcu"));
    }

    @Override
    protected String prepararModificar() {
        return service.prepararModificar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("cta"), valor("fec"), valor("monTot"), valor("numCuo"),
                valor("cuoDes"), valor("monCuo"), valor("monAcu"));
    }

    @Override
    protected String prepararEliminar() {
        return service.prepararEliminar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"), valor("desSec"));
    }

    @Override
    protected String prepararInactivar() {
        return service.prepararInactivar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"), valor("desSec"));
    }

    @Override
    protected String prepararReactivar() {
        return service.prepararReactivar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"), valor("desSec"));
    }

    @Override
    protected String actualizarOperacion() {
        return service.actualizar();
    }

    @Override
    protected String cancelarOperacion() {
        return service.cancelar();
    }

    @Override
    protected void limpiarCampos() {
        super.limpiarCampos();
        inicializarCamposCalculados();
    }
}
