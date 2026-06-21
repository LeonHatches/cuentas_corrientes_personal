package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Color FONDO = new Color(246, 250, 245);
    private final Color TEXTO = new Color(25, 70, 45);

    public MenuPrincipalFrame() {
        setTitle("Cuentas Corrientes de Personal");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("SISTEMA DE CUENTAS CORRIENTES DE PERSONAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TEXTO);

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(FONDO);
        panelTitulo.add(titulo, BorderLayout.CENTER);
        panelTitulo.setBorder(new EmptyBorder(0, 0, 30, 0));

        JPanel panelColumnas = new JPanel();
        panelColumnas.setLayout(new BoxLayout(panelColumnas, BoxLayout.X_AXIS));
        panelColumnas.setBackground(FONDO);

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(FONDO);
        panelIzquierdo.setAlignmentY(Component.TOP_ALIGNMENT);

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

        BotonRedondeado[] botonesIzquierda = {btnEstadoRegistro, btnTipoTrabajador, btnEstadoTrabajador,
                btnTipoPrestamo, btnTipoMovimiento, btnTipoOrganizacion,
                btnTipoDescuento, btnTipoCuentaCorriente};

        int espacio = 15;
        for (BotonRedondeado btn : botonesIzquierda) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIzquierdo.add(btn);
            panelIzquierdo.add(Box.createRigidArea(new Dimension(0, espacio)));
        }

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(FONDO);
        panelDerecho.setAlignmentY(Component.TOP_ALIGNMENT);

        BotonRedondeado btnCentroCosto = new BotonRedondeado("Centro de Costo");
        BotonRedondeado btnEmpresa = new BotonRedondeado("Empresa");
        BotonRedondeado btnTrabajador = new BotonRedondeado("Trabajador");

        btnCentroCosto.addActionListener(e -> abrirVentana(new CentroCostoFrame()));
        btnEmpresa.addActionListener(e -> abrirVentana(new EmpresaFrame()));

        // --- AQUÍ ESTÁ LA LÍNEA AGREGADA PARA QUE FUNCIONE EL BOTÓN ---
        btnTrabajador.addActionListener(e -> abrirVentana(new TrabajadorFrame()));

        BotonRedondeado[] botonesDerecha = {btnCentroCosto, btnEmpresa, btnTrabajador};

        for (BotonRedondeado btn : botonesDerecha) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelDerecho.add(btn);
            panelDerecho.add(Box.createRigidArea(new Dimension(0, espacio)));
        }

        panelColumnas.add(Box.createHorizontalGlue());
        panelColumnas.add(panelIzquierdo);
        panelColumnas.add(Box.createRigidArea(new Dimension(25, 0)));
        panelColumnas.add(panelDerecho);
        panelColumnas.add(Box.createHorizontalGlue());

        JPanel panelSur = new JPanel();
        panelSur.setBackground(FONDO);
        panelSur.setBorder(new EmptyBorder(15, 0, 0, 0));

        BotonRedondeado btnSalir = new BotonRedondeado("Salir del Sistema");
        btnSalir.addActionListener(e -> dispose());
        panelSur.add(btnSalir);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelColumnas, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
    }

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