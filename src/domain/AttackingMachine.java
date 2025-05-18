package domain;

import java.util.ArrayList;
import java.util.List;

public class AttackingMachine extends Machine {
    
    public AttackingMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
    }
    
    @Override
    public int selectMove() {
        Pokemon currentPokemon = getActivePokemon();
        
        // Siempre busca el ataque más poderoso y efectivo
        int bestMoveIndex = 0;
        double bestDamage = 0;
        
        List<Attack> attacks = currentPokemon.getAtaques();
        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            
            // Ignorar ataques de estado, prefiere ataques físicos o especiales
            if (attack.getCategory().equals("Status")) {
                continue;
            }
            
            // Calcular daño potencial (potencia * efectividad)
            double effectiveness = calculateEffectiveness(attack, opponent.getActivePokemon());
            double potentialDamage = attack.getPower() * effectiveness;
            
            if (potentialDamage > bestDamage) {
                bestDamage = potentialDamage;
                bestMoveIndex = i;
            }
        }
        
        // Si no se encontró un buen ataque, usa el más efectivo
        if (bestDamage == 0) {
            return getBestEffectivenessMove();
        }
        
        return bestMoveIndex;
    }
    
    @Override
    public int selectBestPokemon() {
        // Elige el Pokémon con mejores estadísticas ofensivas
        if (opponentHasTypeAdvantage()) {
            // Si el oponente tiene ventaja, buscar un Pokémon con ventaja de tipo
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
            
            // Saltar Pokémon debilitados
            if (pokemon.getPs() <= 0) {
                continue;
            }
            
            // Calcular ratio ofensivo (Ataque * Velocidad / 100)
            double offensiveRatio = (pokemon.getAttack() * pokemon.getSpeed()) / 100.0;
            
            if (offensiveRatio > bestOffensiveRatio) {
                bestOffensiveRatio = offensiveRatio;
                bestOffensiveIndex = i;
            }
        }
        
        return bestOffensiveIndex;
    }
    
    @Override
    public boolean shouldUseItem() {
        // Los atacantes raramente usan ítems, solo cuando están muy bajos de salud
        Pokemon currentPokemon = getActivePokemon();
        return currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.2 && !items.isEmpty();
    }
    
    @Override
    public int selectItem() {
        // Buscar X-Ataque o item que aumente estadísticas ofensivas
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("X-Ataque") || item.getName().contains("Más Ataque")) {
                return i;
            }
        }
        
        // Si no hay items de ataque, usar curativos
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().contains("Poción")) {
                return i;
            }
        }
        
        // Si no hay ítems específicos, usar cualquiera
        return !items.isEmpty() ? 0 : -1;
    }
}