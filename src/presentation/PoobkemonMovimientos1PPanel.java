package presentation;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * PoobkemonMovimientos1PPanel es un panel de selección de movimientos para el modo 1 jugador en Poobkemon.
 * Permite al usuario asignar 4 movimientos a cada uno de sus Pokémon antes de iniciar la batalla contra la máquina.
 * Gestiona la validación de la selección, la transición a la batalla y la integración con la interfaz principal.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Muestra los Pokémon del jugador y permite seleccionar sus movimientos individualmente.</li>
 *   <li>Valida que cada Pokémon tenga exactamente 4 movimientos antes de continuar.</li>
 *   <li>Permite regresar a la pantalla anterior o finalizar la configuración para iniciar la batalla.</li>
 *   <li>Integra la selección del tipo de máquina rival y la configuración de la batalla en el dominio.</li>
 *   <li>Actualiza visualmente el estado de selección de movimientos para cada Pokémon.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PoobkemonMovimientos1PPanel extends BackgroundPanel {
    private final String nombreJugador;
    private final List<String> pokemonesJugador;
    private final List<String> itemsJugador;
    private final Map<String, List<String>> movimientosSeleccionados = new HashMap<>();
    private final PoobkemonGUI app;
    private JPanel centerPanel;

    /**
     * Constructor del panel de selección de movimientos para el modo 1 jugador.
     *
     * @param app Referencia a la interfaz gráfica principal de Poobkemon.
     * @param nombreJugador Nombre del jugador humano.
     * @param pokemonesJugador Lista de nombres de los Pokémon seleccionados por el jugador.
     * @param itemsJugador Lista de nombres de los ítems seleccionados por el jugador.
     */
    public PoobkemonMovimientos1PPanel(PoobkemonGUI app, String nombreJugador, List<String> pokemonesJugador, List<String> itemsJugador) {
        super("mult/Fondos/Pokemon_NormalSelection.jpg");
        this.app = app;
        this.nombreJugador = nombreJugador;
        this.pokemonesJugador = pokemonesJugador;
        this.itemsJugador = itemsJugador; // <-- Guarda la lista

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

            // Preguntar contra qué tipo de máquina quiere pelear
            String tipoMaquina = PoobkemonSeleccionPanel.seleccionarTipoMaquinaStatic(this, "Seleccionar tipo de Máquina");

            // Prepara los movimientos en el formato adecuado
            String[][] movimientosPorPokemon = new String[pokemonesJugador.size()][4];
            for (int i = 0; i < pokemonesJugador.size(); i++) {
                List<String> movs = movimientosSeleccionados.get(pokemonesJugador.get(i));
                for (int j = 0; j < 4; j++) {
                    movimientosPorPokemon[i][j] = movs.get(j);
                }
            }

            try {
                // Configurar la batalla
                app.getPoobkemon().startBattleHumanVsMachine(
                    nombreJugador,
                    "CPU " + tipoMaquina,
                    new ArrayList<>(pokemonesJugador),
                    new ArrayList<>(), // pokemones de la máquina se generan en el dominio
                    new ArrayList<>(itemsJugador),
                    movimientosPorPokemon,
                    tipoMaquina
                );

                // Determinar quién empieza según la moneda
                boolean jugador1Empieza = app.getPoobkemon().whoStarts();
                String monedaElegida = app.getMonedaElegidaJugador1();

                // Si el resultado de la moneda es "Sello" y el jugador 2 empieza, ajusta el turno inicial
                if (!jugador1Empieza && "Sello".equalsIgnoreCase(monedaElegida)) {
                    jugador1Empieza = true;
                }

                // Crear el panel de batalla con las posiciones visuales fijas
                PoobkemonBattlePanel battlePanel = new PoobkemonBattlePanel(
                    app.getPoobkemon(), app,
                    app.getColorJugador1(), app.getColorJugador2(), jugador1Empieza
                );
                app.cambiarPantallaConPanel(battlePanel, "battle");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al iniciar la batalla: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton volver = new GradientButton("Volver");
        volver.setFont(new Font("Times New Roman", Font.BOLD, 20));
        volver.addActionListener(e -> app.cambiarPantallaConPanel(new PoobkemonSeleccion1PPanel(app, nombreJugador), "selection1p"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(volver);
        bottomPanel.add(finalizar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea un panel con los botones de los Pokémon del jugador, permitiendo seleccionar sus movimientos.
     * Actualiza el borde del botón según si el Pokémon tiene 4 movimientos asignados.
     *
     * @param nombreJugador Nombre del jugador.
     * @param pokemones Lista de nombres de los Pokémon del jugador.
     * @param movimientosSeleccionados Mapa de movimientos seleccionados por Pokémon.
     * @return JPanel con los botones de selección de movimientos para cada Pokémon.
     */
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