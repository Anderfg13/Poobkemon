package domain;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int attack(String moveName, String itself) throws PoobkemonException {
        // Delegar la lógica del ataque a la arena de batalla
        return battleArenaNormal.attack(moveName, itself);
        
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

    public void changeTurn() {
        // Delegar el cambio de turno a la arena de batalla
        battleArenaNormal.changeTurn();
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
}


