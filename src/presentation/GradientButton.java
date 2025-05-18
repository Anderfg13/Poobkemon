package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GradientButton extends JButton {
    private boolean hover = false;

    private Color topColor = new Color(56, 142, 100);
    private Color bottomColor = new Color(56, 142, 60);
    private Color topHover = new Color(77, 182, 172);    // Color más claro para hover
    private Color bottomHover = new Color(76, 175, 80);  // Color más claro para hover

    public GradientButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Times New Roman", Font.BOLD, 17));
        setBorder(BorderFactory.createLineBorder(new Color(27, 94, 32), 2));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                presentation.Sonidos.reproducirClick();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int h = getHeight();
        int w = getWidth();
        if (hover) {
            g2.setColor(topHover);
            g2.fillRect(0, 0, w, h / 2);
            g2.setColor(bottomHover);
            g2.fillRect(0, h / 2, w, h - h / 2);
        } else {
            g2.setColor(topColor);
            g2.fillRect(0, 0, w, h / 2);
            g2.setColor(bottomColor);
            g2.fillRect(0, h / 2, w, h - h / 2);
        }
        super.paintComponent(g);
        g2.dispose();
    }
}