package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * ExpertStrategy implementa una estrategia avanzada y adaptable para máquinas en el juego Poobkemon.
 * Esta estrategia toma decisiones considerando múltiples factores como vida, ventaja de tipo, estado del oponente,
 * potencia y precisión de los ataques, y uso estratégico de ítems, adaptando su comportamiento a lo largo de la batalla.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Evalúa el mejor ataque considerando efectividad, potencia, precisión, PP y situación de la batalla.</li>
 *   <li>En los primeros turnos prioriza ataques de estado que debiliten al oponente.</li>
 *   <li>Cambia de Pokémon si tiene desventaja de tipo o poca vida, eligiendo el mejor reemplazo posible.</li>
 *   <li>Utiliza ítems de forma estratégica, priorizando curación o potenciadores según el contexto y el turno.</li>
 *   <li>Adapta su comportamiento a lo largo de la batalla, ajustando su estrategia según el estado propio y del oponente.</li>
 *   <li>Puede considerar huir en situaciones extremadamente desfavorables, aunque con baja probabilidad.</li>
 * </ul>
 *
 * <p>Esta clase implementa la interfaz {@link MachineStrategy} y está diseñada para dotar a las máquinas de un comportamiento experto y flexible.
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class ExpertStrategy implements MachineStrategy, Serializable {
    private static final long serialVersionUID = 1L;
    private final Random random = new Random();
    private int turnCounter = 0;
    
    /**
     * Decide la acción a realizar en el turno actual basándose en la situación del Pokémon activo y del oponente.
     * Considera factores como vida, ventaja de tipo, estado del oponente y uso de ítems.
     *
     * @param machine Máquina que contiene los Pokémon y los ataques.
     * @param battleArena Arena de batalla actual.
     * @return Código de acción: 1 para atacar, 2 para usar ítem, 3 para cambiar Pokémon.
     */
    @Override
    public int decideAction(Machine machine, BattleArena battleArena) {
        turnCounter++;
        Pokemon active = machine.getActivePokemon();
        Pokemon opponent = getOpponentPokemon(machine, battleArena);
        
        // Si no hay información del oponente, atacar
        if (opponent == null) {
            return 1;
        }
        
        double healthRatio = (double) active.getPs() / active.getTotalPs();
        double opponentHealthRatio = (double) opponent.getPs() / opponent.getTotalPs();
        boolean typeDisadvantage = hasTypeDisadvantage(active, opponent);
        boolean typeAdvantage = hasTypeAdvantage(active, opponent);
        
        // Estrategia basada en múltiples factores
        
        // 1. Si tenemos desventaja de tipo y hay mejores opciones, cambiar
        if (typeDisadvantage && machine.getPokemons().size() > 1) {
            int betterPokemon = findBetterPokemon(machine, opponent);
            if (betterPokemon >= 0) {
                return 3; // Cambiar Pokémon
            }
        }
        
        // 2. Si estamos muy dañados pero tenemos ventaja, considerar usar ítem
        if (healthRatio < 0.3 && typeAdvantage && !machine.getItems().isEmpty()) {
            return 2; // Usar ítem
        }
        
        // 3. Si el oponente está casi derrotado y tenemos un buen ataque, atacar
        if (opponentHealthRatio < 0.2 && canDeliverFinishingBlow(active, opponent)) {
            return 1; // Atacar para terminar
        }
        
        // 4. Si estamos en mal estado y el oponente en buen estado, considerar cambiar
        if (healthRatio < 0.3 && opponentHealthRatio > 0.5 && machine.getPokemons().size() > 1) {
            return 3; // Cambiar Pokémon
        }
        
        // 5. Si tenemos muy poca vida y hay ítems disponibles, usar ítem
        if (healthRatio < 0.2 && !machine.getItems().isEmpty()) {
            return 2; // Usar ítem
        }
        
        // Por defecto, ataque calculado
        return 1;
    }
    
    /**
     * Selecciona el mejor ataque considerando efectividad, potencia, precisión y PP.
     * Prioriza ataques de estado en los primeros turnos y ataques físicos/especiales según estadísticas.
     *
     * @param machine Máquina que contiene los Pokémon y los ataques.
     * @param battleArena Arena de batalla actual.
     * @return Nombre del ataque seleccionado, o null si no hay ataques disponibles.
     */
    @Override
    public String selectAttack(Machine machine, BattleArena battleArena) {
        Pokemon active = machine.getActivePokemon();
        Pokemon opponent = getOpponentPokemon(machine, battleArena);
        List<Attack> attacks = active.getAtaques();
        
        if (attacks.isEmpty()) {
            return null;
        }
        
        // Primera ronda: considera ataques de estado
        if (turnCounter <= 2) {
            for (Attack attack : attacks) {
                if (attack instanceof StatusAttack && attack.getPowerPoint() > 0) {
                    // Verificar si afecta estadísticas del oponente negativamente
                    if (isDebuffAttack(attack)) {
                        return attack.getName();
                    }
                }
            }
        }
        
        // Fase media: usa los ataques con mejor relación efectividad/poder
        Attack bestAttack = null;
        double bestScore = 0;
        
        for (Attack attack : attacks) {
            if (attack.getPowerPoint() <= 0) continue;
            
            double effectiveness = 1.0;
            if (opponent != null) {
                int attackType = efectivity.numberType.getOrDefault(attack.getType(), 10);
                int defenderType = efectivity.numberType.getOrDefault(opponent.getType(), 10);
                effectiveness = efectivity.efectividad(attackType, defenderType);
            }
            
            // Cálculo de puntuación avanzado
            double power = attack.getBaseDamage();
            double accuracy = attack.getPrecision() / 100.0;
            double ppRatio = (double) attack.getPowerPoint() / attack.getMaxPowerPoint();
            
            // Fórmula: efectividad * poder * precisión * (1 + bonus por PP)
            double score = effectiveness * power * accuracy * (1 + ppRatio * 0.2);
            
            // Si es un ataque físico y tenemos buen ataque físico, o es especial y tenemos buen ataque especial
            if ((attack instanceof PhysicalAttack && active.getPhysicalAttack() > active.getSpecialAttack()) ||
                (attack instanceof SpecialAttack && active.getSpecialAttack() > active.getPhysicalAttack())) {
                score *= 1.2; // 20% extra por usar el tipo de ataque que mejor nos conviene
            }
            
            if (score > bestScore) {
                bestScore = score;
                bestAttack = attack;
            }
        }
        
        return bestAttack != null ? bestAttack.getName() : null;
    }
    
    /**
     * Selecciona un ítem para usar, priorizando curación o potenciadores según la situación.
     * Considera el estado de salud del Pokémon activo y la cantidad de ítems disponibles.
     *
     * @param machine Máquina que contiene los Pokémon y los ítems.
     * @param battleArena Arena de batalla actual.
     * @return Nombre del ítem seleccionado, o null si no hay ítems disponibles.
     */
    @Override
    public String selectItem(Machine machine, BattleArena battleArena) {
        List<Item> items = machine.getItems();
        
        if (items.isEmpty()) {
            return null;
        }
        
        Pokemon active = machine.getActivePokemon();
        double healthRatio = (double) active.getPs() / active.getTotalPs();
        
        // Situación crítica: usar la mejor poción disponible
        if (healthRatio < 0.3) {
            // Buscar pociones en orden de potencia
            String[] potionTypes = {"Poción Máxima", "Poción Hiperpoción", "Poción Super", "Poción"};
            for (String potionType : potionTypes) {
                for (Item item : items) {
                    if (item.getName().contains(potionType)) {
                        return item.getName();
                    }
                }
            }
        }
        
        // Situación normal: usar item estratégico
        if (turnCounter < 3) {
            // Al inicio de la batalla, considera ítems de mejora de estadísticas
            for (Item item : items) {
                if (item.getName().contains("X-") || 
                    item.getName().contains("Defensa") || 
                    item.getName().contains("Ataque")) {
                    return item.getName();
                }
            }
        }
        
        // Usar la mejor poción disponible si estamos dañados
        if (healthRatio < 0.7) {
            for (Item item : items) {
                if (item.getName().contains("Poción")) {
                    return item.getName();
                }
            }
        }
        
        // Si todo falla, usar el primer ítem disponible
        return items.get(0).getName();
    }
    
    /**
     * Selecciona el mejor Pokémon para enviar a la batalla considerando la situación actual.
     * Evalúa la efectividad de tipo, salud, velocidad y ataque de cada Pokémon disponible.
     *
     * @param machine Máquina que contiene los Pokémon.
     * @param battleArena Arena de batalla actual.
     * @return Índice del Pokémon seleccionado, o -1 si no hay un Pokémon adecuado.
     */
    @Override
    public int selectPokemon(Machine machine, BattleArena battleArena) {
        Pokemon opponent = getOpponentPokemon(machine, battleArena);
        
        if (opponent == null) {
            // Sin información del oponente, seleccionar mejor Pokémon general
            return selectBestOverallPokemon(machine);
        }
        
        List<Pokemon> pokemons = machine.getPokemons();
        Pokemon current = machine.getActivePokemon();
        
        // Calcular puntuación para cada Pokémon considerando todos los factores
        int bestIndex = -1;
        double bestScore = -1;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            // No considerar el Pokémon actual o los debilitados
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            // Factores de puntuación
            double healthScore = (double) p.getPs() / p.getTotalPs();
            
            // Efectividad de tipo (ofensiva y defensiva)
            double offensiveTypeAdvantage = getTypeAdvantage(p, opponent);
            double defensiveTypeResistance = 1.0 / getTypeAdvantage(opponent, p);
            
            // Velocidad relativa
            double speedRatio = (double) p.getSpeed() / Math.max(1, opponent.getSpeed());
            
            // Fuerza de ataque relativa
            double attackRatio;
            if (p.getPhysicalAttack() > p.getSpecialAttack()) {
                attackRatio = (double) p.getPhysicalAttack() / Math.max(1, opponent.getPhysicalDefense());
            } else {
                attackRatio = (double) p.getSpecialAttack() / Math.max(1, opponent.getSpecialDefense());
            }
            
            // Calcular puntuación final
            double score = healthScore * 0.3 + 
                           offensiveTypeAdvantage * 0.25 + 
                           defensiveTypeResistance * 0.2 + 
                           speedRatio * 0.15 + 
                           attackRatio * 0.1;
            
            if (score > bestScore) {
                bestScore = score;
                bestIndex = i;
            }
        }
        
        return bestIndex >= 0 ? bestIndex : selectBestOverallPokemon(machine);
    }
    
    /**
     * Determina si la máquina debería huir de la batalla.
     * La estrategia experta considera huir solo en situaciones extremadamente desfavorables.
     *
     * @param machine Máquina que contiene los Pokémon.
     * @param battleArena Arena de batalla actual.
     * @return true si debería huir, false en caso contrario.
     */
    @Override
    public boolean shouldFlee(Machine machine, BattleArena battleArena) {
        // La estrategia experta considera huir solo en situaciones extremadamente desfavorables
        
        // Contar cuántos Pokémon están en buen estado vs debilitados
        int healthyCount = 0;
        int faintedCount = 0;
        
        for (Pokemon p : machine.getPokemons()) {
            if (p.getPs() <= 0) {
                faintedCount++;
            } else if (p.getPs() > p.getTotalPs() * 0.5) {
                healthyCount++;
            }
        }
        
        // Si casi todos están debilitados y ninguno en buen estado, considerar huir
        if (faintedCount >= machine.getPokemons().size() - 1 && healthyCount == 0) {
            return random.nextInt(100) < 10; // 10% de probabilidad
        }
        
        return false;
    }
    
    /**
     * Selecciona el Pokémon con mejor tipo contra el oponente actual.
     * Si no hay un Pokémon adecuado, selecciona el mejor Pokémon general.
     *
     * @param machine Máquina que contiene los Pokémon.
     * @param battleArena Arena de batalla actual.
     * @return Índice del Pokémon seleccionado, o -1 si no hay un Pokémon adecuado.
     */
    private Pokemon getOpponentPokemon(Machine machine, BattleArena battleArena) {
        for (Coach coach : battleArena.getCoaches()) {
            if (coach != machine) {
                return coach.getActivePokemon();
            }
        }
        return null;
    }
    
    /**
     * Verifica si el Pokémon atacante tiene desventaja de tipo contra el defensor.
     * 
     * @param attacker Pokémon atacante.
     * @param defender Pokémon defensor.
     * @return true si tiene desventaja de tipo, false en caso contrario.
     */
    private boolean hasTypeDisadvantage(Pokemon attacker, Pokemon defender) {
        if (attacker == null || defender == null) return false;
        
        return getTypeAdvantage(attacker, defender) < 1.0;
    }
    
    /**
     * Verifica si el Pokémon atacante tiene ventaja de tipo contra el defensor.
     * 
     * @param attacker Pokémon atacante.
     * @param defender Pokémon defensor.
     * @return true si tiene ventaja de tipo, false en caso contrario.
     */
    private boolean hasTypeAdvantage(Pokemon attacker, Pokemon defender) {
        if (attacker == null || defender == null) return false;
        
        return getTypeAdvantage(attacker, defender) > 1.0;
    }
    
    /**
     * Obtiene la ventaja de tipo entre el Pokémon atacante y el defensor.
     * 
     * @param attacker Pokémon atacante.
     * @param defender Pokémon defensor.
     * @return Valor de ventaja de tipo (1.0 = neutral, >1.0 = ventaja, <1.0 = desventaja).
     */
    private double getTypeAdvantage(Pokemon attacker, Pokemon defender) {
        if (attacker == null || defender == null) return 1.0;
        
        int attackerType = efectivity.numberType.getOrDefault(attacker.getType(), 10);
        int defenderType = efectivity.numberType.getOrDefault(defender.getType(), 10);
        
        return efectivity.efectividad(attackerType, defenderType);
    }
    
    /**
     * Busca un Pokémon mejor para cambiar, considerando ventaja de tipo y estadísticas.
     * 
     * @param machine Máquina que contiene los Pokémon.
     * @param opponent Pokémon oponente actual.
     * @return Índice del mejor Pokémon para cambiar, o -1 si no hay uno adecuado.
     */
    private int findBetterPokemon(Machine machine, Pokemon opponent) {
        if (opponent == null) return -1;
        
        List<Pokemon> pokemons = machine.getPokemons();
        Pokemon current = machine.getActivePokemon();
        int bestIndex = -1;
        double bestAdvantage = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            double typeAdvantage = getTypeAdvantage(p, opponent);
            if (typeAdvantage > bestAdvantage) {
                bestAdvantage = typeAdvantage;
                bestIndex = i;
            }
        }
        
        return bestIndex;
    }
    
    /**
     * Verifica si el Pokémon atacante puede dar el golpe final al defensor.
     * Considera ataques que puedan derrotar al oponente en este turno.
     * 
     * @param attacker Pokémon atacante.
     * @param defender Pokémon defensor.
     * @return true si puede dar el golpe final, false en caso contrario.
     */
    private boolean canDeliverFinishingBlow(Pokemon attacker, Pokemon defender) {
        if (attacker == null || defender == null) return false;
        
        // Verificar si algún ataque puede derrotar al oponente
        for (Attack attack : attacker.getAtaques()) {
            if (attack.getPowerPoint() <= 0) continue;
            
            int estimatedDamage = estimateDamage(attacker, defender, attack);
            if (estimatedDamage >= defender.getPs()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Estima el daño que un ataque causaría a un Pokémon defensor.
     * Utiliza una fórmula simplificada considerando efectividad de tipo, poder del ataque,
     * estadísticas de ataque y defensa del atacante y defensor.
     *
     * @param attacker Pokémon atacante.
     * @param defender Pokémon defensor.
     * @param attack Ataque a evaluar.
     * @return Daño estimado que causaría el ataque al defensor.
     */
    private int estimateDamage(Pokemon attacker, Pokemon defender, Attack attack) {
        // Cálculo simplificado de daño
        double effectiveness = 1.0;
        int attackType = efectivity.numberType.getOrDefault(attack.getType(), 10);
        int defenderType = efectivity.numberType.getOrDefault(defender.getType(), 10);
        effectiveness = efectivity.efectividad(attackType, defenderType);
        
        double power = attack.getBaseDamage();
        double attackStat = 0;
        double defenseStat = 0;
        
        if (attack instanceof PhysicalAttack) {
            attackStat = attacker.getPhysicalAttack();
            defenseStat = defender.getPhysicalDefense();
        } else if (attack instanceof SpecialAttack) {
            attackStat = attacker.getSpecialAttack();
            defenseStat = defender.getSpecialDefense();
        } else {
            return 0; // Ataques de estado no causan daño directo
        }
        
        // Fórmula simplificada
        return (int)((power * attackStat / defenseStat) * effectiveness * 0.2);
    }
    
    /**
     * Selecciona el mejor Pokémon general considerando salud, ataque, defensa y velocidad.
     * Utiliza una puntuación compuesta para determinar el mejor Pokémon disponible.
     *
     * @param machine Máquina que contiene los Pokémon.
     * @return Índice del mejor Pokémon general, o 0 si no hay otros disponibles.
     */
    private int selectBestOverallPokemon(Machine machine) {
        List<Pokemon> pokemons = machine.getPokemons();
        Pokemon current = machine.getActivePokemon();
        int bestIndex = 0;
        double bestScore = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon p = pokemons.get(i);
            
            if (p == current || p.getPs() <= 0) {
                continue;
            }
            
            // Cálculo de puntuación general
            double healthRatio = (double) p.getPs() / p.getTotalPs();
            double attackRatio = (p.getPhysicalAttack() + p.getSpecialAttack()) / 200.0;
            double defenseRatio = (p.getPhysicalDefense() + p.getSpecialDefense()) / 200.0;
            double speedRatio = p.getSpeed() / 100.0;
            
            double score = healthRatio * 0.4 + attackRatio * 0.25 + defenseRatio * 0.25 + speedRatio * 0.1;
            
            if (score > bestScore) {
                bestScore = score;
                bestIndex = i;
            }
        }
        
        return bestIndex;
    }
    
    /**
     * Verifica si un ataque es un ataque de debuff que debilita al oponente.
     * 
     * @param attack Ataque a evaluar.
     * @return true si es un ataque de debuff, false en caso contrario.
     */
    private boolean isDebuffAttack(Attack attack) {
        if (!(attack instanceof StatusAttack)) return false;
        
        // Lista de efectos que debilitan al oponente
        String[] debuffEffects = {"BajarAtaque", "BajarDefensa", "BajarVelocidad", "BajarAtaqueEspecial", "BajarDefensaEspecial"};
        
        for (String effect : debuffEffects) {
            if (attack.getEffect() != null && attack.getEffect().contains(effect)) {
                return true;
            }
        }
        
        return false;
    }
}