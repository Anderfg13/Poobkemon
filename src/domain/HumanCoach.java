package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HumanCoach representa a un entrenador humano en el juego Poobkemon.
 * Permite la interacción directa del usuario para seleccionar ataques, usar ítems, cambiar de Pokémon o huir de la batalla.
 * Gestiona el equipo de Pokémon, los ítems, el Pokémon activo y la lógica de acciones disponibles para el jugador.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite al usuario seleccionar acciones mediante la interfaz (ataque, ítem, cambio, huida).</li>
 *   <li>Gestiona el equipo de Pokémon y el inventario de ítems del entrenador humano.</li>
 *   <li>Incluye métodos auxiliares para ejecutar ataques, usar ítems y cambiar de Pokémon según la selección del usuario.</li>
 *   <li>Permite establecer el oponente para interactuar durante la batalla.</li>
 *   <li>Implementa la penalización por tiempo agotado en el turno, reduciendo PP del Pokémon activo.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class HumanCoach extends Coach {
    private static final long serialVersionUID = 1L;
    private transient Map<Integer, Consumer<HumanCoach>> actionMap = new HashMap<>();
    private String name;
    private Coach opponent;

    public HumanCoach(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(pokemons, items);
        this.name = name;

        initializeActionMap();
    }

    /**
     * Inicializa el mapa de acciones disponibles para el entrenador humano.
     * Cada acción se asocia a un índice y ejecuta la lógica correspondiente.
     */
    private void initializeActionMap() {
        actionMap.put(0, hc -> {
            try {
                performAttack();
            } catch (PoobkemonException e) {
                System.out.println("Error al realizar el ataque: " + e.getMessage());
            }
        });
        actionMap.put(1, hc -> {
            try {
                performItem();
            } catch (PoobkemonException e) {
                System.out.println("Error al usar el ítem: " + e.getMessage());
            }
        });
        actionMap.put(2, hc -> {
            try {
                performSwitch();
            } catch (PoobkemonException e) {
                System.out.println("Error al cambiar de Pokémon: " + e.getMessage());
            }
        });
        actionMap.put(3, hc -> performFlee());
    }

    /**
     * Establece el nombre del entrenador.
     * @param name Nombre del entrenador.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Maneja la penalización por tiempo agotado en el turno.
     * Reduce los PP del Pokémon activo.
     */
    @Override
    public void handleTurnTimeout() {
        Pokemon active = getActivePokemon();
        if (active != null) {
            active.reducePP(); // Método a implementar en Pokemon
        }
    }

    // Métodos auxiliares

    /**
     * Ejecuta el ataque seleccionado por el usuario.
     * @throws PoobkemonException Si ocurre un error al atacar.
     */
    private void performAttack() throws PoobkemonException {
        Pokemon attacker = getActivePokemon();
        int moveIdx = getSelectedMoveIndexFromUI();

        if (moveIdx < 0 || moveIdx >= attacker.getAtaques().size()) {
            //throw new PoobkemonException(PoobkemonException.INVALID_MOVE);
        }

        Attack atk = attacker.getAtaques().get(moveIdx);
        attacker.attack(opponent.getActivePokemon(), atk);
    }

    /**
     * Usa el ítem seleccionado por el usuario sobre el Pokémon activo.
     * @throws PoobkemonException Si ocurre un error al usar el ítem.
     */
    private void performItem() throws PoobkemonException {
        int itemIdx = getSelectedItemIndexFromUI();

        if (itemIdx < 0 || itemIdx >= items.size()) {
            //throw new PoobkemonException(PoobkemonException.INVALID_ITEM);
        }

        useItem(itemIdx);
        items.remove(itemIdx);
    }

    /**
     * Aplica el efecto del ítem seleccionado al Pokémon activo.
     * @param itemIdx Índice del ítem a usar.
     * @throws PoobkemonException Si ocurre un error al aplicar el ítem.
     */
    private void useItem(int itemIdx) throws PoobkemonException {
        Item item = items.get(itemIdx);
        Pokemon target = getActivePokemon();

        if (target == null) {
            //throw new PoobkemonException(PoobkemonException.NO_ACTIVE_POKEMON);
        }

        item.applyItemEffect(target); // Método a implementar en Item
    }

    /**
     * Cambia el Pokémon activo según la selección del usuario.
     * @throws PoobkemonException Si ocurre un error al cambiar de Pokémon.
     */
    private void performSwitch() throws PoobkemonException {
        int idx = getSelectedSwitchIndexFromUI();
        switchToPokemon(idx); // Método ya implementado en Coach
    }

    /**
     * Hace que el entrenador huya de la batalla, debilitando a todos sus Pokémon.
     */
    private void performFlee() {
        for (Pokemon pokemon : pokemons) {
            pokemon.setPs(0);
        }
    }

    /**
     * Obtiene el índice del movimiento seleccionado por el usuario.
     * @return Índice del movimiento seleccionado.
     */
    private int getSelectedMoveIndexFromUI() {
        // Implementación temporal
        return 0;
    }

    /**
     * Obtiene el índice del ítem seleccionado por el usuario.
     * @return Índice del ítem seleccionado.
     */
    private int getSelectedItemIndexFromUI() {
        // Implementación temporal
        return 0;
    }

    /**
     * Obtiene el índice del Pokémon al que se desea cambiar según la selección del usuario.
     * @return Índice del Pokémon seleccionado.
     */
    private int getSelectedSwitchIndexFromUI() {
        // Implementación temporal
        return 0;
    }

    /**
     * Establece el entrenador oponente para la batalla.
     * @param opponent Entrenador oponente.
     */
    public void setOpponent(Coach opponent) {
        this.opponent = opponent;
    }

    /**
     * Devuelve el nombre del entrenador humano.
     * @return Nombre del entrenador.
     */
    public String getName() {
        return name;
    }
}