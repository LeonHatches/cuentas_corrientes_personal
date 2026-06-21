package vista;

import modelo.Prestamo;
import modelo.Trabajador;
import modelo.TipoPrestamo;
import servicio.PrestamoService;
import servicio.TrabajadorService;
import servicio.TipoPrestamoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrestamoFrame extends JFrame {

    private final PrestamoService service = new PrestamoService();
    private final TrabajadorService trabajadorService = new TrabajadorService();
    private final TipoPrestamoService tipoPrestamoService = new TipoPrestamoService();

    private JComboBox<String> cbxTrabajador;
    private JComboBox<String> cbxTipoPrestamo;
    private JTextField txtFechaPrestamo;
    private JTextField txtMonto;
    private JTextField txtCantidadCuotas;
    private JTextField txtMontoCuota;
    private JTextField txtEstadoRegistro;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    // --- PALETA DE COLORES ---
    private final Color FONDO = new Color(246, 250, 245);
    private final Color BLANCO = Color.WHITE;
    private final Color VERDE_CLARO = new Color(218, 239, 211);
    private final Color VERDE_OSCURO = new Color(25, 70, 45);
    private final Color ERROR = new Color(170, 70, 70);
    private final Color OK = new Color(35, 140, 70);

    public PrestamoFrame() {
        setTitle("Mantenimiento - Préstamos");
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

        JLabel titulo = new JLabel("0011 - MANTENIMIENTO DE PRÉSTAMO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelTitulo = new JPanel(new GridLayout(1, 1));
        panelTitulo.setBackground(FONDO);
        panelTitulo.add(titulo);

        // --- PANEL DE REGISTRO (FORMULARIO) ---
        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Datos_del_Préstamo"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbxTrabajador = new JComboBox<>();
        cbxTipoPrestamo = new JComboBox<>();
        txtFechaPrestamo = crearCampoTexto();
        txtMonto = crearCampoTexto();
        txtCantidadCuotas = crearCampoTexto();
        txtMontoCuota = crearCampoTexto();
        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        cbxTrabajador.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxTipoPrestamo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Fila 0: Trabajador y Tipo Préstamo (Llaves foráneas)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Trabajador:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelRegistro.add(cbxTrabajador, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Tipo Préstamo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        panelRegistro.add(cbxTipoPrestamo, gbc);

        // Fila 1: Fecha y Monto Total
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Fecha (AAAAMMDD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelRegistro.add(txtFechaPrestamo, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Monto Total:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        panelRegistro.add(txtMonto, gbc);

        // Fila 2: Cantidad de cuotas y Monto por cuota
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Cant. Cuotas:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelRegistro.add(txtCantidadCuotas, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Monto Cuota:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        panelRegistro.add(txtMontoCuota, gbc);

        // Fila 3: Estado Registro
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(crearEtiqueta("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelRegistro.add(txtEstadoRegistro, gbc);

        // --- PANEL DE TABLA ---
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modeloTabla.addColumn("Cód. Trab.");
        modeloTabla.addColumn("Tipo P.");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Monto Total");
        modeloTabla.addColumn("Cuotas");
        modeloTabla.addColumn("Monto Cuota");
        modeloTabla.addColumn("Est.");

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(31);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setSelectionBackground(new Color(205, 232, 198));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(new Color(215, 230, 210));

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(VERDE_OSCURO);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.getViewport().setBackground(BLANCO);

        JPanel panelTabla = crearPanelBlanco();
        panelTabla.setLayout(new BorderLayout(10, 10));
        panelTabla.setBorder(crearBordeTitulo("Lista_de_Préstamos"));
        panelTabla.setPreferredSize(new Dimension(860, 230));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(15, 15));
        panelCentro.setBackground(FONDO);
        panelCentro.add(panelRegistro, BorderLayout.NORTH);
        panelCentro.add(panelTabla, BorderLayout.CENTER);

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 12, 12));
        panelBotones.setBackground(FONDO);

        BotonRedondeado btnAdicionar = new BotonRedondeado("Adicionar");
        BotonRedondeado btnModificar = new BotonRedondeado("Modificar");
        BotonRedondeado btnEliminar = new BotonRedondeado("Eliminar");
        BotonRedondeado btnCancelar = new BotonRedondeado("Cancelar");
        BotonRedondeado btnInactivar = new BotonRedondeado("Inactivar");
        BotonRedondeado btnReactivar = new BotonRedondeado("Reactivar");
        BotonRedondeado btnActualizar = new BotonRedondeado("Actualizar");
        BotonRedondeado btnSalir = new BotonRedondeado("Salir");

        btnAdicionar.addActionListener(e -> adicionar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCancelar.addActionListener(e -> cancelar());
        btnInactivar.addActionListener(e -> inactivar());
        btnReactivar.addActionListener(e -> reactivar());
        btnActualizar.addActionListener(e -> actualizar());
        btnSalir.addActionListener(e -> dispose());

        panelBotones.add(btnAdicionar); panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar); panelBotones.add(btnCancelar);
        panelBotones.add(btnInactivar); panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar); panelBotones.add(btnSalir);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMensaje.setForeground(VERDE_OSCURO);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setBackground(FONDO);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.add(lblMensaje, BorderLayout.SOUTH);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    // --- MÉTODOS DE COMBOBOX (LLAVES FORÁNEAS) ---
    private void cargarCombos() {
        cbxTrabajador.removeAllItems();
        cbxTrabajador.addItem("Seleccione Trabajador...");
        List<Trabajador> trabajadores = trabajadorService.obtenerListado();
        for (Trabajador t : trabajadores) {
            cbxTrabajador.addItem(t.getTraCod() + " - " + t.getTraNom());
        }

        cbxTipoPrestamo.removeAllItems();
        cbxTipoPrestamo.addItem("Seleccione Tipo...");
        List<TipoPrestamo> tipos = tipoPrestamoService.obtenerListado();
        for (TipoPrestamo tp : tipos) {
            // Formato de 2 dígitos como en la tabla de Tipo Préstamo
            cbxTipoPrestamo.addItem(String.format("%02d", tp.getTipPreCod()) + " - " + tp.getTipPreNom());
        }
    }

    private Integer obtenerIdTrabajador() {
        if (cbxTrabajador.getSelectedIndex() <= 0) return null;
        String seleccionado = (String) cbxTrabajador.getSelectedItem();
        return Integer.parseInt(seleccionado.split(" - ")[0]);
    }

    private Integer obtenerIdTipoPrestamo() {
        if (cbxTipoPrestamo.getSelectedIndex() <= 0) return null;
        String seleccionado = (String) cbxTipoPrestamo.getSelectedItem();
        return Integer.parseInt(seleccionado.split(" - ")[0]);
    }

    private void seleccionarEnCombo(JComboBox<String> combo, String idBuscado) {
        // Agregamos el cero inicial si es el combobox de tipo de préstamo y tiene 1 dígito
        if (combo == cbxTipoPrestamo && idBuscado.length() == 1) {
            idBuscado = "0" + idBuscado;
        }

        for (int i = 1; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(idBuscado + " -")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(0);
    }

    // --- ACCIONES DE BOTONES ---
    private void adicionar() {
        Integer traCod = obtenerIdTrabajador();
        Integer tipCod = obtenerIdTipoPrestamo();

        if (traCod == null) { mostrarMensaje("Error: Debe seleccionar un Trabajador."); return; }
        if (tipCod == null) { mostrarMensaje("Error: Debe seleccionar un Tipo de Préstamo."); return; }

        try {
            double mnt = Double.parseDouble(txtMonto.getText());
            int canCuo = Integer.parseInt(txtCantidadCuotas.getText());
            double mntCuo = Double.parseDouble(txtMontoCuota.getText());

            String mensaje = service.prepararAdicionar(traCod, tipCod, txtFechaPrestamo.getText(), mnt, canCuo, mntCuo);
            mostrarMensaje(mensaje);

        } catch (NumberFormatException e) {
            mostrarMensaje("Error: Los montos y cantidad de cuotas deben ser numéricos.");
        }
    }

    private void modificar() {
        Integer traCod = obtenerIdTrabajador();
        Integer tipCod = obtenerIdTipoPrestamo();

        if (traCod == null || tipCod == null || txtFechaPrestamo.getText().isEmpty()) {
            mostrarMensaje("Seleccione un registro de la tabla para modificar."); return;
        }

        try {
            int fec = Integer.parseInt(txtFechaPrestamo.getText());
            double mnt = Double.parseDouble(txtMonto.getText());
            int canCuo = Integer.parseInt(txtCantidadCuotas.getText());
            double mntCuo = Double.parseDouble(txtMontoCuota.getText());

            String mensaje = service.prepararModificar(traCod, tipCod, fec, mnt, canCuo, mntCuo);
            mostrarMensaje(mensaje);

        } catch (NumberFormatException e) {
            mostrarMensaje("Error: La fecha, los montos y las cuotas deben ser datos numéricos.");
        }
    }

    private void eliminar() { prepararCambioEstado("ELIMINAR"); }
    private void inactivar() { prepararCambioEstado("INACTIVAR"); }
    private void reactivar() { prepararCambioEstado("REACTIVAR"); }

    private void prepararCambioEstado(String accion) {
        Integer traCod = obtenerIdTrabajador();
        Integer tipCod = obtenerIdTipoPrestamo();

        if (traCod == null || tipCod == null || txtFechaPrestamo.getText().isEmpty()) {
            mostrarMensaje("Seleccione un préstamo de la tabla primero."); return;
        }

        try {
            int fec = Integer.parseInt(txtFechaPrestamo.getText());
            String mensaje = "";

            if (accion.equals("ELIMINAR")) mensaje = service.prepararEliminar(traCod, tipCod, fec);
            else if (accion.equals("INACTIVAR")) mensaje = service.prepararInactivar(traCod, tipCod, fec);
            else if (accion.equals("REACTIVAR")) mensaje = service.prepararReactivar(traCod, tipCod, fec);

            mostrarMensaje(mensaje);
        } catch (NumberFormatException e) {
            mostrarMensaje("Error al leer la fecha de la llave primaria.");
        }
    }

    private void actualizar() {
        String mensaje = service.actualizar();
        mostrarMensaje(mensaje);
        cargarTabla();
        limpiarCampos();
    }

    private void cancelar() {
        String mensaje = service.cancelar();
        mostrarMensaje(mensaje);
        limpiarCampos();
        tabla.clearSelection();
    }

    // --- TABLA Y UTILIDADES ---
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Prestamo> lista = service.obtenerListado();

        for (Prestamo p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getPreTraCod(),
                    p.getPreTipCod(),
                    p.getPreFecPre(),
                    p.getPreMnt(),
                    p.getPreCanCuo(),
                    p.getPreMntCuo(),
                    p.getPreEstReg()
            });
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String traCod = modeloTabla.getValueAt(fila, 0).toString();
            String tipCod = modeloTabla.getValueAt(fila, 1).toString();

            seleccionarEnCombo(cbxTrabajador, traCod);
            seleccionarEnCombo(cbxTipoPrestamo, tipCod);

            txtFechaPrestamo.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtMonto.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtCantidadCuotas.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtMontoCuota.setText(modeloTabla.getValueAt(fila, 5).toString());
            txtEstadoRegistro.setText(modeloTabla.getValueAt(fila, 6).toString());
        }
    }

    private void limpiarCampos() {
        cbxTrabajador.setSelectedIndex(0);
        cbxTipoPrestamo.setSelectedIndex(0);
        txtFechaPrestamo.setText("");
        txtMonto.setText("");
        txtCantidadCuotas.setText("");
        txtMontoCuota.setText("");
        txtEstadoRegistro.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
        String texto = mensaje.toLowerCase();
        if (texto.contains("exitosa") || texto.contains("lista") || texto.contains("cancelada")) {
            lblMensaje.setForeground(OK);
        } else {
            lblMensaje.setForeground(ERROR);
        }
    }

    // Utilidades gráficas
    private JPanel crearPanelBlanco() {
        JPanel p = new JPanel(); p.setBackground(BLANCO);
        p.setBorder(BorderFactory.createLineBorder(new Color(205, 225, 200), 1));
        return p;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel l = new JLabel(texto); l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(VERDE_OSCURO); return l;
    }

    private JTextField crearCampoTexto() {
        JTextField c = new JTextField(); c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 210, 170), 1), new EmptyBorder(5, 7, 5, 7)));
        return c;
    }

    private javax.swing.border.TitledBorder crearBordeTitulo(String titulo) {
        javax.swing.border.TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(205, 225, 200), 1), titulo);
        borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 13)); borde.setTitleColor(VERDE_OSCURO);
        return borde;
    }
}