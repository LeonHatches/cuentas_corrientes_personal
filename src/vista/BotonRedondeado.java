package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonRedondeado extends JButton {

    private final Color colorNormal;
    private final Color colorHover;
    private Color colorActual;

    public BotonRedondeado(String texto) {
        super(texto);

        this.colorNormal = new Color(218, 239, 211);
        this.colorHover = new Color(195, 225, 185);
        this.colorActual = colorNormal;

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(new Color(25, 70, 45));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(6, 12, 6, 12));

        setPreferredSize(new Dimension(210, 40));
        setMaximumSize(new Dimension(210, 40));
        setMinimumSize(new Dimension(210, 40));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                colorActual = colorHover;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                colorActual = colorNormal;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Forma más parecida al encabezado de la tabla
        int margen = 1;
        int ancho = getWidth() - 3;
        int alto = getHeight() - 3;

        g2.setColor(colorActual);
        g2.fillRoundRect(margen, margen, ancho, alto, 8, 8);

        // Borde suave verde
        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(new Color(140, 180, 145));
        g2.drawRoundRect(margen, margen, ancho, alto, 8, 8);

        g2.dispose();

        super.paintComponent(g);
    }
}
