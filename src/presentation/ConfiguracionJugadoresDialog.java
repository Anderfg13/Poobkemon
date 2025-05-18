package presentation;

import javax.swing.*;
import java.awt.*;

public class ConfiguracionJugadoresDialog extends JDialog {
    public String nombre1, nombre2;
    public Color color1, color2;
    public String monedaElegida; // "Cara" o "Sello"

    public ConfiguracionJugadoresDialog(JFrame parent, Color colorJugador1, Color colorJugador2) {
        super(parent, "Configuración de Jugadores", true);
        setLayout(new GridLayout(3, 1, 10, 10));

        // Panel jugador 1
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Jugador 1:"));
        JTextField nombreField1 = new JTextField(8);
        panel1.add(nombreField1);
        JButton colorBtn1 = new JButton("Color");
        colorBtn1.setBackground(colorJugador1);
        panel1.add(colorBtn1);

        // Panel jugador 2
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Jugador 2:"));
        JTextField nombreField2 = new JTextField(8);
        panel2.add(nombreField2);
        JButton colorBtn2 = new JButton("Color");
        colorBtn2.setBackground(colorJugador2);
        panel2.add(colorBtn2);

        // Panel moneda
        JPanel panelMoneda = new JPanel();
        panelMoneda.add(new JLabel("Elige:"));
        JRadioButton caraBtn = new JRadioButton("Cara");
        JRadioButton selloBtn = new JRadioButton("Sello");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(caraBtn);
        grupo.add(selloBtn);
        panelMoneda.add(caraBtn);
        panelMoneda.add(selloBtn);

        // Acciones para elegir color
        colorBtn1.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Color Jugador 1", colorBtn1.getBackground());
            if (nuevo != null) colorBtn1.setBackground(nuevo);
        });
        colorBtn2.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Color Jugador 2", colorBtn2.getBackground());
            if (nuevo != null) colorBtn2.setBackground(nuevo);
        });

        // Botón aceptar
        JButton aceptar = new JButton("Aceptar");
        aceptar.addActionListener(e -> {
            nombre1 = nombreField1.getText().trim();
            nombre2 = nombreField2.getText().trim();
            color1 = colorBtn1.getBackground();
            color2 = colorBtn2.getBackground();
            monedaElegida = caraBtn.isSelected() ? "Cara" : selloBtn.isSelected() ? "Sello" : null;
            if (nombre1.isEmpty() || nombre2.isEmpty() || monedaElegida == null) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos y elige cara o sello.");
            } else {
                setVisible(false);
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(aceptar);

        add(panel1);
        add(panel2);
        add(panelMoneda);
        add(panelBoton);

        pack();
        setLocationRelativeTo(parent);
    }
}