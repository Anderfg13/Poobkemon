package presentation;

import domain.Poobkemon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class PauseDialog extends JDialog {
    public PauseDialog(JFrame parent, Poobkemon poobkemon, PoobkemonGUI app,
                      Color colorJugador1, Color colorJugador2,
                      Runnable continuarCallback, Runnable terminarCallback) {
        super(parent, "Pausa", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.add(crearPanelJugador(poobkemon, true, colorJugador1));
        mainPanel.add(crearPanelJugador(poobkemon, false, colorJugador2));
        add(mainPanel, BorderLayout.CENTER);

        // Panel central con los botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(3, 1, 10, 10));
        JButton btnContinuar = new JButton("Continuar");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnTerminar = new JButton("Terminar partida");
        botonesPanel.add(btnContinuar);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnTerminar);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.add(botonesPanel);
        add(centro, BorderLayout.EAST);

        // Acciones de los botones
        btnContinuar.addActionListener(e -> {
            continuarCallback.run();
            dispose();
        });

        btnGuardar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                try {
                    poobkemon.guardarPartida(selectedFile); // Debes implementar este método en tu clase Poobkemon
                    JOptionPane.showMessageDialog(this, "Partida guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnTerminar.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres terminar la partida?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                terminarCallback.run();
                dispose();
            }
        });

        // Si se cierra con la X, continuar la partida
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                continuarCallback.run();
            }
        });
    }

    private JPanel crearPanelJugador(Poobkemon poobkemon, boolean esJugador1, Color colorNombre) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        String nombre = esJugador1 ? poobkemon.getNombreJugador1() : poobkemon.getNombreJugador2();
        JLabel nombreLabel = new JLabel(nombre, SwingConstants.CENTER);
        nombreLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        nombreLabel.setForeground(colorNombre); // <-- Color personalizado
        panel.add(nombreLabel, BorderLayout.NORTH);

        // Pokemones
        List<String> pokemons = poobkemon.getPokemonsName(esJugador1);
        JPanel pokemonsPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        for (String poke : pokemons) {
            JPanel pokePanel = new JPanel(new BorderLayout());
            pokePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            pokePanel.setBackground(colorNombre);
            pokePanel.setOpaque(true);

            JLabel pokeImg = new JLabel(new ImageIcon("mult/gifs/" + poke + ".gif"));
            pokeImg.setHorizontalAlignment(SwingConstants.CENTER);
            pokePanel.add(pokeImg, BorderLayout.CENTER);

            int hp = poobkemon.getPokemonHP(esJugador1, poke);
            int maxHp = poobkemon.getPokemonMaxHP(esJugador1, poke);
            JProgressBar hpBar = new JProgressBar(0, maxHp);
            hpBar.setValue(hp);
            hpBar.setString(hp + " / " + maxHp);
            hpBar.setStringPainted(true);
            hpBar.setForeground(new Color(0, 200, 0));
            pokePanel.add(hpBar, BorderLayout.SOUTH);

            pokemonsPanel.add(pokePanel);
        }
        panel.add(pokemonsPanel, BorderLayout.CENTER);

        // Items
        List<String> items = poobkemon.getItemsJugador(esJugador1);
        JPanel itemsPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        for (String item : items) {
            JLabel itemLabel = new JLabel(new ImageIcon("mult/items/" + item + ".png"));
            itemLabel.setHorizontalAlignment(SwingConstants.CENTER);
            itemsPanel.add(itemLabel);
        }
        panel.add(new JLabel("ITEMS", SwingConstants.CENTER), BorderLayout.SOUTH);
        panel.add(itemsPanel, BorderLayout.SOUTH);

        return panel;
    }
}