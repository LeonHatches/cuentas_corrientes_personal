package vista;

import modelo.CuentaCorriente;
import modelo.Trabajador;
import modelo.TipoCuentaCorriente;
import servicio.CuentaCorrienteService;
import servicio.TrabajadorService;
import servicio.TipoCuentaCorrienteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CuentaCorrienteFrame extends JFrame {

    private final CuentaCorrienteService service = new CuentaCorrienteService();
    private final TrabajadorService trabajadorService = new TrabajadorService();
    private final TipoCuentaCorrienteService tipoCuentaService = new TipoCuentaCorrienteService();

    private JTextField txtCtaCod;
    private JComboBox<String> cbxTrabajador;
    private JComboBox<String> cbxTipoCuenta;
    private JTextField txtFechaApertura;
    private JTextField txtSaldoInicial;
    private JTextField txtSaldoActual;
    private JTextField txtEstadoRegistro;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    private final Color FONDO = new Color(246, 250, 245);
    private final Color BLANCO = Color.WHITE;
    private final Color VERDE_CLARO = new Color(218, 239, 211);
    private final Color VERDE_OSCURO = new Color(25, 70, 45);
    private final Color BORDE_CAJA = new Color(170, 210, 170);
    private final Color ERROR = new Color(170, 70, 70);
    private final Color OK = new Color(35, 140, 70);

    public CuentaCorrienteFrame() {
        setTitle("R13016 - Cuenta Corriente");
        setSize(950, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
        cargarCombos();
        cargarTabla();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(25, 25, 20, 25));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("R13016 - MANTENIMIENTO DE CUENTAS CORRIENTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Datos_de_la_Cuenta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCtaCod = crearCampoTexto();
        cbxTrabajador = crearComboBox();
        cbxTipoCuenta = crearComboBox();
        txtFechaApertura = crearCampoTexto();

        // --- SALDO INICIAL POR DEFECTO PERO NO BLOQUEADO ---
        txtSaldoInicial = crearCampoTexto();
        txtSaldoInicial.setText("0.00");

        txtSaldoActual = crearCampoTexto();
        txtSaldoActual.setText("0.00");

        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Cód. Cuenta (8 dig):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtCtaCod, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Trabajador:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(cbxTrabajador, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Tipo Cuenta:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(cbxTipoCuenta, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Fec Apertura (AAAAMMDD):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtFechaApertura, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Saldo Inicial:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtSaldoInicial, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Saldo Actual:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtSaldoActual, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtEstadoRegistro, gbc);

        modeloTabla = new DefaultTableModel() { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String[] columnas = {"Cuenta Nro.", "Cód. Trab.", "Tipo Cta.", "Fec. Apertura", "Saldo Inic.", "Saldo Act.", "Est."};
        for(String c : columnas) modeloTabla.addColumn(c);

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(31);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(205, 232, 198));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(VERDE_OSCURO);

        tabla.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) cargarDatosSeleccionados(); });

        JPanel panelTabla = crearPanelBlanco();
        panelTabla.setLayout(new BorderLayout());
        panelTabla.setBorder(crearBordeTitulo("Lista_de_Cuentas_Corrientes"));
        panelTabla.setPreferredSize(new Dimension(860, 230));
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(15, 15));
        panelCentro.setBackground(FONDO);
        panelCentro.add(panelRegistro, BorderLayout.NORTH);
        panelCentro.add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 12, 12));
        panelBotones.setBackground(FONDO);
        BotonRedondeado[] botones = { new BotonRedondeado("Adicionar"), new BotonRedondeado("Modificar"), new BotonRedondeado("Eliminar"), new BotonRedondeado("Cancelar"), new BotonRedondeado("Inactivar"), new BotonRedondeado("Reactivar"), new BotonRedondeado("Actualizar"), new BotonRedondeado("Salir") };
        botones[0].addActionListener(e -> adicionar()); botones[1].addActionListener(e -> modificar());
        botones[2].addActionListener(e -> prepararCambio("*", "ELIMINAR")); botones[3].addActionListener(e -> cancelar());
        botones[4].addActionListener(e -> prepararCambio("I", "INACTIVAR")); botones[5].addActionListener(e -> prepararCambio("A", "REACTIVAR"));
        botones[6].addActionListener(e -> actualizar()); botones[7].addActionListener(e -> dispose());
        for (BotonRedondeado b : botones) panelBotones.add(b);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setForeground(VERDE_OSCURO);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setBackground(FONDO);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.add(lblMensaje, BorderLayout.SOUTH);

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    private void cargarCombos() {
        cbxTrabajador.addItem("Seleccione Trabajador...");
        for (Trabajador t : trabajadorService.obtenerListado()) cbxTrabajador.addItem(t.getTraCod() + " - " + t.getTraNom());

        cbxTipoCuenta.addItem("Seleccione Tipo Cta...");
        for (TipoCuentaCorriente c : tipoCuentaService.obtenerListado()) cbxTipoCuenta.addItem(String.format("%02d", c.getTipCtaCod()) + " - " + c.getTipCtaNom());
    }

    private Integer getIdCombo(JComboBox<String> c) {
        if(c.getSelectedIndex() <= 0) return null;
        return Integer.parseInt(((String)c.getSelectedItem()).split(" - ")[0]);
    }

    private void adicionar() {
        Integer traCod = getIdCombo(cbxTrabajador), tipCta = getIdCombo(cbxTipoCuenta);
        if (traCod == null || tipCta == null) { mostrarMensaje("Seleccione Trabajador y Tipo de Cuenta."); return; }
        try {
            mostrarMensaje(service.prepararAdicionar(Integer.parseInt(txtCtaCod.getText()), traCod, tipCta, Integer.parseInt(txtFechaApertura.getText()), Double.parseDouble(txtSaldoInicial.getText()), Double.parseDouble(txtSaldoActual.getText())));
        } catch (NumberFormatException e) { mostrarMensaje("Verifique que la cuenta, fecha y montos sean numéricos."); }
    }

    private void modificar() {
        Integer traCod = getIdCombo(cbxTrabajador), tipCta = getIdCombo(cbxTipoCuenta);
        if (txtCtaCod.getText().isEmpty()) { mostrarMensaje("Seleccione un registro de la tabla."); return; }
        try {
            mostrarMensaje(service.prepararModificar(Integer.parseInt(txtCtaCod.getText()), traCod, tipCta, Integer.parseInt(txtFechaApertura.getText()), Double.parseDouble(txtSaldoInicial.getText()), Double.parseDouble(txtSaldoActual.getText())));
        } catch (NumberFormatException e) { mostrarMensaje("Verifique que la cuenta, fecha y montos sean numéricos."); }
    }

    private void prepararCambio(String est, String op) {
        if (txtCtaCod.getText().isEmpty()) { mostrarMensaje("Seleccione un registro de la tabla."); return; }
        try {
            if(op.equals("ELIMINAR")) mostrarMensaje(service.prepararEliminar(Integer.parseInt(txtCtaCod.getText())));
            else if(op.equals("INACTIVAR")) mostrarMensaje(service.prepararInactivar(Integer.parseInt(txtCtaCod.getText())));
            else mostrarMensaje(service.prepararReactivar(Integer.parseInt(txtCtaCod.getText())));
        } catch (NumberFormatException e) { mostrarMensaje("Código de cuenta inválido."); }
    }

    private void actualizar() { mostrarMensaje(service.actualizar()); cargarTabla(); limpiarCampos(); }
    private void cancelar() { mostrarMensaje(service.cancelar()); limpiarCampos(); tabla.clearSelection(); }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (CuentaCorriente c : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{ c.getCtaCod(), c.getCtaTraCod(), c.getCtaTipCtaCod(), c.getCtaFecApe(), c.getCtaSalIni(), c.getCtaSalAct(), c.getCtaEstReg() });
        }
    }

    private void cargarDatosSeleccionados() {
        int f = tabla.getSelectedRow();
        if (f >= 0) {
            txtCtaCod.setText(modeloTabla.getValueAt(f, 0).toString());

            String tra = modeloTabla.getValueAt(f, 1).toString();
            for(int i=1; i<cbxTrabajador.getItemCount(); i++) if(cbxTrabajador.getItemAt(i).startsWith(tra+" -")) cbxTrabajador.setSelectedIndex(i);

            String tip = String.format("%02d", Integer.parseInt(modeloTabla.getValueAt(f, 2).toString()));
            for(int i=1; i<cbxTipoCuenta.getItemCount(); i++) if(cbxTipoCuenta.getItemAt(i).startsWith(tip+" -")) cbxTipoCuenta.setSelectedIndex(i);

            txtFechaApertura.setText(modeloTabla.getValueAt(f, 3).toString());
            txtSaldoInicial.setText(modeloTabla.getValueAt(f, 4).toString());
            txtSaldoActual.setText(modeloTabla.getValueAt(f, 5).toString());
            txtEstadoRegistro.setText(modeloTabla.getValueAt(f, 6).toString());
        }
    }

    private void limpiarCampos() {
        txtCtaCod.setText("");
        cbxTrabajador.setSelectedIndex(0);
        cbxTipoCuenta.setSelectedIndex(0);
        txtFechaApertura.setText("");
        txtSaldoInicial.setText("0.00"); // No bloqueado, solo valor por defecto
        txtSaldoActual.setText("0.00");
        txtEstadoRegistro.setText("");
    }

    private void mostrarMensaje(String m) {
        lblMensaje.setText(m);
        lblMensaje.setForeground(m.toLowerCase().contains("exitosa") || m.toLowerCase().contains("lista") || m.toLowerCase().contains("cancelada") ? OK : ERROR);
    }

    private JPanel crearPanelBlanco() { JPanel p = new JPanel(); p.setBackground(BLANCO); p.setBorder(BorderFactory.createLineBorder(new Color(205, 225, 200), 1)); return p; }
    private javax.swing.border.TitledBorder crearBordeTitulo(String titulo) { javax.swing.border.TitledBorder borde = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE_CAJA, 1), titulo); borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 14)); borde.setTitleColor(VERDE_OSCURO); return borde; }
    private JTextField crearCampoTexto() { JTextField c = new JTextField(); c.setFont(new Font("Segoe UI", Font.PLAIN, 15)); c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE_CAJA, 1), new EmptyBorder(5, 5, 5, 5))); return c; }
    private JComboBox<String> crearComboBox() { JComboBox<String> c = new JComboBox<>(); c.setFont(new Font("Segoe UI", Font.PLAIN, 15)); c.setBackground(BLANCO); return c; }
    private JLabel crearEtiqueta(String texto) { JLabel e = new JLabel(texto); e.setFont(new Font("Segoe UI", Font.BOLD, 15)); e.setForeground(VERDE_OSCURO); return e; }
}