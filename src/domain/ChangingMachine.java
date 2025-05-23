package domain;

import java.util.ArrayList;

/**
 * ChangingMachine representa una máquina controlada por IA que cambia constantemente de estrategia y Pokémon.
 * Esta máquina se adapta dinámicamente según el Pokémon oponente, alternando entre ataques de estado y ofensivos,
 * y eligiendo el mejor Pokémon disponible según la ventaja de tipo.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Cambia de estrategia cada turno, alternando entre ataques de estado y ataques efectivos.</li>
 *   <li>Selecciona el Pokémon con mayor ventaja de tipo y resistencia frente al oponente.</li>
 *   <li>Decide el uso de ítems en función de la vida actual o el turno.</li>
 *   <li>Utiliza potenciadores de ataque o defensa según la situación y la ventaja de tipo.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class ChangingMachine extends Machine {
    private static final long serialVersionUID = 1L;
    
    private ChangingStrategy strategy;
    private int turnCounter = 0;
    
    /**
     * Crea una nueva instancia de {@code ChangingMachine} con el nombre, equipo y objetos dados.
     *
     * @param name     Nombre de la máquina.
     * @param pokemons Lista de Pokémon que forman el equipo de la máquina.
     * @param items    Lista de nombres de ítems disponibles para la máquina.
     */
    public ChangingMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new ChangingStrategy();
        this.machineType = "Changing";
    }
    
    /**
     * Selecciona el índice del mejor movimiento disponible para el Pokémon activo.
     * Alterna entre ataques de estado y ataques efectivos según el turno.
     *
     * @return Índice del movimiento seleccionado.
     */
    @Override
    public int selectMove() {
        turnCounter++;
        Pokemon currentPokemon = getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        // Cada 2 turnos, considera usar un ataque de estado
        if (turnCounter % 2 == 0) {
            for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
                Attack attack = currentPokemon.getAtaques().get(i);
                if (attack instanceof StatusAttack) {
                    return i;
                }
            }
        }
        
        // Buscar el ataque con mejor efectividad contra el tipo del oponente
        int bestMoveIndex = 0;
        double bestEffectiveness = 0;
        
        for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
            Attack attack = currentPokemon.getAtaques().get(i);
            double effectiveness = calculateEffectiveness(attack, opponentPokemon);
            
            if (effectiveness > bestEffectiveness) {
                bestEffectiveness = effectiveness;
                bestMoveIndex = i;
            }
        }
        
        return bestMoveIndex;
    }
    
    /**
     * Selecciona el índice del mejor Pokémon disponible para cambiar, priorizando la ventaja de tipo.
     * Cambia solo si existe una clara ventaja de tipo (1.5x o mejor).
     *
     * @return Índice del Pokémon seleccionado.
     */
    @Override
    public int selectBestPokemon() {
        // Esta máquina siempre intenta cambiar al Pokémon con mejor tipo
        // contra el Pokémon actual del oponente
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        if (opponentPokemon == null) {
            return activePokemonIndex;
        }
        
        int bestPokemonIndex = activePokemonIndex;
        double bestTypeAdvantage = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            // Saltar el Pokémon actual y los debilitados
            if (i == activePokemonIndex || pokemons.get(i).getPs() <= 0) {
                continue;
            }
            
            int pokemonType = efectivity.numberType.getOrDefault(pokemons.get(i).getType(), 10);
            int opponentType = efectivity.numberType.getOrDefault(opponentPokemon.getType(), 10);
            double typeAdvantage = efectivity.efectividad(pokemonType, opponentType);
            
            // También considerar la resistencia a los ataques del oponente
            double defenseAdvantage = 1.0 / efectivity.efectividad(opponentType, pokemonType);
            double combinedAdvantage = typeAdvantage * defenseAdvantage;
            
            if (combinedAdvantage > bestTypeAdvantage) {
                bestTypeAdvantage = combinedAdvantage;
                bestPokemonIndex = i;
            }
        }
        
        // Cambiar solo si hay una clara ventaja de tipo (1.5x o mejor)
        if (bestTypeAdvantage >= 1.5) {
            return bestPokemonIndex;
        }
        
        // Si no hay una clara ventaja, mantener el Pokémon actual
        return activePokemonIndex;
    }
    
    /**
     * Determina si la máquina debe usar un ítem en el turno actual.
     * Usará un ítem si el Pokémon tiene poca vida o cada 3 turnos.
     *
     * @return {@code true} si debe usar un ítem, {@code false} en caso contrario.
     */
    @Override
    public boolean shouldUseItem() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Usar ítem si el Pokémon tiene poca vida o cada 3 turnos
        return (currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.35 || 
                turnCounter % 3 == 0) && !items.isEmpty();
    }
    
    /**
     * Selecciona el índice del ítem a usar.
     * Prioriza pociones si la vida es baja, potenciadores de ataque si hay ventaja de tipo,
     * y potenciadores de defensa si hay desventaja de tipo.
     * Si no hay ítems específicos, usa el primero disponible.
     *
     * @return Índice del ítem seleccionado, o -1 si no hay ítems.
     */
    @Override
    public int selectItem() {
        Pokemon currentPokemon = getActivePokemon();
        double healthRatio = (double) currentPokemon.getPs() / currentPokemon.getTotalPs();
        
        // Si tiene poca vida, usar poción
        if (healthRatio < 0.4) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.getName().contains("Poción")) {
                    return i;
                }
            }
        } 
        // Si tiene buena vida, usar potenciadores según la situación
        else {
            // Si tiene ventaja de tipo, potenciar el ataque
            if (!opponentHasTypeAdvantage()) {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    if (item.getName().contains("Ataque")) {
                        return i;
                    }
                }
            } 
            // Si tiene desventaja, potenciar la defensa
            else {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    if (item.getName().contains("Defensa")) {
                        return i;
                    }
                }
            }
        }
        
        // Si no hay ítems específicos, usar el primero disponible
        return !items.isEmpty() ? 0 : -1;
    }
}