package vista;

import modelo.*;
import servicio.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrabajadorFrame extends JFrame {

    private final TrabajadorService service = new TrabajadorService();
    private final EmpresaService empService = new EmpresaService();
    private final CentroCostoService ccService = new CentroCostoService();

    private JTextField txtCodigo, txtNombre, txtFecIngreso, txtFecCese, txtFecVacaciones, txtEstadoRegistro;
    private JComboBox<String> cbxEmpresa, cbxCentroCosto, cbxTipoTrabajador, cbxEstadoTrabajador;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    // --- PALETA DE COLORES VERDE INSTITUCIONAL ---
    private final Color FONDO = new Color(246, 250, 245);
    private final Color BLANCO = Color.WHITE;
    private final Color VERDE_CLARO = new Color(218, 239, 211);
    private final Color VERDE_OSCURO = new Color(25, 70, 45);
    private final Color BORDE_CAJA = new Color(170, 210, 170);
    private final Color SELECCION = new Color(205, 232, 198);
    private final Color ERROR = new Color(170, 70, 70);
    private final Color OK = new Color(35, 140, 70);

    public TrabajadorFrame() {
        setTitle("0010 - Mantenimiento de Trabajador");
        setSize(1050, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        construirInterfaz();
        cargarCombosDesdeBD();
        cargarTabla();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("0010 - MANTENIMIENTO DE TRABAJADOR", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Datos_del_Trabajador"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = crearCampoTexto();
        txtNombre = crearCampoTexto();
        txtFecIngreso = crearCampoTexto();
        txtFecCese = crearCampoTexto();
        txtFecVacaciones = crearCampoTexto();

        txtFecIngreso.setToolTipText("Formato: AAAAMMDD");
        txtFecCese.setToolTipText("Obligatorio solo si está Cesado (AAAAMMDD)");
        txtFecVacaciones.setToolTipText("Fecha última salida vacacional (AAAAMMDD)");

        cbxEmpresa = crearComboBox();
        cbxCentroCosto = crearComboBox();

        cbxTipoTrabajador = crearComboBox();
        cbxTipoTrabajador.addItem("1 - Funcionario");
        cbxTipoTrabajador.addItem("2 - Empleado");
        cbxTipoTrabajador.addItem("3 - Obrero");

        cbxEstadoTrabajador = crearComboBox();
        cbxEstadoTrabajador.addItem("A - Activo");
        cbxEstadoTrabajador.addItem("C - Cesado");

        // --- MAGIA VISUAL: BLOQUEO/DESBLOQUEO DE CAJA DE CESE ---
        cbxEstadoTrabajador.addActionListener(e -> {
            String seleccionado = (String) cbxEstadoTrabajador.getSelectedItem();
            if (seleccionado != null && seleccionado.startsWith("A")) {
                txtFecCese.setText("");
                txtFecCese.setEditable(false);
                txtFecCese.setBackground(new Color(235, 235, 235));
            } else {
                txtFecCese.setEditable(true);
                txtFecCese.setBackground(BLANCO);
            }
        });

        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        // -- FILA 0 --
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Código (6 dígit):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; panelRegistro.add(txtCodigo, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Nombre Completo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; panelRegistro.add(txtNombre, gbc);

        // -- FILA 1 --
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Fecha Ingreso:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; panelRegistro.add(txtFecIngreso, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Fecha Cese:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; panelRegistro.add(txtFecCese, gbc);

        // -- FILA 2 --
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Fec. Últ. Vacac:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; panelRegistro.add(txtFecVacaciones, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Empresa:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; panelRegistro.add(cbxEmpresa, gbc);

        // -- FILA 3 --
        gbc.gridy = 3;
        gbc.gridx = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Centro Costo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; panelRegistro.add(cbxCentroCosto, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Tipo Trabajador:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; panelRegistro.add(cbxTipoTrabajador, gbc);

        // -- FILA 4 --
        gbc.gridy = 4;
        gbc.gridx = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Est. Trabajador:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; panelRegistro.add(cbxEstadoTrabajador, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Estado Registro:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; panelRegistro.add(txtEstadoRegistro, gbc);

        // Forzamos la configuración inicial visual de la Fecha de Cese
        cbxEstadoTrabajador.setSelectedIndex(0);

        modeloTabla = new DefaultTableModel() { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String[] columnas = {"Cod", "Nombre", "Fec Ing", "Fec Ces", "Fec Vac", "Emp", "CenCos", "Tip", "Est", "Reg"};
        for (String col : columnas) modeloTabla.addColumn(col);

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(31);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(SELECCION);
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(new Color(215, 230, 210));

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(VERDE_OSCURO);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosSeleccionados();
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.getViewport().setBackground(BLANCO);

        JPanel panelTabla = crearPanelBlanco();
        panelTabla.setLayout(new BorderLayout());
        panelTabla.setBorder(crearBordeTitulo("Lista_de_Trabajadores"));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBackground(FONDO);
        panelCentro.add(panelRegistro, BorderLayout.NORTH);
        panelCentro.add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBackground(FONDO);

        BotonRedondeado[] botones = {
                new BotonRedondeado("Adicionar"), new BotonRedondeado("Modificar"),
                new BotonRedondeado("Eliminar"), new BotonRedondeado("Cancelar"),
                new BotonRedondeado("Inactivar"), new BotonRedondeado("Reactivar"),
                new BotonRedondeado("Actualizar"), new BotonRedondeado("Salir")
        };

        botones[0].addActionListener(e -> ejecutarServicio(1));
        botones[1].addActionListener(e -> ejecutarServicio(2));
        botones[2].addActionListener(e -> mostrarMensaje(service.prepararEliminar(txtCodigo.getText())));
        botones[3].addActionListener(e -> { mostrarMensaje(service.cancelar()); limpiarCampos(); tabla.clearSelection(); });
        botones[4].addActionListener(e -> mostrarMensaje(service.prepararInactivar(txtCodigo.getText())));
        botones[5].addActionListener(e -> mostrarMensaje(service.prepararReactivar(txtCodigo.getText())));
        botones[6].addActionListener(e -> { mostrarMensaje(service.actualizar()); cargarTabla(); limpiarCampos(); });
        botones[7].addActionListener(e -> dispose());

        for (BotonRedondeado b : botones) panelBotones.add(b);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setBackground(FONDO);
        panelSur.add(panelBotones, BorderLayout.NORTH);
        panelSur.add(lblMensaje, BorderLayout.CENTER);

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBlanco() {
        JPanel panel = new JPanel();
        panel.setBackground(BLANCO);
        panel.setBorder(BorderFactory.createLineBorder(new Color(205, 225, 200), 1));
        return panel;
    }

    private void ejecutarServicio(int tipo) {
        try {
            int idEmp = Integer.parseInt(((String) cbxEmpresa.getSelectedItem()).split(" - ")[0]);
            String idCC = ((String) cbxCentroCosto.getSelectedItem()).split(" - ")[0];
            int idTip = Integer.parseInt(((String) cbxTipoTrabajador.getSelectedItem()).split(" - ")[0]);
            String idEst = ((String) cbxEstadoTrabajador.getSelectedItem()).split(" - ")[0];

            if (tipo == 1) {
                mostrarMensaje(service.prepararAdicionar(txtCodigo.getText(), txtNombre.getText(), txtFecIngreso.getText(), txtFecCese.getText(), txtFecVacaciones.getText(), idEmp, idEst, idCC, idTip));
            } else {
                mostrarMensaje(service.prepararModificar(txtCodigo.getText(), txtNombre.getText(), txtFecIngreso.getText(), txtFecCese.getText(), txtFecVacaciones.getText(), idEmp, idEst, idCC, idTip));
            }
        } catch (Exception ex) {
            mostrarMensaje("Error en los datos. Verifique que haya llenado correctamente y seleccionado los combos.");
        }
    }

    private void cargarCombosDesdeBD() {
        for (Empresa emp : empService.obtenerListado()) {
            cbxEmpresa.addItem(emp.getEmpCod() + " - " + emp.getEmpRazSoc());
        }
        for (CentroCosto cc : ccService.obtenerListado()) {
            cbxCentroCosto.addItem(cc.getCenCosCod() + " - " + cc.getCenCosNom());
        }
    }

    private javax.swing.border.TitledBorder crearBordeTitulo(String titulo) {
        javax.swing.border.TitledBorder borde = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE_CAJA, 1), titulo);
        borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        borde.setTitleColor(VERDE_OSCURO);
        return borde;
    }

    private JTextField crearCampoTexto() {
        JTextField c = new JTextField(); c.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE_CAJA, 1), new EmptyBorder(5, 5, 5, 5)));
        return c;
    }

    private JComboBox<String> crearComboBox() {
        JComboBox<String> c = new JComboBox<>(); c.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        c.setBackground(BLANCO); return c;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel e = new JLabel(texto); e.setFont(new Font("Segoe UI", Font.BOLD, 15));
        e.setForeground(VERDE_OSCURO); return e;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Trabajador t : service.obtenerListado()) {
            String fecCesStr = (t.getTraFecCes() == null) ? "" : String.valueOf(t.getTraFecCes());
            String fecVacStr = (t.getTraFecUltSalVac() == null) ? "" : String.valueOf(t.getTraFecUltSalVac());

            modeloTabla.addRow(new Object[]{
                    t.getTraCod(), t.getTraNom(), t.getTraFecIng(), fecCesStr, fecVacStr,
                    t.getTraEmpCod(), t.getTraCenCosCod(), t.getTraTipCod(), t.getTraEstCod(), t.getTraEstReg()
            });
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtFecIngreso.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtFecCese.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtFecVacaciones.setText(modeloTabla.getValueAt(fila, 4).toString());

            // Dispara el bloqueo o desbloqueo de Fecha de Cese
            String estTra = modeloTabla.getValueAt(fila, 8).toString();
            if(estTra.equals("A")) cbxEstadoTrabajador.setSelectedIndex(0);
            else if(estTra.equals("C")) cbxEstadoTrabajador.setSelectedIndex(1);

            txtEstadoRegistro.setText(modeloTabla.getValueAt(fila, 9).toString());
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtFecIngreso.setText("");
        txtFecCese.setText("");
        txtFecVacaciones.setText("");
        txtEstadoRegistro.setText("");
        cbxEstadoTrabajador.setSelectedIndex(0);
    }

    private void mostrarMensaje(String m) {
        lblMensaje.setText(m);
        String texto = m.toLowerCase();
        if (texto.contains("correctamente") || texto.contains("exitosa") || texto.contains("preparada") || texto.contains("cancelada")) {
            lblMensaje.setForeground(OK);
        } else {
            lblMensaje.setForeground(ERROR);
        }
    }
}