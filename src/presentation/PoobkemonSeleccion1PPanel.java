package presentation;

import domain.Poobkemon;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * PoobkemonSeleccion1PPanel es un panel de selección de Pokémon e ítems para el modo 1 jugador en Poobkemon.
 * Permite al usuario elegir hasta 6 Pokémon y 4 ítems antes de iniciar la batalla contra la máquina.
 * Gestiona la navegación entre páginas de selección, la validación de la selección y la transición al panel de movimientos.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Muestra los Pokémon e ítems disponibles en formato paginado, permitiendo seleccionar múltiples unidades de cada uno.</li>
 *   <li>Valida que el usuario seleccione al menos un Pokémon antes de continuar.</li>
 *   <li>Permite reiniciar la selección y regresar al menú anterior.</li>
 *   <li>Integra la transición al panel de selección de movimientos tras confirmar la selección.</li>
 *   <li>Actualiza visualmente la cantidad seleccionada de cada Pokémon e ítem.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PoobkemonSeleccion1PPanel extends BackgroundPanel {
    private final List<String> pokemones = Poobkemon.getAvailablePokemon();
    private final List<String> items = Poobkemon.getAvailableItems();

    private final Map<Integer, Integer> seleccionPokemones = new HashMap<>();
    private final Map<Integer, Integer> seleccionItems = new HashMap<>();
    private int pokeOffset = 0;
    private int itemOffset = 0;
    private final int POKES_POR_PAG = 6;
    private final int ITEMS_POR_PAG = 4;

    private final JPanel pokemonsGrid = new JPanel(new GridLayout(2, 3, 18, 18));
    private final JPanel itemsGrid = new JPanel(new GridLayout(1, 4, 18, 18));
    private final PoobkemonGUI app;
    private final String nombreJugador;

    /**
     * Constructor del panel de selección de Pokémon e ítems para el modo 1 jugador.
     *
     * @param app Referencia a la interfaz gráfica principal de Poobkemon.
     * @param nombreJugador Nombre del jugador humano.
     */
    public PoobkemonSeleccion1PPanel(PoobkemonGUI app, String nombreJugador) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg");
        this.app = app;
        this.nombreJugador = nombreJugador;
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 5, true));
        setBackground(new Color(245, 245, 255));

        // Top: Solo el nombre del jugador
        JLabel playerLabel = new JLabel(nombreJugador, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
        playerLabel.setForeground(new Color(30, 90, 180));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 20));
        topPanel.add(playerLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center: Selección de pokemones e items
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
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
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(decoratePanel(itemsPanel));

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Back, Reiniciar, Continuar
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        bottomPanel.setOpaque(false);

        JButton backButton = new GradientButton("Back");
        JButton resetButton = new GradientButton("Reiniciar selección");
        JButton nextButton = new GradientButton("Continuar");

        backButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        resetButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        nextButton.setFont(new Font("Times New Roman", Font.BOLD, 20));

        resetButton.addActionListener(e -> {
            seleccionPokemones.clear();
            seleccionItems.clear();
            refreshPokemons();
            refreshItems();
        });
        backButton.addActionListener(e -> app.cambiarPantalla("gamemode"));
        nextButton.addActionListener(e -> {
            if (seleccionPokemones.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "¡Debes escoger al menos un pokémon!",
                    "Selección incompleta",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            System.out.println("Selección de " + nombreJugador + ":");
            seleccionPokemones.forEach((idx, cantidad) -> {
                System.out.println("  Poobkemon: " + pokemones.get(idx) + " x" + cantidad);
            });
            seleccionItems.forEach((idx, cantidad) -> {
                System.out.println("  Item: " + items.get(idx) + " x" + cantidad);
            });

            // Aquí puedes continuar al panel de movimientos o siguiente paso
            // Ejemplo:
            List<String> pokes = new ArrayList<>();
            seleccionPokemones.forEach((idx, cantidad) -> {
                for (int i = 0; i < cantidad; i++) pokes.add(pokemones.get(idx));
            });
            List<String> itemsSeleccionados = new ArrayList<>();
            seleccionItems.forEach((idx, cantidad) -> {
                for (int i = 0; i < cantidad; i++) itemsSeleccionados.add(items.get(idx));
            });
            PoobkemonMovimientos1PPanel movPanel = new PoobkemonMovimientos1PPanel(app, nombreJugador, pokes, itemsSeleccionados);
            app.cambiarPantallaConPanel(movPanel, "movimientos1p");
        });

        bottomPanel.add(backButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(nextButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Inicializa las grillas
        refreshPokemons();
        refreshItems();
    }

    /**
     * Decora un panel interno con borde y relleno personalizados.
     *
     * @param inner Panel interno a decorar.
     * @return Panel decorado listo para agregar a la interfaz.
     */
    private JPanel decoratePanel(JPanel inner) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    /**
     * Aplica el estilo visual a los botones de navegación (< y >) para paginación.
     *
     * @param btn Botón de navegación a estilizar.
     */
    private void styleNavButton(JButton btn) {
        btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btn.setBackground(new Color(220, 220, 255));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(40, 40));
    }

    /**
     * Refresca la grilla de selección de Pokémon, mostrando los disponibles en la página actual
     * y actualizando visualmente la cantidad seleccionada de cada uno.
     */
    private void refreshPokemons() {
        pokemonsGrid.removeAll();
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

    /**
     * Refresca la grilla de selección de ítems, mostrando los disponibles en la página actual
     * y actualizando visualmente la cantidad seleccionada de cada uno.
     */
    private void refreshItems() {
        itemsGrid.removeAll();
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