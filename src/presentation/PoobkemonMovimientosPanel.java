package presentation;

import domain.Poobkemon;
import domain.PoobkemonException;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class PoobkemonMovimientosPanel extends BackgroundPanel {
    private final String nombreJugador1;
    private final String nombreJugador2;
    private final List<String> pokemones1;
    private final List<String> pokemones2;
    private final List<String> items1;
    private final List<String> items2;
    private final Map<String, List<String>> movimientosSeleccionados1 = new HashMap<>();
    private final Map<String, List<String>> movimientosSeleccionados2 = new HashMap<>();
    private final PoobkemonGUI app;
    private final JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 20));

    private final boolean vsMachine;
    private final String machineType;

    /**
     * Constructor para batallas normales (humano vs humano)
     */
    public PoobkemonMovimientosPanel(PoobkemonGUI app, String nombreJugador1, String nombreJugador2,
                                     List<String> pokemones1, List<String> pokemones2,
                                     List<String> items1, List<String> items2) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg");
        this.app = app;
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.pokemones1 = pokemones1;
        this.pokemones2 = pokemones2;
        this.items1 = items1;
        this.items2 = items2;
        this.vsMachine = false;
        this.machineType = "";
        
        initUI();
    }

    /**
     * Constructor para batallas contra máquina
     */
    public PoobkemonMovimientosPanel(PoobkemonGUI app, String nombreJugador1, String nombreJugador2,
                                    List<String> pokemones1, List<String> pokemones2,
                                    List<String> items1, List<String> items2,
                                    String machineType, boolean vsMachine) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg");
        this.app = app;
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.pokemones1 = pokemones1;
        this.pokemones2 = pokemones2;
        this.items1 = items1;
        this.items2 = items2;
        this.vsMachine = vsMachine;
        this.machineType = machineType;
        
        initUI();
    }

    /**
     * Inicializa la interfaz de usuario
     */
    private void initUI() {
        this.centerPanel.setOpaque(false);

        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Selecciona los movimientos", SwingConstants.CENTER);
        titulo.setFont(new Font("Times New Roman", Font.BOLD, 28));
        titulo.setForeground(Color.BLACK);
        add(titulo, BorderLayout.NORTH);

        // Panel central con los pokemones de ambos jugadores
        actualizarFilasPokemones();
        add(centerPanel, BorderLayout.CENTER);

        // Botón finalizar y volver
        JButton finalizar = new GradientButton("Finalizar");
        finalizar.setFont(new Font("Times New Roman", Font.BOLD, 20));

        JButton volver = new GradientButton("Volver");
        volver.setFont(new Font("Times New Roman", Font.BOLD, 20));
        volver.addActionListener(e -> {
            // Regresa a la pantalla anterior
            app.cambiarPantalla("selection");
        });

        finalizar.addActionListener(e -> {
            if (vsMachine) {
                iniciarBatalla();
            } else {
                try {
                    // 1. Convertir los movimientos seleccionados a String[][].
                    String[][] pokemAttacks1 = getMovesMatrix();
                    String[][] pokemAttacks2 = new String[pokemones2.size()][4];
                    for (int i = 0; i < pokemones2.size(); i++) {
                        String poke = pokemones2.get(i);
                        List<String> movs = movimientosSeleccionados2.getOrDefault(poke, new ArrayList<>());
                        for (int j = 0; j < 4; j++) {
                            pokemAttacks2[i][j] = (j < movs.size()) ? movs.get(j) : "";
                        }
                    }

                    // 2. Llamar al dominio
                    Poobkemon poobkemon = app.getPoobkemonDominio();
                    poobkemon.startBattleNormal(
                        nombreJugador1,
                        nombreJugador2,
                        new ArrayList<>(pokemones1),
                        new ArrayList<>(pokemones2),
                        new ArrayList<>(items1),
                        new ArrayList<>(items2),
                        pokemAttacks1,
                        pokemAttacks2
                    );

                    // 3. Lanzamiento de moneda antes de la batalla
                    boolean jugador1Empieza = poobkemon.whoStarts();
                    String resultado = jugador1Empieza ? nombreJugador1 : nombreJugador2;

                    // Usa el gif de la moneda y escálalo a un tamaño más pequeño
                    ImageIcon coinGif = new ImageIcon("mult/gifs/gifCoin.gif");
                    Image img = coinGif.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
                    ImageIcon smallCoinGif = new ImageIcon(img);

                    JLabel label = new JLabel("¡" + resultado + " inicia la batalla!", smallCoinGif, JLabel.CENTER);
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                    label.setFont(new Font("Arial", Font.BOLD, 18));
                    label.setHorizontalAlignment(SwingConstants.CENTER);

                    JOptionPane.showMessageDialog(this, label, "Lanzamiento de moneda", JOptionPane.INFORMATION_MESSAGE);

                    // 4. Redirigir a la pantalla de batalla
                    PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(poobkemon, app, app.getColorJugador1(), app.getColorJugador2(), jugador1Empieza);
                    app.cambiarPantallaConPanel(battlePanel, "battle");

                } catch (PoobkemonException ex) {
                    JOptionPane.showMessageDialog(this, "Error al iniciar la batalla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(volver);
        bottomPanel.add(finalizar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel crearFilaPokemones(String nombreJugador, List<String> pokemones, Map<String, List<String>> movimientosSeleccionados, boolean esJugador1) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);

        JLabel nombre = new JLabel(nombreJugador, SwingConstants.CENTER);
        nombre.setFont(new Font("Times New Roman", Font.BOLD, 22));
        nombre.setForeground(esJugador1 ? new Color(30, 90, 180) : new Color(180, 90, 30));
        fila.add(nombre, BorderLayout.NORTH);

        JPanel pokesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        pokesPanel.setOpaque(false);

        for (String poke : pokemones) {
            JButton pokeBtn = new JButton(new ImageIcon(new ImageIcon("mult/PokemonSprites/" + poke + ".png").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
            pokeBtn.setToolTipText(poke);

            // Visual feedback: color si ya tiene 4 movimientos
            if (movimientosSeleccionados.getOrDefault(poke, new ArrayList<>()).size() == 4) {
                pokeBtn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
            } else {
                pokeBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }

            pokeBtn.addActionListener(e -> {
                SeleccionarMovimientosDialog dialog = new SeleccionarMovimientosDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    nombreJugador, poke
                );
                dialog.setVisible(true);
                List<String> movimientos = dialog.getMovimientosSeleccionados();
                if (movimientos.size() == 4) {
                    movimientosSeleccionados.put(poke, movimientos);
                }
                // Refresca toda la fila para actualizar el borde
                actualizarFilasPokemones();
            });

            pokesPanel.add(pokeBtn);
        }
        fila.add(pokesPanel, BorderLayout.CENTER);
        return fila;
    }

    private void actualizarFilasPokemones() {
        centerPanel.removeAll();
        centerPanel.add(crearFilaPokemones(nombreJugador1, pokemones1, movimientosSeleccionados1, true));
        centerPanel.add(crearFilaPokemones(nombreJugador2, pokemones2, movimientosSeleccionados2, false));
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /**
     * Muestra un diálogo para seleccionar el tipo de máquina
     * @return El tipo de máquina seleccionado
     */
    private String seleccionarTipoMaquina(String titulo) {
        String[] tiposMaquina = {"Attacking", "Defensive", "Changing", "Expert"};
        
        // Crear descripciones para cada tipo
        Map<String, String> descripciones = new HashMap<>();
        descripciones.put("Attacking", "Prioriza ataques potentes y estadísticas ofensivas");
        descripciones.put("Defensive", "Enfocada en resistencia y recuperación");
        descripciones.put("Changing", "Cambia estrategias y Pokémon según la situación");
        descripciones.put("Expert", "Combina todas las estrategias de forma inteligente");
        
        // Crear panel con botones de radio
        JPanel panel = new JPanel(new GridLayout(0, 1));
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[tiposMaquina.length];
        
        for (int i = 0; i < tiposMaquina.length; i++) {
            String tipo = tiposMaquina[i];
            buttons[i] = new JRadioButton("<html><b>" + tipo + "</b>: " + descripciones.get(tipo) + "</html>");
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }
        
        // Seleccionar el primero por defecto
        buttons[0].setSelected(true);
        
        // Mostrar diálogo
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            titulo,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    return tiposMaquina[i];
                }
            }
        }
        
        // Valor por defecto
        return "Attacking";
    }

    /**
     * Obtiene la matriz de movimientos para los pokémon del jugador 1
     * @return Matriz de movimientos [pokémon][movimiento]
     */
    private String[][] getMovesMatrix() {
        String[][] pokemAttacks = new String[pokemones1.size()][4];
        for (int i = 0; i < pokemones1.size(); i++) {
            String poke = pokemones1.get(i);
            List<String> movs = movimientosSeleccionados1.getOrDefault(poke, new ArrayList<>());
            for (int j = 0; j < 4; j++) {
                pokemAttacks[i][j] = (j < movs.size()) ? movs.get(j) : "";
            }
        }
        return pokemAttacks;
    }

    /**
     * Inicia la batalla basándose en el tipo (normal o vs máquina)
     */
    private void iniciarBatalla() {
        try {
            if (vsMachine) {
                // Batalla humano vs máquina
                app.getPoobkemon().startBattleHumanVsMachine(
                    nombreJugador1, nombreJugador2,
                    new ArrayList<>(pokemones1), 
                    new ArrayList<>(pokemones2),
                    new ArrayList<>(items1), 
                    getMovesMatrix(),
                    machineType);
            } else {
                // Batalla humano vs humano (código existente)
                app.getPoobkemon().startBattleNormal(
                    nombreJugador1, nombreJugador2,
                    new ArrayList<>(pokemones1),
                    new ArrayList<>(pokemones2),
                    new ArrayList<>(items1), 
                    new ArrayList<>(items2),
                    getMovesMatrix(),
                    new String[pokemones2.size()][4]
                );
            }
            
            // Determinar quién empieza
            boolean jugador1Empieza = app.getPoobkemon().whoStarts();
            
            // Crear y mostrar el panel de batalla
            PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(
                app.getPoobkemon(), app, 
                app.getColorJugador1(), app.getColorJugador2(),
                jugador1Empieza);
            
            app.cambiarPantallaConPanel(battlePanel, "battle");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al iniciar la batalla: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al iniciar batalla", e);
        }
    }
}
