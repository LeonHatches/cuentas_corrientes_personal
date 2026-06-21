package vista;

import modelo.Convenio;
import servicio.ConvenioService;

public class ConvenioFrame extends MaestroFrameBase {
    private final ConvenioService service = new ConvenioService();

    public ConvenioFrame() {
        super("0032 - Mantenimiento de Convenio",
                "0032 - MANTENIMIENTO DE CONVENIO",
                "Datos_del_Convenio",
                "Lista_de_Convenios",
                new String[][]{
                        {"emp", "Empresa:"},
                        {"org", "Organizacion:"},
                        {"tipDes", "Tipo Descuento:"},
                        {"sec", "Secuencia:"},
                        {"des", "Descripcion:"},
                        {"actRef", "Acto Referencia:"},
                        {"actFec", "Fecha Acto AAAAMMDD:"},
                        {"con", "Contenido:"},
                        {"estado", "Estado Registro:"}
                },
                new String[]{"Emp.", "Org.", "Tipo Desc.", "Sec.", "Descripcion", "Acto Ref.", "Fecha Acto", "Contenido", "Estado"});
        cargarTabla();
        ajustarAnchosColumnas(75, 75, 115, 75, 230, 145, 120, 230, 100);
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
                    con.getConActRef(),
                    con.getConActFec(),
                    con.getConCon(),
                    con.getConEstReg()
            });
        }
    }

    @Override
    protected void cargarDatosSeleccionados() {
        cargarFilaEnCampos("emp", "org", "tipDes", "sec", "des", "actRef", "actFec", "con", "estado");
    }

    @Override
    protected String prepararAdicionar() {
        return service.prepararAdicionar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"),
                valor("des"), valor("actRef"), valor("actFec"), valor("con"));
    }

    @Override
    protected String prepararModificar() {
        return service.prepararModificar(valor("emp"), valor("org"), valor("tipDes"), valor("sec"),
                valor("des"), valor("actRef"), valor("actFec"), valor("con"));
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
