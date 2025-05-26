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
    private String name;

    public HumanCoach(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(pokemons, items);
        this.name = name;

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


    /**
     * Devuelve el nombre del entrenador humano.
     * @return Nombre del entrenador.
     */
    public String getName() {
        return name;
    }
}