package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Machine es una clase abstracta que representa a una máquina controlada por IA en el juego Poobkemon.
 * Extiende la clase Coach y define la estructura básica y utilidades para implementar diferentes estrategias automáticas.
 * Permite la evaluación de ataques, selección de Pokémon, uso de ítems y ejecución de turnos según la lógica de cada subclase.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Define métodos abstractos para seleccionar ataques, Pokémon y decidir el uso de ítems según la estrategia de la máquina.</li>
 *   <li>Incluye utilidades para calcular la efectividad de ataques y detectar ventajas de tipo.</li>
 *   <li>Permite establecer el oponente para evaluar estrategias dinámicas durante la batalla.</li>
 *   <li>Implementa la lógica general para ejecutar un turno completo de la máquina en la arena de batalla.</li>
 *   <li>Facilita la extensión para crear máquinas con estrategias ofensivas, defensivas, cambiantes, expertas, etc.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public abstract class Machine extends Coach implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String machineName;
    protected String machineType;
    protected Random random = new Random();
    protected Coach opponent;

    public Machine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(pokemons, items);
        this.machineName = name;
        this.machineType = getClass().getSimpleName();
    }

    /**
     * Método para cambiar el Pokémon activo de la máquina.
     * @param index Índice del Pokémon a cambiar
     * @throws PoobkemonException Si el índice es inválido o el Pokémon está debilitado
     */
    @Override
    public void handleTurnTimeout() {
        // Implementación por defecto para las máquinas
        Pokemon active = getActivePokemon();
        if (active != null) {
            active.reducePP();
        }
    }

    /**
     * Selecciona un movimiento basado en la estrategia de la máquina
     * @return El índice del movimiento seleccionado
     */
    public abstract int selectMove();
    
    /**
     * Evalúa los pokémon del equipo y selecciona el mejor para la batalla
     * según la estrategia de la máquina.
     * @return El índice del pokémon seleccionado
     */
    public abstract int selectBestPokemon();

    /**
     * Evalúa si se debe usar un ítem en el turno actual
     * @return true si se debe usar un ítem, false en caso contrario
     */
    public abstract boolean shouldUseItem();
    
    /**
     * Selecciona el mejor ítem para usar
     * @return El índice del ítem seleccionado
     */
    public abstract int selectItem();

    /**
     * Calcula la eficacia de un movimiento contra un Pokémon oponente
     * @param attack Ataque a evaluar
     * @param defender Pokémon defensor
     * @return Valor de efectividad (mayor es mejor)
     */
    protected double calculateEffectiveness(Attack attack, Pokemon defender) {
        // Convertir tipo del ataque y tipo del defensor a índices
        int attackType = efectivity.numberType.getOrDefault(attack.getType(), 10); // Default a normal si no se encuentra
        int defenderType = efectivity.numberType.getOrDefault(defender.getType(), 10);
        
        // Obtener el multiplicador de efectividad
        return efectivity.efectividad(attackType, defenderType);
    }

    /**
     * Establece el oponente para poder evaluar estrategias
     */
    public void setOpponent(Coach opponent) {
        this.opponent = opponent;
    }

    /**
     * Obtiene el nombre de la máquina
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * Obtiene el tipo de estrategia de la máquina
     */
    public String getMachineType() {
        return machineType;
    }
    
    /**
     * Evalúa qué ataque es el mejor basado en efectividad
     */
    public int getBestEffectivenessMove() {
        Pokemon attacker = getActivePokemon();
        Pokemon defender = opponent.getActivePokemon();
        
        if (attacker == null || defender == null || attacker.getAtaques().isEmpty()) {
            return 0;
        }
        
        double bestEffectiveness = -1;
        int bestMoveIndex = 0;
        
        List<Attack> attacks = attacker.getAtaques();
        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            double effectiveness = calculateEffectiveness(attack, defender);
            
            if (effectiveness > bestEffectiveness) {
                bestEffectiveness = effectiveness;
                bestMoveIndex = i;
            }
        }
        
        return bestMoveIndex;
    }
    
    /**
     * Determina si el oponente tiene ventaja de tipo
     */
    protected boolean opponentHasTypeAdvantage() {
        Pokemon ownPokemon = getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        if (ownPokemon == null || opponentPokemon == null) {
            return false;
        }
        
        int ownType = efectivity.numberType.getOrDefault(ownPokemon.getType(), 10);
        int opponentType = efectivity.numberType.getOrDefault(opponentPokemon.getType(), 10);
        
        return efectivity.efectividad(opponentType, ownType) > 1.0;
    }
    
    /**
     * Encuentra el Pokémon con mejor tipo contra el oponente actual
     */
    protected int getPokemonWithTypeAdvantage() {
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        if (opponentPokemon == null) {
            return 0;
        }
        
        int opponentType = efectivity.numberType.getOrDefault(opponentPokemon.getType(), 10);
        double bestAdvantage = 0;
        int bestPokemonIndex = 0;
        
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);
            
            // Saltar Pokémon debilitados
            if (pokemon.getPs() <= 0) {
                continue;
            }
            
            int pokemonType = efectivity.numberType.getOrDefault(pokemon.getType(), 10);
            double typeAdvantage = efectivity.efectividad(pokemonType, opponentType);
            
            if (typeAdvantage > bestAdvantage) {
                bestAdvantage = typeAdvantage;
                bestPokemonIndex = i;
            }
        }
        
        return bestPokemonIndex;
    }
    
/**
     * Obtiene los ítems disponibles de la máquina
     * @return Lista de ítems
     */
    @Override
    public List<Item> getItems() {
        // Llamar al método de la clase padre
        return super.getItems();
    }

    /**
     * Ejecuta un turno completo de la máquina basado en su estrategia
     * @param battleArena La arena de batalla actual
     * @return true si la acción fue exitosa, false en caso contrario
     */
    public boolean executeTurn(BattleArena battleArena) {
        // Si el Pokémon activo está debilitado, seleccionar otro
        if (getActivePokemon().getPs() <= 0) {
            int bestPokemonIndex = selectBestPokemon();
            try {
                switchPokemon(bestPokemonIndex);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        
        // Verificar si debe usar un ítem
        if (shouldUseItem() && !getItems().isEmpty()) {
            int itemIndex = selectItem();
            if (itemIndex >= 0 && itemIndex < getItems().size()) {
                try {
                    // Lógica para usar el ítem
                    String itemName = getItems().get(itemIndex).getName();
                    battleArena.useItem(itemName);
                    return true;
                } catch (Exception e) {
                    // Si hay un error usando el ítem, continuar con un ataque
                }
            }
        }
        
        // Seleccionar y realizar un ataque
        try {
            int moveIndex = selectMove();
            String moveName = getActivePokemon().getAtaques().get(moveIndex).getName();
            battleArena.attack(moveName, false, battleArena.getCurrentTurn() == 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el Pokémon activo de la máquina
     * @return El Pokémon activo
     */
    @Override
    public String getName() {
        return machineName;
    }

    /**
     * Verifica si la máquina es un tipo de máquina
     * @return true si es una máquina, false en caso contrario
     */
    @Override
    public boolean isMachine() {
        return true;
    }
}  // Fin de la clase Machine