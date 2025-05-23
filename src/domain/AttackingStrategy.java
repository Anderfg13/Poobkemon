package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * AttackingStrategy implementa una estrategia agresiva para máquinas en el juego Poobkemon.
 * Esta estrategia prioriza el uso de ataques con alto daño y la ofensiva directa sobre el oponente.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Prioriza atacar en la mayoría de los turnos, salvo que la vida esté baja.</li>
 *   <li>Selecciona el ataque físico o especial más fuerte disponible, evitando ataques de estado si es posible.</li>
 *   <li>Considera cambiar de Pokémon o usar ítems si el Pokémon activo tiene poca vida.</li>
 *   <li>Prioriza ítems de curación cuando la vida está por debajo del 50%.</li>
 *   <li>Nunca huye de la batalla.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class AttackingStrategy implements MachineStrategy,Serializable {
    private static final long serialVersionUID = 1L;
    private final Random random = new Random();
    
    /**
     * Decide la acción a tomar en el turno actual.
     * Prioriza atacar, pero puede cambiar de Pokémon o usar ítem si la vida está baja.
     *
     * @param machine      Instancia de la máquina que toma la decisión.
     * @param battleArena  Contexto de la batalla actual.
     * @return Código de acción: 1 (atacar), 2 (usar ítem), 3 (cambiar Pokémon).
     */
    @Override
    public int decideAction(Machine machine, BattleArena battleArena) {
        // Prioriza atacar (80% probabilidad)
        Pokemon active = machine.getActivePokemon();
        
        // Si está muy dañado, considera cambiar o usar ítem
        if (active != null && active.getPs() < active.getTotalPs() * 0.3) {
            // 75% probabilidad de cambiar si hay otros pokémon disponibles
            if (random.nextInt(100) < 75 && machine.getPokemons().size() > 1) {
                for (Pokemon p : machine.getPokemons()) {
                    if (p != active && p.getPs() > 0) {
                        return 3; // Cambiar pokémon
                    }
                }
            }
            
            // 20% probabilidad de usar ítem si tiene disponibles
            if (random.nextInt(100) < 20 && !machine.getItems().isEmpty()) {
                return 2; // Usar ítem
            }
        }
        
        // Por defecto, ataca
        return 1;
    }
    
    /**
     * Selecciona el nombre del ataque a usar.
     * Prefiere ataques físicos o especiales con mayor daño y PP disponibles.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Nombre del ataque seleccionado, o null si no hay ataques disponibles.
     */
    @Override
    public String selectAttack(Machine machine, BattleArena battleArena) {
        Pokemon active = machine.getActivePokemon();
        List<Attack> attacks = active.getAtaques();
        
        if (attacks.isEmpty()) {
            return null;
        }
        
        // Busca el ataque más fuerte disponible
        Attack bestAttack = null;
        int maxPower = 0;
        
        for (Attack attack : attacks) {
            // Prefiere ataques físicos o especiales, no de estado
            if ((attack instanceof PhysicalAttack || attack instanceof SpecialAttack) && 
                attack.getPowerPoint() > 0) {
                
                int power = attack.getBaseDamage();
                if (power > maxPower) {
                    maxPower = power;
                    bestAttack = attack;
                }
            }
        }
        
        // Si no encontró ataque físico/especial, usa cualquiera disponible
        if (bestAttack == null) {
            for (Attack attack : attacks) {
                if (attack.getPowerPoint() > 0) {
                    bestAttack = attack;
                    break;
                }
            }
        }
        
        return bestAttack != null ? bestAttack.getName() : null;
    }
    
    /**
     * Selecciona el nombre del ítem a usar.
     * Prioriza ítems de curación si la vida está por debajo del 50%.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Nombre del ítem seleccionado, o  null si no hay ítems disponibles.
     */
    @Override
    public String selectItem(Machine machine, BattleArena battleArena) {
        List<Item> items = machine.getItems();
        
        if (items.isEmpty()) {
            return null;
        }
        
        // Prioriza ítems de curación si la vida está baja
        Pokemon active = machine.getActivePokemon();
        double healthRatio = (double) active.getPs() / active.getTotalPs();
        
        if (healthRatio < 0.5) {
            // Busca el mejor ítem de curación
            for (Item item : items) {
                if (item.getName().contains("Poción")) {
                    return item.getName();
                }
            }
        }
        
        // Si no hay ítems específicos, elige uno al azar
        return items.get(random.nextInt(items.size())).getName();
    }
    
    /**
     * Selecciona el índice del mejor Pokémon ofensivo disponible para cambiar.
     * Busca el Pokémon con mayor suma de ataque físico y especial que no esté debilitado.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Índice del Pokémon seleccionado.
     */
    @Override
    public int selectPokemon(Machine machine, BattleArena battleArena) {
        List<Pokemon> pokemons = machine.getPokemons();
        
        // Busca el pokémon con mejor ataque y que no esté debilitado
        Pokemon current = machine.getActivePokemon();
        int bestIndex = 0;
        int bestAttack = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            // No considerar el pokémon actual o los debilitados
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            // Calcula la puntuación de ataque (física + especial)
            int attackScore = p.getPhysicalAttack() + p.getSpecialAttack();
            if (attackScore > bestAttack) {
                bestAttack = attackScore;
                bestIndex = i;
            }
        }
        
        return bestIndex;
    }
    
    /**
     * Determina si la máquina debe huir de la batalla.
     * Esta estrategia nunca huye.
     *
     * @param machine      Instancia de la máquina.
     * @param battleArena  Contexto de la batalla.
     * @return Boolean  siempre, ya que esta estrategia nunca huye.
     */
    @Override
    public boolean shouldFlee(Machine machine, BattleArena battleArena) {
        // Esta estrategia nunca huye
        return false;
    }
}