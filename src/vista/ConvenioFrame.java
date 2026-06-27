package vista;

import conexion.ConexionBD;
import modelo.Convenio;
import modelo.Empresa;
import modelo.Organizacion;
import modelo.TipoDescuento;
import servicio.ConvenioService;
import servicio.EmpresaService;
import servicio.OrganizacionService;
import servicio.TipoDescuentoService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConvenioFrame extends MaestroFrameBase {
    private final ConvenioService service = new ConvenioService();

    public ConvenioFrame() {
        super("R13015 - Convenio",
                "R13015 - MANTENIMIENTO DE CONVENIO",
                "Datos_del_Convenio",
                "Lista_de_Convenios",
                new String[][]{
                        {"emp", "Empresa:"},
                        {"org", "Organizacion:"},
                        {"tipDes", "Tipo Descuento:"},
                        {"sec", "Secuencia:"},
                        {"des", "Descripcion:"},
                        {"actCod", "Codigo Acta:"},
                        {"con", "Contenido:"},
                        {"estado", "Estado Registro:"}
                },
                new String[]{"Emp.", "Org.", "Tipo Desc.", "Sec.", "Descripcion", "Acta", "Contenido", "Estado"});
        cargarTabla();
        ajustarAnchosColumnas(75, 75, 115, 75, 260, 90, 270, 100);
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

        if ("actCod".equals(clave)) {
            JComboBox<String> combo = crearComboBoxCampo(clave);
            cargarActas(combo);
            return combo;
        }

        return super.crearComponenteCampo(clave);
    }

    private void cargarActas(JComboBox<String> combo) {
        String sql = "SELECT ActCod, ActRef, ActFec FROM R1T_ACTA WHERE ActEstReg = 'A' ORDER BY ActCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                combo.addItem(String.format("%06d - %s - %d",
                        rs.getInt("ActCod"),
                        rs.getString("ActRef"),
                        rs.getInt("ActFec")));
            }

            if (combo.getItemCount() == 0) {
                combo.addItem("No hay actas activas");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar actas para convenio: " + e.getMessage());
            combo.addItem("No se pudo cargar actas");
        }
    }

    @Override
    protected void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Convenio con : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{
                    con.getConEmpCod(),
                    con.getConOrgCod(),
                    con.getConTipDesCod(),
                    con.getConSec(),
                    con.getConDes(),
                    con.getConActCod(),
                    con.getConCon(),
                    con.getConEstReg()
            });
        }
    }

    @Override
    protected void cargarDatosSeleccionados() {
        cargarFilaEnCampos("emp", "org", "tipDes", "sec", "des", "actCod", "con", "estado");
    }

    @Override
    protected String prepararAdicionar() {
        return service.prepararAdicionar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"),
                valor("des"), valor("actCod"), valor("con"));
    }

    @Override
    protected String prepararModificar() {
        return service.prepararModificar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"),
                valor("des"), valor("actCod"), valor("con"));
    }

    @Override
    protected String prepararEliminar() {
        return service.prepararEliminar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"));
    }

    @Override
    protected String prepararInactivar() {
        return service.prepararInactivar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"));
    }

    @Override
    protected String prepararReactivar() {
        return service.prepararReactivar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"));
    }

    @Override
    protected String actualizarOperacion() {
        return service.actualizar();
    }

    @Override
    protected String cancelarOperacion() {
        return service.cancelar();
    }
}
