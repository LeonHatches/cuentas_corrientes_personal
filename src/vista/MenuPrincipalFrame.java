package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Color FONDO = new Color(246, 250, 245);
    private final Color TEXTO = new Color(25, 70, 45);

    public MenuPrincipalFrame() {
        setTitle("Cuentas Corrientes de Personal");
        setSize(680, 700); // Se aumentó el alto para que entren todos los botones
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(35, 45, 35, 45));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("SISTEMA DE CUENTAS CORRIENTES DE PERSONAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TEXTO);

        JPanel panelTitulo = new JPanel(new GridLayout(1, 1));
        panelTitulo.setBackground(FONDO);
        panelTitulo.add(titulo);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(FONDO);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(new EmptyBorder(35, 0, 20, 0));

        // 1. Declarar botones
        BotonRedondeado btnEstadoRegistro = new BotonRedondeado("Estado de Registro");
        BotonRedondeado btnTipoTrabajador = new BotonRedondeado("Tipo Trabajador");
        BotonRedondeado btnEstadoTrabajador = new BotonRedondeado("Estado Trabajador");
        BotonRedondeado btnTipoPrestamo = new BotonRedondeado("Tipo Préstamo");
        BotonRedondeado btnTipoMovimiento = new BotonRedondeado("Tipo Movimiento");
        BotonRedondeado btnTipoOrganizacion = new BotonRedondeado("Tipo Organización");
        BotonRedondeado btnTipoDescuento = new BotonRedondeado("Tipo Descuento");
        BotonRedondeado btnTipoCuentaCorriente = new BotonRedondeado("Tipo Cuenta Corriente");
        BotonRedondeado btnSalir = new BotonRedondeado("Salir");

        // 2. Centrar botones
        btnEstadoRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTipoTrabajador.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEstadoTrabajador.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTipoPrestamo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTipoMovimiento.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTipoOrganizacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTipoDescuento.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTipoCuentaCorriente.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Asignar acciones (eventos)
        btnEstadoRegistro.addActionListener(e -> abrirVentana(new EstadoRegistroFrame()));
        btnTipoTrabajador.addActionListener(e -> abrirVentana(new TipoTrabajadorFrame()));
        btnEstadoTrabajador.addActionListener(e -> abrirVentana(new EstadoTrabajadorFrame()));
        btnTipoPrestamo.addActionListener(e -> abrirVentana(new TipoPrestamoFrame()));
        btnTipoMovimiento.addActionListener(e -> abrirVentana(new TipoMovimientoFrame()));
        btnTipoOrganizacion.addActionListener(e -> abrirVentana(new TipoOrganizacionFrame()));
        btnTipoDescuento.addActionListener(e -> abrirVentana(new TipoDescuentoFrame()));
        btnTipoCuentaCorriente.addActionListener(e -> abrirVentana(new TipoCuentaCorrienteFrame()));

        btnSalir.addActionListener(e -> dispose());

        // 4. Agregar botones al panel con espacio entre ellos
        int espacio = 15;

        panelBotones.add(btnEstadoRegistro);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnTipoTrabajador);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnEstadoTrabajador);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnTipoPrestamo);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnTipoMovimiento);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnTipoOrganizacion);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnTipoDescuento);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnTipoCuentaCorriente);
        panelBotones.add(Box.createRigidArea(new Dimension(0, espacio)));

        panelBotones.add(btnSalir);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
    }

    // Método auxiliar para no repetir tanto código en cada botón
    private void abrirVentana(JFrame ventana) {
        MenuPrincipalFrame.this.setVisible(false);

        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                MenuPrincipalFrame.this.setVisible(true);
            }
        });

        ventana.setVisible(true);
    }
}