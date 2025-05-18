package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PoobkemonMenu extends JPanel {
    public PoobkemonMenu(PoobkemonGUI app, Musica musica) {
        setLayout(new BorderLayout());

        // Fondo escalable
        ScalableImagePanel background = new ScalableImagePanel("mult/Fondos/Pokemon_Inicio.png");
        background.setLayout(new GridBagLayout());

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 18));
        buttonPanel.setOpaque(false);

        JButton newGameButton = new GradientButton("New Game");
        JButton oldGameButton = new GradientButton("Old Game");
        JButton optionsButton = new GradientButton("Options");
        JButton backButton = new GradientButton("Back");

        newGameButton.setPreferredSize(new Dimension(220, 36));
        oldGameButton.setPreferredSize(new Dimension(220, 36));
        optionsButton.setPreferredSize(new Dimension(220, 36));
        backButton.setPreferredSize(new Dimension(220, 36));

        newGameButton.addActionListener(e -> app.cambiarPantalla("gamemode"));
        backButton.addActionListener(e -> app.cambiarPantalla("inicio"));

        buttonPanel.add(newGameButton);
        buttonPanel.add(oldGameButton);
        buttonPanel.add(optionsButton);
        buttonPanel.add(backButton);

        // Panel para agrupar título y botones alineados a la izquierda
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.anchor = GridBagConstraints.NORTHWEST;
        gbcLeft.insets = new Insets(10, 20, 10, 20);

        gbcLeft.gridy = 1;
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.insets = new Insets(40, 20, 20, 20);
        leftPanel.add(buttonPanel, gbcLeft);

        // Agrega el panel izquierdo al fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(50, -300, 20, 20);
        background.add(leftPanel, gbc);

        add(background, BorderLayout.CENTER);
    }

    // Panel personalizado para escalar la imagen de fondo
    class ScalableImagePanel extends JPanel {
        private Image img;

        public ScalableImagePanel(String path) {
            img = new ImageIcon(path).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}