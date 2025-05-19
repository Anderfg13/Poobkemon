package domain;

import java.util.List;
import java.util.Random;

/**
 * Estrategia agresiva que prioriza ataques con alto daño.
 */
public class AttackingStrategy implements MachineStrategy {
    
    private final Random random = new Random();
    
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
    
    @Override
    public boolean shouldFlee(Machine machine, BattleArena battleArena) {
        // Esta estrategia nunca huye
        return false;
    }
}