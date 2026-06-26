package vista;

import conexion.ConexionBD;
import modelo.DescuentoMov;
import modelo.Empresa;
import modelo.Organizacion;
import modelo.TipoDescuento;
import modelo.TipoMovimiento;
import servicio.DescuentoMovService;
import servicio.EmpresaService;
import servicio.OrganizacionService;
import servicio.TipoDescuentoService;
import servicio.TipoMovimientoService;

import javax.swing.*;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DescuentoMovFrame extends MaestroFrameBase {
    private final DescuentoMovService service = new DescuentoMovService();

    public DescuentoMovFrame() {
        super("R13018 - Descuento Movimiento",
                "R13018 - MANTENIMIENTO DE DESCUENTO MOVIMIENTO",
                "Datos_del_Movimiento_de_Descuento",
                "Lista_de_Movimientos_de_Descuento",
                new String[][]{
                        {"emp", "Empresa:"},
                        {"org", "Organizacion:"},
                        {"tipDes", "Tipo Descuento:"},
                        {"conSec", "Sec. Convenio:"},
                        {"desSec", "Sec. Descuento:"},
                        {"tipMov", "Tipo Movimiento:"},
                        {"anio", "Anio Planilla:"},
                        {"mes", "Mes Planilla:"},
                        {"plaNum", "Num. Planilla:"},
                        {"monto", "Monto:"},
                        {"estado", "Estado Registro:"}
                },
                new String[]{"Emp.", "Org.", "Tipo Desc.", "Sec. Conv.", "Sec. Desc.",
                        "Anio", "Mes", "Planilla", "Tipo Mov.", "Monto", "Estado"});
        configurarCombosDependientes();
        configurarMontoCalculado();
        cargarTabla();
        ajustarAnchosColumnas(65, 65, 95, 95, 95, 75, 65, 90, 95, 110, 85);
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

        if ("conSec".equals(clave) || "desSec".equals(clave)) {
            return crearComboBoxCampo(clave);
        }

        if ("tipMov".equals(clave)) {
            JComboBox<String> combo = crearComboBoxCampo(clave);
            List<TipoMovimiento> tipos = new TipoMovimientoService().obtenerListado();
            for (TipoMovimiento tipo : tipos) {
                if ("A".equals(tipo.getTipMovEstReg())) {
                    combo.addItem(tipo.getTipMovCod() + " - " + tipo.getTipMovNom());
                }
            }
            return combo;
        }

        return super.crearComponenteCampo(clave);
    }

    private void configurarCombosDependientes() {
        JComboBox<String> empresa = combos.get("emp");
        JComboBox<String> organizacion = combos.get("org");
        JComboBox<String> tipoDescuento = combos.get("tipDes");
        JComboBox<String> convenio = combos.get("conSec");
        if (empresa == null || organizacion == null || tipoDescuento == null || convenio == null) return;

        empresa.addActionListener(e -> cargarSecuenciasConvenio());
        organizacion.addActionListener(e -> cargarSecuenciasConvenio());
        tipoDescuento.addActionListener(e -> cargarSecuenciasConvenio());
        convenio.addActionListener(e -> cargarSecuenciasDescuento());
        combos.get("desSec").addActionListener(e -> cargarMontoCuotaDescuento());
        cargarSecuenciasConvenio();
    }

    private void configurarMontoCalculado() {
        JTextField monto = campos.get("monto");
        if (monto == null) return;
        monto.setEditable(true);
        monto.setBackground(Color.WHITE);
    }

    private void cargarSecuenciasConvenio() {
        JComboBox<String> combo = combos.get("conSec");
        if (combo == null) return;

        combo.removeAllItems();
        limpiarSecuenciasDescuento("Seleccione primero un convenio");

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

        cargarSecuenciasDescuento();
    }

    private void cargarSecuenciasDescuento() {
        JComboBox<String> combo = combos.get("desSec");
        if (combo == null) return;

        combo.removeAllItems();

        String emp = valor("emp");
        String org = valor("org");
        String tipDes = valor("tipDes");
        String conSec = valor("conSec");

        if (emp.isEmpty() || org.isEmpty() || tipDes.isEmpty() || !conSec.matches("\\d+")) {
            combo.addItem("Seleccione primero un convenio");
            return;
        }

        String sql = "SELECT DesSec, DesCtaCod, DesMonTot, DesMonCuo FROM R1T_DESCUENTO " +
                "WHERE DesConEmpCod = ? AND DesConOrgCod = ? AND DesConTipDesCod = ? " +
                "AND DesConSec = ? AND DesEstReg = 'A' ORDER BY DesSec";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(emp));
            ps.setInt(2, Integer.parseInt(org));
            ps.setString(3, tipDes);
            ps.setInt(4, Integer.parseInt(conSec));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    combo.addItem(String.format("%02d - Cuenta %08d - Total %.2f - Cuota %.2f",
                            rs.getInt("DesSec"),
                            rs.getInt("DesCtaCod"),
                            rs.getBigDecimal("DesMonTot"),
                            rs.getBigDecimal("DesMonCuo")));
                }
            }

            if (combo.getItemCount() == 0) {
                combo.addItem("No hay descuentos para el convenio");
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error al cargar secuencias de descuento: " + e.getMessage());
            combo.addItem("No se pudo cargar descuentos");
        }

        cargarMontoCuotaDescuento();
    }

    private void cargarMontoCuotaDescuento() {
        JTextField monto = campos.get("monto");
        if (monto == null) return;

        String emp = valor("emp");
        String org = valor("org");
        String tipDes = valor("tipDes");
        String conSec = valor("conSec");
        String desSec = valor("desSec");

        if (emp.isEmpty() || org.isEmpty() || tipDes.isEmpty() || !conSec.matches("\\d+") || !desSec.matches("\\d+")) {
            monto.setText("");
            return;
        }

        String sql = "SELECT DesMonCuo FROM R1T_DESCUENTO " +
                "WHERE DesConEmpCod = ? AND DesConOrgCod = ? AND DesConTipDesCod = ? " +
                "AND DesConSec = ? AND DesSec = ? AND DesEstReg = 'A'";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(emp));
            ps.setInt(2, Integer.parseInt(org));
            ps.setString(3, tipDes);
            ps.setInt(4, Integer.parseInt(conSec));
            ps.setInt(5, Integer.parseInt(desSec));

            try (ResultSet rs = ps.executeQuery()) {
                monto.setText(rs.next() ? rs.getBigDecimal("DesMonCuo").toPlainString() : "");
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error al cargar monto de cuota de descuento: " + e.getMessage());
            monto.setText("");
        }
    }

    private void limpiarSecuenciasDescuento(String mensaje) {
        JComboBox<String> combo = combos.get("desSec");
        if (combo == null) return;
        combo.removeAllItems();
        combo.addItem(mensaje);
    }

    @Override
    protected void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (DescuentoMov mov : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{
                    mov.getDesMovConEmpCod(),
                    mov.getDesMovConOrgCod(),
                    mov.getDesMovConTipDesCod(),
                    mov.getDesMovConSec(),
                    mov.getDesMovDesSec(),
                    mov.getDesMovPlaAnio(),
                    mov.getDesMovPlaMes(),
                    mov.getDesMovPlaNum(),
                    mov.getDesMovTipMovCod(),
                    mov.getDesMovMon(),
                    mov.getDesMovEstReg()
            });
        }
    }

    @Override
    protected void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        asignarValor("emp", valorTabla(fila, 0));
        asignarValor("org", valorTabla(fila, 1));
        asignarValor("tipDes", valorTabla(fila, 2));
        asignarValor("conSec", valorTabla(fila, 3));
        asignarValor("desSec", valorTabla(fila, 4));
        asignarValor("anio", valorTabla(fila, 5));
        asignarValor("mes", valorTabla(fila, 6));
        asignarValor("plaNum", valorTabla(fila, 7));
        asignarValor("tipMov", valorTabla(fila, 8));
        asignarValor("monto", valorTabla(fila, 9));
        asignarValor("estado", valorTabla(fila, 10));
    }

    @Override
    protected String prepararAdicionar() {
        return service.prepararAdicionar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("anio"), valor("mes"), valor("plaNum"),
                valor("tipMov"), valor("monto"));
    }

    @Override
    protected String prepararModificar() {
        return service.prepararModificar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("anio"), valor("mes"), valor("plaNum"),
                valor("tipMov"), valor("monto"));
    }

    @Override
    protected String prepararEliminar() {
        return service.prepararEliminar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("anio"), valor("mes"), valor("plaNum"));
    }

    @Override
    protected String prepararInactivar() {
        return service.prepararInactivar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("anio"), valor("mes"), valor("plaNum"));
    }

    @Override
    protected String prepararReactivar() {
        return service.prepararReactivar(valor("emp"), valor("org"), valor("tipDes"), valor("conSec"),
                valor("desSec"), valor("anio"), valor("mes"), valor("plaNum"));
    }

    @Override
    protected String actualizarOperacion() {
        return service.actualizar();
    }

    @Override
    protected String cancelarOperacion() {
        return service.cancelar();
    }

    private String valorTabla(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }
}
