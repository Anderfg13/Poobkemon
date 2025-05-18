package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;


public class PoobkemonInicio extends JPanel {
    private Clip musicaClip;
    private int musicFramePosition = 0;
    private Musica musica;

    public PoobkemonInicio(PoobkemonGUI app, Musica musica) {
        this.musica = musica;
        setLayout(new BorderLayout());

        // Barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        // Botón Abrir
        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad de Abrir no implementada.");
        });

        // Botón Guardar
        JMenuItem saveItem = new JMenuItem("Guardar");
        saveItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad de Guardar no implementada.");
        });

        // Botón Salir con confirmación
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres salir del juego?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Botón para pausar/reanudar música
        JCheckBoxMenuItem musicToggle = new JCheckBoxMenuItem("Pausar música");
        musicToggle.setSelected(musica != null && musica.estaEncendida());
        musicToggle.addActionListener(e -> {
            if (musicToggle.isSelected()) {
                if (musica != null) musica.reproducir();
                musicToggle.setText("Pausar música");
            } else {
                if (musica != null) musica.pausar();
                musicToggle.setText("Reanudar música");
            }
        });

        optionsMenu.add(openItem);
        optionsMenu.add(saveItem);
        optionsMenu.addSeparator();
        optionsMenu.add(musicToggle);
        optionsMenu.addSeparator();
        optionsMenu.add(exitItem);
        menuBar.add(optionsMenu);

        // Panel principal con fondo GIF escalable
        ScalableGifPanel background = new ScalableGifPanel("mult/Fondos/Pokemon_Intro.gif");
        background.setLayout(new GridBagLayout());

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10)); // 2 filas, 1 columna, separación vertical

        JButton playButton = new GradientButton("Play");
        JButton exitButton = new GradientButton("Exit");

        // Acción para el botón verde Exit
        exitButton.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres salir del juego?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Acción para el botón Play
        playButton.addActionListener(e -> app.cambiarPantalla("menu"));

        // Opcional: agrega padding interno para mejor apariencia
        playButton.setMargin(new Insets(8, 30, 8, 30));
        exitButton.setMargin(new Insets(8, 30, 8, 30));

        buttonPanel.add(playButton);
        buttonPanel.add(exitButton);

        // Panel contenedor para limitar el ancho máximo
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.add(buttonPanel, BorderLayout.CENTER);
        containerPanel.setMaximumSize(new Dimension(260, 100)); // Máximo ancho y alto

        // Añadir el panel contenedor en la parte inferior central del fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 250, 30, 250); // Márgenes laterales para centrar

        background.add(containerPanel, gbc);

        add(background, BorderLayout.CENTER);

    }

}