package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PokemonSelectorDialog {
    /**
     * Muestra un diálogo para seleccionar un Pokémon de una lista.
     * @param parent Componente padre para el diálogo.
     * @param pokemonsVivos Lista de nombres de Pokémon vivos.
     * @param hpActual Lista de HP actual de cada Pokémon (mismo orden que pokemonsVivos).
     * @param hpMax Lista de HP máximo de cada Pokémon (mismo orden que pokemonsVivos).
     * @return El nombre del Pokémon seleccionado, o null si se cancela.
     */
    public static String seleccionarPokemon(Component parent, List<String> pokemonsVivos, List<Integer> hpActual, List<Integer> hpMax) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        ButtonGroup group = new ButtonGroup();
        java.util.List<JRadioButton> botones = new java.util.ArrayList<>();

        for (int i = 0; i < pokemonsVivos.size(); i++) {
            String nombre = pokemonsVivos.get(i);
            ImageIcon icon = new ImageIcon("mult/gifs/" + nombre + ".gif");
            JRadioButton btn = new JRadioButton(nombre, icon, false);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setVerticalTextPosition(SwingConstants.CENTER);
            group.add(btn);
            botones.add(btn);

            JPanel pokemonPanel = new JPanel(new BorderLayout());
            pokemonPanel.setOpaque(false);
            pokemonPanel.add(btn, BorderLayout.NORTH);

            // Barra de vida
            int actual = hpActual.get(i);
            int max = hpMax.get(i);
            JProgressBar hpBar = new JProgressBar(0, max);
            hpBar.setValue(actual);
            hpBar.setString(actual + " / " + max);
            hpBar.setStringPainted(true);
            hpBar.setForeground(new Color(0, 200, 0));
            pokemonPanel.add(hpBar, BorderLayout.SOUTH);

            panel.add(pokemonPanel);
        }

        if (!botones.isEmpty()) {
            botones.get(0).setSelected(true);
        }

        int result = JOptionPane.showConfirmDialog(
            parent,
            panel,
            "El pokémon actual ha sido derrotado. Selecciona otro:",
            JOptionPane.OK_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            for (JRadioButton btn : botones) {
                if (btn.isSelected()) {
                    return btn.getText();
                }
            }
        }
        return null;
    }
}