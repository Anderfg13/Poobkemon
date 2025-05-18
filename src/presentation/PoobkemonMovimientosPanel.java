package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PoobkemonMovimientosPanel extends BackgroundPanel {
    private final String nombreJugador1;
    private final String nombreJugador2;
    private final List<String> pokemones1;
    private final List<String> pokemones2;
    private final Map<String, List<String>> movimientosSeleccionados1 = new HashMap<>();
    private final Map<String, List<String>> movimientosSeleccionados2 = new HashMap<>();
    private final PoobkemonGUI app;
    private JPanel centerPanel; // Agrega esto como atributo de la clase

    public PoobkemonMovimientosPanel(PoobkemonGUI app, String nombreJugador1, String nombreJugador2,
                                     List<String> pokemones1, List<String> pokemones2) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg");
        this.app = app;
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.pokemones1 = pokemones1;
        this.pokemones2 = pokemones2;

        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Selecciona los movimientos", SwingConstants.CENTER);
        titulo.setFont(new Font("Times New Roman", Font.BOLD, 28));
        titulo.setForeground(Color.BLACK);
        add(titulo, BorderLayout.NORTH);

        // Panel central con los pokemones de ambos jugadores
        centerPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        centerPanel.setOpaque(false);
        actualizarFilasPokemones();
        add(centerPanel, BorderLayout.CENTER);

        // Botón finalizar y volver
        JButton finalizar = new GradientButton("Finalizar");
        finalizar.setFont(new Font("Times New Roman", Font.BOLD, 20));

        JButton volver = new GradientButton("Volver");
        volver.setFont(new Font("Times New Roman", Font.BOLD, 20));
        volver.addActionListener(e -> {
            // Regresa a la pantalla anterior
            app.cambiarPantalla("selection"); // O el identificador de tu pantalla de selección
        });

        finalizar.addActionListener(e -> {
            // Aquí puedes validar que todos los pokemones tengan 4 movimientos y continuar
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

    // Simula la selección de movimientos (puedes reemplazar esto por un diálogo real)
    private List<String> seleccionarMovimientos(String poke, List<String> actuales) {
        // Aquí deberías mostrar un diálogo real con los movimientos disponibles para ese pokémon
        // Por ahora, solo simula con JOptionPane y texto
        String movs = JOptionPane.showInputDialog(null, "Escribe 4 movimientos separados por coma para " + poke, String.join(",", actuales));
        if (movs == null) return actuales;
        String[] arr = movs.split(",");
        List<String> lista = new ArrayList<>();
        for (String m : arr) {
            if (!m.trim().isEmpty() && lista.size() < 4) lista.add(m.trim());
        }
        return lista;
    }

    private void actualizarFilasPokemones() {
        centerPanel.removeAll();
        centerPanel.add(crearFilaPokemones(nombreJugador1, pokemones1, movimientosSeleccionados1, true));
        centerPanel.add(crearFilaPokemones(nombreJugador2, pokemones2, movimientosSeleccionados2, false));
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}