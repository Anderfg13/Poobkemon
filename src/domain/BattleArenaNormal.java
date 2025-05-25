package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * BattleArenaNormal implementa una arena de batalla estándar para el juego Poobkemon.
 * Permite configurar y gestionar combates entre humanos y máquinas, así como entre diferentes tipos de máquinas.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite batallas Humano vs Máquina, Máquina vs Humano y Máquina vs Máquina.</li>
 *   <li>Asigna ataques a los Pokémon de acuerdo al tipo de máquina (estrategia ofensiva, defensiva, etc.).</li>
 *   <li>Gestiona la creación de entrenadores, asignación de Pokémon, ítems y ataques.</li>
 *   <li>Permite establecer el Pokémon activo y el turno inicial de la batalla.</li>
 *   <li>Incluye utilidades para aplicar efectos de estado y seleccionar ataques aleatorios según la estrategia.</li>
 * </ul>
 *
 * <p>Esta clase extiende {@link BattleArena} y debe ser utilizada para batallas normales en el juego.
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class BattleArenaNormal extends BattleArena {
    private static final long serialVersionUID = 1L;

    public BattleArenaNormal() {
        super();
    }

    /**
     * Configura una batalla entre un humano y una máquina.
     *
     * @param humanName    Nombre del entrenador humano.
     * @param machineName  Nombre de la máquina.
     * @param humanPokemon Lista de nombres de Pokémon del humano.
     * @param machinePokemon Lista de nombres de Pokémon de la máquina.
     * @param humanItems   Lista de ítems del humano.
     * @param humanAttacks Matriz de ataques para los Pokémon del humano.
     * @param machineType  Tipo de máquina a crear (Attacking, Defensive, Changing, Expert).
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public void setupHumanVsMachine(String humanName, String machineName, 
                         ArrayList<String> humanPokemon, ArrayList<String> machinePokemon,
                         ArrayList<String> humanItems, String[][] humanAttacks, 
                         String machineType, Color player1Collor, Color player2Color) throws PoobkemonException {
        // Crear los pokémon para el humano
        ArrayList<Pokemon> humanPokemonList = new ArrayList<>();
        for (int i = 0; i < humanPokemon.size(); i++) {
            Pokemon pokemon = PokemonFactory.createPokemon(humanPokemon.get(i));
            if (pokemon != null) {
                // Asignar ataques a este Pokémon
                assignAttacksToPokemon(pokemon, humanAttacks[i]);
                humanPokemonList.add(pokemon);
            }
        }
        
        // Crear la lista de ítems para el humano
        ArrayList<Item> humanItemsList = new ArrayList<>();
        for (String itemName : humanItems) {
            Item item = ItemFactory.createItem(itemName);
            if (item != null) {
                humanItemsList.add(item);
            }
        }
        
        // Crear el entrenador humano
        Coach humanCoach = new HumanCoach(humanName, humanPokemonList, humanItems);
        
        // Crear los pokémon para la máquina con ataques aleatorios
        ArrayList<Pokemon> machinePokemonList = new ArrayList<>();
        for (String pokemonName : machinePokemon) {
            Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);
            if (pokemon != null) {
                // Asignar ataques aleatorios basados en el tipo de máquina
                assignRandomAttacksForMachine(pokemon, machineType);
                machinePokemonList.add(pokemon);
            }
        }
        
        // Crear la máquina según el tipo
        Machine machine;
        switch (machineType) {
            case "Attacking":
                machine = new AttackingMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            case "Defensive":
                machine = new DefensiveMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            case "Changing":
                machine = new ChangingMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            case "Expert":
                machine = new ExpertMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            default:
                machine = new AttackingMachine(machineName, machinePokemonList, new ArrayList<>());
        }
        
        // Asignar los entrenadores al array de coaches
        coaches[0] = humanCoach;  // Humano es el jugador 1
        coaches[1] = machine;     // Máquina es el jugador 2
        
        // Establecer el Pokémon activo para cada entrenador
        if (!humanPokemonList.isEmpty()) {
            humanCoach.setActivePokemon(humanPokemonList.get(0));
        }
        
        if (!machinePokemonList.isEmpty()) {
            machine.setActivePokemon(machinePokemonList.get(0));
        }
        
        // Establecer el turno inicial
        currentTurn = 0;  // El humano comienza

        // Establecer al humano como oponente de la máquina
        machine.setOpponent(humanCoach); 
    }

    /**
     * Configura una batalla entre una máquina y un humano (máquina como player 1).
     *
     * @param machineName  Nombre de la máquina.
     * @param humanName    Nombre del entrenador humano.
     * @param machinePokemon Lista de nombres de Pokémon de la máquina.
     * @param humanPokemon Lista de nombres de Pokémon del humano.
     * @param humanItems   Lista de ítems del humano.
     * @param humanAttacks Matriz de ataques para los Pokémon del humano.
     * @param machineType  Tipo de máquina a crear (Attacking, Defensive, Changing, Expert).
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public void setupMachineVsHuman(String machineName, String humanName, 
                                ArrayList<String> machinePokemon, ArrayList<String> humanPokemon,
                                ArrayList<String> humanItems, String[][] humanAttacks, 
                                String machineType, Color player1Color, Color player2Color) throws PoobkemonException {
        
        // Crear los pokémon para el humano
        ArrayList<Pokemon> humanPokemonList = new ArrayList<>();
        for (int i = 0; i < humanPokemon.size(); i++) {
            Pokemon pokemon = PokemonFactory.createPokemon(humanPokemon.get(i));
            if (pokemon != null) {
                // Asignar ataques a este Pokémon
                assignAttacksToPokemon(pokemon, humanAttacks[i]);
                humanPokemonList.add(pokemon);
            }
        }
        
        // Crear la lista de ítems para el humano
        ArrayList<Item> humanItemsList = new ArrayList<>();
        for (String itemName : humanItems) {
            Item item = ItemFactory.createItem(itemName);
            if (item != null) {
                humanItemsList.add(item);
            }
        }
        
        // Crear el entrenador humano
        Coach humanCoach = new HumanCoach(humanName, humanPokemonList, humanItems);
        
        // Crear los pokémon para la máquina con ataques aleatorios
        ArrayList<Pokemon> machinePokemonList = new ArrayList<>();
        for (String pokemonName : machinePokemon) {
            Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);
            if (pokemon != null) {
                // Asignar ataques aleatorios basados en el tipo de máquina
                assignRandomAttacksForMachine(pokemon, machineType);
                machinePokemonList.add(pokemon);
            }
        }
        
        // Crear la máquina según el tipo
        Machine machine;
        switch (machineType) {
            case "Attacking":
                machine = new AttackingMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            case "Defensive":
                machine = new DefensiveMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            case "Changing":
                machine = new ChangingMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            case "Expert":
                machine = new ExpertMachine(machineName, machinePokemonList, new ArrayList<>());
                break;
            default:
                machine = new AttackingMachine(machineName, machinePokemonList, new ArrayList<>());
        }
        
        // Asignar los entrenadores al array de coaches, pero ahora la máquina es el jugador 1
        coaches[0] = machine;      // Máquina es el jugador 1
        coaches[1] = humanCoach;   // Humano es el jugador 2

        coaches[0].setColor(player1Color);
        System.out.println("Color de la máquina 1: " + player1Color);
        coaches[1].setColor(player2Color);
        
        // Establecer el Pokémon activo para cada entrenador
        if (!machinePokemonList.isEmpty()) {
            machine.setActivePokemon(machinePokemonList.get(0));
        }
        
        if (!humanPokemonList.isEmpty()) {
            humanCoach.setActivePokemon(humanPokemonList.get(0));
        }
        
        // Establecer el turno inicial
        currentTurn = 0;  // La máquina comienza

        // Establecer al humano como oponente de la máquina
        machine.setOpponent(humanCoach); 
    }

    /**
     * Configura una batalla entre dos máquinas.
     *
     * @param machine1Name    Nombre de la primera máquina.
     * @param machine2Name    Nombre de la segunda máquina.
     * @param machine1Pokemon Lista de nombres de Pokémon de la primera máquina.
     * @param machine2Pokemon Lista de nombres de Pokémon de la segunda máquina.
     * @param machine1Type    Tipo de la primera máquina.
     * @param machine2Type    Tipo de la segunda máquina.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    @Override
    public void setupMachineVsMachine(
        String machine1Name, String machine2Name,
        ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon,
        String machine1Type, String machine2Type,
        Color player1Color, Color player2Color
    ) throws PoobkemonException {
    
    // Crear los pokémon para la primera máquina
    ArrayList<Pokemon> machine1PokemonList = new ArrayList<>();
    for (String pokemonName : machine1Pokemon) {
        Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);
        if (pokemon != null) {
            // Asignar ataques aleatorios basados en el tipo de máquina
            assignRandomAttacksForMachine(pokemon, machine1Type);
            machine1PokemonList.add(pokemon);
        }
    }
    
    // Crear los pokémon para la segunda máquina
    ArrayList<Pokemon> machine2PokemonList = new ArrayList<>();
    for (String pokemonName : machine2Pokemon) {
        Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);
        if (pokemon != null) {
            // Asignar ataques aleatorios basados en el tipo de máquina
            assignRandomAttacksForMachine(pokemon, machine2Type);
            machine2PokemonList.add(pokemon);
        }
    }
    
    // Crear la primera máquina según su tipo
    Machine machine1;
    switch (machine1Type) {
        case "Attacking":
            machine1 = new AttackingMachine(machine1Name, machine1PokemonList, new ArrayList<>());
            break;
        case "Defensive":
            machine1 = new DefensiveMachine(machine1Name, machine1PokemonList, new ArrayList<>());
            break;
        case "Changing":
            machine1 = new ChangingMachine(machine1Name, machine1PokemonList, new ArrayList<>());
            break;
        case "Expert":
            machine1 = new ExpertMachine(machine1Name, machine1PokemonList, new ArrayList<>());
            break;
        default:
            machine1 = new AttackingMachine(machine1Name, machine1PokemonList, new ArrayList<>());
    }
    
    // Crear la segunda máquina según su tipo
    Machine machine2;
    switch (machine2Type) {
        case "Attacking":
            machine2 = new AttackingMachine(machine2Name, machine2PokemonList, new ArrayList<>());
            break;
        case "Defensive":
            machine2 = new DefensiveMachine(machine2Name, machine2PokemonList, new ArrayList<>());
            break;
        case "Changing":
            machine2 = new ChangingMachine(machine2Name, machine2PokemonList, new ArrayList<>());
            break;
        case "Expert":
            machine2 = new ExpertMachine(machine2Name, machine2PokemonList, new ArrayList<>());
            break;
        default:
            machine2 = new AttackingMachine(machine2Name, machine2PokemonList, new ArrayList<>());
    }
    
    // Establecer los oponentes para que cada máquina conozca a su rival
    machine1.setOpponent(machine2);
    machine2.setOpponent(machine1);
    
    // Asignar las máquinas al array de coaches
    coaches[0] = machine1;
    coaches[1] = machine2;

    // ASIGNA LOS COLORES AQUÍ
    coaches[0].setColor(player1Color);
    System.out.println("Color de la máquina 1: " + player1Color);
    coaches[1].setColor(player2Color);
    
    // Establecer el Pokémon activo para cada máquina
    if (!machine1PokemonList.isEmpty()) {
        machine1.setActivePokemon(machine1PokemonList.get(0));
    }
    
    if (!machine2PokemonList.isEmpty()) {
        machine2.setActivePokemon(machine2PokemonList.get(0));
    }
    
    // Establecer el turno inicial
    currentTurn = 0;  // La primera máquina comienza
}
    /**
     * Asigna ataques a un pokémon a partir de un array de nombres de ataques.
     *
     * @param pokemon     El pokémon al que asignar los ataques.
     * @param attackNames Array con los nombres de los ataques.
     */
    private void assignAttacksToPokemon(Pokemon pokemon, String[] attackNames) {
        ArrayList<Attack> attacks = new ArrayList<>();
        for (String attackName : attackNames) {
            if (attackName != null && !attackName.isEmpty()) {
                Attack attack = AttackFactory.createAttack(attackName);
                if (attack != null) {
                    attacks.add(attack);
                }
            }
        }
        pokemon.setAtaques(attacks);
    }

    /**
     * Asigna ataques aleatorios a un pokémon según el tipo de máquina.
     *
     * @param pokemon     El pokémon al que asignar los ataques.
     * @param machineType El tipo de máquina que determina la estrategia de selección de ataques.
     */
    private void assignRandomAttacksForMachine(Pokemon pokemon, String machineType) {
        // Obtener todos los ataques disponibles
        List<String> physicalAttacks = Poobkemon.getPhysicalAttacks();
        List<String> specialAttacks = Poobkemon.getSpecialAttacks();
        List<String> statusAttacks = Poobkemon.getStatusAttacks();
        
        ArrayList<Attack> selectedAttacks = new ArrayList<>();
        
        // Seleccionar ataques según el tipo de máquina
        switch (machineType) {
            case "Attacking":
                // Priorizar ataques físicos y especiales
                addRandomAttacks(selectedAttacks, physicalAttacks, 2);
                addRandomAttacks(selectedAttacks, specialAttacks, 2);
                break;
            case "Defensive":
                // Priorizar ataques de estado y defensivos
                addRandomAttacks(selectedAttacks, statusAttacks, 2);
                addRandomAttacks(selectedAttacks, physicalAttacks, 1);
                addRandomAttacks(selectedAttacks, specialAttacks, 1);
                break;
            case "Changing":
                // Variedad de ataques
                addRandomAttacks(selectedAttacks, physicalAttacks, 1);
                addRandomAttacks(selectedAttacks, specialAttacks, 1);
                addRandomAttacks(selectedAttacks, statusAttacks, 2);
                break;
            case "Expert":
                // Selección balanceada y estratégica
                addRandomAttacks(selectedAttacks, physicalAttacks, 1);
                addRandomAttacks(selectedAttacks, specialAttacks, 1);
                addRandomAttacks(selectedAttacks, statusAttacks, 2);
                break;
            default:
                // Mezcla general por defecto
                addRandomAttacks(selectedAttacks, physicalAttacks, 2);
                addRandomAttacks(selectedAttacks, specialAttacks, 1);
                addRandomAttacks(selectedAttacks, statusAttacks, 1);
        }
        
        // Asegurarse de tener 4 ataques (o menos si no hay suficientes disponibles)
        while (selectedAttacks.size() < 4) {
            // Intentar agregar ataques físicos primero, luego especiales, luego de estado
            if (!addRandomAttack(selectedAttacks, physicalAttacks)) {
                if (!addRandomAttack(selectedAttacks, specialAttacks)) {
                    addRandomAttack(selectedAttacks, statusAttacks);
                }
            }
        }
        
        pokemon.setAtaques(selectedAttacks);
    }

    /**
     * Agrega un número específico de ataques aleatorios a la lista de ataques seleccionados.
     *
     * @param selectedAttacks  Lista de ataques donde agregar los nuevos.
     * @param availableAttacks Lista de nombres de ataques disponibles.
     * @param count            Número de ataques a agregar.
     */
    private void addRandomAttacks(ArrayList<Attack> selectedAttacks, List<String> availableAttacks, int count) {
        for (int i = 0; i < count && !availableAttacks.isEmpty(); i++) {
            addRandomAttack(selectedAttacks, availableAttacks);
        }
    }

    /**
     * Agrega un ataque aleatorio a la lista de ataques seleccionados.
     *
     * @param selectedAttacks  Lista de ataques donde agregar el nuevo.
     * @param availableAttacks Lista de nombres de ataques disponibles.
     * @return {@code true} si se pudo agregar un ataque, {@code false} si no había ataques disponibles.
     */
    private boolean addRandomAttack(ArrayList<Attack> selectedAttacks, List<String> availableAttacks) {
        if (availableAttacks.isEmpty()) {
            return false;
        }
        
        // Seleccionar un ataque aleatorio
        int randomIndex = (int)(Math.random() * availableAttacks.size());
        String attackName = availableAttacks.get(randomIndex);
        
        // Crear el ataque y agregarlo a la lista
        Attack attack = AttackFactory.createAttack(attackName);
        if (attack != null) {
            selectedAttacks.add(attack);
            // Eliminar el ataque de la lista de disponibles para evitar duplicados
            availableAttacks.remove(randomIndex);
            return true;
        }
        
        return false;
    }
}