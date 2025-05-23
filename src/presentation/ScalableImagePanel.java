package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * ScalableImagePanel es un panel personalizado para mostrar una imagen escalable en la interfaz gráfica.
 * Permite adaptar la imagen al tamaño del panel, manteniendo la calidad visual y la proporción de la imagen.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Muestra una imagen en cualquier tamaño de panel, escalando la imagen automáticamente.</li>
 *   <li>Permite cambiar la imagen mostrada mediante el constructor.</li>
 *   <li>Utiliza el método paintComponent para dibujar la imagen escalada al tamaño actual del panel.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class ScalableImagePanel extends JPanel {
    private Image img;

    /**
     * Constructor del panel escalable de imagen.
     *
     * @param path Ruta del archivo de imagen a mostrar.
     */
    public ScalableImagePanel(String path) {
        img = new ImageIcon(path).getImage();
    }

    /**
     * Sobrescribe el método paintComponent para dibujar la imagen escalada al tamaño del panel.
     *
     * @param g Objeto Graphics utilizado para dibujar la imagen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}