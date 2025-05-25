package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.*;
import java.awt.Color;

/**
 * Poobkemon es la clase principal que gestiona la lógica central del juego Poobkemon.
 * Permite la inicialización de batallas, la gestión de entrenadores, Pokémon, ataques, ítems y el flujo de turnos.
 * Sirve como punto de entrada para la interacción entre la interfaz gráfica y la lógica de juego.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite iniciar batallas en modo normal, supervivencia, humano vs máquina y máquina vs máquina.</li>
 *   <li>Gestiona la asignación de equipos, ataques y objetos para cada entrenador.</li>
 *   <li>Incluye métodos para procesar ataques, cambios de Pokémon, uso de ítems y efectos de estado.</li>
 *   <li>Proporciona utilidades para consultar información de los Pokémon, ataques e ítems disponibles.</li>
 *   <li>Permite consultar el estado actual de la batalla, los equipos y los movimientos asignados.</li>
 *   <li>Facilita la integración con la interfaz gráfica mediante métodos de consulta y actualización de estado.</li>
 *   <li>Incluye lógica para determinar el turno, el resultado de la batalla y la gestión de eventos especiales.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Poobkemon implements Serializable {
    private static final long serialVersionUID = 1L;
    private BattleArena battleArenaNormal;
    //private ArrayList<BattleArena> battleArenas;
    private Map<String, String[][]> survivalMoves = new HashMap<>();
    private String battleType = "PVP"; // Valor por defecto

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
                            String[][] pokemAttacks1, String[][] pokemAttacks2, Color player1Color, Color player2Color) throws PoobkemonException {
        battleArenaNormal = new BattleArenaNormal(); // Crear la arena de batalla
        battleArenaNormal.setupCoaches(coachName1, coachName2, pokemons1, pokemons2, items1, items2, pokemAttacks1, pokemAttacks2, player1Color, player2Color);
        this.battleType = "PVP";
    }

    /**
     * Inicia una nueva batalla entre dos entrenadores.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public void startBattleSurvival( ArrayList<String> pokemons1,
                            ArrayList<String> pokemons2,
                            String[][] pokemAttacks1, String[][] pokemAttacks2, Color player1Color, Color player2Color) throws PoobkemonException {
        battleArenaNormal = new BattleArenaNormal(); // Crear la arena de batalla
        battleArenaNormal.setupCoaches("Player 1", "Player 2", pokemons1, pokemons2, new ArrayList<>(),  new ArrayList<>() , pokemAttacks1, pokemAttacks2, player1Color, player2Color);

        // Guardar los movimientos asignados para cada jugador
        survivalMoves.put("Player 1", pokemAttacks1);
        survivalMoves.put("Player 2", pokemAttacks2);
        this.battleType = "PVP";
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
                               String machineType, Color player1Collor, Color player2Color) throws PoobkemonException {
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
            humanItems, humanAttacks, machineType, player1Collor, player2Color);
            this.battleType = "PVM";
    }

    /**
     * Inicia una batalla entre una máquina y un humano (máquina como player 1)
     */
    public void startBattleMachineVsHuman(String machineName, String humanName, 
                               ArrayList<String> machinePokemon, ArrayList<String> humanPokemon,
                               ArrayList<String> humanItems, String[][] humanAttacks, 
                               String machineType, Color player1Color, Color player2Color) throws PoobkemonException {
        // Crear una arena de batalla apropiada
        battleArenaNormal = new BattleArenaNormal();
        this.battleType = "PVM";
        // Configurar los entrenadores (máquina y humano)
        battleArenaNormal.setupMachineVsHuman(machineName, humanName, 
            machinePokemon, humanPokemon, 
            humanItems, humanAttacks, machineType, player1Color, player2Color);
    }

    /**
     * Inicia una batalla entre dos máquinas
     */
    public void startBattleMachineVsMachine(String machine1Name, String machine2Name, 
                                ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon,
                                String machine1Type, String machine2Type, Color player1Color, Color player2Color) throws PoobkemonException {
        // Crear una arena de batalla apropiada
        battleArenaNormal = new BattleArenaNormal();
        
        // Configurar las dos máquinas
        battleArenaNormal.setupMachineVsMachine(machine1Name, machine2Name, 
            machine1Pokemon, machine2Pokemon, 
            machine1Type, machine2Type, player1Color, player2Color);
            this.battleType = "MVM";
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
        List<String> ataquesEstado = getStatusAttacks();
        return ataquesEstado.contains(nombreAtaque);
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

    public int getPokemonHP(boolean esJugador1, String nombrePokemon) {
        // Busca el Pokémon por nombre en el coach correspondiente y retorna su vida actual
        for (Pokemon p : battleArenaNormal.getCoach(esJugador1 ? 0 : 1).getPokemons()) {
            if (p.getName().equals(nombrePokemon)) {
                return p.getPs();
            }
        }
        return 0; // O lanza una excepción si prefieres
    }

    public int getPokemonMaxHP(boolean esJugador1, String nombrePokemon) {
        // Busca el Pokémon por nombre en el coach correspondiente y retorna su vida máxima
        for (Pokemon p : battleArenaNormal.getCoach(esJugador1 ? 0 : 1).getPokemons()) {
            if (p.getName().equals(nombrePokemon)) {
                return p.getTotalPs();
            }
        }
        return 0; // O lanza una excepción si prefieres
    }

    public String getNombreJugador1() {
        return battleArenaNormal.getCoach(0).getName();
    }

    public String getNombreJugador2() {
        return battleArenaNormal.getCoach(1).getName();
    }

    public List<String> getPokemonsName(boolean esJugador1) {
        List<String> nombres = new ArrayList<>();
        for (Pokemon p : battleArenaNormal.getCoach(esJugador1 ? 0 : 1).getPokemons()) {
            nombres.add(p.getName());
        }
        return nombres;
    }

    public void guardarPartida(File archivo) throws Exception {
        // Puedes cambiar la extensión a ".poob" o ".dat" si lo prefieres
        if (!archivo.getName().endsWith(".poob")) {
            throw new Exception("El archivo debe tener extensión .poob");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this); // Serializa el objeto Poobkemon completo
        } catch (Exception e) {
            throw new Exception("Error al guardar la partida: " + e.getMessage());

        }    
    }        

    public static Poobkemon open(File archivo) throws Exception {
        if (!archivo.getName().endsWith(".poob")) {
            throw new Exception("El archivo debe tener extensión .poob");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Poobkemon) ois.readObject(); // Deserializa el objeto Poobkemon
        } catch (Exception e) {
            throw new Exception("Error al abrir la partida: " + e.getMessage());
        }
    }

    public Color getColorJugador1() {
        return battleArenaNormal.getCoach(0).getColorCoach();
    }

    public Color getColorJugador2() {
        return battleArenaNormal.getCoach(1).getColorCoach();
    }

    public boolean jugador1Empieza() {
    // Ejemplo: si tienes una variable booleana
    // return jugador1Empieza;
    // O si tienes un método en battleArenaNormal:
    // return battleArenaNormal.jugador1Empieza();
    // Si no tienes nada, puedes devolver true por defecto:
    return true;
}

public String getBattleType() {
    return battleType;
}

public void setBattleType(String tipo) {
    this.battleType = tipo;
}
}




