package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class MaestroFrameBase extends JFrame {
    protected final Map<String, JTextField> campos = new LinkedHashMap<>();
    protected final Map<String, JComboBox<String>> combos = new LinkedHashMap<>();
    protected JTable tabla;
    protected DefaultTableModel modeloTabla;
    protected JLabel lblMensaje;

    private final Color FONDO = new Color(246, 250, 245);
    private final Color BLANCO = Color.WHITE;
    private final Color VERDE_CLARO = new Color(218, 239, 211);
    private final Color VERDE_OSCURO = new Color(25, 70, 45);
    private final Color BORDE_VERDE = new Color(140, 180, 145);
    private final Color BORDE_SUAVE = new Color(170, 210, 170);
    private final Color ERROR = new Color(170, 70, 70);
    private final Color OK = new Color(35, 140, 70);

    protected MaestroFrameBase(String tituloVentana, String tituloPantalla, String tituloRegistro,
                               String tituloTabla, String[][] definicionCampos, String[] columnas) {
        setTitle(tituloVentana);
        setSize(1180, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        construirInterfaz(tituloPantalla, tituloRegistro, tituloTabla, definicionCampos, columnas);
    }

    private void construirInterfaz(String tituloPantalla, String tituloRegistro, String tituloTabla,
                                   String[][] definicionCampos, String[] columnas) {
        JPanel panelPrincipal = new JPanel(new BorderLayout(16, 16));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(24, 28, 22, 28));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel(tituloPantalla, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(VERDE_OSCURO);

        JPanel panelRegistro = crearPanelBlanco();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(crearBordeTitulo(tituloRegistro));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < definicionCampos.length; i++) {
            String clave = definicionCampos[i][0];
            String etiqueta = definicionCampos[i][1];
            JComponent componenteCampo = crearComponenteCampo(clave);

            gbc.gridx = (i % 2) * 2;
            gbc.gridy = i / 2;
            gbc.weightx = 0;
            panelRegistro.add(crearEtiqueta(etiqueta), gbc);

            gbc.gridx = (i % 2) * 2 + 1;
            gbc.gridy = i / 2;
            gbc.weightx = 1;
            panelRegistro.add(componenteCampo, gbc);
        }

        JTextField estado = campos.get("estado");
        if (estado != null) {
            estado.setEditable(false);
            estado.setBackground(new Color(242, 248, 240));
        }

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String columna : columnas) {
            modeloTabla.addColumn(columna);
        }

        tabla = new JTable(modeloTabla);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionBackground(new Color(205, 232, 198));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(BORDE_SUAVE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(VERDE_OSCURO);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(true);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 30));

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component componente = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                componente.setBackground(VERDE_CLARO);
                componente.setForeground(VERDE_OSCURO);
                componente.setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (componente instanceof JComponent) {
                    ((JComponent) componente).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BORDE_VERDE));
                }
                return componente;
            }
        };
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getTableHeader().setDefaultRenderer(centrado);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosSeleccionados();
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTabla.getViewport().setBackground(BLANCO);

        JPanel panelTabla = crearPanelBlanco();
        panelTabla.setLayout(new BorderLayout());
        panelTabla.setBorder(crearBordeTitulo(tituloTabla));
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

        btnAdicionar.addActionListener(e -> mostrarMensaje(prepararAdicionar()));
        btnModificar.addActionListener(e -> mostrarMensaje(prepararModificar()));
        btnEliminar.addActionListener(e -> mostrarMensaje(prepararEliminar()));
        btnInactivar.addActionListener(e -> mostrarMensaje(prepararInactivar()));
        btnReactivar.addActionListener(e -> mostrarMensaje(prepararReactivar()));
        btnCancelar.addActionListener(e -> {
            mostrarMensaje(cancelarOperacion());
            limpiarCampos();
            tabla.clearSelection();
        });
        btnActualizar.addActionListener(e -> {
            mostrarMensaje(actualizarOperacion());
            cargarTabla();
            limpiarCampos();
        });
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

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setBackground(FONDO);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.add(lblMensaje, BorderLayout.SOUTH);

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    protected String valor(String clave) {
        JComboBox<String> combo = combos.get(clave);
        if (combo != null) {
            Object seleccionado = combo.getSelectedItem();
            if (seleccionado == null) {
                return "";
            }
            return extraerCodigoCombo(seleccionado.toString());
        }

        JTextField campo = campos.get(clave);
        return campo == null ? "" : campo.getText();
    }

    protected void asignarValor(String clave, String valor) {
        JComboBox<String> combo = combos.get(clave);
        if (combo != null) {
            seleccionarComboPorCodigo(combo, valor);
            return;
        }

        JTextField campo = campos.get(clave);
        if (campo != null) {
            campo.setText(valor == null ? "" : valor);
        }
    }

    protected void cargarFilaEnCampos(String... claves) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        for (int i = 0; i < claves.length; i++) {
            Object valor = modeloTabla.getValueAt(fila, i);

            JComboBox<String> combo = combos.get(claves[i]);
            if (combo != null) {
                seleccionarComboPorCodigo(combo, valor == null ? "" : valor.toString());
                continue;
            }

            JTextField campo = campos.get(claves[i]);
            if (campo != null) {
                campo.setText(valor == null ? "" : valor.toString());
            }
        }
    }

    protected void limpiarCampos() {
        for (JTextField campo : campos.values()) {
            campo.setText("");
        }
        for (JComboBox<String> combo : combos.values()) {
            combo.setSelectedIndex(combo.getItemCount() > 0 ? 0 : -1);
        }
    }

    protected void ajustarAnchosColumnas(int... anchos) {
        for (int i = 0; i < anchos.length && i < tabla.getColumnModel().getColumnCount(); i++) {
            TableColumn columna = tabla.getColumnModel().getColumn(i);
            columna.setPreferredWidth(anchos[i]);
            columna.setMinWidth(Math.min(55, anchos[i]));
        }
    }

    protected void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
        String texto = mensaje == null ? "" : mensaje.toLowerCase();
        if (texto.contains("correctamente") || texto.contains("preparada") || texto.contains("cancelada")) {
            lblMensaje.setForeground(OK);
        } else {
            lblMensaje.setForeground(ERROR);
        }
    }

    private JPanel crearPanelBlanco() {
        JPanel panel = new JPanel();
        panel.setBackground(BLANCO);
        panel.setBorder(BorderFactory.createLineBorder(BORDE_SUAVE, 1));
        return panel;
    }

    private javax.swing.border.TitledBorder crearBordeTitulo(String titulo) {
        javax.swing.border.TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1), titulo);
        borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        borde.setTitleColor(VERDE_OSCURO);
        return borde;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etiqueta.setForeground(VERDE_OSCURO);
        return etiqueta;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBackground(BLANCO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1),
                new EmptyBorder(5, 7, 5, 7)));
        return campo;
    }

    protected JComponent crearComponenteCampo(String clave) {
        JTextField campo = crearCampoTexto();
        campos.put(clave, campo);
        return campo;
    }

    protected JComboBox<String> crearComboBoxCampo(String clave) {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(BLANCO);
        combo.setBorder(BorderFactory.createLineBorder(BORDE_SUAVE, 1));
        combos.put(clave, combo);
        return combo;
    }

    private String extraerCodigoCombo(String texto) {
        String limpio = texto == null ? "" : texto.trim();
        if (limpio.isEmpty()) return "";
        int fin = limpio.length();
        for (String separador : new String[]{" ", "-", "|"}) {
            int indice = limpio.indexOf(separador);
            if (indice > 0) fin = Math.min(fin, indice);
        }
        String codigo = limpio.substring(0, fin).trim();
        try {
            return String.valueOf(Integer.parseInt(codigo));
        } catch (NumberFormatException e) {
            return codigo;
        }
    }

    private void seleccionarComboPorCodigo(JComboBox<String> combo, String codigoBuscado) {
        String codigo = codigoBuscado == null ? "" : codigoBuscado.trim();
        String codigoNormalizado = codigo;
        try {
            codigoNormalizado = String.valueOf(Integer.parseInt(codigo));
        } catch (NumberFormatException ignored) {
        }

        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            String codigoItem = extraerCodigoCombo(item);
            if (codigoItem.equals(codigoNormalizado) || codigoItem.equals(codigo)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(combo.getItemCount() > 0 ? 0 : -1);
    }

    protected abstract void cargarTabla();
    protected abstract void cargarDatosSeleccionados();
    protected abstract String prepararAdicionar();
    protected abstract String prepararModificar();
    protected abstract String prepararEliminar();
    protected abstract String prepararInactivar();
    protected abstract String prepararReactivar();
    protected abstract String actualizarOperacion();
    protected abstract String cancelarOperacion();
}
