package presentation;

import domain.Poobkemon;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class PoobkemonSeleccionPanel extends BackgroundPanel {
    private final List<String> pokemones = Poobkemon.getAvailablePokemon();
    private final List<String> items = Poobkemon.getAvailableItems();

    private final Map<Integer, Integer> seleccionPokemones = new HashMap<>();
    private final Map<Integer, Integer> seleccionItems = new HashMap<>();
    private final Map<Integer, Integer> seleccionPokemones1 = new HashMap<>();
    private final Map<Integer, Integer> seleccionItems1 = new HashMap<>();
    private final Map<Integer, Integer> seleccionPokemones2 = new HashMap<>();
    private final Map<Integer, Integer> seleccionItems2 = new HashMap<>();
    private int jugadorActual = 1; // 1 para Player 1, 2 para Player 2

    private int pokeOffset = 0;
    private int itemOffset = 0;
    private final int POKES_POR_PAG = 6;
    private final int ITEMS_POR_PAG = 4;

    protected JPanel pokemonsGrid = new JPanel(new GridLayout(2, 3, 18, 18));
    private final JPanel itemsGrid = new JPanel(new GridLayout(1, 4, 18, 18));
    private final JLabel player1Label;
    protected JLabel player2Label;
    private final PoobkemonGUI app;

    private List<String> pokemones1 = new ArrayList<>();
    private List<String> pokemones2 = new ArrayList<>();
    private List<String> items1 = new ArrayList<>();
    private List<String> items2 = new ArrayList<>();

    public PoobkemonSeleccionPanel(PoobkemonGUI app, String nombreJugador1, String nombreJugador2) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg"); // Llama al constructor del fondo
        this.app = app;
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 5, true));
        setBackground(new Color(245, 245, 255));

        // Usa los nombres recibidos
        player1Label = new JLabel(nombreJugador1, SwingConstants.CENTER);
        player2Label = new JLabel(nombreJugador2, SwingConstants.CENTER);
        player1Label.setFont(new Font("Times New Roman", Font.BOLD, 26));
        player2Label.setFont(new Font("Times New Roman", Font.BOLD, 26));
        player1Label.setForeground(new Color(30, 90, 180));
        player2Label.setForeground(new Color(180, 90, 30));

        // Top: Player labels
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setOpaque(false);
        // Reduce el padding superior e inferior
        topPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 20));
        JButton player1Button = new JButton(nombreJugador1);
        JButton player2Button = new JButton(nombreJugador2);
        player1Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        player2Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        player1Button.setPreferredSize(new Dimension(80, 24));
        player2Button.setPreferredSize(new Dimension(80, 24));
        player1Button.setFocusPainted(false);
        player2Button.setFocusPainted(false);
        player1Button.setBackground(new Color(220, 230, 255));
        player2Button.setBackground(new Color(255, 230, 220));

        player1Button.addActionListener(_ -> {
            jugadorActual = 1;
            refreshPokemons();
            refreshItems();
            player1Button.setBackground(new Color(120, 220, 255)); // activo
            player2Button.setBackground(new Color(255, 230, 220)); // inactivo
        });
        player2Button.addActionListener(e -> {
            jugadorActual = 2;
            refreshPokemons();
            refreshItems();
            player2Button.setBackground(new Color(120, 220, 255)); // activo
            player1Button.setBackground(new Color(220, 230, 255)); // inactivo
        });

        // En el topPanel:
        topPanel.removeAll();
        topPanel.add(player1Button);
        topPanel.add(player2Button);
        add(topPanel, BorderLayout.NORTH);

        // Center: Selección de pokemones e items
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        // Reduce el padding general
        centerPanel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        // Pokemons
        JPanel pokemonsPanel = new JPanel(new BorderLayout());
        pokemonsPanel.setOpaque(false);
        JLabel pokemonsLabel = new JLabel("Pokemons");
        pokemonsLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        pokemonsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        pokemonsPanel.add(pokemonsLabel, BorderLayout.NORTH);

        JButton pokeLeft = new JButton("<");
        JButton pokeRight = new JButton(">");
        styleNavButton(pokeLeft);
        styleNavButton(pokeRight);
        pokeLeft.addActionListener(e -> {
            if (pokeOffset > 0) {
                pokeOffset -= POKES_POR_PAG;
                refreshPokemons();
            }
        });
        pokeRight.addActionListener(e -> {
            if (pokeOffset + POKES_POR_PAG < pokemones.size()) {
                pokeOffset += POKES_POR_PAG;
                refreshPokemons();
            }
        });

        JPanel pokeNavLeft = new JPanel(new BorderLayout());
        pokeNavLeft.setOpaque(false);
        pokeNavLeft.add(pokeLeft, BorderLayout.CENTER);
        JPanel pokeNavRight = new JPanel(new BorderLayout());
        pokeNavRight.setOpaque(false);
        pokeNavRight.add(pokeRight, BorderLayout.CENTER);

        pokemonsPanel.add(pokeNavLeft, BorderLayout.WEST);
        pokemonsPanel.add(pokeNavRight, BorderLayout.EAST);

        pokemonsGrid.setOpaque(false);
        pokemonsPanel.add(pokemonsGrid, BorderLayout.CENTER);

        // Items
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setOpaque(false);
        JLabel itemsLabel = new JLabel("Items");
        itemsLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        itemsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        itemsPanel.add(itemsLabel, BorderLayout.NORTH);

        JButton itemLeft = new JButton("<");
        JButton itemRight = new JButton(">");
        styleNavButton(itemLeft);
        styleNavButton(itemRight);
        itemLeft.addActionListener(e -> {
            if (itemOffset > 0) {
                itemOffset -= ITEMS_POR_PAG;
                refreshItems();
            }
        });
        itemRight.addActionListener(e -> {
            if (itemOffset + ITEMS_POR_PAG < items.size()) {
                itemOffset += ITEMS_POR_PAG;
                refreshItems();
            }
        });

        JPanel itemNavLeft = new JPanel(new BorderLayout());
        itemNavLeft.setOpaque(false);
        itemNavLeft.add(itemLeft, BorderLayout.CENTER);
        JPanel itemNavRight = new JPanel(new BorderLayout());
        itemNavRight.setOpaque(false);
        itemNavRight.add(itemRight, BorderLayout.CENTER);

        itemsPanel.add(itemNavLeft, BorderLayout.WEST);
        itemsPanel.add(itemNavRight, BorderLayout.EAST);

        itemsGrid.setOpaque(false);
        itemsPanel.add(itemsGrid, BorderLayout.CENTER);

        // Añadir a centerPanel
        centerPanel.add(decoratePanel(pokemonsPanel));
        // Reduce el espacio vertical entre pokemones e items
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(decoratePanel(itemsPanel));

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Back, Reiniciar, Start
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        bottomPanel.setOpaque(false);

        JButton backButton = new GradientButton("Back");
        JButton resetButton = new GradientButton("Reiniciar selección");
        JButton nextButton = new GradientButton("Continuar");

        backButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        resetButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        nextButton.setFont(new Font("Times New Roman", Font.BOLD, 20));

        resetButton.addActionListener(e -> {
            if (jugadorActual == 1) {
                seleccionPokemones1.clear();
                seleccionItems1.clear();
            } else {
                seleccionPokemones2.clear();
                seleccionItems2.clear();
            }
            refreshPokemons();
            refreshItems();
        });
        backButton.addActionListener(e -> app.cambiarPantalla("gamemode"));
        nextButton.addActionListener(e -> {
            // Verificar solo los Pokémon de jugadores humanos
            boolean pokemonesSuficientes = true;
            
            if (!app.isMachinePlayer1() && !app.isMachineVsMachine()) {
                pokemonesSuficientes = pokemonesSuficientes && !seleccionPokemones1.isEmpty();
            }
            
            if (!app.isMachinePlayer2() && !app.isMachineVsMachine()) {
                pokemonesSuficientes = pokemonesSuficientes && !seleccionPokemones2.isEmpty();
            }
            
            if (!pokemonesSuficientes) {
                JOptionPane.showMessageDialog(
                    this,
                    "¡Todos los jugadores humanos deben escoger al menos un pokémon!",
                    "Selección incompleta",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Mostrar selecciones en consola (para depuración)
            System.out.println("Selección de " + player1Label.getText() + ":");
            seleccionPokemones1.forEach((idx, cantidad) -> {
                System.out.println("  Poobkemon: " + pokemones.get(idx) + " x" + cantidad);
            });
            seleccionItems1.forEach((idx, cantidad) -> {
                System.out.println("  Item: " + items.get(idx) + " x" + cantidad);
            });

            System.out.println("Selección de " + player2Label.getText() + ":");
            seleccionPokemones2.forEach((idx, cantidad) -> {
                System.out.println("  Poobkemon: " + pokemones.get(idx) + " x" + cantidad);
            });
            seleccionItems2.forEach((idx, cantidad) -> {
                System.out.println("  Item: " + items.get(idx) + " x" + cantidad);
            });

            // Iniciar batalla
            iniciarBatalla();
        });

        bottomPanel.add(backButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(nextButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Inicializa las grillas
        refreshPokemons();
        refreshItems();
    }

    private JPanel decoratePanel(JPanel inner) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        // Reduce el padding interno
        outer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    private void styleNavButton(JButton btn) {
        btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btn.setBackground(new Color(220, 220, 255));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(40, 40));
    }

    private void refreshPokemons() {
        pokemonsGrid.removeAll();
        Map<Integer, Integer> seleccionPokemonesActual = (jugadorActual == 1) ? seleccionPokemones1 : seleccionPokemones2;
        int totalSeleccionados = seleccionPokemonesActual.values().stream().mapToInt(Integer::intValue).sum();
        for (int i = 0; i < POKES_POR_PAG; i++) {
            int idx = pokeOffset + i;
            if (idx < pokemones.size()) {
                String nombre = pokemones.get(idx);
                ImageIcon icon = new ImageIcon("mult/PokemonSprites/" + nombre + ".png");
                Image img = icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
                JButton pokeBtn = new JButton(new ImageIcon(img));
                pokeBtn.setToolTipText(nombre);
                pokeBtn.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true));
                pokeBtn.setFocusPainted(false);
                pokeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                pokeBtn.addActionListener(e -> {
                    if (totalSeleccionados < 6) {
                        int count = seleccionPokemonesActual.getOrDefault(idx, 0);
                        seleccionPokemonesActual.put(idx, count + 1);
                        refreshPokemons();
                    }
                });

                if (seleccionPokemonesActual.containsKey(idx)) {
                    pokeBtn.setBackground(new Color(120, 220, 120));
                    pokeBtn.setText("" + seleccionPokemonesActual.get(idx));
                } else {
                    pokeBtn.setBackground(Color.WHITE);
                    pokeBtn.setText("");
                }
                pokeBtn.setOpaque(true);
                pokemonsGrid.add(pokeBtn);
            } else {
                pokemonsGrid.add(new JLabel());
            }
        }
        pokemonsGrid.revalidate();
        pokemonsGrid.repaint();
    }

    private void refreshItems() {
        itemsGrid.removeAll();
        Map<Integer, Integer> seleccionItemsActual = (jugadorActual == 1) ? seleccionItems1 : seleccionItems2;
        int totalSeleccionados = seleccionItemsActual.values().stream().mapToInt(Integer::intValue).sum();
        for (int i = 0; i < ITEMS_POR_PAG; i++) {
            int idx = itemOffset + i;
            if (idx < items.size()) {
                String nombre = items.get(idx);
                ImageIcon icon = new ImageIcon("mult/Items/" + nombre + ".png");
                Image img = icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
                JButton itemBtn = new JButton(new ImageIcon(img));
                itemBtn.setToolTipText(nombre);
                itemBtn.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true));
                itemBtn.setFocusPainted(false);
                itemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                itemBtn.addActionListener(e -> {
                    if (totalSeleccionados < 4) {
                        int count = seleccionItemsActual.getOrDefault(idx, 0);
                        seleccionItemsActual.put(idx, count + 1);
                        refreshItems();
                    }
                });

                if (seleccionItemsActual.containsKey(idx)) {
                    itemBtn.setBackground(new Color(255, 200, 120));
                    itemBtn.setText("" + seleccionItemsActual.get(idx));
                } else {
                    itemBtn.setBackground(Color.WHITE);
                    itemBtn.setText("");
                }
                itemBtn.setOpaque(true);
                itemsGrid.add(itemBtn);
            } else {
                itemsGrid.add(new JLabel());
            }
        }
        itemsGrid.revalidate();
        itemsGrid.repaint();
    }

    private void iniciarBatalla() {
        // Convertir los mapas de selecciones a listas
        pokemones1.clear();
        pokemones2.clear();
        items1.clear();
        items2.clear();
        
        // Solo poblar pokemones1 desde las selecciones si el jugador 1 es humano
        if (!app.isMachinePlayer1() && !app.isMachineVsMachine()) {
            seleccionPokemones1.forEach((idx, cantidad) -> {
                String pokemon = pokemones.get(idx);
                for (int i = 0; i < cantidad; i++) {
                    pokemones1.add(pokemon);
                }
            });
        } else {
            // Si el jugador 1 es una máquina, generar Pokémon aleatorios
            pokemones1.addAll(generarPokemonesAleatorios(4));
        }
        
        // Solo poblar pokemones2 desde las selecciones si el jugador 2 es humano
        if (!app.isMachinePlayer2() && !app.isMachineVsMachine()) {
            seleccionPokemones2.forEach((idx, cantidad) -> {
                String pokemon = pokemones.get(idx);
                for (int i = 0; i < cantidad; i++) {
                    pokemones2.add(pokemon);
                }
            });
        } else {
            // Si el jugador 2 es una máquina, generar Pokémon aleatorios
            pokemones2.addAll(generarPokemonesAleatorios(4));
        }
        
        // Poblar items solo para humanos
        if (!app.isMachinePlayer1() && !app.isMachineVsMachine()) {
            seleccionItems1.forEach((idx, cantidad) -> {
                String item = items.get(idx);
                for (int i = 0; i < cantidad; i++) {
                    items1.add(item);
                }
            });
        }
        
        if (!app.isMachinePlayer2() && !app.isMachineVsMachine()) {
            seleccionItems2.forEach((idx, cantidad) -> {
                String item = items.get(idx);
                for (int i = 0; i < cantidad; i++) {
                    items2.add(item);
                }
            });
        }
        
        // Convertir a ArrayList para compatibilidad con el API
        ArrayList<String> pokes1 = new ArrayList<>(pokemones1);
        ArrayList<String> pokes2 = new ArrayList<>(pokemones2);
        ArrayList<String> itemsList1 = new ArrayList<>(items1);
        ArrayList<String> itemsList2 = new ArrayList<>(items2);
        
        try {
            if (app.isBattleWithMachine()) {
                if (app.isMachineVsMachine()) {
                    // Código para batallas máquina vs máquina
                    String tipoMaquina1 = seleccionarTipoMaquinaStatic(this, "Seleccionar tipo para Máquina 1");
                    String tipoMaquina2 = seleccionarTipoMaquinaStatic(this, "Seleccionar tipo para Máquina 2");
                    
                    // Iniciar directamente la batalla máquina vs máquina
                    app.getPoobkemon().startBattleMachineVsMachine(
                        "CPU " + tipoMaquina1, "CPU " + tipoMaquina2,
                        pokes1, pokes2, tipoMaquina1, tipoMaquina2);
                        
                    // Crear y mostrar el panel de batalla
                    boolean jugador1Empieza = app.getPoobkemon().whoStarts();
                    PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(
                        app.getPoobkemon(), app, app.getColorJugador1(), app.getColorJugador2(), jugador1Empieza);
                    app.cambiarPantallaConPanel(battlePanel, "battle");
                } 
                else if (app.isMachinePlayer1()) {
                    // Batalla máquina vs humano
                    String tipoMaquina = seleccionarTipoMaquinaStatic(this, "Seleccionar tipo de Máquina");
                    
                    // Ir a la pantalla de selección de movimientos
                    PoobkemonMovimientosPanel movimientosPanel = new PoobkemonMovimientosPanel(
                        app, "CPU " + tipoMaquina, app.getNombreJugador2(), 
                        pokes1, pokes2, itemsList1, itemsList2, 
                        tipoMaquina, true);
                    app.cambiarPantallaConPanel(movimientosPanel, "movimientos");
                } 
                else {
                    // Batalla humano vs máquina
                    String tipoMaquina = seleccionarTipoMaquinaStatic(this, "Seleccionar tipo de Máquina");
                    
                    // Ir a la pantalla de selección de movimientos
                    PoobkemonMovimientosPanel movimientosPanel = new PoobkemonMovimientosPanel(
                        app, app.getNombreJugador1(), "CPU " + tipoMaquina, 
                        pokes1, pokes2, itemsList1, itemsList2, 
                        tipoMaquina, true);
                    app.cambiarPantallaConPanel(movimientosPanel, "movimientos");
                }
            } 
            else {
                // Batalla humano vs humano (normal)
                PoobkemonMovimientosPanel movimientosPanel = new PoobkemonMovimientosPanel(
                    app, app.getNombreJugador1(), app.getNombreJugador2(), 
                    pokes1, pokes2, itemsList1, itemsList2);
                app.cambiarPantallaConPanel(movimientosPanel, "movimientos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al iniciar la batalla: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al iniciar batalla", e);
        }
    }

    /**
     * Muestra un diálogo para seleccionar el tipo de máquina
     * @param titulo Título del diálogo
     * @return El tipo de máquina seleccionado
     */
    private String seleccionarTipoMaquina(String titulo) {
        String[] tiposMaquina = {"Attacking", "Defensive", "Changing", "Expert", "Gemini"};
        
        // Crear descripciones para cada tipo
        Map<String, String> descripciones = new HashMap<>();
        descripciones.put("Attacking", "Prioriza ataques potentes y estadísticas ofensivas");
        descripciones.put("Defensive", "Enfocada en resistencia y recuperación");
        descripciones.put("Changing", "Cambia estrategias y Pokémon según la situación");
        descripciones.put("Expert", "Combina todas las estrategias de forma inteligente");
        descripciones.put("Gemini", "IA avanzada utilizando Google Gemini 2 Flash");
        
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
     * Genera una lista de Pokémon aleatorios para las máquinas
     * @param cantidad Cantidad de Pokémon a generar
     * @return Lista de nombres de Pokémon aleatorios
     */
    private List<String> generarPokemonesAleatorios(int cantidad) {
        List<String> todosPokemons = new ArrayList<>(pokemones);
        List<String> seleccionados = new ArrayList<>();
        
        // Si hay menos Pokémon disponibles que la cantidad solicitada
        if (todosPokemons.size() <= cantidad) {
            return todosPokemons;
        }
        
        // Seleccionar Pokémon aleatorios sin repetición
        Random random = new Random();
        while (seleccionados.size() < cantidad && !todosPokemons.isEmpty()) {
            int index = random.nextInt(todosPokemons.size());
            seleccionados.add(todosPokemons.get(index));
            todosPokemons.remove(index);
        }
        
        return seleccionados;
    }

    /**
     * Actualiza la interfaz según el jugador actual y si es una máquina
     */
    private void actualizarInterfazSeleccion() {
        // Establecer el título y configuración según el jugador actual
        if (jugadorActual == 1) {
            // Ocultar selección si el jugador 1 es una máquina
            boolean esMaquina = app.isMachinePlayer1() || app.isMachineVsMachine();
            pokemonsGrid.setEnabled(!esMaquina);
            itemsGrid.setEnabled(!esMaquina);
            
            if (esMaquina) {
                JOptionPane.showMessageDialog(this, 
                    "Los Pokémon para la máquina se seleccionarán automáticamente",
                    "Selección automática", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Ocultar selección si el jugador 2 es una máquina
            boolean esMaquina = app.isMachinePlayer2() || app.isMachineVsMachine();
            pokemonsGrid.setEnabled(!esMaquina);
            itemsGrid.setEnabled(!esMaquina);
            
            if (esMaquina) {
                JOptionPane.showMessageDialog(this, 
                    "Los Pokémon para la máquina se seleccionarán automáticamente",
                    "Selección automática", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        // Actualizar la visualización
        refreshPokemons();
        refreshItems();
    }

    public static String seleccionarTipoMaquinaStatic(Component parent, String titulo) {
        String[] tiposMaquina = {"Attacking", "Defensive", "Changing", "Expert", "Gemini"};
        Map<String, String> descripciones = new HashMap<>();
        descripciones.put("Attacking", "Prioriza ataques potentes y estadísticas ofensivas");
        descripciones.put("Defensive", "Enfocada en resistencia y recuperación");
        descripciones.put("Changing", "Cambia estrategias y Pokémon según la situación");
        descripciones.put("Expert", "Combina todas las estrategias de forma inteligente");
        descripciones.put("Gemini", "IA avanzada utilizando Google Gemini 2 Flash");
    
        JPanel panel = new JPanel(new GridLayout(0, 1));
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[tiposMaquina.length];
    
        for (int i = 0; i < tiposMaquina.length; i++) {
            String tipo = tiposMaquina[i];
            buttons[i] = new JRadioButton("<html><b>" + tipo + "</b>: " + descripciones.get(tipo) + "</html>");
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }
        buttons[0].setSelected(true);
    
        int result = JOptionPane.showConfirmDialog(
            parent,
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
        return "Attacking";
    }
}

class BackgroundPanel extends JPanel {
    private final Image background;
    public BackgroundPanel(String path) {
        this.background = new ImageIcon(path).getImage();
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}