package vista;

import modelo.PrestamoMov;
import modelo.Trabajador;
import modelo.TipoPrestamo;
import modelo.Prestamo;
import servicio.PrestamoMovService;
import servicio.TrabajadorService;
import servicio.TipoPrestamoService;
import servicio.PrestamoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

public class PrestamoMovFrame extends JFrame {

    private final PrestamoMovService service = new PrestamoMovService();
    private final TrabajadorService trabajadorService = new TrabajadorService();
    private final TipoPrestamoService tipoPrestamoService = new TipoPrestamoService();
    private final PrestamoService prestamoService = new PrestamoService(); // Para buscar secuencias

    private JComboBox<String> cbxTrabajador, cbxTipoPrestamo, cbxSecuencia, cbxTipoMovimiento;
    private JTextField txtAnio, txtMes, txtPlanilla, txtMonto, txtEstadoRegistro;

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

    public PrestamoMovFrame() {
        setTitle("R13012 - Préstamo Movimiento");
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

        JLabel titulo = new JLabel("R13012 - MANTENIMIENTO DE CUOTAS DE PRÉSTAMO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Datos_de_la_Cuota_en_Planilla"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbxTrabajador = crearComboBox();
        cbxTipoPrestamo = crearComboBox();

        // La secuencia ahora es un ComboBox filtrado
        cbxSecuencia = crearComboBox();
        cbxSecuencia.addItem("Seleccione un Préstamo...");

        cbxTipoMovimiento = crearComboBox();
        cbxTipoMovimiento.addItem("C - Cargo");
        cbxTipoMovimiento.addItem("A - Abono");

        txtAnio = crearCampoTexto();
        txtMes = crearCampoTexto();
        txtPlanilla = crearCampoTexto();
        txtPlanilla.setText("1"); // Planilla por defecto

        txtMonto = crearCampoTexto();
        txtMonto.setText("0.00");

        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        // Eventos para recargar la lista de secuencias
        cbxTrabajador.addActionListener(e -> cargarSecuencias());
        cbxTipoPrestamo.addActionListener(e -> cargarSecuencias());

        // Evento para auto-llenar el monto al seleccionar la secuencia
        cbxSecuencia.addActionListener(e -> autoLlenarMonto());

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Trabajador:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(cbxTrabajador, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Tipo Préstamo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(cbxTipoPrestamo, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Secuencia Préstamo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(cbxSecuencia, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Tipo Movimiento:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(cbxTipoMovimiento, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Año Planilla (AAAA):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtAnio, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Mes Planilla (MM):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtMes, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Nro Planilla:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtPlanilla, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Monto:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtMonto, gbc);

        // Fila 4
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtEstadoRegistro, gbc);

        modeloTabla = new DefaultTableModel() { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String[] columnas = {"Trab.", "Tipo", "Sec.", "Año", "Mes", "Planilla", "Mov.", "Monto", "Est."};
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
        panelTabla.setBorder(crearBordeTitulo("Lista_de_Cuotas_y_Movimientos"));
        panelTabla.setPreferredSize(new Dimension(860, 200));
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
        cbxTipoPrestamo.addItem("Seleccione Tipo...");
        for (TipoPrestamo tp : tipoPrestamoService.obtenerListado()) cbxTipoPrestamo.addItem(String.format("%02d", tp.getTipPreCod()) + " - " + tp.getTipPreNom());
    }

    private void cargarSecuencias() {
        cbxSecuencia.removeAllItems();
        cbxSecuencia.addItem("Seleccione Secuencia...");

        Integer traCod = getIdCombo(cbxTrabajador);
        Integer tipCod = getIdCombo(cbxTipoPrestamo);

        if (traCod != null && tipCod != null) {
            for (Prestamo p : prestamoService.obtenerListado()) {
                if (p.getPreTraCod() == traCod && p.getPreTipCod() == tipCod && "A".equals(p.getPreEstReg())) {
                    cbxSecuencia.addItem(String.valueOf(p.getPreSec()));
                }
            }
        }
    }

    private void autoLlenarMonto() {
        if (cbxSecuencia.getSelectedIndex() > 0) {
            Integer traCod = getIdCombo(cbxTrabajador);
            Integer tipCod = getIdCombo(cbxTipoPrestamo);
            int sec = Integer.parseInt((String) cbxSecuencia.getSelectedItem());

            for (Prestamo p : prestamoService.obtenerListado()) {
                if (p.getPreTraCod() == traCod && p.getPreTipCod() == tipCod && p.getPreSec() == sec) {
                    txtMonto.setText(String.format(Locale.US, "%.2f", p.getPreMonCuo()));
                    break;
                }
            }
        }
    }

    private Integer getIdCombo(JComboBox<String> c) {
        if(c.getSelectedIndex() <= 0) return null;
        return Integer.parseInt(((String)c.getSelectedItem()).split(" - ")[0]);
    }

    private String getTipoMov() { return ((String)cbxTipoMovimiento.getSelectedItem()).substring(0, 1); }

    private void adicionar() {
        Integer tra = getIdCombo(cbxTrabajador), tip = getIdCombo(cbxTipoPrestamo);
        if (tra == null || tip == null) { mostrarMensaje("Seleccione Trabajador y Tipo."); return; }
        if (cbxSecuencia.getSelectedIndex() <= 0) { mostrarMensaje("Seleccione una Secuencia."); return; }
        try {
            int sec = Integer.parseInt((String) cbxSecuencia.getSelectedItem());
            mostrarMensaje(service.prepararAdicionar(tra, tip, sec, Integer.parseInt(txtAnio.getText()), Integer.parseInt(txtMes.getText()), Integer.parseInt(txtPlanilla.getText()), getTipoMov(), Double.parseDouble(txtMonto.getText())));
        } catch (Exception e) { mostrarMensaje("Campos numéricos inválidos."); }
    }

    private void modificar() {
        Integer tra = getIdCombo(cbxTrabajador), tip = getIdCombo(cbxTipoPrestamo);
        if (tra == null || cbxSecuencia.getSelectedIndex() <= 0) { mostrarMensaje("Seleccione un registro válido."); return; }
        try {
            int sec = Integer.parseInt((String) cbxSecuencia.getSelectedItem());
            mostrarMensaje(service.prepararModificar(tra, tip, sec, Integer.parseInt(txtAnio.getText()), Integer.parseInt(txtMes.getText()), Integer.parseInt(txtPlanilla.getText()), getTipoMov(), Double.parseDouble(txtMonto.getText())));
        } catch (Exception e) { mostrarMensaje("Campos numéricos inválidos."); }
    }

    private void prepararCambio(String est, String op) {
        Integer tra = getIdCombo(cbxTrabajador), tip = getIdCombo(cbxTipoPrestamo);
        if (tra == null || cbxSecuencia.getSelectedIndex() <= 0) { mostrarMensaje("Seleccione un registro."); return; }
        try {
            int sec = Integer.parseInt((String) cbxSecuencia.getSelectedItem());
            if(op.equals("ELIMINAR")) mostrarMensaje(service.prepararEliminar(tra, tip, sec, Integer.parseInt(txtAnio.getText()), Integer.parseInt(txtMes.getText()), Integer.parseInt(txtPlanilla.getText())));
            else if(op.equals("INACTIVAR")) mostrarMensaje(service.prepararInactivar(tra, tip, sec, Integer.parseInt(txtAnio.getText()), Integer.parseInt(txtMes.getText()), Integer.parseInt(txtPlanilla.getText())));
            else mostrarMensaje(service.prepararReactivar(tra, tip, sec, Integer.parseInt(txtAnio.getText()), Integer.parseInt(txtMes.getText()), Integer.parseInt(txtPlanilla.getText())));
        } catch (Exception e) { mostrarMensaje("Error al leer datos llave."); }
    }

    private void actualizar() { mostrarMensaje(service.actualizar()); cargarTabla(); limpiarCampos(); }
    private void cancelar() { mostrarMensaje(service.cancelar()); limpiarCampos(); tabla.clearSelection(); }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (PrestamoMov p : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{ p.getPreMovTraCod(), p.getPreMovTipCod(), p.getPreMovPreSec(), p.getPreMovPlaAnio(), p.getPreMovPlaMes(), p.getPreMovPlaNum(), p.getPreMovTipMovCod(), p.getPreMovMonDes(), p.getPreMovEstReg() });
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String tra = modeloTabla.getValueAt(fila, 0).toString();
            for(int i=1; i<cbxTrabajador.getItemCount(); i++) if(cbxTrabajador.getItemAt(i).startsWith(tra+" -")) cbxTrabajador.setSelectedIndex(i);

            String tip = String.format("%02d", Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString()));
            for(int i=1; i<cbxTipoPrestamo.getItemCount(); i++) if(cbxTipoPrestamo.getItemAt(i).startsWith(tip+" -")) cbxTipoPrestamo.setSelectedIndex(i);

            // La secuencia se cargó sola por el evento, la seleccionamos
            String sec = modeloTabla.getValueAt(fila, 2).toString();
            for(int i=1; i<cbxSecuencia.getItemCount(); i++) if(cbxSecuencia.getItemAt(i).equals(sec)) cbxSecuencia.setSelectedIndex(i);

            txtAnio.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtMes.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtPlanilla.setText(modeloTabla.getValueAt(fila, 5).toString());

            String mov = modeloTabla.getValueAt(fila, 6).toString();
            cbxTipoMovimiento.setSelectedIndex(mov.equals("C") ? 0 : 1);

            txtMonto.setText(modeloTabla.getValueAt(fila, 7).toString());
            txtEstadoRegistro.setText(modeloTabla.getValueAt(fila, 8).toString());
        }
    }

    private void limpiarCampos() {
        cbxTrabajador.setSelectedIndex(0);
        cbxTipoPrestamo.setSelectedIndex(0);
        cbxSecuencia.removeAllItems();
        cbxSecuencia.addItem("Seleccione un Préstamo...");
        cbxTipoMovimiento.setSelectedIndex(0);
        txtAnio.setText("");
        txtMes.setText("");
        txtPlanilla.setText("1");
        txtMonto.setText("0.00");
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