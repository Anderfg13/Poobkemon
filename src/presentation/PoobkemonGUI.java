package presentation;

import javax.swing.*;
import java.awt.*;
import domain.Poobkemon;

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

    public void cambiarPantalla(String nombre) {
        layout.show(mainPanel, nombre);
    }

    public void cambiarPantallaConPanel(JPanel nuevoPanel, String nombre) {
        mainPanel.add(nuevoPanel, nombre);
        layout.show(mainPanel, nombre);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

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

    public String getNombreJugadorHumano() {
        return nombreJugador1;
    }

    public Poobkemon getPoobkemonDominio() {
        return poobkemon;
    }

    public Color getColorJugador1() {
        return colorJugador1;
    }

    public Color getColorJugador2() {
        return colorJugador2;
    }

    public String getPokemonActivoJugador1() {
        // Suponiendo que el dominio tiene un método así:
        // return poobkemonDominio.getActivePokemonName(true);
        // O si tienes acceso directo al entrenador:
        // return poobkemonDominio.getBattleArena().getCoach(0).getActivePokemon().getName();
        // Ajusta según tu estructura real:
        return poobkemon.getActivePokemonName(true);
    }

    public String getPokemonActivoJugador2() {
        // Igual que arriba, pero para el segundo jugador
        return poobkemon.getActivePokemonName(false);
    }

    public String getNombreJugador1() {
        return nombreJugador1;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public void mostrarMenuPrincipal() {
        cambiarPantalla("menu");
    }

    public void pausarMusicaGlobal() {
        musicaGlobal.pausar();
    }

    public void reanudarMusicaGlobal() {
        musicaGlobal.reproducir();
    }

    public void reproducirMusicaBatalla() {
        musicaBatalla.reproducir();
    }

    public void pausarMusicaBatalla() {
        musicaBatalla.pausar();
    }

    public Poobkemon getPoobkemon() {
        return poobkemon;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PoobkemonGUI::new);
        
    }
}