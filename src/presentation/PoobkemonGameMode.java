package presentation;

import javax.swing.*;
import java.awt.*;

public class PoobkemonGameMode extends JPanel {
    public PoobkemonGameMode(PoobkemonGUI app, Musica musica) {
        setLayout(new BorderLayout());

        // Fondo escalable
        ScalableImagePanel background = new ScalableImagePanel("mult/Fondos/Pokemon_GameMode.png");
        background.setLayout(new GridBagLayout());

        // Panel de botones (centrado)
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 18));
        buttonPanel.setOpaque(false);

        // Carga las imágenes
        ImageIcon porigonIcon = new ImageIcon("mult/porygon.png");
        ImageIcon humanIcon = new ImageIcon("mult/human.png");

        int iconSize = 35;
        ImageIcon porigonScaled = new ImageIcon(porigonIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon humanScaled = new ImageIcon(humanIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        // Player vs Player: humano a la izquierda y derecha
        JButton pvpButton = new GradientButton("Player vs Player");
        pvpButton.setHorizontalAlignment(SwingConstants.CENTER);

        pvpButton.addActionListener(e -> {
            String[] opciones = {"Normal", "Supervivencia"};
            int seleccion = JOptionPane.showOptionDialog(
                this,
                "¿Qué modo de juego quieres?",
                "Selecciona modo de juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            if (seleccion == 0) {
                app.actualizarSeleccionPanel();
                app.cambiarPantalla("selection");
            } else if (seleccion == 1) {
                // Aquí puedes iniciar el modo Supervivencia
                System.out.println("Modo Supervivencia seleccionado");
            }
        });

        JPanel pvpRow = new JPanel(new BorderLayout());
        pvpRow.setOpaque(false);
        pvpRow.add(new JLabel(humanScaled), BorderLayout.WEST);
        pvpRow.add(pvpButton, BorderLayout.CENTER);
        pvpRow.add(new JLabel(humanScaled), BorderLayout.EAST);

        // Player vs Machine: humano izquierda, porigon derecha
        JButton pvmButton = new GradientButton("Player vs Machine");
        pvmButton.setHorizontalAlignment(SwingConstants.CENTER);

        pvmButton.addActionListener(e -> {
            String[] tiposEntrenador = {"Defensive", "Attacking", "Changing", "Expert"};
            int seleccion = JOptionPane.showOptionDialog(
                this,
                "¿Contra qué tipo de entrenador quieres luchar?",
                "Selecciona tipo de entrenador",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposEntrenador,
                tiposEntrenador[0]
            );
            if (seleccion != -1) {
                System.out.println("Tipo de entrenador seleccionado: " + tiposEntrenador[seleccion]);
                // Aquí puedes guardar el tipo seleccionado para usarlo después
                PoobkemonSeleccion1PPanel seleccionPanel = new PoobkemonSeleccion1PPanel(app, app.getNombreJugadorHumano());
                app.cambiarPantallaConPanel(seleccionPanel, "selection1p");
            }
        });

        JPanel pvmRow = new JPanel(new BorderLayout());
        pvmRow.setOpaque(false);
        pvmRow.add(new JLabel(humanScaled), BorderLayout.WEST);
        pvmRow.add(pvmButton, BorderLayout.CENTER);
        pvmRow.add(new JLabel(porigonScaled), BorderLayout.EAST);

        // Machine vs Machine: porigon izquierda y derecha
        JButton mvmButton = new GradientButton("Machine vs Machine");
        mvmButton.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel mvmRow = new JPanel(new BorderLayout());
        mvmRow.setOpaque(false);
        mvmRow.add(new JLabel(porigonScaled), BorderLayout.WEST);
        mvmRow.add(mvmButton, BorderLayout.CENTER);
        mvmRow.add(new JLabel(porigonScaled), BorderLayout.EAST);

        // Back button (solo el botón)
        JButton backButton = new GradientButton("Back");
        backButton.addActionListener(e -> app.cambiarPantalla("menu"));

        Dimension largoBoton = new Dimension(220, 40); // 300 de ancho, 36 de alto
        pvpButton.setPreferredSize(largoBoton);
        pvmButton.setPreferredSize(largoBoton);
        mvmButton.setPreferredSize(largoBoton);
        backButton.setPreferredSize(largoBoton);

        // Agrega los paneles al panel de botones
        buttonPanel.add(pvpRow);
        buttonPanel.add(pvmRow);
        buttonPanel.add(mvmRow);
        buttonPanel.add(backButton);

        // Centrar el panel de botones en el fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        // Márgenes: new Insets(arriba, izquierda, abajo, derecha)
        gbc.insets = new Insets(55, 3, 0, 0); // Ejemplo: 80px arriba, 100px izquierda
        background.add(buttonPanel, gbc);

        add(background, BorderLayout.CENTER);
    }
}