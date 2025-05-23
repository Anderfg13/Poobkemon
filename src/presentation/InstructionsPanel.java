package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * esta clase muestra una pantalla de instrucciones detalladas para el juego Poobkemon.
 * Incluye un fondo personalizado, un área de texto deslizable con fuente de estilo antiguo,
 * y un botón estilizado para regresar al menú principal.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Fondo escalable con imagen personalizada.</li>
 *   <li>Área de texto con instrucciones claras y detalladas sobre el uso del juego.</li>
 *   <li>Barra de desplazamiento vertical personalizada en color y tamaño.</li>
 *   <li>Botón "Volver" con estilo antiguo y efecto visual al interactuar.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */

public class InstructionsPanel extends JPanel {
    /**
     * Crea un nuevo panel de instrucciones para el juego Poobkemon.
     *
     * @param app Referencia a la instancia principal de PoobkemonGUI para permitir la navegación.
     */
    public InstructionsPanel(PoobkemonGUI app) {
        setLayout(new BorderLayout());

        // Fondo escalable
        JPanel background = new JPanel() {
            private final Image img = new ImageIcon("mult/instrcutions.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new GridBagLayout());

        // Panel central transparente
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());

        // Texto de prueba largo
        String texto = """
        Bienvenido a Poobkemon, entrenador.

        En este mundo, los Poobkemon luchan por la gloria y el honor.
        Elige sabiamente a tus compañeros y prepárate para la batalla.

       INSTRUCCIONES DEL JUEGO

        1. Inicio del Juego:
        - Al abrir el juego, verás el menú principal con opciones para comenzar una nueva partida, cargar una partida antigua o ver las instrucciones.
        - Puedes configurar los nombres y colores de los jugadores desde el menú "Vista > Colores".

        2. Selección de Modo de Juego:
        - Elige entre los modos: Jugador vs Jugador, Jugador vs Máquina o Máquina vs Máquina.
        - Según el modo, selecciona los Poobkemon y los ítems para cada jugador.

        3. Selección de Poobkemon e Ítems:
        - Cada jugador debe seleccionar 4 Poobkemon de la lista disponible.
        - También puedes elegir ítems que te ayudarán durante la batalla (curación, revivir, etc.).

        4. Selección de Movimientos:
        - Para cada Poobkemon, selecciona 4 movimientos de ataque o defensa.
        - Los movimientos determinan las acciones que puedes realizar en combate.

        5. Batalla:
        - El combate se desarrolla por turnos. Cada jugador elige un movimiento, usa un ítem o cambia de Poobkemon.
        - El objetivo es debilitar a todos los Poobkemon del rival.
        - Puedes ver la vida y el estado de tus Poobkemon en pantalla.
        - Usa los ítems estratégicamente para curar o revivir a tus Poobkemon.

        6. Fin del Juego:
        - El juego termina cuando todos los Poobkemon de un jugador han sido debilitados.
        - El ganador será anunciado en pantalla.

        CONSEJOS:
        - Piensa bien tus movimientos y elige sabiamente cuándo atacar, defender o cambiar de Poobkemon.
        - Aprovecha los ítems en los momentos críticos.
        - ¡Diviértete y conviértete en el mejor entrenador Poobkemon!

        ¡Buena suerte, entrenador!
        """;

        JTextArea textArea = new JTextArea(texto);
        // Intenta usar una fuente antigua, si está disponible
        Font fuenteAntigua = new Font("Old English Text MT", Font.PLAIN, 22);
        if (!fuenteAntigua.getFamily().equals("Dialog")) {
            textArea.setFont(fuenteAntigua);
        } else {
            textArea.setFont(new Font("Serif", Font.ITALIC, 22));
        }
        textArea.setForeground(new Color(60, 40, 20));
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 260));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        // Personaliza la barra de desplazamiento
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(160, 120, 60); // Color del "pulgar"
                this.trackColor = new Color(230, 220, 180); // Color de fondo
            }
            @Override
            protected Dimension getMinimumThumbSize() {
                return new Dimension(12, 40); // ancho, alto mínimo del "pulgar"
            }
        });
        verticalBar.setPreferredSize(new Dimension(16, Integer.MAX_VALUE)); // ancho de la barra

        // Botón para volver al menú principal con estilo personalizado
        JButton volverBtn = new JButton("Volver") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(new Color(180, 150, 90));
                } else if (getModel().isRollover()) {
                    g.setColor(new Color(210, 180, 110));
                } else {
                    g.setColor(new Color(200, 170, 100));
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        volverBtn.setFont(new Font("Old English Text MT", Font.BOLD, 18));
        volverBtn.setForeground(new Color(60, 40, 20));
        volverBtn.setOpaque(false);
        volverBtn.setContentAreaFilled(false);
        volverBtn.setBorder(BorderFactory.createLineBorder(new Color(120, 90, 40), 2, true));
        volverBtn.setFocusPainted(false);
        volverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volverBtn.setPreferredSize(new Dimension(100, 20));
        volverBtn.addActionListener(e -> app.mostrarMenuPrincipal());

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(volverBtn, BorderLayout.SOUTH);

        background.add(centerPanel, new GridBagConstraints());
        add(background, BorderLayout.CENTER);
    }
}