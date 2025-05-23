package domain;

/**
 * MachineStrategy es una interfaz que define la estrategia a utilizar por las máquinas en el juego Poobkemon.
 * Permite implementar diferentes comportamientos automáticos para la toma de decisiones durante la batalla,
 * como seleccionar ataques, decidir el uso de ítems, cambiar de Pokémon o huir de la batalla.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Define el método para decidir la acción principal del turno (atacar, usar ítem o cambiar de Pokémon).</li>
 *   <li>Permite seleccionar el ataque más adecuado según la estrategia implementada.</li>
 *   <li>Incluye la lógica para elegir el ítem óptimo en cada situación.</li>
 *   <li>Facilita la selección del mejor Pokémon para cambiar durante la batalla.</li>
 *   <li>Permite decidir si la máquina debe huir de la batalla en situaciones desfavorables.</li>
 * </ul>
 *
 * <p>Esta interfaz debe ser implementada por todas las estrategias de máquina (defensiva, ofensiva, experta, etc.).
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public interface MachineStrategy {
    
    /**
     * Decide qué acción tomar en el turno actual.
     * @param machine Instancia de la máquina que toma la decisión.
     * @param battleArena Contexto de la batalla actual.
     * @return 1=Ataque, 2=Item, 3=Cambio de pokémon
     */
    int decideAction(Machine machine, BattleArena battleArena);
    
    /**
     * Selecciona un ataque para usar.
     * @param machine Instancia de la máquina.
     * @param battleArena Contexto de la batalla.
     * @return Nombre del ataque seleccionado
     */
    String selectAttack(Machine machine, BattleArena battleArena);
    
    /**
     * Selecciona un ítem para usar.
     * @param machine Instancia de la máquina.
     * @param battleArena Contexto de la batalla.
     * @return Nombre del ítem seleccionado
     */
    String selectItem(Machine machine, BattleArena battleArena);
    
    /**
     * Selecciona un pokémon para cambiar.
     * @param machine Instancia de la máquina.
     * @param battleArena Contexto de la batalla.
     * @return Índice del pokémon seleccionado
     */
    int selectPokemon(Machine machine, BattleArena battleArena);
    
    /**
     * Decide si huir de la batalla.
     * @param machine Instancia de la máquina.
     * @param battleArena Contexto de la batalla.
     * @return true si decide huir, false de lo contrario
     */
    boolean shouldFlee(Machine machine, BattleArena battleArena);
}