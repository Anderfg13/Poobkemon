package presentation;

import javax.swing.*;
import java.awt.*;

public class ScalableGifPanel extends JPanel {
    private ImageIcon gifIcon;

    public ScalableGifPanel(String path) {
        gifIcon = new ImageIcon(path);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = gifIcon.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}