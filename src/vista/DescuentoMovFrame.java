package vista;

import modelo.DescuentoMov;
import servicio.DescuentoMovService;

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
                        {"anio", "Anio Planilla:"},
                        {"mes", "Mes Planilla:"},
                        {"plaNum", "Num. Planilla:"},
                        {"tipMov", "Tipo Movimiento:"},
                        {"monto", "Monto:"},
                        {"estado", "Estado Registro:"}
                },
                new String[]{"Emp.", "Org.", "Tipo Desc.", "Sec. Conv.", "Sec. Desc.",
                        "Anio", "Mes", "Planilla", "Tipo Mov.", "Monto", "Estado"});
        cargarTabla();
        ajustarAnchosColumnas(65, 65, 95, 95, 95, 75, 65, 90, 95, 110, 85);
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
        cargarFilaEnCampos("emp", "org", "tipDes", "conSec", "desSec",
                "anio", "mes", "plaNum", "tipMov", "monto", "estado");
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
}
