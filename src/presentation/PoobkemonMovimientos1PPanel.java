package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PoobkemonMovimientos1PPanel extends BackgroundPanel {
    private final String nombreJugador;
    private final List<String> pokemonesJugador;
    private final Map<String, List<String>> movimientosSeleccionados = new HashMap<>();
    private final PoobkemonGUI app;
    private JPanel centerPanel;

    public PoobkemonMovimientos1PPanel(PoobkemonGUI app, String nombreJugador, List<String> pokemonesJugador) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg");
        this.app = app;
        this.nombreJugador = nombreJugador;
        this.pokemonesJugador = pokemonesJugador;

        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Selecciona los movimientos", SwingConstants.CENTER);
        titulo.setFont(new Font("Times New Roman", Font.BOLD, 28));
        titulo.setForeground(Color.BLACK);
        add(titulo, BorderLayout.NORTH);

        // Panel central con los pokemones del jugador
        centerPanel = new JPanel(new GridLayout(1, 1, 0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(crearFilaPokemones(nombreJugador, pokemonesJugador, movimientosSeleccionados));
        add(centerPanel, BorderLayout.CENTER);

        // Botón finalizar y volver
        JButton finalizar = new GradientButton("Finalizar");
        finalizar.setFont(new Font("Times New Roman", Font.BOLD, 20));
        finalizar.addActionListener(e -> {
            // Validar que todos los pokemones tengan 4 movimientos
            boolean incompleto = false;
            for (String poke : pokemonesJugador) {
                List<String> movs = movimientosSeleccionados.getOrDefault(poke, new ArrayList<>());
                if (movs.size() != 4) {
                    incompleto = true;
                    break;
                }
            }
            if (incompleto) {
                JOptionPane.showMessageDialog(this, "Todos los pokemones deben tener 4 movimientos.", "Faltan movimientos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Aquí puedes continuar al siguiente paso del juego
            System.out.println("Movimientos seleccionados por " + nombreJugador + ":");
            for (String poke : pokemonesJugador) {
                System.out.println("  " + poke + ": " + movimientosSeleccionados.get(poke));
            }
            // app.cambiarPantalla("siguientePaso"); // Cambia esto por tu flujo real
        });

        JButton volver = new GradientButton("Volver");
        volver.setFont(new Font("Times New Roman", Font.BOLD, 20));
        volver.addActionListener(e -> app.cambiarPantalla("selection1p"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(volver);
        bottomPanel.add(finalizar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel crearFilaPokemones(String nombreJugador, List<String> pokemones, Map<String, List<String>> movimientosSeleccionados) {
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 18, 18));
        gridPanel.setOpaque(false);

        for (String poke : pokemones) {
            JButton pokeBtn = new JButton(new ImageIcon(
                new ImageIcon("mult/PokemonSprites/" + poke + ".png")
                    .getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)
            ));
            pokeBtn.setToolTipText(poke);

            // Visual feedback: color si ya tiene 4 movimientos
            List<String> movs = movimientosSeleccionados.getOrDefault(poke, new ArrayList<>());
            if (movs.size() == 4) {
                pokeBtn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
            } else {
                pokeBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 0, 120), 2));
            }

            pokeBtn.setContentAreaFilled(false);
            pokeBtn.setFocusPainted(false);

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
                // Refresca la vista para actualizar el borde
                centerPanel.removeAll();
                centerPanel.add(crearFilaPokemones(nombreJugador, pokemones, movimientosSeleccionados));
                centerPanel.revalidate();
                centerPanel.repaint();
            });

            gridPanel.add(pokeBtn);
        }
        // Si hay menos de 6 pokemones, rellena los espacios vacíos
        for (int i = pokemones.size(); i < 6; i++) {
            gridPanel.add(new JLabel());
        }
        return gridPanel;
    }
}