package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import domain.Poobkemon; // Asegúrate de tener acceso a los métodos del dominio

public class PoobkemonBattlePanel extends BackgroundPanel { // Cambia JPanel por BackgroundPanel
    private JLabel timerLabel;
    private JLabel pokemonNameLabel;
    private JProgressBar hpBar1, hpBar2;
    private JLabel pokemonGif1, pokemonGif2;
    private JButton pauseButton;
    private JButton fightBtn, itemsBtn, pokemonsBtn, fleeBtn;
    private Color colorJugador1, colorJugador2;
    private int tiempoRestante = 20;
    private Timer timer;
    private boolean turnoJugador1 = true; // Cambia según tu lógica
    private JPanel buttonsPanel; // Hazlo atributo de la clase para poder reemplazarlo
    private Poobkemon poobkemon;
    private PoobkemonGUI app;
    private JLabel hpLabel1, hpLabel2;

    // Ejemplo de datos, reemplaza por tu lógica/dominio
    private String nombrePokemon1 = "Raichu";
    private String nombrePokemon2 = "Charizard";
    private int vidaActual1 = 83, vidaMax1 = 100;
    private int vidaActual2 = 60, vidaMax2 = 120;

    public PoobkemonBattlePanel(Poobkemon poobkemon, PoobkemonGUI app, Color colorJugador1, Color colorJugador2) {
        super("mult/Fondos/Pokemon_NormalMode2.png");
        this.poobkemon = poobkemon;
        this.app = app;
        this.colorJugador1 = colorJugador1;
        this.colorJugador2 = colorJugador2;
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        // Obtén los nombres de los pokemones activos usando la GUI
        nombrePokemon1 = app.getPokemonActivoJugador1();
        nombrePokemon2 = app.getPokemonActivoJugador2();

        // Temporizador arriba
        timerLabel = new JLabel("20", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        timerLabel.setForeground(Color.WHITE); // <-- Esta línea cambia el color a blanco
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        timerPanel.setBorder(BorderFactory.createEmptyBorder(8, 200, 8, 200));
        add(timerPanel, BorderLayout.NORTH);

        // Panel central (Pokemones, barras y pausa)
        JPanel centerPanel = new JPanel(null);
        centerPanel.setOpaque(false);

        // Barra de vida rival
        hpBar2 = new JProgressBar(0, vidaMax2);
        hpBar2.setValue(vidaActual2);
        hpBar2.setBounds(480, 40, 200, 18);
        hpBar2.setForeground(new Color(0, 200, 0));
        centerPanel.add(hpBar2);

        // Gif rival
        pokemonGif2 = new JLabel(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));
        pokemonGif2.setBounds(500, 70, 120, 120);
        centerPanel.add(pokemonGif2);

        // Gif jugador
        pokemonGif1 = new JLabel(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
        pokemonGif1.setBounds(100, 180, 120, 120);
        centerPanel.add(pokemonGif1);

        // Barra de vida jugador
        hpBar1 = new JProgressBar(0, vidaMax1);
        hpBar1.setValue(vidaActual1);
        hpBar1.setBounds(80, 15, 200, 18);    // Antes: 310
        hpBar1.setForeground(new Color(0, 200, 0));
        centerPanel.add(hpBar1);

        // Después de crear las barras de vida en el constructor:
        JLabel hpLabel1 = new JLabel();
        hpLabel1.setBounds(80, 0, 200, 18);  // Antes: 290
        hpLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        hpLabel1.setForeground(Color.WHITE);
        centerPanel.add(hpLabel1);

        JLabel hpLabel2 = new JLabel();
        hpLabel2.setBounds(480, 20, 200, 18);
        hpLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        hpLabel2.setForeground(Color.WHITE);
        centerPanel.add(hpLabel2);

        this.hpLabel1 = hpLabel1;
        this.hpLabel2 = hpLabel2;

        // Botón de pausa en el centro
        pauseButton = new JButton("II");
        pauseButton.setBounds(370, 210, 40, 40);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        pauseButton.setFocusPainted(false);
        centerPanel.add(pauseButton);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior (nombre y botones)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Nombre del pokémon con el turno
        pokemonNameLabel = new JLabel(nombrePokemon1, SwingConstants.CENTER);
        pokemonNameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        pokemonNameLabel.setPreferredSize(new Dimension(300, 60));
        bottomPanel.add(pokemonNameLabel, BorderLayout.WEST);

        // Panel de botones
        buttonsPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonsPanel.setOpaque(false);
        fightBtn = new JButton("Fight");
        itemsBtn = new JButton("Items");
        pokemonsBtn = new JButton("Pokémons");
        fleeBtn = new JButton("Flee");
        Font btnFont = new Font("Arial", Font.BOLD, 20);
        fightBtn.setFont(btnFont);
        itemsBtn.setFont(btnFont);
        pokemonsBtn.setFont(btnFont);
        fleeBtn.setFont(btnFont);
        mostrarPanelBotones(); // Inicializa con los botones principales
        bottomPanel.add(buttonsPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Inicializa colores de botones según el turno
        actualizarColoresBotones();

        // Temporizador
        iniciarTemporizador();

        // Ejemplo: Cambiar de turno al presionar pausa
        pauseButton.addActionListener(e -> cambiarTurno());

        // En el constructor, después de crear los botones:
        fightBtn.addActionListener(e -> {
            // Suponiendo que tienes una referencia a Poobkemon (el dominio)
            // y una variable turnoJugador1 (true si es el turno del jugador 1)
            List<String> ataques = poobkemon.getActivePokemonMoves(turnoJugador1);
            mostrarPanelAtaques(ataques.toArray(new String[0]));
        });

        actualizarBarrasDeVida();
    }

    private void actualizarColoresBotones() {
        Color color = turnoJugador1 ? colorJugador1 : colorJugador2;
        fightBtn.setBackground(color);
        itemsBtn.setBackground(color);
        pokemonsBtn.setBackground(color);
        fleeBtn.setBackground(color);
        fightBtn.setForeground(Color.WHITE);
        itemsBtn.setForeground(Color.WHITE);
        pokemonsBtn.setForeground(Color.WHITE);
        fleeBtn.setForeground(Color.WHITE);

        // Actualiza el nombre del pokémon con el turno y su fondo
        pokemonNameLabel.setText(turnoJugador1 ? nombrePokemon1 : nombrePokemon2);
        pokemonNameLabel.setOpaque(true);
        pokemonNameLabel.setBackground(color);
        pokemonNameLabel.setForeground(Color.WHITE);
    }

    private void iniciarTemporizador() {
        if (timer != null) timer.cancel();
        tiempoRestante = 20;
        timerLabel.setText(String.valueOf(tiempoRestante));
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    tiempoRestante--;
                    timerLabel.setText(String.valueOf(tiempoRestante));
                    if (tiempoRestante <= 0) {
                        timer.cancel();
                        cambiarTurno(); // Cambia el turno cuando se acaba el tiempo
                    }
                });
            }
        }, 1000, 1000);
    }

    private void cambiarTurno() {
        // Muestra mensaje de tiempo agotado
        String jugador = turnoJugador1 ? app.getNombreJugador1() : app.getNombreJugador2();
        JOptionPane.showMessageDialog(this, 
            "¡Se ha acabado el tiempo para " + jugador + "!", 
            "Tiempo agotado", 
            JOptionPane.WARNING_MESSAGE);

        // 1. Cambia el turno en el dominio
        poobkemon.changeTurn();

        // 2. Actualiza el turno en la GUI (si tienes una variable local, actualízala)
        turnoJugador1 = !turnoJugador1;

        // 3. Actualiza colores, pokémon activo, gifs, etc.
        actualizarColoresBotones();

        // Si tienes métodos para actualizar los gifs y nombres:
        nombrePokemon1 = app.getPokemonActivoJugador1();
        nombrePokemon2 = app.getPokemonActivoJugador2();
        pokemonGif1.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
        pokemonGif2.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));

        // Reinicia el temporizador para el nuevo turno
        iniciarTemporizador();
        actualizarBarrasDeVida();
    }

    private void mostrarPanelBotones() {
        buttonsPanel.removeAll();
        buttonsPanel.setLayout(new GridLayout(2, 2, 8, 8));
        buttonsPanel.add(fightBtn);
        buttonsPanel.add(itemsBtn);
        buttonsPanel.add(pokemonsBtn);
        buttonsPanel.add(fleeBtn);
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void mostrarPanelAtaques(String[] ataques) {
        buttonsPanel.removeAll();
        buttonsPanel.setLayout(new GridLayout(2, 2, 8, 8));
        for (int i = 0; i < 4; i++) {
            String nombreAtaque = ataques[i];
            JButton ataqueBtn = new JButton(nombreAtaque);
            ataqueBtn.setFont(new Font("Arial", Font.BOLD, 18));
            ataqueBtn.addActionListener(e -> {
                // Aquí va la lógica de ataque
                mostrarPanelBotones(); // Vuelve al panel de botones principal
            });
            buttonsPanel.add(ataqueBtn);
        }
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void actualizarBarrasDeVida() {
        int vidaActual1 = poobkemon.getActivePokemonCurrentHP(true);
        int vidaMax1 = poobkemon.getActivePokemonMaxHP(true);
        int vidaActual2 = poobkemon.getActivePokemonCurrentHP(false);
        int vidaMax2 = poobkemon.getActivePokemonMaxHP(false);

        hpBar1.setMaximum(vidaMax1);
        hpBar1.setValue(vidaActual1);
        hpBar2.setMaximum(vidaMax2);
        hpBar2.setValue(vidaActual2);

        hpLabel1.setText(vidaActual1 + " / " + vidaMax1);
        hpLabel2.setText(vidaActual2 + " / " + vidaMax2);
    }

    // Métodos para actualizar vida, gifs, nombres, etc. según el dominio...
}