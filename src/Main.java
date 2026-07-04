import vista.MenuPrincipalFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalFrame ventana = new MenuPrincipalFrame();
            ventana.setVisible(true);
        });
    }
}