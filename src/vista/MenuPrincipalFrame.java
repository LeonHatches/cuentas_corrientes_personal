package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Color FONDO = new Color(246, 250, 245);
    private final Color TEXTO = new Color(25, 70, 45);

    public MenuPrincipalFrame() {
        setTitle("Cuentas Corrientes de Personal");
        setSize(920, 800); // Un poco más alto para que quepan todos los botones holgadamente
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 18));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(34, 48, 34, 48));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("SISTEMA DE CUENTAS CORRIENTES DE PERSONAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(TEXTO);

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(FONDO);
        panelTitulo.add(titulo, BorderLayout.CENTER);
        panelTitulo.setBorder(new EmptyBorder(0, 0, 22, 0));

        // --- BOTONES: TABLAS REFERENCIALES (Columna Izquierda) ---
        BotonRedondeado btnEstadoRegistro = new BotonRedondeado("Estado de Registro");
        BotonRedondeado btnTipoTrabajador = new BotonRedondeado("Tipo Trabajador");
        BotonRedondeado btnEstadoTrabajador = new BotonRedondeado("Estado Trabajador");
        BotonRedondeado btnTipoPrestamo = new BotonRedondeado("Tipo Préstamo");
        BotonRedondeado btnTipoMovimiento = new BotonRedondeado("Tipo Movimiento");
        BotonRedondeado btnTipoOrganizacion = new BotonRedondeado("Tipo Organización");
        BotonRedondeado btnTipoDescuento = new BotonRedondeado("Tipo Descuento");
        BotonRedondeado btnTipoCuentaCorriente = new BotonRedondeado("Tipo Cuenta Corriente");

        btnEstadoRegistro.addActionListener(e -> abrirVentana(new EstadoRegistroFrame()));
        btnTipoTrabajador.addActionListener(e -> abrirVentana(new TipoTrabajadorFrame()));
        btnEstadoTrabajador.addActionListener(e -> abrirVentana(new EstadoTrabajadorFrame()));
        btnTipoPrestamo.addActionListener(e -> abrirVentana(new TipoPrestamoFrame()));
        btnTipoMovimiento.addActionListener(e -> abrirVentana(new TipoMovimientoFrame()));
        btnTipoOrganizacion.addActionListener(e -> abrirVentana(new TipoOrganizacionFrame()));
        btnTipoDescuento.addActionListener(e -> abrirVentana(new TipoDescuentoFrame()));
        btnTipoCuentaCorriente.addActionListener(e -> abrirVentana(new TipoCuentaCorrienteFrame()));

        // --- BOTONES: TABLAS MAESTRAS Y TRANSACCIONALES (Columna Derecha) ---
        BotonRedondeado btnCentroCosto = new BotonRedondeado("Centro de Costo");
        BotonRedondeado btnEmpresa = new BotonRedondeado("Empresa");
        BotonRedondeado btnTrabajador = new BotonRedondeado("Trabajador");
        BotonRedondeado btnOrganizacion = new BotonRedondeado("Organización");
        BotonRedondeado btnConvenio = new BotonRedondeado("Convenio");
        BotonRedondeado btnDescuento = new BotonRedondeado("Descuento");
        BotonRedondeado btnDescuentoMov = new BotonRedondeado("Descuento Mov.");
        BotonRedondeado btnPrestamo = new BotonRedondeado("Préstamo");
        BotonRedondeado btnPrestamoMov = new BotonRedondeado("Cuotas de Préstamo");
        BotonRedondeado btnCuentaCorriente = new BotonRedondeado("Cuenta Corriente");
        BotonRedondeado btnActa = new BotonRedondeado("Actas");

        btnCentroCosto.addActionListener(e -> abrirVentana(new CentroCostoFrame()));
        btnEmpresa.addActionListener(e -> abrirVentana(new EmpresaFrame()));
        btnOrganizacion.addActionListener(e -> abrirVentana(new OrganizacionFrame()));
        btnConvenio.addActionListener(e -> abrirVentana(new ConvenioFrame()));
        btnDescuento.addActionListener(e -> abrirVentana(new DescuentoFrame()));
        btnDescuentoMov.addActionListener(e -> abrirVentana(new DescuentoMovFrame()));
        btnTrabajador.addActionListener(e -> abrirVentana(new TrabajadorFrame()));
        btnPrestamo.addActionListener(e -> abrirVentana(new PrestamoFrame()));
        btnPrestamoMov.addActionListener(e -> abrirVentana(new PrestamoMovFrame()));
        btnCuentaCorriente.addActionListener(e -> abrirVentana(new CuentaCorrienteFrame()));
        btnActa.addActionListener(e -> abrirVentana(new ActaFrame()));

        // Aplicar el mismo tamaño a todos los botones
        BotonRedondeado[] todosLosBotones = {
                btnEstadoRegistro, btnTipoTrabajador, btnEstadoTrabajador, btnTipoPrestamo,
                btnTipoMovimiento, btnTipoOrganizacion, btnTipoDescuento, btnTipoCuentaCorriente,
                btnCentroCosto, btnEmpresa, btnTrabajador, btnOrganizacion, btnConvenio,
                btnDescuento, btnDescuentoMov, btnPrestamo, btnPrestamoMov, btnCuentaCorriente, btnActa
        };

        for (BotonRedondeado btn : todosLosBotones) {
            btn.setPreferredSize(new Dimension(260, 44));
        }

        // --- CONSTRUCCIÓN DE LA CUADRÍCULA (11 Filas x 2 Columnas) ---
        JPanel panelBotones = new JPanel(new GridLayout(11, 2, 32, 12));
        panelBotones.setBackground(FONDO);
        panelBotones.setBorder(new EmptyBorder(0, 80, 0, 80));

        // Fila 1
        panelBotones.add(btnEstadoRegistro);      panelBotones.add(btnCentroCosto);
        // Fila 2
        panelBotones.add(btnTipoTrabajador);      panelBotones.add(btnEmpresa);
        // Fila 3
        panelBotones.add(btnEstadoTrabajador);    panelBotones.add(btnTrabajador);
        // Fila 4
        panelBotones.add(btnTipoPrestamo);        panelBotones.add(btnOrganizacion);
        // Fila 5
        panelBotones.add(btnTipoMovimiento);      panelBotones.add(btnConvenio);
        // Fila 6
        panelBotones.add(btnTipoOrganizacion);    panelBotones.add(btnDescuento);
        // Fila 7
        panelBotones.add(btnTipoDescuento);       panelBotones.add(btnDescuentoMov);
        // Fila 8
        panelBotones.add(btnTipoCuentaCorriente); panelBotones.add(btnPrestamo);
        // Fila 9 (Llenamos el espacio izquierdo con un panel vacío invisible)
        panelBotones.add(crearEspacioVacio());    panelBotones.add(btnPrestamoMov);
        // Fila 10
        panelBotones.add(crearEspacioVacio());    panelBotones.add(btnCuentaCorriente);
        // Fila 11
        panelBotones.add(crearEspacioVacio());    panelBotones.add(btnActa);

        // --- PANEL INFERIOR (Botón de Salir) ---
        JPanel panelSur = new JPanel();
        panelSur.setBackground(FONDO);
        panelSur.setBorder(new EmptyBorder(12, 0, 0, 0));

        BotonRedondeado btnSalir = new BotonRedondeado("Salir del Sistema");
        btnSalir.setPreferredSize(new Dimension(260, 44));
        btnSalir.addActionListener(e -> dispose());
        panelSur.add(btnSalir);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

    // Método auxiliar para rellenar los huecos de la cuadrícula
    private JPanel crearEspacioVacio() {
        JPanel espacio = new JPanel();
        espacio.setOpaque(false);
        return espacio;
    }

    private void abrirVentana(JFrame ventana) {
        this.setVisible(false);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                MenuPrincipalFrame.this.setVisible(true);
            }
        });
        ventana.setVisible(true);
    }
}