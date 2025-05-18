package presentation;

import domain.Poobkemon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

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

    private final JPanel pokemonsGrid = new JPanel(new GridLayout(2, 3, 18, 18));
    private final JPanel itemsGrid = new JPanel(new GridLayout(1, 4, 18, 18));
    private final JLabel player1Label;
    private final JLabel player2Label;
    private final PoobkemonGUI app;

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

        player1Button.addActionListener(e -> {
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
            if (seleccionPokemones1.isEmpty() || seleccionPokemones2.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "¡Ambos jugadores deben escoger al menos un pokémon!",
                    "Selección incompleta",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            System.out.println("Selección de " + player1Button.getText() + ":");
            seleccionPokemones1.forEach((idx, cantidad) -> {
                System.out.println("  Poobkemon: " + pokemones.get(idx) + " x" + cantidad);
            });
            seleccionItems1.forEach((idx, cantidad) -> {
                System.out.println("  Item: " + items.get(idx) + " x" + cantidad);
            });

            System.out.println("Selección de " + player2Button.getText() + ":");
            seleccionPokemones2.forEach((idx, cantidad) -> {
                System.out.println("  Poobkemon: " + pokemones.get(idx) + " x" + cantidad);
            });
            seleccionItems2.forEach((idx, cantidad) -> {
                System.out.println("  Item: " + items.get(idx) + " x" + cantidad);
            });

            // Ejemplo:
            List<String> pokes1 = new ArrayList<>();
            seleccionPokemones1.forEach((idx, cantidad) -> {
                for (int i = 0; i < cantidad; i++) pokes1.add(pokemones.get(idx));
            });
            List<String> pokes2 = new ArrayList<>();
            seleccionPokemones2.forEach((idx, cantidad) -> {
                for (int i = 0; i < cantidad; i++) pokes2.add(pokemones.get(idx));
            });

            // Construye las listas de ítems seleccionados para cada jugador
            List<String> itemsSel1 = new ArrayList<>();
            seleccionItems1.forEach((idx, cantidad) -> {
                for (int i = 0; i < cantidad; i++) itemsSel1.add(items.get(idx));
            });
            List<String> itemsSel2 = new ArrayList<>();
            seleccionItems2.forEach((idx, cantidad) -> {
                for (int i = 0; i < cantidad; i++) itemsSel2.add(items.get(idx));
            });

            // Ahora sí, pásalas al constructor:
            PoobkemonMovimientosPanel movPanel = new PoobkemonMovimientosPanel(
                app, nombreJugador1, nombreJugador2, pokes1, pokes2, itemsSel1, itemsSel2
            );
            app.cambiarPantallaConPanel(movPanel, "movimientos"); // Implementa este método para cambiar el panel central dinámicamente
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
        Map<Integer, Integer> seleccionPokemones = (jugadorActual == 1) ? seleccionPokemones1 : seleccionPokemones2;
        int totalSeleccionados = seleccionPokemones.values().stream().mapToInt(Integer::intValue).sum();
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
                        int count = seleccionPokemones.getOrDefault(idx, 0);
                        seleccionPokemones.put(idx, count + 1);
                        refreshPokemons();
                    }
                });

                if (seleccionPokemones.containsKey(idx)) {
                    pokeBtn.setBackground(new Color(120, 220, 120));
                    pokeBtn.setText("" + seleccionPokemones.get(idx));
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
        Map<Integer, Integer> seleccionItems = (jugadorActual == 1) ? seleccionItems1 : seleccionItems2;
        int totalSeleccionados = seleccionItems.values().stream().mapToInt(Integer::intValue).sum();
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
                        int count = seleccionItems.getOrDefault(idx, 0);
                        seleccionItems.put(idx, count + 1);
                        refreshItems();
                    }
                });

                if (seleccionItems.containsKey(idx)) {
                    itemBtn.setBackground(new Color(255, 200, 120));
                    itemBtn.setText("" + seleccionItems.get(idx));
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