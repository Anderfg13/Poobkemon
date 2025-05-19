package domain;

/**
 * Interfaz que define la estrategia a utilizar por las máquinas.
 */
public interface MachineStrategy {
    
    /**
     * Decide qué acción tomar en el turno actual.
     * @return 1=Ataque, 2=Item, 3=Cambio de pokémon
     */
    int decideAction(Machine machine, BattleArena battleArena);
    
    /**
     * Selecciona un ataque para usar.
     * @return Nombre del ataque seleccionado
     */
    String selectAttack(Machine machine, BattleArena battleArena);
    
    /**
     * Selecciona un ítem para usar.
     * @return Nombre del ítem seleccionado
     */
    String selectItem(Machine machine, BattleArena battleArena);
    
    /**
     * Selecciona un pokémon para cambiar.
     * @return Índice del pokémon seleccionado
     */
    int selectPokemon(Machine machine, BattleArena battleArena);
    
    /**
     * Decide si huir de la batalla.
     * @return true si decide huir, false de lo contrario
     */
    boolean shouldFlee(Machine machine, BattleArena battleArena);
}