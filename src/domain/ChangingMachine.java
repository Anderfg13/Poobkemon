package domain;

import java.util.ArrayList;

/**
 * Máquina que cambia constantemente de estrategia y Pokémon.
 * Se adapta según el Pokémon oponente.
 */
public class ChangingMachine extends Machine {
    
    private ChangingStrategy strategy;
    private int turnCounter = 0;
    
    public ChangingMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new ChangingStrategy();
        this.machineType = "Changing";
    }
    
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
    
    @Override
    public boolean shouldUseItem() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Usar ítem si el Pokémon tiene poca vida o cada 3 turnos
        return (currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.35 || 
                turnCounter % 3 == 0) && !items.isEmpty();
    }
    
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