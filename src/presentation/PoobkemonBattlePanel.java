package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import domain.Poobkemon; // Asegúrate de tener acceso a los métodos del dominio
import domain.PoobkemonException;

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

    public PoobkemonBattlePanel(Poobkemon poobkemon, PoobkemonGUI app, Color colorJugador1, Color colorJugador2, boolean jugador1Empieza) {
        super("mult/Fondos/Pokemon_NormalMode2.png");
        this.poobkemon = poobkemon;
        this.app = app;
        this.colorJugador1 = colorJugador1;
        this.colorJugador2 = colorJugador2;
        this.turnoJugador1 = jugador1Empieza; // <-- Aquí se inicializa según la moneda
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        // Pausa la música global y reproduce la de batalla
        app.pausarMusicaGlobal();
        app.reproducirMusicaBatalla();

        // Obtén los nombres de los pokemones activos usando la GUI
        nombrePokemon1 = app.getPokemonActivoJugador1();
        nombrePokemon2 = app.getPokemonActivoJugador2();

        // Temporizador arriba
        timerLabel = new JLabel("20", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
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
        pokemonGif1.setBounds(100, 130, 120, 120);
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
        pauseButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        pauseButton.setFocusPainted(false);
        centerPanel.add(pauseButton);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior (nombre y botones)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Nombre del pokémon con el turno
        pokemonNameLabel = new JLabel(nombrePokemon1, SwingConstants.CENTER);
        pokemonNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        pokemonNameLabel.setPreferredSize(new Dimension(300, 60));
        bottomPanel.add(pokemonNameLabel, BorderLayout.WEST);

        // Panel de botones
        buttonsPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonsPanel.setOpaque(false);
        fightBtn = new JButton("Fight");
        itemsBtn = new JButton("Items");
        pokemonsBtn = new JButton("Pokémons");
        fleeBtn = new JButton("Flee");
        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
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

        itemsBtn.addActionListener(e -> {
            boolean esJugador1 = turnoJugador1;
            java.util.List<String> items = poobkemon.getItemsJugador(esJugador1);
            if (items == null || items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tienes ítems.", "Ítems", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarPanelItems(items);
            }
        });

        fleeBtn.addActionListener(e -> {
            String jugador = turnoJugador1 ? app.getNombreJugador1() : app.getNombreJugador2();
            String rival = turnoJugador1 ? app.getNombreJugador2() : app.getNombreJugador1();
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres abandonar la partida?",
                "Confirmar abandono",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (opcion == JOptionPane.YES_OPTION) {
                // Detiene el temporizador si está activo
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                JOptionPane.showMessageDialog(
                    this,
                    jugador + " ha abandonado la partida.\n" + rival + " gana.",
                    "Partida terminada",
                    JOptionPane.INFORMATION_MESSAGE
                );
                // Ejemplo en el lugar donde termina la batalla
                app.pausarMusicaBatalla();
                app.reanudarMusicaGlobal();
                app.mostrarMenuPrincipal();
            }
        });

        pokemonsBtn.addActionListener(e -> {
            boolean esJugador1 = turnoJugador1;
            String pokemonActual = esJugador1 ? nombrePokemon1 : nombrePokemon2;
            java.util.List<String> pokemonsVivos = poobkemon.getPokemonsVivos(esJugador1);

            // Elimina el pokémon actual de la lista para evitar que lo seleccione de nuevo (opcional)
            pokemonsVivos.remove(pokemonActual);

            if (pokemonsVivos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tienes otros pokémon vivos para cambiar.", "Cambio de Pokémon", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Panel con imágenes y nombres
            JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
            ButtonGroup group = new ButtonGroup();
            java.util.List<JRadioButton> botones = new java.util.ArrayList<>();
            for (String nombre : pokemonsVivos) {
                ImageIcon icon = new ImageIcon("mult/gifs/" + nombre + ".gif");
                JRadioButton btn = new JRadioButton(nombre, icon, false);
                btn.setHorizontalTextPosition(SwingConstants.RIGHT);
                btn.setVerticalTextPosition(SwingConstants.CENTER);
                group.add(btn);
                panel.add(btn);
                botones.add(btn);
            }

            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Selecciona el pokémon al que deseas cambiar:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                for (JRadioButton btn : botones) {
                    if (btn.isSelected()) {
                        String nuevoPokemon = btn.getText();
                        poobkemon.cambiarPokemonActivo(esJugador1, nuevoPokemon);
                        // Actualiza nombres y gifs
                        nombrePokemon1 = app.getPokemonActivoJugador1();
                        nombrePokemon2 = app.getPokemonActivoJugador2();
                        pokemonGif1.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
                        pokemonGif2.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));
                        actualizarBarrasDeVida();
                        mostrarPanelBotones();
                        cambiarTurno(); // Termina el turno tras cambiar de pokémon
                        break;
                    }
                }
            }
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

        // 2. Actualiza el turno en la GUI
        turnoJugador1 = !turnoJugador1;

        // 3. Actualiza colores, pokémon activo, gifs, etc.
        actualizarColoresBotones();

        // 4. Actualiza nombres y gifs
        nombrePokemon1 = app.getPokemonActivoJugador1();
        nombrePokemon2 = app.getPokemonActivoJugador2();
        pokemonGif1.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
        pokemonGif2.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));

        // 5. Vuelve siempre al panel principal de botones
        mostrarPanelBotones();

        // 6. Reinicia el temporizador y actualiza barras de vida
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
        Color color = turnoJugador1 ? colorJugador1 : colorJugador2;
        for (int i = 0; i < 4; i++) {
            String nombreAtaque = ataques[i];
            JButton ataqueBtn = new JButton(nombreAtaque);

            int ppActual = poobkemon.getPPDeAtaqueActual(turnoJugador1, nombreAtaque);
            ataqueBtn.setToolTipText("PP: " + ppActual);

            ataqueBtn.setFont(new Font("Times New Roman", Font.BOLD, 18));
            ataqueBtn.setBackground(color);
            ataqueBtn.setForeground(Color.WHITE);

            ataqueBtn.addActionListener(e -> {
                try {
                    boolean toItself = poobkemon.esAtaqueSobreSiMismo(nombreAtaque);
                    int damage = poobkemon.attack(nombreAtaque, toItself, turnoJugador1);
                    actualizarBarrasDeVida();
                    mostrarPanelBotones();

                    String nombreAtacante = turnoJugador1 ? nombrePokemon1 : nombrePokemon2;
                    if (damage == 0 && !toItself) {
                        JOptionPane.showMessageDialog(this,
                            nombreAtacante + " ha fallado el ataque, daño 0",
                            "Ataque fallido", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            (toItself ? "¡Te has curado o aplicado un efecto!" : "¡Has atacado al rival!") +
                            "\nDaño/efecto: " + damage, 
                            "Resultado del ataque", JOptionPane.INFORMATION_MESSAGE);
                    }

                    // --- Verifica si el oponente perdió ---
                    boolean oponenteTieneVivos = poobkemon.tienePokemonesVivos(!turnoJugador1);
                    if (!oponenteTieneVivos) {
                        String ganador = turnoJugador1 ? app.getNombreJugador1() : app.getNombreJugador2();
                        JOptionPane.showMessageDialog(this,
                            "¡" + ganador + " ha ganado la partida!",
                            "Fin de la partida",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        // Ejemplo en el lugar donde termina la batalla
                        app.pausarMusicaBatalla();
                        app.reanudarMusicaGlobal();
                        app.mostrarMenuPrincipal();
                        return; // No cambiar turno si ya terminó
                    }

                    // ...después de actualizarBarrasDeVida() y mostrarPanelBotones()...

                    // Verifica si el pokémon del oponente murió
                    oponenteTieneVivos = poobkemon.tienePokemonesVivos(!turnoJugador1);
                    int psOponente = poobkemon.getActivePokemonCurrentHP(!turnoJugador1);

                    if (psOponente <= 0 && oponenteTieneVivos) {
                        // Obtener nombres de pokémon vivos del oponente
                        java.util.List<String> pokemonsVivos = poobkemon.getPokemonsVivos(!turnoJugador1);

                        // Crear panel con botones con imagen y nombre
                        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
                        ButtonGroup group = new ButtonGroup();
                        java.util.List<JRadioButton> botones = new java.util.ArrayList<>();
                        for (String nombre : pokemonsVivos) {
                            ImageIcon icon = new ImageIcon("mult/gifs/" + nombre + ".gif"); // Ajusta la ruta si es necesario
                            JRadioButton btn = new JRadioButton(nombre, icon, false);
                            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
                            btn.setVerticalTextPosition(SwingConstants.CENTER);
                            group.add(btn);
                            panel.add(btn);
                            botones.add(btn);
                        }

                        int result = JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "El pokémon actual ha sido derrotado. Selecciona otro:",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                        );

                        if (result == JOptionPane.OK_OPTION) {
                            for (JRadioButton btn : botones) {
                                if (btn.isSelected()) {
                                    String nuevoPokemon = btn.getText();
                                    poobkemon.cambiarPokemonActivo(!turnoJugador1, nuevoPokemon);
                                    // Actualiza nombres y gifs
                                    nombrePokemon1 = app.getPokemonActivoJugador1();
                                    nombrePokemon2 = app.getPokemonActivoJugador2();
                                    pokemonGif1.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
                                    pokemonGif2.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));
                                    actualizarBarrasDeVida();
                                    break;
                                }
                            }
                        }
                    }

                    // --- Verifica si el oponente perdió todos sus pokémon ---
                    if (!oponenteTieneVivos) {
                        String ganador = turnoJugador1 ? app.getNombreJugador1() : app.getNombreJugador2();
                        JOptionPane.showMessageDialog(this,
                            "¡" + ganador + " ha ganado la partida!",
                            "Fin de la partida",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        // Ejemplo en el lugar donde termina la batalla
                        app.pausarMusicaBatalla();
                        app.reanudarMusicaGlobal();
                        app.mostrarMenuPrincipal();
                        return;
                    }

                    cambiarTurno();
                } catch (PoobkemonException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonsPanel.add(ataqueBtn);
        }
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void mostrarPanelItems(List<String> items) {
        buttonsPanel.removeAll();
        int filas = (int) Math.ceil(items.size() / 2.0);
        buttonsPanel.setLayout(new GridLayout(filas, 2, 8, 8));
        Color color = turnoJugador1 ? colorJugador1 : colorJugador2;
        for (String item : items) {
            JButton itemBtn = new JButton(item);

            ImageIcon icon = new ImageIcon("mult/items/" + item + ".png");
            itemBtn.setIcon(icon);
            itemBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
            itemBtn.setVerticalTextPosition(SwingConstants.CENTER);

            itemBtn.setFont(new Font("Times New Roman", Font.BOLD, 18));
            itemBtn.setBackground(color);
            itemBtn.setForeground(Color.WHITE);

            itemBtn.addActionListener(e -> {
                try {
                    if (item.equalsIgnoreCase("Revivir")) {
                        // Obtener pokémon muertos del jugador actual
                        java.util.List<String> pokemonsMuertos = poobkemon.getPokemonsMuertos(turnoJugador1);
                        if (pokemonsMuertos.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No tienes pokémon muertos para revivir.", "Revivir", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        // Panel de selección con imágenes y nombres
                        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
                        ButtonGroup group = new ButtonGroup();
                        java.util.List<JRadioButton> botones = new java.util.ArrayList<>();
                        for (String nombre : pokemonsMuertos) {
                            ImageIcon pokeIcon = new ImageIcon("mult/gifs/" + nombre + ".gif");
                            JRadioButton btn = new JRadioButton(nombre, pokeIcon, false);
                            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
                            btn.setVerticalTextPosition(SwingConstants.CENTER);
                            group.add(btn);
                            panel.add(btn);
                            botones.add(btn);
                        }
                        int result = JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Selecciona el pokémon que deseas revivir:",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                        );
                        if (result == JOptionPane.OK_OPTION) {
                            for (JRadioButton btn : botones) {
                                if (btn.isSelected()) {
                                    String pokemonARevivir = btn.getText();
                                    poobkemon.revivirPokemon(turnoJugador1, pokemonARevivir); // Método en el dominio
                                    poobkemon.eliminarItem(turnoJugador1, item); // Elimina el ítem del inventario
                                    JOptionPane.showMessageDialog(this, "Has revivido a " + pokemonARevivir, "Ítem usado", JOptionPane.INFORMATION_MESSAGE);
                                    actualizarBarrasDeVida();
                                    mostrarPanelBotones();
                                    cambiarTurno();
                                    break;
                                }
                            }
                        }
                    } else {
                        // Ítems curativos: solo si el pokémon activo no tiene la vida completa
                        int vidaActual = poobkemon.getActivePokemonCurrentHP(turnoJugador1);
                        int vidaMax = poobkemon.getActivePokemonMaxHP(turnoJugador1);
                        if (vidaActual == vidaMax) {
                            JOptionPane.showMessageDialog(this, "El pokémon ya tiene la vida completa.", "Ítem no permitido", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        poobkemon.useItem(item, turnoJugador1);
                        JOptionPane.showMessageDialog(this, "Has usado el ítem: " + item, "Ítem usado", JOptionPane.INFORMATION_MESSAGE);
                        actualizarBarrasDeVida();
                        mostrarPanelBotones();
                        cambiarTurno();
                    }
                } catch (PoobkemonException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonsPanel.add(itemBtn);
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