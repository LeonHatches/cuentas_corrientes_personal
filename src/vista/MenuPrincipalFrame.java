package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Color FONDO = new Color(246, 250, 245);
    private final Color TEXTO = new Color(25, 70, 45);

    public MenuPrincipalFrame() {
        setTitle("Cuentas Corrientes de Personal");
        setSize(800, 650);
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
        panelColumnas.setBackground(FONDO);
        panelColumnas.setLayout(new BoxLayout(panelColumnas, BoxLayout.X_AXIS));

        // --- COLUMNA IZQUIERDA: Tablas Referenciales ---
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(FONDO);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

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
        BotonRedondeado btnPrestamo = new BotonRedondeado("Préstamo");
        BotonRedondeado btnPrestamoMov = new BotonRedondeado("Cuotas de Préstamo");
        BotonRedondeado btnCuentaCorriente = new BotonRedondeado("Cuenta Corriente");
        BotonRedondeado btnActa = new BotonRedondeado("Actas");

        btnCentroCosto.addActionListener(e -> abrirVentana(new CentroCostoFrame()));
        btnEmpresa.addActionListener(e -> abrirVentana(new EmpresaFrame()));
        btnTrabajador.addActionListener(e -> abrirVentana(new TrabajadorFrame()));
        btnPrestamo.addActionListener(e -> abrirVentana(new PrestamoFrame()));
        btnPrestamoMov.addActionListener(e -> abrirVentana(new PrestamoMovFrame()));
        btnCuentaCorriente.addActionListener(e -> abrirVentana(new CuentaCorrienteFrame()));
        btnActa.addActionListener(e -> abrirVentana(new ActaFrame()));

        BotonRedondeado[] botonesDerecha = {
                btnCentroCosto, btnEmpresa, btnTrabajador,
                btnPrestamo, btnPrestamoMov, btnCuentaCorriente, btnActa
        };

        for (BotonRedondeado btn : botonesDerecha) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelDerecho.add(btn);
            panelDerecho.add(Box.createRigidArea(new Dimension(0, espacio)));
        }

        panelColumnas.add(Box.createHorizontalGlue());
        panelColumnas.add(panelIzquierdo);
        panelColumnas.add(Box.createRigidArea(new Dimension(40, 0))); // Separación entre columnas
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
        MenuPrincipalFrame.this.setVisible(false); // Oculta el menú principal

        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                MenuPrincipalFrame.this.setVisible(true); // Vuelve a mostrar el menú al cerrar la ventana
            }
        });

        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        // Esto asegura que la interfaz se construya en el hilo correcto de Java
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipalFrame().setVisible(true);
        });
    }
}