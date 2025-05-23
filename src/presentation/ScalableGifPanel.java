package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * ScalableGifPanel es un panel personalizado para mostrar un GIF animado escalable en la interfaz gráfica.
 * Permite adaptar la animación al tamaño del panel, manteniendo la calidad visual y la animación del GIF.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Muestra un GIF animado en cualquier tamaño de panel, escalando la imagen automáticamente.</li>
 *   <li>Permite cambiar el GIF mostrado mediante el constructor.</li>
 *   <li>Utiliza el método paintComponent para dibujar el GIF escalado al tamaño actual del panel.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class ScalableGifPanel extends JPanel {
    private ImageIcon gifIcon;

    /**
     * Constructor del panel escalable de GIF.
     *
     * @param path Ruta del archivo GIF a mostrar.
     */
    public ScalableGifPanel(String path) {
        gifIcon = new ImageIcon(path);
    }

    /**
     * Sobrescribe el método paintComponent para dibujar el GIF escalado al tamaño del panel.
     *
     * @param g Objeto Graphics utilizado para dibujar el GIF.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = gifIcon.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}