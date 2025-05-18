package presentation;

import javax.swing.*;
import java.awt.*;

public class ScalableImagePanel extends JPanel {
    private Image img;

    public ScalableImagePanel(String path) {
        img = new ImageIcon(path).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}