package domain;

import java.util.ArrayList;

/**
 * AttackingMachine representa una máquina controlada por IA con enfoque ofensivo.
 * Esta máquina prioriza el uso de ataques potentes y selecciona movimientos que maximicen el daño al oponente.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Selecciona el ataque más poderoso y efectivo disponible, evitando ataques de estado si es posible.</li>
 *   <li>Cambia de Pokémon buscando siempre el que tenga mejores estadísticas ofensivas o ventaja de tipo.</li>
 *   <li>Utiliza ítems ofensivos o curativos cuando el Pokémon activo tiene poca vida.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class AttackingMachine extends Machine {
    
    private AttackingStrategy strategy;
    
    /**
     * Crea una nueva instancia de {@code AttackingMachine} con el nombre, equipo y objetos dados.
     *
     * @param name     Nombre de la máquina.
     * @param pokemons Lista de Pokémon que forman el equipo de la máquina.
     * @param items    Lista de nombres de ítems disponibles para la máquina.
     */
    public AttackingMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new AttackingStrategy();
        this.machineType = "Attacking";
    }
    
    /**
     * Selecciona el índice del mejor movimiento ofensivo disponible para el Pokémon activo.
     * Prefiere ataques físicos o especiales sobre los de estado y maximiza el daño potencial.
     *
     * @return Índice del movimiento seleccionado.
     */
    @Override
    public int selectMove() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Siempre busca el ataque más poderoso y efectivo
        int bestMoveIndex = 0;
        double bestDamage = 0;
        
        for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
            Attack attack = currentPokemon.getAtaques().get(i);
            
            // Preferir ataques físicos o especiales sobre los de estado
            if (attack instanceof StatusAttack) {
                continue;
            }
            
            // Calcular daño potencial (potencia * efectividad)
            double effectiveness = calculateEffectiveness(attack, opponent.getActivePokemon());
            double potentialDamage = attack.getBaseDamage() * effectiveness;
            
            if (potentialDamage > bestDamage) {
                bestDamage = potentialDamage;
                bestMoveIndex = i;
            }
        }
        
        // Si no encontró ningún buen ataque, usar el más efectivo
        if (bestDamage == 0) {
            return getBestEffectivenessMove();
        }
        
        return bestMoveIndex;
    }
    
    /**
     * Selecciona el índice del mejor Pokémon ofensivo disponible para cambiar.
     * Busca ventaja de tipo o mejores estadísticas de ataque y velocidad.
     *
     * @return Índice del Pokémon seleccionado.
     */
    @Override
    public int selectBestPokemon() {
        // Si el oponente tiene ventaja de tipo, buscar un Pokémon con ventaja
        if (opponentHasTypeAdvantage()) {
            int advantageIndex = getPokemonWithTypeAdvantage();
            if (advantageIndex != activePokemonIndex && pokemons.get(advantageIndex).getPs() > 0) {
                return advantageIndex;
            }
        }
        
        // Buscar Pokémon con mejores estadísticas de ataque
        int bestOffensiveIndex = activePokemonIndex;
        double bestOffensiveRatio = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);
            
            // Saltar Pokémon debilitados y el actual
            if (i == activePokemonIndex || pokemon.getPs() <= 0) {
                continue;
            }
            
            // Calcular ratio ofensivo (Ataque * Velocidad / 100)
            double offensiveRatio = (pokemon.getPhysicalAttack() * pokemon.getSpeed()) / 100.0;
            
            if (offensiveRatio > bestOffensiveRatio) {
                bestOffensiveRatio = offensiveRatio;
                bestOffensiveIndex = i;
            }
        }
        
        return bestOffensiveIndex;
    }
    
    /**
     * Determina si la máquina debe usar un ítem en el turno actual.
     * Usará un ítem si el Pokémon activo tiene menos del 30% de su vida total y hay ítems disponibles.
     *
     * @return {@code true} si debe usar un ítem, {@code false} en caso contrario.
     */
    @Override
    public boolean shouldUseItem() {
        // Usar item solo cuando el Pokémon tiene baja vida pero aún es viable
        Pokemon currentPokemon = getActivePokemon();
        return currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.3 && !items.isEmpty();
    }
    
    /**
     * Selecciona el índice del ítem a usar.
     * Prioriza ítems que aumentan el ataque ("X-Ataque") y luego curativos ("Poción").
     * Si no hay ítems específicos, usa el primero disponible.
     *
     * @return Índice del ítem seleccionado, o -1 si no hay ítems.
     */
    @Override
    public int selectItem() {
        // Buscar X-Ataque o ítem que aumente estadísticas ofensivas
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("X-Ataque") || item.getName().contains("Ataque")) {
                return i;
            }
        }
        
        // Si no hay ítems de ataque, usar curativos
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("Poción")) {
                return i;
            }
        }
        
        // Si no hay ningún ítem específico, usar el primero disponible
        return !items.isEmpty() ? 0 : -1;
    }
}