package vista;

import modelo.TipoMovimiento;
import servicio.TipoMovimientoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TipoMovimientoFrame extends JFrame {

    private final TipoMovimientoService service = new TipoMovimientoService();

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtEstadoRegistro;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;

    private final Color FONDO = new Color(246, 250, 245);
    private final Color BLANCO = Color.WHITE;
    private final Color VERDE_CLARO = new Color(218, 239, 211);
    private final Color VERDE_OSCURO = new Color(25, 70, 45);
    private final Color ERROR = new Color(170, 70, 70);
    private final Color OK = new Color(35, 140, 70);

    public TipoMovimientoFrame() {
        setTitle("Mantenimiento - Tipo Movimiento");
        setSize(930, 800);
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

        JLabel titulo = new JLabel("R13006 - MANTENIMIENTO DE TIPO MOVIMIENTO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelTitulo = new JPanel(new GridLayout(1, 1));
        panelTitulo.setBackground(FONDO);
        panelTitulo.add(titulo);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo("Registro_Tipo_Movimiento"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = crearEtiqueta("Código:");
        JLabel lblNombre = crearEtiqueta("Nombre:");
        JLabel lblEstado = crearEtiqueta("Estado Registro:");

        txtCodigo = crearCampoTexto();
        txtNombre = crearCampoTexto();
        txtEstadoRegistro = crearCampoTexto();
        txtEstadoRegistro.setEditable(false);
        txtEstadoRegistro.setBackground(new Color(242, 248, 240));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panelRegistro.add(lblCodigo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelRegistro.add(lblNombre, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panelRegistro.add(lblEstado, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        panelRegistro.add(txtEstadoRegistro, gbc);

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Estado Registro");

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(31);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setSelectionBackground(new Color(205, 232, 198));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(new Color(215, 230, 210));
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(VERDE_OSCURO);
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(205, 225, 200), 1));
        scrollTabla.getViewport().setBackground(BLANCO);
        scrollTabla.setPreferredSize(new Dimension(850, 280));

        JPanel panelTabla = crearPanelBlanco();
        panelTabla.setLayout(new BorderLayout(10, 10));
        panelTabla.setBorder(crearBordeTitulo("Tabla_Tipo_Movimiento"));
        panelTabla.setPreferredSize(new Dimension(860, 330));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(15, 15));
        panelCentro.setBackground(FONDO);
        panelCentro.add(panelRegistro, BorderLayout.NORTH);
        panelCentro.add(panelTabla, BorderLayout.CENTER);

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

        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);

        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblMensaje.setForeground(new Color(70, 95, 75));
        lblMensaje.setBorder(new EmptyBorder(8, 5, 0, 5));

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setBackground(FONDO);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.add(lblMensaje, BorderLayout.SOUTH);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBlanco() {
        JPanel panel = new JPanel();
        panel.setBackground(BLANCO);
        panel.setBorder(BorderFactory.createLineBorder(new Color(205, 225, 200), 1));
        return panel;
    }

    private javax.swing.border.TitledBorder crearBordeTitulo(String titulo) {
        javax.swing.border.TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(205, 225, 200), 1),
                titulo
        );

        borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        borde.setTitleColor(VERDE_OSCURO);

        return borde;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 15));
        etiqueta.setForeground(VERDE_OSCURO);
        return etiqueta;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setForeground(new Color(40, 60, 45));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 210, 170), 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        return campo;
    }

    private void adicionar() {
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();

        String mensaje = service.prepararAdicionar(codigo, nombre);
        mostrarMensaje(mensaje);
    }

    private void modificar() {
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();

        String mensaje = service.prepararModificar(codigo, nombre);
        mostrarMensaje(mensaje);
    }

    private void eliminar() {
        String codigo = txtCodigo.getText();

        String mensaje = service.prepararEliminar(codigo);
        mostrarMensaje(mensaje);
    }

    private void inactivar() {
        String codigo = txtCodigo.getText();

        String mensaje = service.prepararInactivar(codigo);
        mostrarMensaje(mensaje);
    }

    private void reactivar() {
        String codigo = txtCodigo.getText();

        String mensaje = service.prepararReactivar(codigo);
        mostrarMensaje(mensaje);
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

    private void cargarTabla() {
        modeloTabla.setRowCount(0);

        List<TipoMovimiento> lista = service.obtenerListado();

        for (TipoMovimiento tipo : lista) {
            modeloTabla.addRow(new Object[]{
                    tipo.getTipMovCod(),
                    tipo.getTipMovNom(),
                    tipo.getTipMovEstReg()
            });
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtCodigo.setText(valorCelda(fila, 0));
            txtNombre.setText(valorCelda(fila, 1));
            txtEstadoRegistro.setText(valorCelda(fila, 2));
        }
    }

    private String valorCelda(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEstadoRegistro.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);

        String texto = mensaje.toLowerCase();

        if (texto.contains("éxito")
                || texto.contains("preparada")
                || texto.contains("canceló")
                || texto.contains("correctamente")) {
            lblMensaje.setForeground(OK);
        } else {
            lblMensaje.setForeground(ERROR);
        }
    }
}