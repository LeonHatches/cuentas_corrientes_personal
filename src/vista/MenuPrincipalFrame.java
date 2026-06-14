package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Color FONDO = new Color(246, 250, 245);
    private final Color TEXTO = new Color(25, 70, 45);

    public MenuPrincipalFrame() {
        setTitle("Cuentas Corrientes de Personal");
        setSize(680, 430);
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
        panelBotones.setBorder(new EmptyBorder(55, 0, 20, 0));

        BotonRedondeado btnEstadoRegistro = new BotonRedondeado("Estado de Registro");
        BotonRedondeado btnSalir = new BotonRedondeado("Salir");

        btnEstadoRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEstadoRegistro.addActionListener(e -> {
            EstadoRegistroFrame ventana = new EstadoRegistroFrame();

            MenuPrincipalFrame.this.setVisible(false);

            ventana.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    MenuPrincipalFrame.this.setVisible(true);
                }
            });

            ventana.setVisible(true);
        });

        btnSalir.addActionListener(e -> dispose());

        panelBotones.add(btnEstadoRegistro);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 18)));
        panelBotones.add(btnSalir);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
    }
}