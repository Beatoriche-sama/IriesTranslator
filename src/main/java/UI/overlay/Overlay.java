package UI.overlay;

import javax.swing.*;
import java.awt.*;

public class Overlay extends JFrame {

    final static Color TRANSPARENT = new Color(0, 0, 0, 0);
    final static Color ALPHA_TRANSPARENT = new Color(0, 0, 0, 1);

    public Overlay() {
        setType(Type.UTILITY);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setBackground(TRANSPARENT);
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setTransparent() {
        setBackground(TRANSPARENT);
    }

    public void setClickable() {
        setBackground(ALPHA_TRANSPARENT);
    }

}
