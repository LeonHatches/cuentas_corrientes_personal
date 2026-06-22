package vista;

import modelo.Acta;
import servicio.ActaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ActaFrame extends JFrame {

    private final ActaService service = new ActaService();

    private JTextField txtActCod;
    private JTextField txtActRef;
    private JTextField txtActFec;
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

    public ActaFrame() {
        // Título exacto solicitado
        setTitle("R13019 - Actas");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
        cargarTabla();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(25, 25, 20, 25));
        setContentPane(panelPrincipal);

        // Subtítulo exacto solicitado
        JLabel titulo = new JLabel("R13019 - MANTENIMIENTO DE ACTAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Datos_del_Acta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtActCod = crearCampoTexto();
        txtActRef = crearCampoTexto();
        txtActFec = crearCampoTexto();

        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Código de Acta (6 dig):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtActCod, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Referencia:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtActRef, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Fecha (AAAAMMDD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panelRegistro.add(txtActFec, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelRegistro.add(crearEtiqueta("Estado Registro:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panelRegistro.add(txtEstadoRegistro, gbc);

        modeloTabla = new DefaultTableModel() { @Override public boolean isCellEditable(int r, int c) { return false; } };
        String[] columnas = {"Código Acta", "Referencia", "Fecha", "Est."};
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
        panelTabla.setBorder(crearBordeTitulo("Lista_de_Actas"));
        panelTabla.setPreferredSize(new Dimension(800, 230));
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

    private void adicionar() {
        try {
            mostrarMensaje(service.prepararAdicionar(Integer.parseInt(txtActCod.getText()), txtActRef.getText(), Integer.parseInt(txtActFec.getText())));
        } catch (NumberFormatException e) { mostrarMensaje("Verifique que el código y la fecha sean numéricos."); }
    }

    private void modificar() {
        if (txtActCod.getText().isEmpty()) { mostrarMensaje("Seleccione un registro de la tabla."); return; }
        try {
            mostrarMensaje(service.prepararModificar(Integer.parseInt(txtActCod.getText()), txtActRef.getText(), Integer.parseInt(txtActFec.getText())));
        } catch (NumberFormatException e) { mostrarMensaje("Verifique que el código y la fecha sean numéricos."); }
    }

    private void prepararCambio(String est, String op) {
        if (txtActCod.getText().isEmpty()) { mostrarMensaje("Seleccione un registro de la tabla."); return; }
        try {
            if(op.equals("ELIMINAR")) mostrarMensaje(service.prepararEliminar(Integer.parseInt(txtActCod.getText())));
            else if(op.equals("INACTIVAR")) mostrarMensaje(service.prepararInactivar(Integer.parseInt(txtActCod.getText())));
            else mostrarMensaje(service.prepararReactivar(Integer.parseInt(txtActCod.getText())));
        } catch (NumberFormatException e) { mostrarMensaje("Código de acta inválido."); }
    }

    private void actualizar() { mostrarMensaje(service.actualizar()); cargarTabla(); limpiarCampos(); }
    private void cancelar() { mostrarMensaje(service.cancelar()); limpiarCampos(); tabla.clearSelection(); }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Acta a : service.obtenerListado()) {
            modeloTabla.addRow(new Object[]{ a.getActCod(), a.getActRef(), a.getActFec(), a.getActEstReg() });
        }
    }

    private void cargarDatosSeleccionados() {
        int f = tabla.getSelectedRow();
        if (f >= 0) {
            txtActCod.setText(modeloTabla.getValueAt(f, 0).toString());
            txtActRef.setText(modeloTabla.getValueAt(f, 1).toString());
            txtActFec.setText(modeloTabla.getValueAt(f, 2).toString());
            txtEstadoRegistro.setText(modeloTabla.getValueAt(f, 3).toString());
        }
    }

    private void limpiarCampos() {
        txtActCod.setText("");
        txtActRef.setText("");
        txtActFec.setText("");
        txtEstadoRegistro.setText("");
    }

    private void mostrarMensaje(String m) {
        lblMensaje.setText(m);
        lblMensaje.setForeground(m.toLowerCase().contains("exitosa") || m.toLowerCase().contains("lista") || m.toLowerCase().contains("cancelada") ? OK : ERROR);
    }

    private JPanel crearPanelBlanco() { JPanel p = new JPanel(); p.setBackground(BLANCO); p.setBorder(BorderFactory.createLineBorder(new Color(205, 225, 200), 1)); return p; }
    private javax.swing.border.TitledBorder crearBordeTitulo(String titulo) { javax.swing.border.TitledBorder borde = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE_CAJA, 1), titulo); borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 14)); borde.setTitleColor(VERDE_OSCURO); return borde; }
    private JTextField crearCampoTexto() { JTextField c = new JTextField(); c.setFont(new Font("Segoe UI", Font.PLAIN, 15)); c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE_CAJA, 1), new EmptyBorder(5, 5, 5, 5))); return c; }
    private JLabel crearEtiqueta(String texto) { JLabel e = new JLabel(texto); e.setFont(new Font("Segoe UI", Font.BOLD, 15)); e.setForeground(VERDE_OSCURO); return e; }
}