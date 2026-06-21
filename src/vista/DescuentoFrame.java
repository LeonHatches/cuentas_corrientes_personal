package vista;

import modelo.Descuento;
import servicio.DescuentoService;

public class DescuentoFrame extends MaestroFrameBase {
    private final DescuentoService service = new DescuentoService();

    public DescuentoFrame() {
        super("0017 - Mantenimiento de Descuento",
                "0017 - MANTENIMIENTO DE DESCUENTO",
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
        cargarTabla();
        ajustarAnchosColumnas(65, 65, 95, 95, 95, 95, 105, 110, 105, 105, 110, 120, 85);
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
}
