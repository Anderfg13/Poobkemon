package presentation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * GradientButton es un botón personalizado para Swing que muestra un fondo con gradiente de color
 * y efectos visuales al pasar el mouse (hover). Permite mejorar la apariencia visual de la interfaz de usuario
 * en Poobkemon, brindando una experiencia más atractiva e interactiva.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Muestra un gradiente vertical de color que cambia al pasar el mouse sobre el botón.</li>
 *   <li>Incluye un borde personalizado y fuente estilizada para el texto.</li>
 *   <li>Reproduce un sonido al hacer clic, integrando la clase {@code Sonidos} del paquete {@code presentation}.</li>
 *   <li>Desactiva el área de contenido estándar y el enfoque visual predeterminado de los botones Swing.</li>
 *   <li>Permite personalizar el texto del botón mediante el constructor.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
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