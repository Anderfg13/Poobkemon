package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DefensiveStrategy implementa una estrategia defensiva para máquinas en el juego Poobkemon.
 * Esta estrategia prioriza la supervivencia, el uso de ítems curativos o defensivos y la selección de ataques de estado.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Si la vida del Pokémon activo es baja, prioriza el uso de ítems o el cambio de Pokémon.</li>
 *   <li>Prefiere ataques de estado para debilitar al oponente o fortalecer la defensa propia.</li>
 *   <li>Selecciona ataques basados en la efectividad de tipo cuando no hay ataques de estado disponibles.</li>
 *   <li>Elige ítems de curación o defensa antes que otros tipos de ítems.</li>
 *   <li>Puede huir de la batalla si la situación es muy desfavorable (baja probabilidad).</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class DefensiveStrategy implements MachineStrategy, Serializable {
    private static final long serialVersionUID = 1L;
    private final Random random = new Random();
    
    /**
     * Decide la acción a tomar en el turno actual.
     * Puede atacar, usar ítem o cambiar de Pokémon según la vida y los recursos disponibles.
     *
     * @param machine      Instancia de la máquina que toma la decisión.
     * @param battleArena  Contexto de la batalla actual.
     * @return Código de acción: 1 (atacar), 2 (usar ítem), 3 (cambiar Pokémon).
     */
    @Override
    public int decideAction(Machine machine, BattleArena battleArena) {
        Pokemon active = machine.getActivePokemon();
        
        // Si la vida está por debajo del 50%, considera opciones defensivas
        if (active != null && active.getPs() < active.getTotalPs() * 0.5) {
            // 50% probabilidad de usar ítem si tiene disponibles
            if (random.nextInt(100) < 50 && !machine.getItems().isEmpty()) {
                return 2; // Usar ítem
            }
            
            // 30% probabilidad de cambiar si hay otros pokémon disponibles
            if (random.nextInt(100) < 30 && machine.getPokemons().size() > 1) {
                for (Pokemon p : machine.getPokemons()) {
                    if (p != active && p.getPs() > 0) {
                        return 3; // Cambiar pokémon
                    }
                }
            }
        }
        
        // Por defecto, ataca
        return 1;
    }
    
    /**
     * Selecciona el nombre del ataque a usar.
     * Prioriza ataques de estado, luego ataques efectivos según el tipo del oponente,
     * y finalmente uno aleatorio si no hay opciones claras.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Nombre del ataque seleccionado, o {@code null} si no hay ataques disponibles.
     */
    @Override
    public String selectAttack(Machine machine, BattleArena battleArena) {
        Pokemon active = machine.getActivePokemon();
        List<Attack> attacks = active.getAtaques();
        
        if (attacks.isEmpty()) {
            return null;
        }
        
        // Prioriza ataques de estado primero
        for (Attack attack : attacks) {
            if (attack instanceof StatusAttack && attack.getPowerPoint() > 0) {
                return attack.getName();
            }
        }
        
        // Si no hay ataques de estado, elige uno basado en efectividad
        Pokemon opponentPokemon = null;
        for (Coach coach : battleArena.getCoaches()) {
            if (coach != machine) {
                opponentPokemon = coach.getActivePokemon();
                break;
            }
        }
        
        if (opponentPokemon != null) {
            // Busca el ataque más efectivo contra el tipo del oponente
            Attack bestAttack = null;
            double bestEffectiveness = 0;
            
            for (Attack attack : attacks) {
                if (attack.getPowerPoint() <= 0) continue;
                
                int attackType = efectivity.numberType.getOrDefault(attack.getType(), 10);
                int defenderType = efectivity.numberType.getOrDefault(opponentPokemon.getType(), 10);
                double effectiveness = efectivity.efectividad(attackType, defenderType);
                
                if (effectiveness > bestEffectiveness) {
                    bestEffectiveness = effectiveness;
                    bestAttack = attack;
                }
            }
            
            if (bestAttack != null) {
                return bestAttack.getName();
            }
        }
        
        // Si no pudo determinar por efectividad, elige un ataque al azar con PP
        List<Attack> availableAttacks = new ArrayList<>();
        for (Attack attack : attacks) {
            if (attack.getPowerPoint() > 0) {
                availableAttacks.add(attack);
            }
        }
        
        if (!availableAttacks.isEmpty()) {
            return availableAttacks.get(random.nextInt(availableAttacks.size())).getName();
        }
        
        return null;
    }
    
    /**
     * Selecciona el nombre del ítem a usar.
     * Prioriza ítems de curación o defensa, y si no hay, elige uno al azar.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Nombre del ítem seleccionado, o {@code null} si no hay ítems disponibles.
     */
    @Override
    public String selectItem(Machine machine, BattleArena battleArena) {
        List<Item> items = machine.getItems();
        
        if (items.isEmpty()) {
            return null;
        }
        
        // Prioriza ítems defensivos o de curación
        for (Item item : items) {
            if (item.getName().contains("Poción") || 
                item.getName().contains("Revivir")) {
                return item.getName();
            }
        }
        
        // Si no hay ítems específicos, elige uno al azar
        return items.get(random.nextInt(items.size())).getName();
    }
    
    /**
     * Selecciona el índice del mejor Pokémon defensivo disponible para cambiar.
     * Busca el Pokémon con mayor suma de defensa física y especial que no esté debilitado.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Índice del Pokémon seleccionado.
     */
    @Override
    public int selectPokemon(Machine machine, BattleArena battleArena) {
        List<Pokemon> pokemons = machine.getPokemons();
        
        // Busca el pokémon con mejor defensa y que no esté debilitado
        Pokemon current = machine.getActivePokemon();
        int bestIndex = 0;
        int bestDefense = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            // No considerar el pokémon actual o los debilitados
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            // Calcula la puntuación de defensa (física + especial)
            int defenseScore = p.getPhysicalDefense() + p.getSpecialDefense();
            if (defenseScore > bestDefense) {
                bestDefense = defenseScore;
                bestIndex = i;
            }
        }
        
        return bestIndex;
    }
    
    /**
     * Determina si la máquina debe huir de la batalla.
     * Puede huir con baja probabilidad si la vida del Pokémon activo es muy baja.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return {@code true} si decide huir, {@code false} en caso contrario.
     */
    @Override
    public boolean shouldFlee(Machine machine, BattleArena battleArena) {
        // 5% de probabilidad de huir si la situación es muy desfavorable
        Pokemon active = machine.getActivePokemon();
        if (active != null && active.getPs() < active.getTotalPs() * 0.2) {
            return random.nextInt(100) < 5;
        }
        return false;
    }
}