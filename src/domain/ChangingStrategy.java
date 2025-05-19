package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Estrategia que prioriza el cambio de Pokémon para obtener ventaja de tipo.
 */
public class ChangingStrategy implements MachineStrategy {
    
    private final Random random = new Random();
    private int turnCounter = 0;
    
    @Override
    public int decideAction(Machine machine, BattleArena battleArena) {
        turnCounter++;
        Pokemon active = machine.getActivePokemon();
        Pokemon opponentActive = getOpponentPokemon(machine, battleArena);
        
        // Si no hay información del oponente, atacar
        if (opponentActive == null) {
            return 1;
        }
        
        // Verificar si tenemos desventaja de tipo
        boolean hasTypeDisadvantage = hasTypeDisadvantage(active, opponentActive);
        
        // Si tenemos desventaja de tipo, consideramos cambiar (70% de probabilidad)
        if (hasTypeDisadvantage && random.nextInt(100) < 70) {
            // Buscar un Pokémon con ventaja de tipo
            int betterPokemonIndex = findBetterPokemon(machine, opponentActive);
            if (betterPokemonIndex >= 0) {
                return 3; // Cambiar Pokémon
            }
        }
        
        // Cada 3 turnos, considera cambiar de Pokémon para confundir al oponente
        if (turnCounter % 3 == 0 && machine.getPokemons().size() > 1 && random.nextInt(100) < 40) {
            return 3; // Cambiar Pokémon
        }
        
        // Si el Pokémon está debilitado, usar ítem o cambiar
        if (active.getPs() < active.getTotalPs() * 0.3) {
            if (!machine.getItems().isEmpty() && random.nextInt(100) < 40) {
                return 2; // Usar ítem
            }
        }
        
        // Por defecto, atacar
        return 1;
    }
    
    @Override
    public String selectAttack(Machine machine, BattleArena battleArena) {
        Pokemon active = machine.getActivePokemon();
        Pokemon opponent = getOpponentPokemon(machine, battleArena);
        List<Attack> attacks = active.getAtaques();
        
        if (attacks.isEmpty()) {
            return null;
        }
        
        // Seleccionar ataque con mejor efectividad de tipo
        Attack bestAttack = null;
        double bestEffectiveness = 0;
        
        for (Attack attack : attacks) {
            if (attack.getPowerPoint() <= 0) continue;
            
            double effectiveness = 1.0;
            if (opponent != null) {
                int attackType = efectivity.numberType.getOrDefault(attack.getType(), 10);
                int defenderType = efectivity.numberType.getOrDefault(opponent.getType(), 10);
                effectiveness = efectivity.efectividad(attackType, defenderType);
            }
            
            // Combinar efectividad con potencia de ataque
            double attackScore = attack.getBaseDamage() * effectiveness;
            
            if (bestAttack == null || attackScore > bestEffectiveness) {
                bestAttack = attack;
                bestEffectiveness = attackScore;
            }
        }
        
        return bestAttack != null ? bestAttack.getName() : null;
    }
    
    @Override
    public String selectItem(Machine machine, BattleArena battleArena) {
        List<Item> items = machine.getItems();
        
        if (items.isEmpty()) {
            return null;
        }
        
        // Priorizar ítems de curación
        Pokemon active = machine.getActivePokemon();
        double healthRatio = (double) active.getPs() / active.getTotalPs();
        
        if (healthRatio < 0.5) {
            for (Item item : items) {
                if (item.getName().contains("Poción")) {
                    return item.getName();
                }
            }
        }
        
        // Si no hay pociones, elegir cualquier ítem
        return items.get(random.nextInt(items.size())).getName();
    }
    
    @Override
    public int selectPokemon(Machine machine, BattleArena battleArena) {
        Pokemon opponentActive = getOpponentPokemon(machine, battleArena);
        
        if (opponentActive == null) {
            // Sin información del oponente, seleccionar Pokémon saludable
            return selectHealthyPokemon(machine);
        }
        
        // Buscar el Pokémon con mejor ventaja de tipo
        return findBetterPokemon(machine, opponentActive);
    }
    
    @Override
    public boolean shouldFlee(Machine machine, BattleArena battleArena) {
        // Esta estrategia puede huir si todos los Pokémon tienen desventaja
        Pokemon opponentActive = getOpponentPokemon(machine, battleArena);
        
        if (opponentActive == null) {
            return false;
        }
        
        // Contar cuántos Pokémon tienen ventaja
        int pokemonWithAdvantage = 0;
        for (Pokemon p : machine.getPokemons()) {
            if (p.getPs() > 0 && !hasTypeDisadvantage(p, opponentActive)) {
                pokemonWithAdvantage++;
            }
        }
        
        // Si ningún Pokémon tiene ventaja y estamos en mala situación, considerar huir
        if (pokemonWithAdvantage == 0) {
            return random.nextInt(100) < 5; // 5% de probabilidad
        }
        
        return false;
    }
    
    // Métodos auxiliares
    
    private Pokemon getOpponentPokemon(Machine machine, BattleArena battleArena) {
        for (Coach coach : battleArena.getCoaches()) {
            if (coach != machine) {
                return coach.getActivePokemon();
            }
        }
        return null;
    }
    
    private boolean hasTypeDisadvantage(Pokemon attacker, Pokemon defender) {
        if (attacker == null || defender == null) return false;
        
        int attackerType = efectivity.numberType.getOrDefault(attacker.getType(), 10);
        int defenderType = efectivity.numberType.getOrDefault(defender.getType(), 10);
        
        // Si la efectividad es menor que 1, tenemos desventaja
        return efectivity.efectividad(attackerType, defenderType) < 1.0;
    }
    
    private int findBetterPokemon(Machine machine, Pokemon opponent) {
        if (opponent == null) return -1;
        
        List<Pokemon> pokemons = machine.getPokemons();
        Pokemon current = machine.getActivePokemon();
        int bestIndex = -1;
        double bestAdvantage = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            // No considerar el Pokémon actual o los debilitados
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            int pokemonType = efectivity.numberType.getOrDefault(p.getType(), 10);
            int opponentType = efectivity.numberType.getOrDefault(opponent.getType(), 10);
            double typeAdvantage = efectivity.efectividad(pokemonType, opponentType);
            
            if (typeAdvantage > bestAdvantage) {
                bestAdvantage = typeAdvantage;
                bestIndex = i;
            }
        }
        
        return bestIndex >= 0 ? bestIndex : selectHealthyPokemon(machine);
    }
    
    private int selectHealthyPokemon(Machine machine) {
        List<Pokemon> pokemons = machine.getPokemons();
        Pokemon current = machine.getActivePokemon();
        int bestIndex = 0;
        double bestHealth = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            double healthRatio = (double) p.getPs() / p.getTotalPs();
            if (healthRatio > bestHealth) {
                bestHealth = healthRatio;
                bestIndex = i;
            }
        }
        
        return bestIndex;
    }
}