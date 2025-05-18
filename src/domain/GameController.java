package domain;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Coach humanTrainer;
    private Coach machineTrainer;
    private boolean gameOver;
    private boolean playerTurn;

    /**
     * Constructor para el controlador del juego
     */
    public GameController() {
        this.gameOver = false;
        this.playerTurn = true;
    }

    /**
     * Inicializa una partida con un entrenador humano y uno de máquina
     * @param playerName Nombre del jugador humano
     * @param machineType Tipo de máquina ("defensive" o "attacking")
     * @param playerPokemons Lista de nombres de pokémon para el jugador
     * @param machinePokemons Lista de nombres de pokémon para la máquina
     */
    public void initializeGame(String playerName, String machineType, 
                              List<String> playerPokemons, List<String> machinePokemons) {
        // Crear entrenador humano
        humanTrainer = new HumanCoach(playerName, new ArrayList<>(), new ArrayList<>());
        
        // Crear máquina con una lista vacía de pokémon y items (se poblarán después)
        machineTrainer = MachineFactory.createMachine(machineType, "CPU " + machineType, new ArrayList<>(), new ArrayList<>());
        
        // Añadir Pokémon a cada entrenador
        setupPokemonTeams(playerPokemons, machinePokemons);
        
        // Inicializar estado del juego
        gameOver = false;
        playerTurn = true;
    }
    
    /**
     * Configura los equipos de Pokémon para ambos entrenadores
     */
    private void setupPokemonTeams(List<String> playerPokemonNames, List<String> machinePokemonNames) {
        // Configurar el equipo del jugador humano
        for (String pokemonName : playerPokemonNames) {
            try {
                ((HumanCoach)humanTrainer).agregarPokemon(pokemonName);
            } catch (Exception e) {
                System.err.println("Error al agregar Pokémon al jugador: " + e.getMessage());
            }
        }
        
        // Configurar el equipo de la máquina
        for (String pokemonName : machinePokemonNames) {
            try {
                Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);
                // Asignar movimientos según el tipo de máquina
                assignMovesBasedOnStrategy(pokemon, ((Machine)machineTrainer).getMachineType());
                machineTrainer.getPokemons().add(pokemon);
            } catch (Exception e) {
                System.err.println("Error al agregar Pokémon a la máquina: " + e.getMessage());
            }
        }
    }
    
    /**
     * Asigna movimientos a un Pokémon según la estrategia de la máquina
     */
    private void assignMovesBasedOnStrategy(Pokemon pokemon, String strategyType) {
        List<Attack> availableAttacks = new ArrayList<>();
        
        if (strategyType.equalsIgnoreCase("Defensive")) {
            // Priorizar ataques defensivos y de estado
            availableAttacks.addAll(getDefensiveAttacks());
        } else {
            // Priorizar ataques ofensivos
            availableAttacks.addAll(getOffensiveAttacks());
        }
        
        // Asignar hasta 4 ataques al Pokémon
        int attackCount = Math.min(4, availableAttacks.size());
        for (int i = 0; i < attackCount; i++) {
            pokemon.addAttack(availableAttacks.get(i));
        }
    }
    
    /**
     * Procesa un turno del jugador humano
     * @param actionType Tipo de acción ("attack", "switch", "item")
     * @param actionValue Valor de la acción (nombre del ataque, índice del Pokémon, nombre del ítem)
     * @return Resultado de la acción como un mensaje
     */
    public String processPlayerTurn(String actionType, String actionValue) {
        if (!playerTurn || gameOver) {
            return "No es tu turno o el juego ha terminado";
        }
        
        String result = "";
        
        try {
            if (actionType.equals("attack")) {
                // Procesar ataque
                result = "Usaste " + actionValue;
                // Aquí iría la lógica del ataque
            } else if (actionType.equals("switch")) {
                // Cambiar Pokémon
                int index = Integer.parseInt(actionValue);
                humanTrainer.switchPokemon(index);
                result = "Cambiaste a " + humanTrainer.getActivePokemon().getName();
            } else if (actionType.equals("item")) {
                // Usar ítem
                result = "Usaste " + actionValue;
                // Aquí iría la lógica del ítem
            }
            
            // Cambiar turno
            playerTurn = false;
            
            // Verificar si el juego ha terminado
            checkGameOver();
            
            // Si el juego no ha terminado, procesar el turno de la máquina
            if (!gameOver) {
                result += "\n" + processMachineTurn();
            }
            
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }
        
        return result;
    }
    
    /**
     * Procesa el turno de la máquina automáticamente
     * @return Resultado de la acción como un mensaje
     */
    public String processMachineTurn() {
        if (gameOver) {
            return "El juego ha terminado";
        }
        
        String result = "";
        
        try {
            Machine machine = (Machine)machineTrainer;
            
            // Si el Pokémon activo está debilitado, seleccionar otro
            if (machineTrainer.getActivePokemon().getPs() <= 0) {
                int bestPokemonIndex = machine.selectBestPokemon();
                machineTrainer.switchPokemon(bestPokemonIndex);
                result = "La máquina cambió a " + machineTrainer.getActivePokemon().getName();
            } else {
                // Seleccionar movimiento según la estrategia
                String moveName = machine.selectMove();
                result = "La máquina usó " + moveName;
                
                // Aquí iría la lógica para aplicar el movimiento
            }
            
            // Cambiar turno de vuelta al jugador
            playerTurn = true;
            
            // Verificar si el juego ha terminado
            checkGameOver();
            
        } catch (Exception e) {
            result = "Error en el turno de la máquina: " + e.getMessage();
        }
        
        return result;
    }
    
    /**
     * Obtiene una lista de ataques defensivos
     */
    private List<Attack> getDefensiveAttacks() {
        List<Attack> defensiveAttacks = new ArrayList<>();
        // Aquí se agregarían ataques defensivos desde AttackFactory
        // Este método tendría que implementarse según los ataques disponibles
        return defensiveAttacks;
    }
    
    /**
     * Obtiene una lista de ataques ofensivos
     */
    private List<Attack> getOffensiveAttacks() {
        List<Attack> offensiveAttacks = new ArrayList<>();
        // Aquí se agregarían ataques ofensivos desde AttackFactory
        // Este método tendría que implementarse según los ataques disponibles
        return offensiveAttacks;
    }
    
    /**
     * Verifica si el juego ha terminado
     */
    private void checkGameOver() {
        if (humanTrainer.areAllPokemonFainted() || machineTrainer.areAllPokemonFainted()) {
            gameOver = true;
        }
    }
    
    /**
     * Obtiene el resultado final del juego
     * @return Mensaje con el resultado del juego
     */
    public String getGameResult() {
        if (!gameOver) {
            return "El juego aún está en curso";
        }
        
        if (!humanTrainer.areAllPokemonFainted()) {
            return "¡Felicidades! Has ganado la batalla.";
        } else {
            return "Has perdido la batalla. Mejor suerte la próxima vez.";
        }
    }
    
    // Getters adaptados para trabajar con Coach en lugar de Trainer
    
    public Coach getHumanTrainer() {
        return humanTrainer;
    }
    
    public Coach getMachineTrainer() {
        return machineTrainer;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public boolean isPlayerTurn() {
        return playerTurn;
    }
}