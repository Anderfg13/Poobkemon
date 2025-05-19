package domain;

import java.util.ArrayList;

/**
 * Máquina con enfoque ofensivo.
 * Prioriza ataques potentes y estadísticas de ataque.
 */
public class AttackingMachine extends Machine {
    
    private AttackingStrategy strategy;
    
    public AttackingMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new AttackingStrategy();
        this.machineType = "Attacking";
    }
    
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
    
    @Override
    public boolean shouldUseItem() {
        // Usar item solo cuando el Pokémon tiene baja vida pero aún es viable
        Pokemon currentPokemon = getActivePokemon();
        return currentPokemon.getPs() < currentPokemon.getTotalPs() * 0.3 && !items.isEmpty();
    }
    
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