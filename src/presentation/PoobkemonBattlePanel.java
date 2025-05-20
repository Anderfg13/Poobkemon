package presentation;

import domain.Coach;
import domain.Poobkemon;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class PoobkemonBattlePanel extends BackgroundPanel {
    private JLabel timerLabel;
    private JLabel pokemonNameLabel;
    private JProgressBar hpBar1, hpBar2;
    private JLabel pokemonGif1, pokemonGif2;
    private JButton pauseButton;
    private JButton fightBtn, itemsBtn, pokemonsBtn, fleeBtn;
    private Color colorJugador1, colorJugador2;
    private int tiempoRestante = 20;
    private Timer timer;
    private boolean turnoJugador1 = true;
    private JPanel buttonsPanel;
    private Poobkemon poobkemon;
    private PoobkemonGUI app;
    private JLabel hpLabel1, hpLabel2;

    // Ejemplo de datos, reemplaza por tu lógica/dominio
    private String nombrePokemon1 = "Raichu";
    private String nombrePokemon2 = "Charizard";
    private int vidaActual1 = 83, vidaMax1 = 100;
    private int vidaActual2 = 60, vidaMax2 = 120;

    // Añadir como atributos de clase en PoobkemonBattlePanel
    private boolean hasMachine;
    private boolean isMachineVsMachine; // Cambiado de isMachineVsMaquina para mantener consistencia

    // Añadir estas declaraciones junto a los otros campos de clase
    private JLabel player1Label, player2Label;

    public PoobkemonBattlePanel(Poobkemon poobkemon, PoobkemonGUI app, Color colorJugador1, Color colorJugador2, boolean jugador1Empieza) {
        super("mult/Fondos/Pokemon_NormalMode2.png");
        this.poobkemon = poobkemon;
        this.app = app;
        this.colorJugador1 = colorJugador1;
        this.colorJugador2 = colorJugador2;
        this.turnoJugador1 = jugador1Empieza;
        
        // Detectar tipo de batalla
        this.hasMachine = app.isBattleWithMachine();
        this.isMachineVsMachine = app.isMachineVsMachine();
        
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
        timerLabel.setForeground(Color.WHITE);
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
        hpBar1.setBounds(80, 15, 200, 18);
        hpBar1.setForeground(new Color(0, 200, 0));
        centerPanel.add(hpBar1);

        // Etiquetas de vida - usar nombres distintos para variables locales
        JLabel hpLabel1Local = new JLabel();
        hpLabel1Local.setBounds(80, 0, 200, 18);
        hpLabel1Local.setHorizontalAlignment(SwingConstants.CENTER);
        hpLabel1Local.setForeground(Color.WHITE);
        centerPanel.add(hpLabel1Local);

        JLabel hpLabel2Local = new JLabel();
        hpLabel2Local.setBounds(480, 20, 200, 18);
        hpLabel2Local.setHorizontalAlignment(SwingConstants.CENTER);
        hpLabel2Local.setForeground(Color.WHITE);
        centerPanel.add(hpLabel2Local);

        this.hpLabel1 = hpLabel1Local;
        this.hpLabel2 = hpLabel2Local;

        // Etiqueta para el nombre del pokémon/entrenador 1
        player1Label = new JLabel();
        player1Label.setBounds(80, -15, 200, 20);
        player1Label.setHorizontalAlignment(SwingConstants.CENTER);
        player1Label.setForeground(Color.WHITE);
        centerPanel.add(player1Label);

        // Etiqueta para el nombre del pokémon/entrenador 2
        player2Label = new JLabel();
        player2Label.setBounds(480, 0, 200, 20);
        player2Label.setHorizontalAlignment(SwingConstants.CENTER);
        player2Label.setForeground(Color.WHITE);
        centerPanel.add(player2Label);

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
        pauseButton.addActionListener(_ -> cambiarTurno());

        // En el constructor, después de crear los botones:
        fightBtn.addActionListener(_ -> {
            List<String> ataques = poobkemon.getActivePokemonMoves(turnoJugador1);
            mostrarPanelAtaques(ataques.toArray(String[]::new)); // Optimizado
        });

        itemsBtn.addActionListener(_ -> {
            boolean esJugador1 = turnoJugador1;
            java.util.List<String> items = poobkemon.getItemsJugador(esJugador1);
            if (items == null || items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tienes ítems.", "Ítems", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarPanelItems(items);
            }
        });

        fleeBtn.addActionListener(_ -> {
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

        pokemonsBtn.addActionListener(_ -> {
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

        // Si es una batalla contra máquina, iniciar el primer turno de máquina si corresponde
        if (hasMachine) {
            if (isMachineVsMachine) {
                // Para batallas máquina vs máquina, configurar el panel para el modo automático
                SwingUtilities.invokeLater(() -> iniciarBatallaMaquinaVsMaquina());
            } else if ((turnoJugador1 && app.isMachinePlayer1()) || 
                       (!turnoJugador1 && !app.isMachinePlayer1())) {
                // Si el turno actual corresponde a una máquina, ejecutarlo automáticamente
                SwingUtilities.invokeLater(() -> ejecutarTurnoMaquina());
            }
        }
        
        actualizarBarrasDeVida();
        // Actualizar los nombres iniciales
        actualizarPokemonActivos();
        // Si el primer turno es de la máquina, ejecuta su turno automáticamente
        if (!turnoJugador1 && app.isBattleWithMachine() && !app.isMachinePlayer1()) {
            SwingUtilities.invokeLater(this::ejecutarTurnoMaquina);
        }
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
            @Override
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
        // Detener temporizador actual
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        // IMPORTANTE: Primero verificar si algún Pokémon necesita ser reemplazado
        // antes de cambiar el turno
        
        // Verificar Pokémon del Jugador 1
        int vidaActualJ1 = poobkemon.getActivePokemonCurrentHP(true);
        if (vidaActualJ1 <= 0 && poobkemon.tienePokemonesVivos(true)) {
            // Necesita cambiar Pokémon - jugador 1
            manejarPokemonDerrotado(true);
            // Si es jugador humano, no continuar automáticamente
            if (!app.isMachinePlayer1() && !isMachineVsMachine) {
                return;
            }
        }
        
        // Verificar Pokémon del Jugador 2
        int vidaActualJ2 = poobkemon.getActivePokemonCurrentHP(false);
        if (vidaActualJ2 <= 0 && poobkemon.tienePokemonesVivos(false)) {
            // Necesita cambiar Pokémon - jugador 2
            manejarPokemonDerrotado(false);
            // Si es jugador humano, no continuar automáticamente
            if (!app.isMachinePlayer2() && !isMachineVsMachine) {
                return;
            }
        }
        
        // Ahora sí, cambiar el turno en el dominio
        poobkemon.changeTurn();
        
        // Actualizar turno en la interfaz
        turnoJugador1 = !turnoJugador1;
        
        // Actualizar interfaz
        actualizarPokemonActivos();
        actualizarColoresBotones();
        actualizarBarrasDeVida();
        
        // Verificar si hay un ganador antes de continuar
        if (verificarFinJuego()) {
            return;
        }
        
        // Determinar si el siguiente turno es de una máquina
        boolean esTurnoMaquina = false;
        
        if (hasMachine && !isMachineVsMachine) {
            // Verificar explícitamente si el entrenador actual es una máquina
            Coach currentCoach = poobkemon.getBattleArena().getCurrentCoach();
            esTurnoMaquina = currentCoach.isMachine();
        }
        
        if (esTurnoMaquina) {
            // Si es turno de la máquina, ejecutarlo automáticamente
            ejecutarTurnoMaquina();
        } else if (isMachineVsMachine) {
            // Código para batalla máquina vs máquina
            iniciarBatallaMaquinaVsMaquina();
        } else {
            // Si es turno humano, mostrar panel de botones y reiniciar temporizador
            mostrarPanelBotones();
            iniciarTemporizador();
            // Asegurar explícitamente que los botones estén habilitados
            habilitarBotones(true);
        }
    }

    /**
     * Ejecuta automáticamente un turno de la máquina
     */
    private void ejecutarTurnoMaquina() {
        // Desactivar botones durante el turno de la máquina
        habilitarBotones(false);
        
        // Mostrar mensaje indicando que la máquina está pensando
        timerLabel.setText("IA");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        // Verificar que es realmente el turno de una máquina
        Coach currentCoach = poobkemon.getBattleArena().getCurrentCoach();
        if (!currentCoach.isMachine()) {
            System.out.println("¡Error! Se solicitó turno de máquina pero el coach actual no es una máquina");
            // En lugar de cambiar el turno, simplemente habilitamos los botones para el jugador humano
            mostrarPanelBotones();
            habilitarBotones(true);
            iniciarTemporizador();
            return;
        }
        
        // Espera visual para simular "pensamiento" de la máquina
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Procesar el turno de la máquina
                        String resultadoAccion = poobkemon.processMachineTurn();
                        
                        // IMPORTANTE: Actualizar la interfaz INMEDIATAMENTE
                        actualizarPokemonActivos();
                        actualizarBarrasDeVida();
                        
                        // Mostrar mensaje con la acción realizada
                        JOptionPane.showMessageDialog(
                            PoobkemonBattlePanel.this,
                            resultadoAccion,
                            "Turno de la máquina",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        // Verificar si hay un ganador
                        if (verificarFinJuego()) {
                            return; // No cambiar turno si ya terminó
                        }
                        
                        // Verificar si el Pokémon del oponente quedó debilitado
                        boolean jugadorHumano = app.isMachinePlayer1() ? false : true;
                        int psOponente = poobkemon.getActivePokemonCurrentHP(jugadorHumano);
                        if (psOponente <= 0 && poobkemon.tienePokemonesVivos(jugadorHumano)) {
                            manejarPokemonDerrotado(jugadorHumano);
                        }
                        
                        // Cambio de turno (SOLO AQUÍ - eliminar el cambio de turno en cambiarTurno para máquinas)
                        poobkemon.changeTurn();
                        turnoJugador1 = !turnoJugador1;
                        actualizarPokemonActivos();
                        actualizarColoresBotones();
                        actualizarBarrasDeVida();
                        
                        // IMPORTANTE: Verificar si estamos en modo máquina vs máquina
                        if (isMachineVsMachine) {
                            // Si es máquina vs máquina, continuar con el siguiente turno automáticamente
                            // después de una pequeña pausa para que se vea el resultado
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    SwingUtilities.invokeLater(() -> ejecutarTurnoMaquina());
                                }
                            }, 1000); // Esperar 1 segundo entre turnos
                        } else {
                            // Como ahora es turno del humano, mostramos los botones y activamos el temporizador
                            mostrarPanelBotones();
                            habilitarBotones(true);
                            iniciarTemporizador();
                        }
                        
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                            PoobkemonBattlePanel.this,
                            "Error en el turno de la máquina: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error en el turno de la máquina", e);
                        
                        // En caso de error en modo máquina vs máquina, intentar continuar
                        if (isMachineVsMachine) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    SwingUtilities.invokeLater(() -> ejecutarTurnoMaquina());
                                }
                            }, 1000);
                        } else {
                            // En caso de error, habilitar los botones para el usuario humano
                            mostrarPanelBotones();
                            habilitarBotones(true);
                            iniciarTemporizador();
                        }
                    }
                });
            }
        }, 1500); // Esperar 1.5 segundos para simular "pensamiento"
    }

    /**
     * Inicia una batalla automática entre dos máquinas
     */
    private void iniciarBatallaMaquinaVsMaquina() {
        // En batalla máquina vs máquina, desactivar botones permanentemente
        habilitarBotones(false);
        
        // Botón para pausar/continuar la simulación
        pauseButton.setText("▶/II");
        pauseButton.setEnabled(true);
        
        // Variable para controlar si la simulación está pausada
        final boolean[] pausado = {false};
        
        // Reconfigurar botón de pausa
        pauseButton.removeActionListener(pauseButton.getActionListeners()[0]);
        pauseButton.addActionListener(_ -> {
            pausado[0] = !pausado[0];
            if (!pausado[0]) {
                // Si se reanuda, continuar con la simulación
                ejecutarTurnoMaquina();
            }
        });
        
        // Actualizar interfaz para mostrar nombres de máquinas
        actualizarPokemonActivos();
        
        // Iniciar la simulación
        ejecutarTurnoMaquina();
    }

    /**
     * Actualiza los Pokémon activos en la interfaz
     */
    private void actualizarPokemonActivos() {
        // Obtener nombres de Pokémon activos actualizados
        nombrePokemon1 = app.getPokemonActivoJugador1();
        nombrePokemon2 = app.getPokemonActivoJugador2();
        
        // Obtener nombres de los entrenadores
        String nombreEntrenador1 = poobkemon.getBattleArena().getCoach(0).getName();
        String nombreEntrenador2 = poobkemon.getBattleArena().getCoach(1).getName();
        
        // Actualizar etiquetas con información del entrenador y Pokémon
        player1Label.setText(nombreEntrenador1 + ": " + nombrePokemon1);
        player2Label.setText(nombreEntrenador2 + ": " + nombrePokemon2);
        
        // Actualizar GIFs
        pokemonGif1.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
        pokemonGif2.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));
    }

    /**
     * Habilita o deshabilita todos los botones de acción
     */
    private void habilitarBotones(boolean habilitar) {
        fightBtn.setEnabled(habilitar);
        itemsBtn.setEnabled(habilitar);
        pokemonsBtn.setEnabled(habilitar);
        fleeBtn.setEnabled(habilitar);
    }
    
    /**
     * Verifica si la batalla ha terminado
     * @return true si el juego ha terminado
     */
    private boolean verificarFinJuego() {
        boolean j1TienePokemon = poobkemon.tienePokemonesVivos(true);
        boolean j2TienePokemon = poobkemon.tienePokemonesVivos(false);
        
        if (!j1TienePokemon || !j2TienePokemon) {
            String ganador;
            if (!j1TienePokemon) {
                ganador = app.getNombreJugador2();
            } else {
                ganador = app.getNombreJugador1();
            }
            
            JOptionPane.showMessageDialog(
                this,
                "¡" + ganador + " ha ganado la partida!",
                "Fin de la partida",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Detener temporizadores
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            
            // Finalizar batalla
            app.pausarMusicaBatalla();
            app.reanudarMusicaGlobal();
            app.mostrarMenuPrincipal();
            return true;
        }
        return false;
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

    private void mostrarPanelAtaques(String[] ataques) {
        // Verificar si el array de ataques está vacío
        if (ataques == null || ataques.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Este Pokémon no tiene ataques disponibles.", 
                "Sin ataques", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        buttonsPanel.removeAll();
        buttonsPanel.setLayout(new GridLayout(2, 2, 8, 8));
        Color color = turnoJugador1 ? colorJugador1 : colorJugador2;
        
        for (int i = 0; i < Math.min(4, ataques.length); i++) {
            String nombreAtaque = ataques[i];
            JButton ataqueBtn = new JButton(nombreAtaque);

            try {
                int ppActual = poobkemon.getPPDeAtaqueActual(turnoJugador1, nombreAtaque);
                ataqueBtn.setToolTipText("PP: " + ppActual);
            } catch (Exception e) {
                // Si no se pueden obtener los PP, no mostrar tooltip
            }

            ataqueBtn.setFont(new Font("Times New Roman", Font.BOLD, 18));
            ataqueBtn.setBackground(color);
            ataqueBtn.setForeground(Color.WHITE);

            ataqueBtn.addActionListener(_ -> {
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

                    // Verificar si el oponente perdió todos sus pokémon
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
                        app.pausarMusicaBatalla();
                        app.reanudarMusicaGlobal();
                        app.mostrarMenuPrincipal();
                        return;
                    }

                    // Verificar si el pokémon del oponente murió pero aún tiene otros vivos
                    int psOponente = poobkemon.getActivePokemonCurrentHP(!turnoJugador1);
                    if (psOponente <= 0 && oponenteTieneVivos) {
                        manejarPokemonDerrotado(!turnoJugador1);
                    }

                    cambiarTurno();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            buttonsPanel.add(ataqueBtn);
        }
        
        // Si hay menos de 4 ataques, rellenar con botones deshabilitados
        for (int i = ataques.length; i < 4; i++) {
            JButton emptyBtn = new JButton("");
            emptyBtn.setEnabled(false);
            buttonsPanel.add(emptyBtn);
        }
        
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    /**
     * Maneja el cambio de Pokémon cuando uno es derrotado
     * @param jugador true para jugador 1, false para jugador 2
     */
    private void manejarPokemonDerrotado(boolean jugador) {
        // Comprobar si el jugador es una máquina
        boolean esMaquina = isMachineVsMachine || 
                           (jugador && app.isMachinePlayer1()) || 
                           (!jugador && app.isMachinePlayer2());
        
        if (esMaquina) {
            // Si es una máquina, procesar automáticamente el cambio
            try {
                String resultado = poobkemon.processMachineTurn();
                JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Cambio de Pokémon",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Actualizar interfaz después del cambio
                nombrePokemon1 = app.getPokemonActivoJugador1();
                nombrePokemon2 = app.getPokemonActivoJugador2();
                pokemonGif1.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon1 + ".gif"));
                pokemonGif2.setIcon(new ImageIcon("mult/gifs/" + nombrePokemon2 + ".gif"));
                actualizarBarrasDeVida();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al cambiar Pokémon de la máquina: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            // Código existente para jugadores humanos - sin cambios
            java.util.List<String> pokemonsVivos = poobkemon.getPokemonsVivos(jugador);

            // Crear panel con botones con imagen y nombre
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

            // Seleccionar el primer botón por defecto
            if (!botones.isEmpty()) {
                botones.get(0).setSelected(true);
            }

            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "El pokémon actual ha sido derrotado. Selecciona otro:",
                JOptionPane.OK_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                for (JRadioButton btn : botones) {
                    if (btn.isSelected()) {
                        String nuevoPokemon = btn.getText();
                        poobkemon.cambiarPokemonActivo(jugador, nuevoPokemon);
                        
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

            itemBtn.addActionListener(_ -> {
                try {
                    if (item.equalsIgnoreCase("Revivir")) {
                        mostrarDialogoRevivir();
                    } else {
                        // Ítems curativos: solo si el pokémon activo no tiene la vida completa
                        int vidaActual = poobkemon.getActivePokemonCurrentHP(turnoJugador1);
                        int vidaMax = poobkemon.getActivePokemonMaxHP(turnoJugador1);
                        if (vidaActual == vidaMax) {
                            JOptionPane.showMessageDialog(this, "El pokémon ya tiene la vida completa.", 
                                "Ítem no permitido", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        poobkemon.useItem(item, turnoJugador1);
                        JOptionPane.showMessageDialog(this, "Has usado el ítem: " + item, 
                            "Ítem usado", JOptionPane.INFORMATION_MESSAGE);
                        actualizarBarrasDeVida();
                        mostrarPanelBotones();
                        cambiarTurno();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        buttonsPanel.add(itemBtn);
        }
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void mostrarDialogoRevivir() {
        // Obtener pokémon muertos del jugador actual
        java.util.List<String> pokemonsMuertos = poobkemon.getPokemonsMuertos(turnoJugador1);
        if (pokemonsMuertos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes pokémon muertos para revivir.", 
                "Revivir", JOptionPane.INFORMATION_MESSAGE);
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
        
        // Seleccionar el primer pokémon por defecto
        if (!botones.isEmpty()) {
            botones.get(0).setSelected(true);
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
                    try {
                        poobkemon.revivirPokemon(turnoJugador1, pokemonARevivir);
                        poobkemon.eliminarItem(turnoJugador1, "Revivir");
                        JOptionPane.showMessageDialog(this, "Has revivido a " + pokemonARevivir, 
                            "Ítem usado", JOptionPane.INFORMATION_MESSAGE);
                        actualizarBarrasDeVida();
                        mostrarPanelBotones();
                        cambiarTurno();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, 
                            "Error al revivir: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        }
    }
}