package domain;

import java.util.ArrayList;

/**
 * Máquina con estrategia experta.
 * Toma las mejores decisiones basadas en múltiples factores.
 */
public class ExpertMachine extends Machine {
    
    private ExpertStrategy strategy;
    private int turnsInBattle = 0;
    
    public ExpertMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.strategy = new ExpertStrategy();
        this.machineType = "Expert";
    }
    
    @Override
    public int selectMove() {
        turnsInBattle++;
        Pokemon currentPokemon = getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        // Primera ronda: evaluar si conviene un ataque de estado
        if (turnsInBattle <= 2) {
            for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
                Attack attack = currentPokemon.getAtaques().get(i);
                if (attack instanceof StatusAttack) {
                    return i;
                }
            }
        }
        
        // Analizar la situación de forma completa
        double healthRatio = (double) currentPokemon.getPs() / currentPokemon.getTotalPs();
        double opponentHealthRatio = (double) opponentPokemon.getPs() / opponentPokemon.getTotalPs();
        
        // Si el oponente está débil, usar el ataque más potente
        if (opponentHealthRatio < 0.3) {
            int bestMoveIndex = 0;
            double bestPower = 0;
            
            for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
                Attack attack = currentPokemon.getAtaques().get(i);
                
                if (!(attack instanceof StatusAttack)) {
                    double effectiveness = calculateEffectiveness(attack, opponentPokemon);
                    double power = attack.getBaseDamage() * effectiveness;
                    
                    if (power > bestPower) {
                        bestPower = power;
                        bestMoveIndex = i;
                    }
                }
            }
            
            if (bestPower > 0) {
                return bestMoveIndex;
            }
        }
        
        // Si estamos débiles pero el oponente no está a punto de derrotarnos,
        // considerar ataques defensivos o de estrategia
        if (healthRatio < 0.4 && opponentHealthRatio > 0.5) {
            // Buscar ataques que reduzcan estadísticas del oponente
            for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
                Attack attack = currentPokemon.getAtaques().get(i);
                if (attack instanceof StatusAttack) {
                    return i;
                }
            }
        }
        
        // Evaluar todos los ataques considerando efectividad, potencia y PP
        int bestOverallIndex = 0;
        double bestOverallScore = 0;
        
        for (int i = 0; i < currentPokemon.getAtaques().size(); i++) {
            Attack attack = currentPokemon.getAtaques().get(i);
            
            double effectiveness = calculateEffectiveness(attack, opponentPokemon);
            double power = attack.getBaseDamage();
            
            // Calcular una puntuación combinada
            double overallScore = effectiveness * power * (attack.getPrecision() / 100.0);
            
            // Para ataques de estado, evaluar de forma diferente
            if (attack instanceof StatusAttack) {
                overallScore = 50 * effectiveness;
            }
            
            if (overallScore > bestOverallScore) {
                bestOverallScore = overallScore;
                bestOverallIndex = i;
            }
        }
        
        return bestOverallIndex;
    }
    
    @Override
    public int selectBestPokemon() {
        Pokemon currentPokemon = getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        // Evaluar todos los factores para el cambio
        double healthRatio = (double) currentPokemon.getPs() / currentPokemon.getTotalPs();
        boolean typeDisadvantage = opponentHasTypeAdvantage();
        
        // Si tenemos desventaja de tipo y poca vida, cambiar
        if (typeDisadvantage && healthRatio < 0.5) {
            int bestChoiceIndex = getPokemonWithTypeAdvantage();
            
            // Verificar que el Pokémon elegido no esté debilitado
            if (bestChoiceIndex != activePokemonIndex && pokemons.get(bestChoiceIndex).getPs() > 0) {
                return bestChoiceIndex;
            }
        }
        
        // Si estamos muy débiles, buscar el mejor reemplazo
        if (healthRatio < 0.25) {
            // Evaluar cada Pokémon considerando vida, estadísticas y ventaja de tipo
            int bestChoiceIndex = activePokemonIndex;
            double bestScore = 0;
            
            for (int i = 0; i < pokemons.size(); i++) {
                Pokemon pokemon = pokemons.get(i);
                
                // Saltar Pokémon debilitados
                if (pokemon.getPs() <= 0) {
                    continue;
                }
                
                // Calcular ventaja de tipo
                int pokemonType = efectivity.numberType.getOrDefault(pokemon.getType(), 10);
                int opponentType = efectivity.numberType.getOrDefault(opponentPokemon.getType(), 10);
                double typeAdvantage = efectivity.efectividad(pokemonType, opponentType);
                
                // Calcular resistencia a ataques del oponente
                double defenseResistance = 1.0 / efectivity.efectividad(opponentType, pokemonType);
                
                // Calcular score general (vida * ventaja * resistencia)
                double pokemonHealthRatio = (double) pokemon.getPs() / pokemon.getTotalPs();
                double score = pokemonHealthRatio * typeAdvantage * defenseResistance;
                
                if (score > bestScore) {
                    bestScore = score;
                    bestChoiceIndex = i;
                }
            }
            
            return bestChoiceIndex;
        }
        
        // Por defecto, mantener el Pokémon actual
        return activePokemonIndex;
    }
    
    @Override
    public boolean shouldUseItem() {
        Pokemon currentPokemon = getActivePokemon();
        double healthRatio = (double) currentPokemon.getPs() / currentPokemon.getTotalPs();
        
        // Usar ítem en momentos estratégicos
        return (healthRatio < 0.4 && turnsInBattle > 3) || healthRatio < 0.25;
    }
    
    @Override
    public int selectItem() {
        Pokemon currentPokemon = getActivePokemon();
        double healthRatio = (double) currentPokemon.getPs() / currentPokemon.getTotalPs();
        
        // Si estamos muy débiles, priorizar curación
        if (healthRatio < 0.3) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.getName().contains("Poción") || item.getName().contains("Restaurar")) {
                    return i;
                }
            }
        }
        
        // Si tenemos buena salud pero estamos en desventaja, usar aumentos de estadísticas
        else if (opponentHasTypeAdvantage()) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.getName().contains("X-") || item.getName().contains("Aumento")) {
                    return i;
                }
            }
        }
        
        // Por defecto, usar el primer ítem disponible
        return !items.isEmpty() ? 0 : -1;
    }
}