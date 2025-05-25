package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * GameController gestiona el flujo principal de una partida de Poobkemon entre un entrenador humano y una máquina.
 * Se encarga de la inicialización de los entrenadores, la configuración de equipos, la gestión de turnos, 
 * la aplicación de acciones (ataques, cambios, uso de ítems) y la verificación del estado del juego.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite inicializar una partida con diferentes tipos de máquinas (Attacking, Defensive, Changing, Expert).</li>
 *   <li>Gestiona el turno de cada jugador y procesa las acciones realizadas por el humano y la máquina.</li>
 *   <li>Configura los equipos de Pokémon y asigna movimientos según la estrategia de la máquina.</li>
 *   <li>Incluye métodos para procesar ataques, cambios de Pokémon y uso de ítems por parte del jugador y la máquina.</li>
 *   <li>Verifica el estado del juego y determina el resultado final de la partida.</li>
 *   <li>Permite obtener referencias a los entrenadores y consultar el estado actual del juego.</li>
 * </ul>
 *
 * <p>Esta clase es el punto de entrada para controlar la lógica de una batalla estándar entre un humano y una máquina.
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
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
        
        // Convertir String a enum MachineType
        MachineFactory.MachineType type;
        switch(machineType.toLowerCase()) {
            case "attacking":
                type = MachineFactory.MachineType.ATTACKING;
                break;
            case "defensive":
                type = MachineFactory.MachineType.DEFENSIVE;
                break;
            case "changing":
                type = MachineFactory.MachineType.CHANGING;
                break;
            case "expert":
                type = MachineFactory.MachineType.EXPERT;
                break;
            default:
                type = MachineFactory.MachineType.ATTACKING; // Valor por defecto
        }
        
        // Establecer una dificultad media por defecto
        int difficulty = 2;
        
        // Crear máquina con la firma correcta
        machineTrainer = MachineFactory.createMachine(type, "CPU " + machineType, difficulty);
        
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
              int moveIndex = machine.selectMove();
    
              // Obtener el nombre del ataque usando el índice
               String moveName = machineTrainer.getActivePokemon().getAtaques().get(moveIndex).getName();
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
    
    /**
     * Crea una máquina para una batalla contra el jugador.
     */
    public Machine createMachineOpponent(int difficulty, String name) {
        MachineFactory.MachineType type;
        
        // La dificultad determina el tipo de máquina
        switch (difficulty) {
            case 1:
                type = MachineFactory.MachineType.ATTACKING; // Enfoque ofensivo
                break;
            case 2:
                type = MachineFactory.MachineType.DEFENSIVE; // Enfoque defensivo
                break;
            case 3:
                type = MachineFactory.MachineType.CHANGING; // Enfoque adaptativo
                break;
            case 4:
                type = MachineFactory.MachineType.EXPERT; // Enfoque experto
                break;
            default:
                type = MachineFactory.MachineType.ATTACKING; // Por defecto
        }
        
        return MachineFactory.createMachine(type, name, difficulty);
    }

    /**
     * Ejecuta un turno de la máquina.
     */
    public boolean executeMachineTurn(Machine machine, BattleArena battleArena) {
        return machine.executeTurn(battleArena);
    }
    
    /**
     * Obtiene los entrenadores del juego
     * @return Lista de entrenadores
     */    
    public Coach getHumanTrainer() {
        return humanTrainer;
    }
    
    /**
     * Obtiene el entrenador de la máquina
     * @return Entrenador de la máquina
     */
    public Coach getMachineTrainer() {
        return machineTrainer;
    }
    
    /**
     * Verifica si el juego ha terminado
     * @return true si el juego ha terminado, false en caso contrario
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Verifica si es el turno del jugador
     * @return true si es el turno del jugador, false si es el turno de la máquina
     */
    public boolean isPlayerTurn() {
        return playerTurn;
    }
}