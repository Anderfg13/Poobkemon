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
        setContentPane(new FondoPanel("mult/Fondos/Pokemon_ChooseAttacks.jpeg"));
        setLayout(null);
        setSize(500, 500);
        setLocationRelativeTo(parent);

        // GIF del pokémon
        JLabel gifLabel = new JLabel(new ImageIcon("mult/gifs/" + pokemon + ".gif"));
        gifLabel.setBounds(30, 120, 120, 120);
        gifLabel.setOpaque(false);
        add(gifLabel);

        // Panel de botones de tipo
        JPanel tiposPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        tiposPanel.setOpaque(false);
        tiposPanel.setBounds(160, 20, 300, 32);

        JButton fisicoBtn = new GradientButton("Fisico");
        JButton especialBtn = new GradientButton("Especial");
        JButton statusBtn = new GradientButton("Status");

        tiposPanel.add(fisicoBtn);
        tiposPanel.add(especialBtn);
        tiposPanel.add(statusBtn);

        add(tiposPanel);

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

        // Botones Agregar, Eliminar y Confirmar
        agregarBtn = new GradientButton("Agregar");
        JButton eliminarBtn = new GradientButton("Eliminar");
        confirmarBtn = new GradientButton("Confirmar");
        agregarBtn.setBounds(60, 340, 100, 32);
        eliminarBtn.setBounds(190, 340, 100, 32);
        confirmarBtn.setBounds(320, 340, 120, 32);
        add(agregarBtn);
        add(eliminarBtn);
        add(confirmarBtn);

        // Acción para mostrar ataques físicos
        fisicoBtn.addActionListener(e -> mostrarMovimientos(Poobkemon.getPhysicalAttacks()));
        especialBtn.addActionListener(e -> mostrarMovimientos(Poobkemon.getSpecialAttacks()));
        statusBtn.addActionListener(e -> mostrarMovimientos(Poobkemon.getStatusAttacks()));

        // Acción para agregar movimiento seleccionado
        agregarBtn.addActionListener(e -> {
            boolean added = false;
            for (Component c : movimientosPanel.getComponents()) {
                if (c instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) c;
                    if (cb.isSelected()) {
                        if (modeloLista.contains(cb.getText())) {
                            JOptionPane.showMessageDialog(this,
                                "No puedes seleccionar el mismo ataque más de una vez.",
                                "Ataque repetido",
                                JOptionPane.WARNING_MESSAGE);
                        } else if (modeloLista.getSize() < 4) {
                            modeloLista.addElement(cb.getText());
                            added = true;
                        }
                    }
                }
            }
            if (!added && modeloLista.getSize() >= 4) {
                JOptionPane.showMessageDialog(this,
                    "Solo puedes seleccionar 4 ataques.",
                    "Límite alcanzado",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        // Acción para eliminar movimiento seleccionado
        eliminarBtn.addActionListener(e -> {
            int idx = listaSeleccionados.getSelectedIndex();
            if (idx != -1) {
                modeloLista.remove(idx);
            }
        });

        // Acción para confirmar selección
        confirmarBtn.addActionListener(e -> {
            if (modeloLista.getSize() == 4) {
                movimientosSeleccionados.clear();
                for (int i = 0; i < modeloLista.size(); i++) {
                    movimientosSeleccionados.add(modeloLista.get(i));
                }
                // Imprime en consola el nombre del pokémon y los ataques seleccionados
                System.out.println("Movimientos seleccionados para " + pokemon + ":");
                for (String mov : movimientosSeleccionados) {
                    System.out.println("  - " + mov);
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
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}