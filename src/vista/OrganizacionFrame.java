package vista;

import modelo.Organizacion;
import servicio.OrganizacionService;

public class OrganizacionFrame extends MaestroFrameBase {
    private final OrganizacionService service = new OrganizacionService();

    public OrganizacionFrame() {
        super("R13014 - Organizacion",
                "R13014 - MANTENIMIENTO DE ORGANIZACION",
                "Datos_de_la_Organizacion",
                "Lista_de_Organizaciones",
                new String[][]{
                        {"codigo", "Codigo:"},
                        {"nombre", "Nombre:"},
                        {"ruc", "RUC (11 digitos):"},
                        {"tipo", "Tipo Organizacion:"},
                        {"estado", "Estado Registro:"}
                },
                new String[]{"Codigo", "Nombre", "RUC", "Tipo Org.", "Estado Registro"});
        cargarTabla();
        ajustarAnchosColumnas(90, 360, 190, 150, 150);
    }

    @Override
    protected void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Organizacion org : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{
                    org.getOrgCod(),
                    org.getOrgNom(),
                    org.getOrgRuc(),
                    org.getTipOrgCod(),
                    org.getEstReg()
            });
        }
    }

    @Override
    protected void cargarDatosSeleccionados() {
        cargarFilaEnCampos("codigo", "nombre", "ruc", "tipo", "estado");
    }

    @Override
    protected String prepararAdicionar() {
        return service.prepararAdicionar(valor("codigo"), valor("nombre"), valor("ruc"), valor("tipo"));
    }

    @Override
    protected String prepararModificar() {
        return service.prepararModificar(valor("codigo"), valor("nombre"), valor("ruc"), valor("tipo"));
    }

    @Override
    protected String prepararEliminar() {
        return service.prepararEliminar(valor("codigo"));
    }

    @Override
    protected String prepararInactivar() {
        return service.prepararInactivar(valor("codigo"));
    }

    @Override
    protected String prepararReactivar() {
        return service.prepararReactivar(valor("codigo"));
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
