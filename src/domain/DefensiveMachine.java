package domain;

import java.util.ArrayList;

public class DefensiveMachine extends Machine {
    
    public DefensiveMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
    }
    
    @Override
    public int selectMove() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Si el Pokémon tiene poca vida, prioriza ataques de estado o defensivos
        if (currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.3) {
            // Buscar ataques que aumenten defensa o modifiquen estado
            for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
                Attack attack = currentPokemon.getAtaques().get(i);
                if (attack.getCategory().equals("Status")) {
                    return i;
                }
            }
        }
        
        // Si el oponente tiene ventaja de tipo, usa ataques que sean efectivos
        if (opponentHasTypeAdvantage()) {
            return getBestEffectivenessMove();
        }
        
        // Por defecto, selecciona un ataque basado en la efectividad
        int effectiveMove = getBestEffectivenessMove();
        
        // Si no hay ataques particularmente efectivos, selecciona uno aleatorio
        if (effectiveMove == 0 && currentPokemon.getAtaques().size() > 1) {
            return random.nextInt(currentPokemon.getAtaques().size());
        }
        
        return effectiveMove;
    }
    
    @Override
    public int selectBestPokemon() {
        // Si el Pokémon actual tiene baja vida, cambia a otro con más resistencia
        Pokemon currentPokemon = getActivePokemon();
        if (currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.25) {
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
                double defensiveRatio = (pokemon.getPs() * pokemon.getDefense()) / 100.0;
                
                if (defensiveRatio > bestDefensiveRatio) {
                    bestDefensiveRatio = defensiveRatio;
                    bestDefensiveIndex = i;
                }
            }
            
            return bestDefensiveIndex;
        }
        
        // Si el oponente tiene ventaja de tipo, busca un Pokémon con ventaja
        if (opponentHasTypeAdvantage()) {
            int advantageIndex = getPokemonWithTypeAdvantage();
            if (advantageIndex != activePokemonIndex && pokemons.get(advantageIndex).getPs() > 0) {
                return advantageIndex;
            }
        }
        
        // Si no hay necesidad de cambio, mantén el Pokémon actual
        return activePokemonIndex;
    }
    
    @Override
    public boolean shouldUseItem() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Usar item si el Pokémon está por debajo del 40% de salud
        return currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.4 && !items.isEmpty();
    }
    
    @Override
    public int selectItem() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Buscar poción o item curativo
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("Poción") || item.getName().contains("Restaurar")) {
                return i;
            }
        }
        
        // Si no hay pociones, usar cualquier item
        return !items.isEmpty() ? 0 : -1;
    }
}