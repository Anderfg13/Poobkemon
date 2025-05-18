package presentation;

import domain.Poobkemon; // Asegúrate de tener acceso a los métodos del dominio
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class SeleccionarMovimientosDialog extends JDialog {
    private final List<String> movimientosSeleccionados = new ArrayList<>();
    private final JPanel movimientosPanel;
    private final JButton agregarBtn;
    private final JButton confirmarBtn;
    private final DefaultListModel<String> modeloLista;
    private final JList<String> listaSeleccionados;

    public SeleccionarMovimientosDialog(JFrame parent, String nombreJugador, String pokemon) {
        super(parent, nombreJugador + " - Seleccionar movimientos para " + pokemon, true);
        setContentPane(new FondoPanel("mult/Fondos/Pokemon_ChooseAttacks.jpg"));
        setLayout(null);
        setSize(500, 500);
        setLocationRelativeTo(parent);

        // GIF del pokémon
        JLabel gifLabel = new JLabel(new ImageIcon("mult/gifs/" + pokemon + ".gif"));
        gifLabel.setBounds(30, 120, 120, 120);
        add(gifLabel);

        // Panel de botones de tipo
        JButton fisicoBtn = new JButton("Ataque Fisico");
        JButton especialBtn = new JButton("Ataque Especial");
        JButton statusBtn = new JButton("Status");
        fisicoBtn.setBounds(160, 20, 120, 32);
        especialBtn.setBounds(290, 20, 140, 32);
        statusBtn.setBounds(440, 20, 90, 32);
        add(fisicoBtn);
        add(especialBtn);
        add(statusBtn);

        // Panel para mostrar movimientos disponibles
        movimientosPanel = new JPanel();
        movimientosPanel.setLayout(new BoxLayout(movimientosPanel, BoxLayout.Y_AXIS));
        movimientosPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(movimientosPanel);
        scroll.setBounds(160, 60, 300, 180);
        add(scroll);

        // Lista de movimientos seleccionados
        modeloLista = new DefaultListModel<>();
        listaSeleccionados = new JList<>(modeloLista);
        JScrollPane seleccionadosScroll = new JScrollPane(listaSeleccionados);
        seleccionadosScroll.setBounds(30, 260, 430, 60);
        add(seleccionadosScroll);

        // Botones Agregar y Confirmar
        agregarBtn = new JButton("Agregar");
        confirmarBtn = new JButton("Confirmar");
        agregarBtn.setBounds(120, 340, 100, 32);
        confirmarBtn.setBounds(260, 340, 120, 32);
        add(agregarBtn);
        add(confirmarBtn);

        // Acción para mostrar ataques físicos
        fisicoBtn.addActionListener(e -> mostrarMovimientos(Poobkemon.getPhysicalAttacks()));
        especialBtn.addActionListener(e -> mostrarMovimientos(Poobkemon.getSpecialAttacks()));
        statusBtn.addActionListener(e -> mostrarMovimientos(Poobkemon.getStatusAttacks()));

        // Acción para agregar movimiento seleccionado
        agregarBtn.addActionListener(e -> {
            for (Component c : movimientosPanel.getComponents()) {
                if (c instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) c;
                    if (cb.isSelected() && modeloLista.getSize() < 4 && !modeloLista.contains(cb.getText())) {
                        modeloLista.addElement(cb.getText());
                    }
                }
            }
        });

        // Acción para confirmar selección
        confirmarBtn.addActionListener(e -> {
            if (modeloLista.getSize() == 4) {
                movimientosSeleccionados.clear();
                for (int i = 0; i < modeloLista.size(); i++) {
                    movimientosSeleccionados.add(modeloLista.get(i));
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Debes seleccionar 4 movimientos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void mostrarMovimientos(List<String> movimientos) {
        movimientosPanel.removeAll();
        for (String mov : movimientos) {
            movimientosPanel.add(new JCheckBox(mov));
        }
        movimientosPanel.revalidate();
        movimientosPanel.repaint();
    }

    public List<String> getMovimientosSeleccionados() {
        return movimientosSeleccionados;
    }

    // Panel de fondo con imagen
    static class FondoPanel extends JPanel {
        private final Image fondo;
        public FondoPanel(String ruta) {
            fondo = new ImageIcon(ruta).getImage();
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}