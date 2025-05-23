package domain;

import java.util.ArrayList;

/**
 * DefensiveMachine representa una máquina controlada por IA con enfoque defensivo.
 * Esta máquina prioriza la resistencia, la recuperación y el uso de ataques de estado para debilitar al oponente y proteger a su equipo.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Prioriza ataques defensivos o de estado cuando el Pokémon activo tiene poca vida.</li>
 *   <li>Intenta cambiar a un Pokémon con mejores estadísticas defensivas si el actual está en peligro.</li>
 *   <li>Utiliza ítems curativos o defensivos cuando la salud es baja.</li>
 *   <li>Selecciona ataques con mejor efectividad si el oponente tiene ventaja de tipo.</li>
 *   <li>Si no hay ataques efectivos, selecciona uno aleatorio.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class DefensiveMachine extends Machine {
    private static final long serialVersionUID = 1L;
    private DefensiveStrategy strategy;
    
    /**
     * Crea una nueva instancia de {@code DefensiveMachine} con el nombre, equipo y objetos dados.
     *
     * @param name     Nombre de la máquina.
     * @param pokemons Lista de Pokémon que forman el equipo de la máquina.
     * @param items    Lista de nombres de ítems disponibles para la máquina.
     */
    public DefensiveMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new DefensiveStrategy();
        this.machineType = "Defensive";
    }
    
    /**
     * Selecciona el índice del mejor movimiento defensivo o de estado disponible para el Pokémon activo.
     * Prioriza ataques de estado si la vida es baja, ataques efectivos si el oponente tiene ventaja de tipo,
     * o selecciona uno aleatorio si no hay ataques particularmente efectivos.
     *
     * @return Índice del movimiento seleccionado.
     */
    @Override
    public int selectMove() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Si tiene poca vida, priorizar ataques defensivos o de estado
        if (currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.4) {
            // Buscar ataques que aumenten defensa o modifiquen estado
            for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
                Attack attack = currentPokemon.getAtaques().get(i);
                if (attack instanceof StatusAttack) {
                    return i;
                }
            }
        }
        
        // Si el oponente tiene ventaja de tipo, intentar usar ataques efectivos
        if (opponentHasTypeAdvantage()) {
            return getBestEffectivenessMove();
        }
        
        // Seleccionar ataque con mejor efectividad
        int effectiveMove = getBestEffectivenessMove();
        
        // Si no hay ataques particularmente efectivos, selecciona uno aleatorio
        if (effectiveMove == 0 && currentPokemon.getAtaques().size() > 1) {
            return random.nextInt(currentPokemon.getAtaques().size());
        }
        
        return effectiveMove;
    }
    
    /**
     * Selecciona el índice del mejor Pokémon defensivo disponible para cambiar.
     * Prioriza Pokémon con más vida y mejores estadísticas defensivas si el actual está en peligro,
     * o busca ventaja de tipo si el oponente la tiene.
     *
     * @return Índice del Pokémon seleccionado.
     */
    @Override
    public int selectBestPokemon() {
        // Si el Pokémon actual tiene baja vida, cambiar a otro con más resistencia
        Pokemon currentPokemon = getActivePokemon();
        if (currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.3) {
            // Buscar Pokémon con más vida y mejores estadísticas defensivas
            int bestDefensiveIndex = activePokemonIndex;
            double bestDefensiveRatio = 0;
            
            for (int i = 0; i < pokemons.size(); i++) {
                Pokemon pokemon = pokemons.get(i);
                
                // Saltar el Pokémon actual y los debilitados
                if (i == activePokemonIndex || pokemon.getPs() <= 0) {
                    continue;
                }
                
                // Calcular ratio defensivo (PS * Defensa / 100)
                double defensiveRatio = (pokemon.getPs() * pokemon.getPhysicalDefense()) / 100.0;
                
                if (defensiveRatio > bestDefensiveRatio) {
                    bestDefensiveRatio = defensiveRatio;
                    bestDefensiveIndex = i;
                }
            }
            
            return bestDefensiveIndex;
        }
        
        // Si el oponente tiene ventaja de tipo, buscar un Pokémon con ventaja
        if (opponentHasTypeAdvantage()) {
            int advantageIndex = getPokemonWithTypeAdvantage();
            if (advantageIndex != activePokemonIndex && pokemons.get(advantageIndex).getPs() > 0) {
                return advantageIndex;
            }
        }
        
        // Si no hay necesidad de cambio, mantener el Pokémon actual
        return activePokemonIndex;
    }
    
    /**
     * Determina si la máquina debe usar un ítem en el turno actual.
     * Usará un ítem si el Pokémon activo tiene menos del 40% de su vida total y hay ítems disponibles.
     *
     * @return {@code true} si debe usar un ítem, {@code false} en caso contrario.
     */
    @Override
    public boolean shouldUseItem() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Usar ítem si el Pokémon está por debajo del 40% de salud
        return currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.4 && !items.isEmpty();
    }
    
    /**
     * Selecciona el índice del ítem a usar.
     * Prioriza pociones o ítems curativos, luego ítems defensivos.
     * Si no hay ítems específicos, usa el primero disponible.
     *
     * @return Índice del ítem seleccionado, o -1 si no hay ítems.
     */
    @Override
    public int selectItem() {
        // Buscar poción o ítem curativo
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("Poción") || item.getName().contains("Restaurar")) {
                return i;
            }
        }
        
        // Si no hay pociones, buscar ítems defensivos
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("Defensa") || item.getName().contains("X-Defensa")) {
                return i;
            }
        }
        
        // Si no hay ítems específicos, usar el primero disponible
        return !items.isEmpty() ? 0 : -1;
    }
}