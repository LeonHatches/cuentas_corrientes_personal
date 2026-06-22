package vista;

import modelo.Prestamo;
import modelo.Trabajador;
import modelo.TipoPrestamo;
import modelo.CuentaCorriente;
import servicio.PrestamoService;
import servicio.TrabajadorService;
import servicio.TipoPrestamoService;
import servicio.CuentaCorrienteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class PrestamoFrame extends JFrame {

    private final PrestamoService service = new PrestamoService();
    private final TrabajadorService trabajadorService = new TrabajadorService();
    private final TipoPrestamoService tipoPrestamoService = new TipoPrestamoService();
    private final CuentaCorrienteService cuentaService = new CuentaCorrienteService();

    private JComboBox<String> cbxTrabajador, cbxTipoPrestamo, cbxCtaCorriente;
    private JTextField txtSecuencia, txtFechaPrestamo, txtMonto;
    private JTextField txtCantidadCuotas, txtMontoCuota, txtMontoAcumulado, txtEstadoRegistro;

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

    public PrestamoFrame() {
        setTitle("Mantenimiento - Préstamos");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
        cargarCombosIniciales();
        cargarTabla();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("MANTENIMIENTO DE PRÉSTAMOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Datos_del_Préstamo"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbxTrabajador = crearComboBox();
        cbxTipoPrestamo = crearComboBox();

        cbxCtaCorriente = crearComboBox();
        cbxCtaCorriente.addItem("Primero seleccione Trabajador...");

        txtSecuencia = crearCampoTexto();
        txtFechaPrestamo = crearCampoTexto();
        txtMonto = crearCampoTexto();
        txtCantidadCuotas = crearCampoTexto();

        txtMontoCuota = crearCampoTexto();
        txtMontoCuota.setEditable(false);
        txtMontoCuota.setBackground(new Color(242, 248, 240));
        txtMontoCuota.setText("0.00");

        txtMontoAcumulado = crearCampoTexto();
        txtMontoAcumulado.setEditable(false);
        txtMontoAcumulado.setBackground(new Color(242, 248, 240));
        txtMontoAcumulado.setText("0.00");

        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        cbxTrabajador.addActionListener(e -> {
            if (cbxTrabajador.getSelectedIndex() > 0) {
                cargarCuentasDelTrabajador();
            } else {
                cbxCtaCorriente.removeAllItems();
                cbxCtaCorriente.addItem("Primero seleccione Trabajador...");
            }
        });

        DocumentListener calculadorEnVivo = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calcularCuota(); }
            public void removeUpdate(DocumentEvent e) { calcularCuota(); }
            public void changedUpdate(DocumentEvent e) { calcularCuota(); }
        };
        txtMonto.getDocument().addDocumentListener(calculadorEnVivo);
        txtCantidadCuotas.getDocument().addDocumentListener(calculadorEnVivo);

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Trabajador:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(cbxTrabajador, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Tipo Préstamo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(cbxTipoPrestamo, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Secuencia:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtSecuencia, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Cta Corriente:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(cbxCtaCorriente, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Fecha (AAAAMMDD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtFechaPrestamo, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Monto Total:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtMonto, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Cant. Cuotas:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtCantidadCuotas, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Monto Cuota:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtMontoCuota, gbc);

        // Fila 4
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Monto Acumulado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtMontoAcumulado, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Estado Registro:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtEstadoRegistro, gbc);

        modeloTabla = new DefaultTableModel() { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String[] col = {"Trab.", "Tipo", "Sec.", "Cuenta", "Fecha", "Monto", "Cuotas", "Monto Cuota", "Acumulado", "Est."};
        for(String c : col) modeloTabla.addColumn(c);

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
        panelTabla.setBorder(crearBordeTitulo("Lista_de_Préstamos"));
        panelTabla.setPreferredSize(new Dimension(860, 200));
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(15, 15));
        panelCentro.setBackground(FONDO);
        panelCentro.add(panelRegistro, BorderLayout.NORTH);
        panelCentro.add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 12, 12));
        panelBotones.setBackground(FONDO);
        BotonRedondeado[] btn = { new BotonRedondeado("Adicionar"), new BotonRedondeado("Modificar"), new BotonRedondeado("Eliminar"), new BotonRedondeado("Cancelar"), new BotonRedondeado("Inactivar"), new BotonRedondeado("Reactivar"), new BotonRedondeado("Actualizar"), new BotonRedondeado("Salir") };
        btn[0].addActionListener(e -> adicionar()); btn[1].addActionListener(e -> modificar());
        btn[2].addActionListener(e -> prepararCambio("*", "ELIMINAR")); btn[3].addActionListener(e -> cancelar());
        btn[4].addActionListener(e -> prepararCambio("I", "INACTIVAR")); btn[5].addActionListener(e -> prepararCambio("A", "REACTIVAR"));
        btn[6].addActionListener(e -> actualizar()); btn[7].addActionListener(e -> dispose());
        for (BotonRedondeado b : btn) panelBotones.add(b);

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

    private void calcularCuota() {
        try {
            double mon = Double.parseDouble(txtMonto.getText());
            int cuo = Integer.parseInt(txtCantidadCuotas.getText());
            txtMontoCuota.setText(cuo > 0 ? String.format(Locale.US, "%.2f", mon / cuo) : "0.00");
        } catch (NumberFormatException e) { txtMontoCuota.setText("0.00"); }
    }

    private void cargarCombosIniciales() {
        cbxTrabajador.addItem("Seleccione Trabajador...");
        for (Trabajador t : trabajadorService.obtenerListado()) cbxTrabajador.addItem(t.getTraCod() + " - " + t.getTraNom());
        cbxTipoPrestamo.addItem("Seleccione Tipo...");
        for (TipoPrestamo tp : tipoPrestamoService.obtenerListado()) cbxTipoPrestamo.addItem(String.format("%02d", tp.getTipPreCod()) + " - " + tp.getTipPreNom());
    }

    private void cargarCuentasDelTrabajador() {
        cbxCtaCorriente.removeAllItems();
        cbxCtaCorriente.addItem("Seleccione Cuenta...");

        Integer traCod = getIdCombo(cbxTrabajador);
        if (traCod != null) {
            for (CuentaCorriente cta : cuentaService.obtenerListado()) {
                if (cta.getCtaTraCod() == traCod && "A".equals(cta.getCtaEstReg())) {
                    cbxCtaCorriente.addItem(cta.getCtaCod() + " - (Tipo " + cta.getCtaTipCtaCod() + ")");
                }
            }
        }
    }

    private Integer getIdCombo(JComboBox<String> c) {
        if(c.getSelectedIndex() <= 0) return null;
        return Integer.parseInt(((String)c.getSelectedItem()).split(" - ")[0]);
    }

    private void seleccionarEnCombo(JComboBox<String> combo, String idBuscado) {
        if (combo == cbxTipoPrestamo && idBuscado.length() == 1) idBuscado = "0" + idBuscado;
        for (int i = 1; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).startsWith(idBuscado + " -")) {
                combo.setSelectedIndex(i); return;
            }
        }
        combo.setSelectedIndex(0);
    }

    private void adicionar() {
        Integer tra = getIdCombo(cbxTrabajador), tip = getIdCombo(cbxTipoPrestamo), cta = getIdCombo(cbxCtaCorriente);
        if (tra == null || tip == null) { mostrarMensaje("Seleccione Trabajador y Tipo."); return; }
        if (cta == null) { mostrarMensaje("Debe seleccionar una cuenta corriente del trabajador."); return; }
        try {
            mostrarMensaje(service.prepararAdicionar(tra, tip, Integer.parseInt(txtSecuencia.getText()), cta, Integer.parseInt(txtFechaPrestamo.getText()), Double.parseDouble(txtMonto.getText()), Integer.parseInt(txtCantidadCuotas.getText()), Double.parseDouble(txtMontoCuota.getText()), Double.parseDouble(txtMontoAcumulado.getText())));
        } catch (Exception e) { mostrarMensaje("Verifique que todos los campos numéricos estén correctos."); }
    }

    private void modificar() {
        Integer tra = getIdCombo(cbxTrabajador), tip = getIdCombo(cbxTipoPrestamo), cta = getIdCombo(cbxCtaCorriente);
        if (tra == null || txtSecuencia.getText().isEmpty()) { mostrarMensaje("Seleccione un registro."); return; }
        if (cta == null) { mostrarMensaje("Debe seleccionar una cuenta corriente válida."); return; }
        try {
            mostrarMensaje(service.prepararModificar(tra, tip, Integer.parseInt(txtSecuencia.getText()), cta, Integer.parseInt(txtFechaPrestamo.getText()), Double.parseDouble(txtMonto.getText()), Integer.parseInt(txtCantidadCuotas.getText()), Double.parseDouble(txtMontoCuota.getText()), Double.parseDouble(txtMontoAcumulado.getText())));
        } catch (Exception e) { mostrarMensaje("Verifique que todos los campos numéricos estén correctos."); }
    }

    private void prepararCambio(String est, String op) {
        Integer tra = getIdCombo(cbxTrabajador), tip = getIdCombo(cbxTipoPrestamo);
        if (tra == null || txtSecuencia.getText().isEmpty()) { mostrarMensaje("Seleccione un registro."); return; }
        try {
            int sec = Integer.parseInt(txtSecuencia.getText());
            if(op.equals("ELIMINAR")) mostrarMensaje(service.prepararEliminar(tra, tip, sec));
            else if(op.equals("INACTIVAR")) mostrarMensaje(service.prepararInactivar(tra, tip, sec));
            else mostrarMensaje(service.prepararReactivar(tra, tip, sec));
        } catch (Exception e) { mostrarMensaje("Error al leer datos de la llave."); }
    }

    private void actualizar() { mostrarMensaje(service.actualizar()); cargarTabla(); limpiarCampos(); }
    private void cancelar() { mostrarMensaje(service.cancelar()); limpiarCampos(); tabla.clearSelection(); }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Prestamo p : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{ p.getPreTraCod(), p.getPreTipCod(), p.getPreSec(), p.getPreCtaCod(), p.getPreFec(), p.getPreMon(), p.getPreCuo(), p.getPreMonCuo(), p.getPreMonAcuDes(), p.getPreEstReg() });
        }
    }

    private void cargarDatosSeleccionados() {
        int f = tabla.getSelectedRow();
        if (f >= 0) {
            String tra = modeloTabla.getValueAt(f, 0).toString();
            seleccionarEnCombo(cbxTrabajador, tra);

            String tip = modeloTabla.getValueAt(f, 1).toString();
            seleccionarEnCombo(cbxTipoPrestamo, tip);

            txtSecuencia.setText(modeloTabla.getValueAt(f, 2).toString());

            String cta = modeloTabla.getValueAt(f, 3).toString();
            seleccionarEnCombo(cbxCtaCorriente, cta);

            txtFechaPrestamo.setText(modeloTabla.getValueAt(f, 4).toString());
            txtMonto.setText(modeloTabla.getValueAt(f, 5).toString());
            txtCantidadCuotas.setText(modeloTabla.getValueAt(f, 6).toString());
            txtMontoCuota.setText(modeloTabla.getValueAt(f, 7).toString());
            txtMontoAcumulado.setText(modeloTabla.getValueAt(f, 8).toString());
            txtEstadoRegistro.setText(modeloTabla.getValueAt(f, 9).toString());
        }
    }

    private void limpiarCampos() {
        cbxTrabajador.setSelectedIndex(0);
        cbxTipoPrestamo.setSelectedIndex(0);
        cbxCtaCorriente.removeAllItems();
        cbxCtaCorriente.addItem("Primero seleccione Trabajador...");

        txtSecuencia.setText(""); txtFechaPrestamo.setText(""); txtMonto.setText("");
        txtCantidadCuotas.setText("");
        txtMontoCuota.setText("0.00");
        txtMontoAcumulado.setText("0.00");
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