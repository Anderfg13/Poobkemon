package presentation;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import domain.Poobkemon;

/**
 * PoobkemonMenu es el panel principal del menú de Poobkemon.
 * Permite al usuario acceder a las opciones de nueva partida, cargar partida, instrucciones y regresar al inicio.
 * Gestiona la disposición visual de los botones y el fondo escalable, integrando la navegación con la interfaz principal.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Muestra un fondo escalable personalizado para el menú.</li>
 *   <li>Incluye botones estilizados para iniciar nueva partida, cargar partida, ver instrucciones y regresar.</li>
 *   <li>Gestiona la navegación entre pantallas mediante acciones sobre los botones.</li>
 *   <li>Permite personalizar el tamaño y disposición de los botones en el panel.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PoobkemonMenu extends JPanel {

    /**
     * Constructor del panel de menú principal.
     * Inicializa el fondo, los botones y la lógica de navegación entre pantallas.
     *
     * @param app Referencia a la interfaz gráfica principal de Poobkemon.
     * @param musica Instancia de la clase Musica para controlar la música de fondo.
     */
    public PoobkemonMenu(PoobkemonGUI app, Musica musica) {
        setLayout(new BorderLayout());

        // Fondo escalable
        ScalableImagePanel background = new ScalableImagePanel("mult/Fondos/Pokemon_Inicio.png");
        background.setLayout(new GridBagLayout());

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 18));
        buttonPanel.setOpaque(false);

        JButton newGameButton = new GradientButton("New Game");
        JButton oldGameButton = new GradientButton("Old Game");
        JButton optionsButton = new GradientButton("Instructions");
        JButton backButton = new GradientButton("Back");

        newGameButton.setPreferredSize(new Dimension(220, 36));
        oldGameButton.setPreferredSize(new Dimension(220, 36));
        optionsButton.setPreferredSize(new Dimension(220, 36));
        backButton.setPreferredSize(new Dimension(220, 36));

        newGameButton.addActionListener(e -> app.cambiarPantalla("gamemode"));
        backButton.addActionListener(e -> app.cambiarPantalla("inicio"));
        optionsButton.addActionListener(e -> app.cambiarPantallaConPanel(new InstructionsPanel(app), "instructions"));
        oldGameButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    Poobkemon partidaCargada = Poobkemon.open(selectedFile); // Carga la partida
                    app.setPoobkemon(partidaCargada); // Actualiza la instancia en la GUI (debes tener este método)
                    app.setColorJugador1(partidaCargada.getColorJugador1());
                    app.setColorJugador2(partidaCargada.getColorJugador2());
                    JOptionPane.showMessageDialog(this, "Partida cargada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Cambia a la arena de batalla con la partida cargada
                    PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(
                        app.getPoobkemon(), app,
                        app.getColorJugador1(), app.getColorJugador2(), partidaCargada.jugador1Empieza()
                    );
                    app.cambiarPantallaConPanel(battlePanel, "battle");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(newGameButton);
        buttonPanel.add(oldGameButton);
        buttonPanel.add(optionsButton);
        buttonPanel.add(backButton);

        // Panel para agrupar título y botones alineados a la izquierda
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.anchor = GridBagConstraints.NORTHWEST;
        gbcLeft.insets = new Insets(10, 20, 10, 20);

        gbcLeft.gridy = 1;
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.insets = new Insets(40, 20, 20, 20);
        leftPanel.add(buttonPanel, gbcLeft);

        // Agrega el panel izquierdo al fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(50, -300, 20, 20);
        background.add(leftPanel, gbc);

        add(background, BorderLayout.CENTER);
    }

    /**
     * ScalableImagePanel es un panel personalizado para mostrar una imagen de fondo escalable.
     * Permite adaptar la imagen al tamaño del panel, manteniendo la calidad visual.
     */
    class ScalableImagePanel extends JPanel {
        private Image img;

        /**
         * Constructor del panel de imagen escalable.
         *
         * @param path Ruta de la imagen a mostrar como fondo.
         */
        public ScalableImagePanel(String path) {
            img = new ImageIcon(path).getImage();
        }

        /**
         * Sobrescribe el método paintComponent para dibujar la imagen escalada al tamaño del panel.
         *
         * @param g Objeto Graphics para dibujar la imagen.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}