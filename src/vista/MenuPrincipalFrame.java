package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Color FONDO = new Color(246, 250, 245);
    private final Color TEXTO = new Color(25, 70, 45);

    public MenuPrincipalFrame() {
        setTitle("Cuentas Corrientes de Personal");
        setSize(920, 760);
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

        BotonRedondeado[] botonesIzquierda = {
                btnEstadoRegistro, btnTipoTrabajador, btnEstadoTrabajador, btnTipoPrestamo,
                btnTipoMovimiento, btnTipoOrganizacion, btnTipoDescuento, btnTipoCuentaCorriente
        };

        int espacio = 15;
        for (BotonRedondeado btn : botonesIzquierda) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIzquierdo.add(btn);
            panelIzquierdo.add(Box.createRigidArea(new Dimension(0, espacio)));
        }

        // --- COLUMNA DERECHA: Tablas Maestras y Transaccionales ---
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(FONDO);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        BotonRedondeado btnCentroCosto = new BotonRedondeado("Centro de Costo");
        BotonRedondeado btnEmpresa = new BotonRedondeado("Empresa");
        BotonRedondeado btnTrabajador = new BotonRedondeado("Trabajador");
        BotonRedondeado btnOrganizacion = new BotonRedondeado("Organizacion");
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

        // --- AQUÍ ESTÁ LA LÍNEA AGREGADA PARA QUE FUNCIONE EL BOTÓN ---
        btnTrabajador.addActionListener(e -> abrirVentana(new TrabajadorFrame()));
        btnPrestamo.addActionListener(e -> abrirVentana(new PrestamoFrame()));
        btnPrestamoMov.addActionListener(e -> abrirVentana(new PrestamoMovFrame()));
        btnCuentaCorriente.addActionListener(e -> abrirVentana(new CuentaCorrienteFrame()));
        btnActa.addActionListener(e -> abrirVentana(new ActaFrame()));

        JPanel panelBotones = new JPanel(new GridLayout(8, 2, 32, 16));
        panelBotones.setBackground(FONDO);
        panelBotones.setBorder(new EmptyBorder(0, 105, 4, 105));
        BotonRedondeado[] botonesDerecha = {
                btnCentroCosto, btnEmpresa, btnTrabajador,
                btnPrestamo, btnPrestamoMov, btnCuentaCorriente, btnActa
        };

        BotonRedondeado[] botones = {
                btnEstadoRegistro, btnCentroCosto,
                btnTipoTrabajador, btnEmpresa,
                btnEstadoTrabajador, btnTrabajador,
                btnTipoPrestamo, btnOrganizacion,
                btnTipoMovimiento, btnConvenio,
                btnTipoOrganizacion, btnDescuento,
                btnTipoDescuento, btnDescuentoMov,
                btnTipoCuentaCorriente
        };

        for (BotonRedondeado btn : botones) {
            btn.setPreferredSize(new Dimension(260, 44));
            panelBotones.add(btn);
        }

        panelColumnas.add(Box.createHorizontalGlue());
        panelColumnas.add(panelIzquierdo);
        panelColumnas.add(Box.createRigidArea(new Dimension(40, 0))); // Separación entre columnas
        panelColumnas.add(panelDerecho);
        panelColumnas.add(Box.createHorizontalGlue());
        JPanel espacioVacio = new JPanel();
        espacioVacio.setOpaque(false);
        panelBotones.add(espacioVacio);

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

    private void abrirVentana(JFrame ventana) {
        MenuPrincipalFrame.this.setVisible(false); // Oculta el menú principal

        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                MenuPrincipalFrame.this.setVisible(true); // Vuelve a mostrar el menú al cerrar la ventana
            }
        });

        ventana.setVisible(true);
    }
}