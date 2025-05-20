package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Poobkemon {
    private BattleArena battleArenaNormal;
    private ArrayList<BattleArena> battleArenas;
    private Map<String, String[][]> survivalMoves = new HashMap<>();

    //Metodo que envia informacion de los pokemones disponibles a la GUI
    public static List<String> getAvailablePokemon() {
        return Arrays.asList(PokemonFactory.POKEMON_REGISTRY.keySet().toArray(new String[0]));
    }
    
    // Metodo que envia informacion de los items disponibles a la GUI
    public static List<String> getAvailableItems() {
        // Obtiene las claves del mapa ITEM_REGISTRY de ItemFactory
        return new ArrayList<>(ItemFactory.getItemNames());
    }

    /**
     * Obtiene la lista de ataques físicos disponibles.
     */
    public static List<String> getPhysicalAttacks() {
        return AttackFactory.getAllAttacks().values().stream()
            .filter(attack -> "Physical".equals(attack.getAttackType()))
            .map(Attack::getName)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de ataques especiales disponibles.
     */
    public static List<String> getSpecialAttacks() {
        return AttackFactory.getAllAttacks().values().stream()
            .filter(attack -> "Special".equals(attack.getAttackType()))
            .map(Attack::getName)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de ataques de estado disponibles.
     */
    public static List<String> getStatusAttacks() {
        return AttackFactory.getAllAttacks().values().stream()
            .filter(attack -> "Status".equals(attack.getAttackType()))
            .map(Attack::getName)
            .collect(Collectors.toList());
    }

    public static ArrayList<String> getAvailableAttacks() {
        ArrayList<String> allAttacks = new ArrayList<>();
        allAttacks.addAll(getPhysicalAttacks());
        allAttacks.addAll(getSpecialAttacks());
        allAttacks.addAll(getStatusAttacks());
        return allAttacks;
    } 


    public int attack(String nombreAtaque, boolean toItself, boolean esJugador1) throws PoobkemonException {
        return battleArenaNormal.attack(nombreAtaque, toItself, esJugador1);
    }

    /**
     * Inicia una nueva batalla entre dos entrenadores.
     * @param coachName1 Nombre del primer entrenador.
     * @param coachName2 Nombre del segundo entrenador.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public void startBattleNormal(String coachName1, String coachName2, ArrayList<String> pokemons1,
                            ArrayList<String> pokemons2, ArrayList<String> items1, ArrayList<String> items2,
                            String[][] pokemAttacks1, String[][] pokemAttacks2) throws PoobkemonException {
        battleArenaNormal = new BattleArenaNormal(); // Crear la arena de batalla
        battleArenaNormal.setupCoaches(coachName1, coachName2, pokemons1, pokemons2, items1, items2, pokemAttacks1, pokemAttacks2);
    }

    /**
     * Inicia una nueva batalla entre dos entrenadores.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public void startBattleSurvival( ArrayList<String> pokemons1,
                            ArrayList<String> pokemons2,
                            String[][] pokemAttacks1, String[][] pokemAttacks2) throws PoobkemonException {
        battleArenaNormal = new BattleArenaNormal(); // Crear la arena de batalla
        battleArenaNormal.setupCoaches("Player 1", "Player 2", pokemons1, pokemons2, new ArrayList<>(),  new ArrayList<>() , pokemAttacks1, pokemAttacks2);

        // Guardar los movimientos asignados para cada jugador
        survivalMoves.put("Player 1", pokemAttacks1);
        survivalMoves.put("Player 2", pokemAttacks2);
    }

    // Método para obtener los movimientos asignados en modo Survival
    public Map<String, String[][]> getSurvivalMoves() {
        return survivalMoves;
    }

    public void flee(){
        battleArenaNormal.flee();
    }
    
    public void useItem(String itemName) throws PoobkemonException {
        // Delegar la lógica del uso de ítem a la arena de batalla
        battleArenaNormal.useItem(itemName);
    }

    public void switchToPokemon(int index) throws PoobkemonException {
        System.out.println(1);
        // Delegar la lógica del cambio de Pokémon a la arena de batalla
        battleArenaNormal.switchToPokemon(index);
    }

    public BattleArena getBattleArena() {
        return battleArenaNormal;
    }

    public void statusEffect(){
        // Delegar la lógica del efecto de estado a la arena de batalla
        battleArenaNormal.statusEffect();
    }



    public void setCurrentPokemon(int index) throws PoobkemonException {
        // Delegar la lógica del cambio de Pokémon a la arena de batalla
        battleArenaNormal.setCurrentPokemon(index);
    }

    /**
     * Inicia una batalla entre un humano y una máquina (humano como player 1)
     */
    public void startBattleHumanVsMachine(String humanName, String machineName, 
                               ArrayList<String> humanPokemon, ArrayList<String> machinePokemon,
                               ArrayList<String> humanItems, String[][] humanAttacks, 
                               String machineType) throws PoobkemonException {
        // Crear una arena de batalla apropiada
        battleArenaNormal = new BattleArenaNormal();

        // Si la lista de pokémon de la máquina está vacía, genera 6 aleatorios
        if (machinePokemon == null || machinePokemon.isEmpty()) {
            ArrayList<String> disponibles = new ArrayList<>(getAvailablePokemon());
            Collections.shuffle(disponibles);
            machinePokemon = new ArrayList<>(disponibles.subList(0, Math.min(6, disponibles.size())));
        }

        // Configurar los entrenadores (humano y máquina)
        // La máquina necesita una configuración especial basada en su tipo
        battleArenaNormal.setupHumanVsMachine(humanName, machineName, 
            humanPokemon, machinePokemon, 
            humanItems, humanAttacks, machineType);
    }

    /**
     * Inicia una batalla entre una máquina y un humano (máquina como player 1)
     */
    public void startBattleMachineVsHuman(String machineName, String humanName, 
                               ArrayList<String> machinePokemon, ArrayList<String> humanPokemon,
                               ArrayList<String> humanItems, String[][] humanAttacks, 
                               String machineType) throws PoobkemonException {
        // Crear una arena de batalla apropiada
        battleArenaNormal = new BattleArenaNormal();
        
        // Configurar los entrenadores (máquina y humano)
        battleArenaNormal.setupMachineVsHuman(machineName, humanName, 
            machinePokemon, humanPokemon, 
            humanItems, humanAttacks, machineType);
    }

    /**
     * Inicia una batalla entre dos máquinas
     */
    public void startBattleMachineVsMachine(String machine1Name, String machine2Name, 
                                ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon,
                                String machine1Type, String machine2Type) throws PoobkemonException {
        // Crear una arena de batalla apropiada
        battleArenaNormal = new BattleArenaNormal();
        
        // Configurar las dos máquinas
        battleArenaNormal.setupMachineVsMachine(machine1Name, machine2Name, 
            machine1Pokemon, machine2Pokemon, 
            machine1Type, machine2Type);
    }

    public boolean whoStarts(){
        // Delegar la lógica del quien empieza a la arena de batalla
        return battleArenaNormal.whoStarts();
    }

    public List<String> getActivePokemonMoves(boolean esJugador1) {
        // Devuelve los 4 movimientos del pokémon activo del jugador correspondiente
        return battleArenaNormal.getActivePokemonMoves(esJugador1);
    }

    public String getActivePokemonName(boolean esJugador1) {
        // Suponiendo que tienes una referencia a la arena de batalla
        // y que coaches[0] es jugador1, coaches[1] es jugador2
        return battleArenaNormal.getActivePokemonName(esJugador1);
    }

    public int getActivePokemonCurrentHP(boolean esJugador1) {
        return battleArenaNormal.getActivePokemonCurrentHP(esJugador1);
    }
    public int getActivePokemonMaxHP(boolean esJugador1) {
        return battleArenaNormal.getActivePokemonMaxHP(esJugador1);
    }

    public int getPPDeAtaqueActual(boolean esJugador1, String nombreAtaque) {
        return battleArenaNormal.getPPDeAtaqueActual(esJugador1, nombreAtaque);
    }

    public List<String> getItemsJugador(boolean esJugador1) {
        return battleArenaNormal.getItemsJugador(esJugador1);
    }

    public boolean esAtaqueSobreSiMismo(String nombreAtaque) {
        // Puedes mejorar esto usando el dominio o una propiedad del ataque
        List<String> ataquesSelf = List.of("Curación", "Reflejo", "Danza Espada"); // Ejemplo
        return ataquesSelf.contains(nombreAtaque);
    }

    /**
     * Procesa el turno de la máquina y devuelve un mensaje con la acción realizada.
     * @return Mensaje describiendo la acción realizada por la máquina
     */
    public String processMachineTurn() {
        if (battleArenaNormal == null) {
            return "No hay una batalla en curso";
        }
        
        try {
            // Obtener el entrenador actual
            Coach currentCoach = battleArenaNormal.getCurrentCoach();
            
            // Verificar si es una máquina
            if (currentCoach instanceof Machine) {
                Machine machine = (Machine) currentCoach;
                
                // Verificar si el Pokémon activo está debilitado
                if (machine.getActivePokemon().getPs() <= 0) {
                    // Seleccionar mejor Pokémon
                    int bestPokemonIndex = machine.selectBestPokemon();
                    machine.switchPokemon(bestPokemonIndex);
                    return "La máquina cambió a " + machine.getActivePokemon().getName();
                } else {
                    // Seleccionar y usar un movimiento
                    int moveIndex = machine.selectMove();
                    String moveName = machine.getActivePokemon().getAtaques().get(moveIndex).getName();
                    
                    // Ejecutar el ataque
                    battleArenaNormal.attack(moveName, false, 
                        battleArenaNormal.getCurrentTurn() == 0);
                    
                    return "La máquina usó " + moveName;
                }
            } else {
                return "No es el turno de la máquina";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el turno de la máquina: " + e.getMessage();
        }
    }

    /**
     * Cambia el turno al siguiente jugador.
     */
    public void changeTurn() {
        if (battleArenaNormal != null) {
            battleArenaNormal.changeTurn();
        }
    }

    /**
     * Verifica si el jugador indicado tiene Pokémon vivos.
     * @param esJugador1 true para el jugador 1, false para el jugador 2
     * @return true si tiene al menos un Pokémon vivo
     */
    public boolean tienePokemonesVivos(boolean esJugador1) {
        if (battleArenaNormal == null) return false;
        
        Coach coach = esJugador1 ? 
            battleArenaNormal.getCoach(0) : 
            battleArenaNormal.getCoach(1);
        
        for (Pokemon pokemon : coach.getPokemons()) {
            if (pokemon.getPs() > 0) {
                return true;
            }
        }
        return false;
    }

    public List<String> getPokemonsVivos(boolean esJugador1) {
        return battleArenaNormal.getPokemonsVivos(esJugador1);
    }

    public void cambiarPokemonActivo(boolean esJugador1, String nombrePokemon) {
        battleArenaNormal.cambiarPokemonActivo(esJugador1, nombrePokemon);
    }

    public void useItem(String nombreItem, boolean esJugador1) throws PoobkemonException {
        battleArenaNormal.useItem(nombreItem, esJugador1);
    }

    public List<String> getPokemonsMuertos(boolean esJugador1) {
        return battleArenaNormal.getPokemonsMuertos(esJugador1);
    }
    
    public void revivirPokemon(boolean esJugador1, String nombrePokemon) throws PoobkemonException {
        battleArenaNormal.revivirPokemon(esJugador1, nombrePokemon);
    }

    public void eliminarItem(boolean esJugador1, String nombreItem) {
        battleArenaNormal.eliminarItem(esJugador1, nombreItem);
    }

    private Coach createMachineCoach(String name, String machineType, ArrayList<String> pokemonNames, ArrayList<String> items) {
        // Convertir nombres de pokémon a objetos Pokemon
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        for (String pokemonName : pokemonNames) {
            pokemons.add(PokemonFactory.createPokemon(pokemonName));
        }
        
        // Crear la máquina según el tipo
        if (machineType.equals("Gemini")) {
            return new GeminiMachine(name, pokemons, items);
        } else if (machineType.equalsIgnoreCase("attacking")) {
            return new AttackingMachine(name, pokemons, items);
        } else if (machineType.equalsIgnoreCase("defensive")) {
            return new DefensiveMachine(name, pokemons, items);
        } else if (machineType.equalsIgnoreCase("changing")) {
            return new ChangingMachine(name, pokemons, items);
        } else if (machineType.equalsIgnoreCase("expert")) {
            return new ExpertMachine(name, pokemons, items);
        } else {
            // Por defecto, usar AttackingMachine
            return new AttackingMachine(name, pokemons, items);
        }
    }
}


