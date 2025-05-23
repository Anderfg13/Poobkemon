package presentation;

import domain.Log;
import domain.Poobkemon;
import domain.PoobkemonException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

/**
 * PoobkemonGameMode es un panel de selección de modo de juego para Poobkemon.
 * Permite al usuario elegir entre los modos Player vs Player, Player vs Machine y Machine vs Machine,
 * así como regresar al menú principal. Gestiona la configuración inicial de la batalla, la selección de equipos,
 * ataques y el lanzamiento de moneda para determinar el turno inicial.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite seleccionar el modo de juego y configura la batalla según la opción elegida.</li>
 *   <li>Gestiona el lanzamiento de moneda para determinar qué jugador inicia en modos competitivos.</li>
 *   <li>Asigna equipos y ataques aleatorios a los jugadores o máquinas según el modo seleccionado.</li>
 *   <li>Integra la transición entre pantallas y paneles de la interfaz gráfica.</li>
 *   <li>Incluye botones personalizados con imágenes y estilos para mejorar la experiencia visual.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PoobkemonGameMode extends JPanel {
    /**
     * Crea el panel de selección de modo de juego, inicializando los botones y la lógica de transición.
     *
     * @param app Referencia a la interfaz gráfica principal de Poobkemon.
     * @param musica Instancia de la clase Musica para controlar la música de fondo.
     */
    public PoobkemonGameMode(PoobkemonGUI app, Musica musica) {
        setLayout(new BorderLayout());

        // Fondo escalable
        ScalableImagePanel background = new ScalableImagePanel("mult/Fondos/Pokemon_GameMode.png");
        background.setLayout(new GridBagLayout());

        // Panel de botones (centrado)
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 18));
        buttonPanel.setOpaque(false);

        // Carga las imágenes
        ImageIcon porigonIcon = new ImageIcon("mult/porygon.png");
        ImageIcon humanIcon = new ImageIcon("mult/human.png");

        int iconSize = 35;
        ImageIcon porigonScaled = new ImageIcon(porigonIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon humanScaled = new ImageIcon(humanIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        // Player vs Player: humano a la izquierda y derecha
        JButton pvpButton = new GradientButton("Player vs Player");
        pvpButton.setHorizontalAlignment(SwingConstants.CENTER);

        pvpButton.addActionListener(e -> {
            String[] opciones = {"Normal", "Supervivencia"};
            int seleccion = JOptionPane.showOptionDialog(
                this,
                "¿Qué modo de juego quieres?",
                "Selecciona modo de juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            if (seleccion == 0) {
                app.actualizarSeleccionPanel();
                app.cambiarPantalla("selection");
            } else if (seleccion == 1) {
                // Lanzamiento de moneda con GIF
                String[] opcionesMoneda = {"Cara", "Sello"};
                int resultadoMoneda = (int) (Math.random() * 2); // 0 o 1
                String quienEmpieza = (resultadoMoneda == 0) ? app.getNombreJugador1() : app.getNombreJugador2();

                // Carga el GIF de la moneda y lo escala a un tamaño menor (por ejemplo, 60x60 píxeles)
                ImageIcon gifCoin = new ImageIcon("mult/gifs/gifCoin.gif");
                Image img = gifCoin.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
                ImageIcon gifCoinSmall = new ImageIcon(img);

                JLabel label = new JLabel(
                    "<html>¡Lanzando la moneda!<br>Resultado: <b>" + opcionesMoneda[resultadoMoneda] +
                    "</b><br>Empieza: <b>" + quienEmpieza + "</b></html>", gifCoinSmall, JLabel.CENTER
                );
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.BOTTOM);

                JOptionPane.showMessageDialog(
                    this,
                    label,
                    "Lanzamiento de moneda",
                    JOptionPane.INFORMATION_MESSAGE
                );
                boolean jugador1Empieza = (resultadoMoneda == 0);

                // 1. Obtén todos los pokémon y ataques disponibles
                ArrayList<String> pokemonesDisponibles = new ArrayList<>(Poobkemon.getAvailablePokemon()); // ArrayList<String>
                ArrayList<String> ataquesDisponibles = Poobkemon.getAvailableAttacks(); // ArrayList<String>

                // --- JUGADOR 1 ---
                Collections.shuffle(pokemonesDisponibles);
                ArrayList<String> pokemonesAleatoriosJ1 = new ArrayList<>(pokemonesDisponibles.subList(0, 6));
                String[][] ataquesPorPokemonJ1 = new String[6][4];
                for (int i = 0; i < 6; i++) {
                    Collections.shuffle(ataquesDisponibles);
                    for (int j = 0; j < 4; j++) {
                        ataquesPorPokemonJ1[i][j] = ataquesDisponibles.get(j);
                    }
                }

                // --- JUGADOR 2 ---
                Collections.shuffle(pokemonesDisponibles);
                ArrayList<String> pokemonesAleatoriosJ2 = new ArrayList<>(pokemonesDisponibles.subList(0, 6));
                String[][] ataquesPorPokemonJ2 = new String[6][4];
                for (int i = 0; i < 6; i++) {
                    Collections.shuffle(ataquesDisponibles);
                    for (int j = 0; j < 4; j++) {
                        ataquesPorPokemonJ2[i][j] = ataquesDisponibles.get(j);
                    }
                }

                // 4. Llama a startBattleNormal en Poobkemon con los nombres de los jugadores y sin items
                String nombreJugador1 = app.getNombreJugador1();
                String nombreJugador2 = app.getNombreJugador2();
                try {
                    app.getPoobkemon().startBattleNormal(
                        nombreJugador1, nombreJugador2,
                        pokemonesAleatoriosJ1, pokemonesAleatoriosJ2,
                        new ArrayList<>(), new ArrayList<>(),
                        ataquesPorPokemonJ1, ataquesPorPokemonJ2,
                        app.getColorJugador1(), app.getColorJugador2()
                    );
                    // Crea el panel de batalla y cámbialo
                    PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(
                        app.getPoobkemon(), app,
                        app.getColorJugador1(), app.getColorJugador2(), jugador1Empieza
                    );
                    app.cambiarPantallaConPanel(battlePanel, "battle");
                } catch (PoobkemonException ex) {
                    Log.record(ex);
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel pvpRow = new JPanel(new BorderLayout());
        pvpRow.setOpaque(false);
        pvpRow.add(new JLabel(humanScaled), BorderLayout.WEST);
        pvpRow.add(pvpButton, BorderLayout.CENTER);
        pvpRow.add(new JLabel(humanScaled), BorderLayout.EAST);

        // Player vs Machine: humano izquierda, porigon derecha
        JButton pvmButton = new GradientButton("Player vs Machine");
        pvmButton.setHorizontalAlignment(SwingConstants.CENTER);

        pvmButton.addActionListener(e -> {
            app.configureBattleHumanVsMachine(); // Configura tipo de batalla

            // Crea el panel de selección para un jugador y lo muestra
            PoobkemonSeleccion1PPanel seleccionPanel = new PoobkemonSeleccion1PPanel(app, app.getNombreJugador1());
            app.cambiarPantallaConPanel(seleccionPanel, "selection1P");
        });

        JPanel pvmRow = new JPanel(new BorderLayout());
        pvmRow.setOpaque(false);
        pvmRow.add(new JLabel(humanScaled), BorderLayout.WEST);
        pvmRow.add(pvmButton, BorderLayout.CENTER);
        pvmRow.add(new JLabel(porigonScaled), BorderLayout.EAST);

        // Machine vs Machine: porigon izquierda y derecha
        JButton mvmButton = new GradientButton("Machine vs Machine");
        mvmButton.setHorizontalAlignment(SwingConstants.CENTER);

        mvmButton.addActionListener(e -> {
            app.configureBattleMachineVsMachine(); // Configura tipo de batalla

            // Usa el mismo método de selección de tipo de máquina que tienes en PoobkemonSeleccionPanel
            String tipoMaquina1 = PoobkemonSeleccionPanel.seleccionarTipoMaquinaStatic(this, "Seleccionar tipo para Máquina 1");
            String tipoMaquina2 = PoobkemonSeleccionPanel.seleccionarTipoMaquinaStatic(this, "Seleccionar tipo para Máquina 2");

            // Genera 6 pokémon aleatorios para cada máquina
            ArrayList<String> pokemonesDisponibles = new ArrayList<>(Poobkemon.getAvailablePokemon());
            Collections.shuffle(pokemonesDisponibles);
            ArrayList<String> pokemonesAleatorios1 = new ArrayList<>(pokemonesDisponibles.subList(0, 6));
            Collections.shuffle(pokemonesDisponibles);
            ArrayList<String> pokemonesAleatorios2 = new ArrayList<>(pokemonesDisponibles.subList(0, 6));

            try {
                app.getPoobkemon().startBattleMachineVsMachine(
                    "CPU " + tipoMaquina1, "CPU " + tipoMaquina2,
                    pokemonesAleatorios1, pokemonesAleatorios2,
                    tipoMaquina1, tipoMaquina2, app.getColorJugador1(), app.getColorJugador2()
                );
                boolean jugador1Empieza = app.getPoobkemon().whoStarts();
                PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(
                    app.getPoobkemon(), app, app.getColorJugador1(), app.getColorJugador2(), jugador1Empieza
                );
                app.cambiarPantallaConPanel(battlePanel, "battle");
            } catch (Exception ex) {
                Log.record(ex);
                JOptionPane.showMessageDialog(this, "Error al iniciar la batalla: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel mvmRow = new JPanel(new BorderLayout());
        mvmRow.setOpaque(false);
        mvmRow.add(new JLabel(porigonScaled), BorderLayout.WEST);
        mvmRow.add(mvmButton, BorderLayout.CENTER);
        mvmRow.add(new JLabel(porigonScaled), BorderLayout.EAST);

        // Back button (solo el botón)
        JButton backButton = new GradientButton("Back");
        backButton.addActionListener(e -> app.cambiarPantalla("menu"));

        Dimension largoBoton = new Dimension(220, 40); // 300 de ancho, 36 de alto
        pvpButton.setPreferredSize(largoBoton);
        pvmButton.setPreferredSize(largoBoton);
        mvmButton.setPreferredSize(largoBoton);
        backButton.setPreferredSize(largoBoton);

        // Agrega los paneles al panel de botones
        buttonPanel.add(pvpRow);
        buttonPanel.add(pvmRow);
        buttonPanel.add(mvmRow);
        buttonPanel.add(backButton);

        // Centrar el panel de botones en el fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        // Márgenes: new Insets(arriba, izquierda, abajo, derecha)
        gbc.insets = new Insets(55, 3, 0, 0); // Ejemplo: 80px arriba, 100px izquierda
        background.add(buttonPanel, gbc);

        add(background, BorderLayout.CENTER);
    }
}