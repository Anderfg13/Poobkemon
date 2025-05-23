package presentation;

import domain.Poobkemon;
import java.awt.*;
import javax.swing.*;

/**
 * PoobkemonGUI es la ventana principal de la interfaz gráfica del juego Poobkemon.
 * Gestiona la navegación entre pantallas, la configuración de música, los colores y nombres de los jugadores,
 * así como la integración con el dominio del juego y la gestión de los modos de batalla.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite cambiar entre las diferentes pantallas del juego usando un CardLayout.</li>
 *   <li>Gestiona la música de fondo y de batalla, permitiendo pausar y reanudar según el contexto.</li>
 *   <li>Permite configurar los colores y nombres de los jugadores mediante un cuadro de diálogo personalizado.</li>
 *   <li>Incluye menú de opciones para abrir, guardar, pausar música y salir del juego con confirmación.</li>
 *   <li>Proporciona métodos para consultar y modificar el estado de la batalla y los jugadores.</li>
 *   <li>Integra la lógica para batallas humano vs máquina, máquina vs máquina y humano vs humano.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PoobkemonGUI extends JFrame {
    private CardLayout layout;
    private JPanel mainPanel;
    private Musica musica;
    private JCheckBoxMenuItem musicToggle;
    private Poobkemon poobkemon;

    // Variables globales para los contrincantes
    private Color colorJugador1 = Color.RED;
    private Color colorJugador2 = Color.BLUE;
    private String nombreJugador1 = "Jugador 1";
    private String nombreJugador2 = "Jugador 2";
    private String monedaElegidaJugador1 = "Cara";

    private Musica musicaGlobal;
    private Musica musicaBatalla;

    private boolean battleWithMachine = false;
    private boolean machineVsMachine = false;
    private boolean machineIsPlayer1 = false;

    /**
     * Constructor de la interfaz gráfica principal.
     * Inicializa la música, los paneles, el menú y las variables globales de configuración.
     */
    public PoobkemonGUI() {
        musica = new Musica("mult/musicaIntro.wav");
        musica.reproducir();

        musicaGlobal = new Musica("mult/tu_musica_global.wav");
        musicaBatalla = new Musica("mult/Pokemon_BattleAudio.wav");
        musicaGlobal.reproducir();

        poobkemon = new Poobkemon();

        // --- MENÚ ---
        JMenuBar menuBar = new JMenuBar();

        // Menú Vista (nuevo, a la izquierda)
        JMenu vistaMenu = new JMenu("Vista");
        JMenuItem coloresItem = new JMenuItem("Colores");
        coloresItem.addActionListener(e -> {
            ConfiguracionJugadoresDialog dialog = new ConfiguracionJugadoresDialog(this, colorJugador1, colorJugador2);
            dialog.setVisible(true);
            // Si el usuario aceptó:
            if (dialog.nombre1 != null && dialog.nombre2 != null && dialog.monedaElegida != null) {
                colorJugador1 = dialog.color1;
                colorJugador2 = dialog.color2;
                nombreJugador1 = dialog.nombre1;
                nombreJugador2 = dialog.nombre2;
                monedaElegidaJugador1 = dialog.monedaElegida;
                // Ahora puedes usar estas variables en cualquier parte de la clase
                System.out.println("Jugador 1: " + nombreJugador1 + " Color: " + colorJugador1);
                System.out.println("Jugador 2: " + nombreJugador2 + " Color: " + colorJugador2);
                System.out.println("Moneda: " + monedaElegidaJugador1);
            }
        });
        vistaMenu.add(coloresItem);
        menuBar.add(vistaMenu); // <-- Se agrega primero para que quede a la izquierda

        JMenu optionsMenu = new JMenu("Options");

        // Abrir
        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Abrir no implementada."));

        // Guardar
        JMenuItem saveItem = new JMenuItem("Guardar");
        saveItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Guardar no implementada."));

        // Pausar/Reanudar música
        musicToggle = new JCheckBoxMenuItem("Pausar música");
        musicToggle.setSelected(musica.estaEncendida());
        musicToggle.addActionListener(e -> {
            if (musicToggle.isSelected()) {
                musica.reproducir();
                musicToggle.setText("Pausar música");
            } else {
                musica.pausar();
                musicToggle.setText("Reanudar música");
            }
        });

        // Salir con confirmación
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres salir del juego?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        optionsMenu.add(openItem);
        optionsMenu.add(saveItem);
        optionsMenu.addSeparator();
        optionsMenu.add(musicToggle);
        optionsMenu.addSeparator();
        optionsMenu.add(exitItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        // --- FIN MENÚ ---

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        mainPanel.add(new PoobkemonInicio(this, musica), "inicio");
        mainPanel.add(new PoobkemonMenu(this, musica), "menu");
        mainPanel.add(new PoobkemonGameMode(this, musica), "gamemode");
        mainPanel.add(new PoobkemonSeleccionPanel(this, nombreJugador1, nombreJugador2), "selection");

        setContentPane(mainPanel);
        setTitle("Poobkemon Garcia-Romero");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                    PoobkemonGUI.this,
                    "¿Seguro que quieres salir del juego?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setSize(720, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Cambia la pantalla visible en el CardLayout al panel identificado por el nombre dado.
     * @param nombre Nombre del panel a mostrar.
     */
    public void cambiarPantalla(String nombre) {
        layout.show(mainPanel, nombre);
    }

    /**
     * Cambia la pantalla visible en el CardLayout agregando un nuevo panel con el nombre dado.
     * @param nuevoPanel Panel a agregar y mostrar.
     * @param nombre Nombre identificador del panel.
     */
    public void cambiarPantallaConPanel(JPanel nuevoPanel, String nombre) {
        mainPanel.add(nuevoPanel, nombre);
        layout.show(mainPanel, nombre);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Actualiza el panel de selección de Pokémon con los nombres actuales de los jugadores.
     * Elimina el panel anterior y agrega uno nuevo actualizado.
     */
    public void actualizarSeleccionPanel() {
        // Elimina el panel anterior si existe
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof PoobkemonSeleccionPanel) {
                mainPanel.remove(comp);
                break;
            }
        }
        // Agrega el panel actualizado con los nombres actuales
        mainPanel.add(new PoobkemonSeleccionPanel(this, nombreJugador1, nombreJugador2), "selection");
    }

    /**
     * Obtiene el nombre del jugador humano (jugador 1).
     * @return Nombre del jugador humano.
     */
    public String getNombreJugadorHumano() {
        return nombreJugador1;
    }

    /**
     * Obtiene la instancia principal del dominio Poobkemon.
     * @return Instancia de Poobkemon.
     */
    public Poobkemon getPoobkemonDominio() {
        return poobkemon;
    }

    /**
     * Obtiene el color configurado para el jugador 1.
     * @return Color del jugador 1.
     */
    public Color getColorJugador1() {
        return colorJugador1;
    }

    /**
     * Obtiene el color configurado para el jugador 2.
     * @return Color del jugador 2.
     */
    public Color getColorJugador2() {
        return colorJugador2;
    }

    /**
     * Obtiene el nombre del Pokémon activo del jugador 1.
     * @return Nombre del Pokémon activo del jugador 1.
     */
    public String getPokemonActivoJugador1() {
        // Suponiendo que el dominio tiene un método así:
        // return poobkemonDominio.getActivePokemonName(true);
        // O si tienes acceso directo al entrenador:
        // return poobkemonDominio.getBattleArena().getCoach(0).getActivePokemon().getName();
        // Ajusta según tu estructura real:
        return poobkemon.getActivePokemonName(true);
    }

    /**
     * Obtiene el nombre del Pokémon activo del jugador 2.
     * @return Nombre del Pokémon activo del jugador 2.
     */
    public String getPokemonActivoJugador2() {
        // Igual que arriba, pero para el segundo jugador
        return poobkemon.getActivePokemonName(false);
    }

    /**
     * Obtiene el nombre del jugador 1.
     * @return Nombre del jugador 1.
     */
    public String getNombreJugador1() {
        return nombreJugador1;
    }

    /**
     * Obtiene el nombre del jugador 2.
     * @return Nombre del jugador 2.
     */
    public String getNombreJugador2() {
        return nombreJugador2;
    }

    /**
     * Muestra el menú principal del juego.
     */
    public void mostrarMenuPrincipal() {
        cambiarPantalla("menu");
    }

    /**
     * Pausa la música global de fondo.
     */
    public void pausarMusicaGlobal() {
        musicaGlobal.pausar();
    }

    /**
     * Reanuda la música global de fondo.
     */
    public void reanudarMusicaGlobal() {
        musicaGlobal.reproducir();
    }

    /**
     * Reproduce la música de batalla.
     */
    public void reproducirMusicaBatalla() {
        musicaBatalla.reproducir();
    }

    /**
     * Pausa la música de batalla.
     */
    public void pausarMusicaBatalla() {
        musicaBatalla.pausar();
    }

    /**
     * Obtiene la instancia principal de Poobkemon.
     * @return Instancia de Poobkemon.
     */
    public Poobkemon getPoobkemon() {
        return poobkemon;
    }

    /**
     * Indica si la batalla es contra una máquina.
     * @return true si la batalla es humano vs máquina.
     */
    public boolean isBattleWithMachine() {
        return battleWithMachine;
    }

    /**
     * Indica si la batalla es máquina vs máquina.
     * @return true si ambos jugadores son máquinas.
     */
    public boolean isMachineVsMachine() {
        return machineVsMachine;
    }

    /**
     * Indica si el jugador 1 es una máquina.
     * @return true si el jugador 1 es máquina.
     */
    public boolean isMachinePlayer1() {
        return machineIsPlayer1;
    }

    /**
     * Indica si el jugador 2 es una máquina.
     * @return true si el jugador 2 es máquina.
     */
    public boolean isMachinePlayer2() {
        return battleWithMachine && !machineIsPlayer1 && !machineVsMachine;
    }

    // Estos métodos deben llamarse al configurar la batalla
    /**
     * Configura la batalla para modo humano vs máquina (jugador 2 es máquina).
     */
    public void configureBattleHumanVsMachine() {
        battleWithMachine = true;
        machineVsMachine = false;
        machineIsPlayer1 = false;
    }

    /**
     * Configura la batalla para modo máquina vs humano (jugador 1 es máquina).
     */
    public void configureBattleMachineVsHuman() {
        battleWithMachine = true;
        machineVsMachine = false;
        machineIsPlayer1 = true;
    }

    /**
     * Configura la batalla para modo máquina vs máquina.
     */
    public void configureBattleMachineVsMachine() {
        battleWithMachine = true;
        machineVsMachine = true;
    }

    /**
     * Obtiene la moneda elegida por el jugador 1 para el sorteo inicial.
     * @return "Cara" o "Sello" según la selección.
     */
    public String getMonedaElegidaJugador1() {
        return monedaElegidaJugador1;
    }

    /**
     * Método principal para iniciar la aplicación Poobkemon.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PoobkemonGUI::new);
        
    }
}