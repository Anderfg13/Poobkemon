package domain;

import java.util.ArrayList;

/**
 * Máquina con enfoque defensivo.
 * Prioriza resistencia, recuperación y ataques de estado.
 */
public class DefensiveMachine extends Machine {
    
    private DefensiveStrategy strategy;
    
    public DefensiveMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new DefensiveStrategy();
        this.machineType = "Defensive";
    }
    
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
    
    @Override
    public boolean shouldUseItem() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Usar ítem si el Pokémon está por debajo del 40% de salud
        return currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.4 && !items.isEmpty();
    }
    
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