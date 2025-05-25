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
    private ArrayList<BattleArena> battleArenas;
    private Map<String, String[][]> survivalMoves = new HashMap<>();
    private String battleType = "PVP"; // Valor por defecto

    /**
     * Constructor de la clase Poobkemon.
     * Inicializa la lista de arenas de batalla.
     */
    public static List<String> getAvailablePokemon() {
        return Arrays.asList(PokemonFactory.POKEMON_REGISTRY.keySet().toArray(new String[0]));
    }
    
    /*
     * Obtiene la lista de ítems disponibles en el juego.
     * Utiliza el registro de ítems de ItemFactory para obtener los nombres de los ítems.
     */
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
     * Estos ataques no infligen daño directamente, sino que aplican efectos como parálisis, sueño, etc.
     */
    public static List<String> getStatusAttacks() {
        return AttackFactory.getAllAttacks().values().stream()
            .filter(attack -> "Status".equals(attack.getAttackType()))
            .map(Attack::getName)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista completa de todos los ataques disponibles en el juego.
     * Combina ataques físicos, especiales y de estado en una sola lista.
     *
     * @return Lista de nombres de todos los ataques disponibles.
     */
    public static ArrayList<String> getAvailableAttacks() {
        ArrayList<String> allAttacks = new ArrayList<>();
        allAttacks.addAll(getPhysicalAttacks());
        allAttacks.addAll(getSpecialAttacks());
        allAttacks.addAll(getStatusAttacks());
        return allAttacks;
    } 

    /**
     * Metodo que permite atacar a un Pokémon con un ataque específico.
     * @param nombreAtaque
     * @param toItself
     * @param esJugador1
     * @return
     * @throws PoobkemonException
     */
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

    /**
     * Permite al jugador huir de la batalla actual.
     * Delegará la lógica de huida a la arena de batalla.
     */
    public void flee(){
        battleArenaNormal.flee();
    }
    
    /**
     * Permite al jugador atacar con un ataque específico.
     * Delegará la lógica de ataque a la arena de batalla.
     * @param attackName Nombre del ataque a usar.
     * @param toItself Indica si el ataque es a sí mismo o al oponente.
     * @throws PoobkemonException Si ocurre un error al atacar.
     */
    public void useItem(String itemName) throws PoobkemonException {
        // Delegar la lógica del uso de ítem a la arena de batalla
        battleArenaNormal.useItem(itemName);
    }

    /**
     * Cambia el Pokémon activo del jugador.
     * Delegará la lógica del cambio de Pokémon a la arena de batalla.
     * @param index Índice del Pokémon a cambiar.
     * @throws PoobkemonException Si ocurre un error al cambiar de Pokémon.
     */
    public void switchToPokemon(int index) throws PoobkemonException {
        System.out.println(1);
        // Delegar la lógica del cambio de Pokémon a la arena de batalla
        battleArenaNormal.switchToPokemon(index);
    }

    /**
     * Obtiene la arena de batalla actual.
     * @return La arena de batalla en curso.
     */
    public BattleArena getBattleArena() {
        return battleArenaNormal;
    }

    /**
     * Aplica un efecto de estado al Pokémon activo.
     * Delegará la lógica del efecto de estado a la arena de batalla.
     */
    public void statusEffect(){
        // Delegar la lógica del efecto de estado a la arena de batalla
        battleArenaNormal.statusEffect();
    }

    /**
     * Establece el Pokémon actual del jugador.
     * @param index Índice del Pokémon a establecer como activo.
     * @throws PoobkemonException Si ocurre un error al establecer el Pokémon.
     */
    public void setCurrentPokemon(int index) throws PoobkemonException {
        // Delegar la lógica del cambio de Pokémon a la arena de batalla
        battleArenaNormal.setCurrentPokemon(index);
    }

    /**
     * Inicia una batalla entre un humano y una máquina (humano como player 1).
     * Si la máquina no tiene Pokémon, se generan 6 aleatorios.
     *
     * @param humanName Nombre del entrenador humano.
     * @param machineName Nombre de la máquina.
     * @param humanPokemon Lista de nombres de Pokémon del humano.
     * @param machinePokemon Lista de nombres de Pokémon de la máquina (puede ser null o vacía).
     * @param humanItems Lista de ítems del humano.
     * @param humanAttacks Matriz de ataques del humano (4 ataques por Pokémon).
     * @param machineType Tipo de máquina (por ejemplo, "attacking", "defensive", etc.).
     * @param player1Collor Color del jugador 1.
     * @param player2Color Color del jugador 2.
     * @throws PoobkemonException Si ocurre un error al iniciar la batalla.
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
     * Inicia una batalla entre una máquina y un humano (máquina como player 1).
     * Si el humano no tiene Pokémon, se generan 6 aleatorios.
     *
     * @param machineName Nombre de la máquina.
     * @param humanName Nombre del entrenador humano.
     * @param machinePokemon Lista de nombres de Pokémon de la máquina.
     * @param humanPokemon Lista de nombres de Pokémon del humano (puede ser null o vacía).
     * @param humanItems Lista de ítems del humano.
     * @param humanAttacks Matriz de ataques del humano (4 ataques por Pokémon).
     * @param machineType Tipo de máquina (por ejemplo, "attacking", "defensive", etc.).
     * @param player1Color Color del jugador 1.
     * @param player2Color Color del jugador 2.
     * @throws PoobkemonException Si ocurre un error al iniciar la batalla.
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
     * Inicia una batalla entre dos máquinas.
     * Si alguna máquina no tiene Pokémon, se generan 6 aleatorios.
     *
     * @param machine1Name Nombre de la primera máquina.
     * @param machine2Name Nombre de la segunda máquina.
     * @param machine1Pokemon Lista de nombres de Pokémon de la primera máquina (puede ser null o vacía).
     * @param machine2Pokemon Lista de nombres de Pokémon de la segunda máquina (puede ser null o vacía).
     * @param machine1Type Tipo de la primera máquina (por ejemplo, "attacking", "defensive", etc.).
     * @param machine2Type Tipo de la segunda máquina (por ejemplo, "attacking", "defensive", etc.).
     * @param player1Color Color del jugador 1.
     * @param player2Color Color del jugador 2.
     * @throws PoobkemonException Si ocurre un error al iniciar la batalla.
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

    /**
     * Verifica si hay una batalla en curso.
     * @return true si hay una batalla activa, false en caso contrario.
     */
    public boolean whoStarts(){
        // Delegar la lógica del quien empieza a la arena de batalla
        return battleArenaNormal.whoStarts();
    }

    /**
     * Obtiene los movimientos del Pokémon activo del jugador.
     * @param esJugador1
     * @return
     */
    public List<String> getActivePokemonMoves(boolean esJugador1) {
        // Devuelve los 4 movimientos del pokémon activo del jugador correspondiente
        return battleArenaNormal.getActivePokemonMoves(esJugador1);
    }

    /**
     * Obtiene el nombre del Pokémon activo del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Nombre del Pokémon activo
     */
    public String getActivePokemonName(boolean esJugador1) {
        // Suponiendo que tienes una referencia a la arena de batalla
        // y que coaches[0] es jugador1, coaches[1] es jugador2
        return battleArenaNormal.getActivePokemonName(esJugador1);
    }

    /**
     * Obtiene el tipo del Pokémon activo del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Tipo del Pokémon activo
     */
    public int getActivePokemonCurrentHP(boolean esJugador1) {
        return battleArenaNormal.getActivePokemonCurrentHP(esJugador1);
    }

    /**
     * Obtiene el máximo HP del Pokémon activo del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Máximo HP del Pokémon activo
     */
    public int getActivePokemonMaxHP(boolean esJugador1) {
        return battleArenaNormal.getActivePokemonMaxHP(esJugador1);
    }

    /**
     * Obtiene los puntos de poder (PP) del ataque actual del Pokémon activo.
     * @param esJugador1
     * @param nombreAtaque
     * @return
     */
    public int getPPDeAtaqueActual(boolean esJugador1, String nombreAtaque) {
        return battleArenaNormal.getPPDeAtaqueActual(esJugador1, nombreAtaque);
    }

    /**
     * Obtiene los ítems del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Lista de ítems del jugador
     */
    public List<String> getItemsJugador(boolean esJugador1) {
        return battleArenaNormal.getItemsJugador(esJugador1);
    }

    /**
     * Verifica si un ataque es un ataque de estado que se aplica a sí mismo.
     * @param nombreAtaque Nombre del ataque a verificar
     * @return true si el ataque es un ataque de estado sobre sí mismo, false en caso contrario
     */
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

    /**
     * Obtiene la lista de Pokémon vivos del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Lista de nombres de Pokémon vivos
     */
    public List<String> getPokemonsVivos(boolean esJugador1) {
        return battleArenaNormal.getPokemonsVivos(esJugador1);
    }

    /**
     * Cambia el Pokémon activo del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @param nombrePokemon Nombre del Pokémon a cambiar como activo
     */
    public void cambiarPokemonActivo(boolean esJugador1, String nombrePokemon) {
        battleArenaNormal.cambiarPokemonActivo(esJugador1, nombrePokemon);
    }

    /**
     * Usa un ítem específico del jugador.
     * @param nombreItem Nombre del ítem a usar
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @throws PoobkemonException Si ocurre un error al usar el ítem
     */
    public void useItem(String nombreItem, boolean esJugador1) throws PoobkemonException {
        battleArenaNormal.useItem(nombreItem, esJugador1);
    }

    /**
     * Obtiene la lista de Pokémon muertos del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Lista de nombres de Pokémon muertos
     */
    public List<String> getPokemonsMuertos(boolean esJugador1) {
        return battleArenaNormal.getPokemonsMuertos(esJugador1);
    }

    /**
     * Revive un Pokémon muerto del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @param nombrePokemon Nombre del Pokémon a revivir
     * @throws PoobkemonException Si ocurre un error al revivir el Pokémon
     */
    public void revivirPokemon(boolean esJugador1, String nombrePokemon) throws PoobkemonException {
        battleArenaNormal.revivirPokemon(esJugador1, nombrePokemon);
    }

    /**
     * Elimina un ítem del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @param nombreItem Nombre del ítem a eliminar
     */
    public void eliminarItem(boolean esJugador1, String nombreItem) {
        battleArenaNormal.eliminarItem(esJugador1, nombreItem);
    }

    /**
     * Crea un entrenador de máquina con el tipo y Pokémon especificados.
     * @param name Nombre del entrenador de máquina
     * @param machineType Tipo de máquina (por ejemplo, "attacking", "defensive", etc.)
     * @param pokemonNames Lista de nombres de Pokémon para el entrenador
     * @param items Lista de ítems para el entrenador
     * @return Un objeto Coach que representa al entrenador de máquina
     */
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

    /**
     * Obtiene el Pokémon activo del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return El Pokémon activo del jugador
     */
    public int getPokemonHP(boolean esJugador1, String nombrePokemon) {
        // Busca el Pokémon por nombre en el coach correspondiente y retorna su vida actual
        for (Pokemon p : battleArenaNormal.getCoach(esJugador1 ? 0 : 1).getPokemons()) {
            if (p.getName().equals(nombrePokemon)) {
                return p.getPs();
            }
        }
        return 0; // O lanza una excepción si prefieres
    }

    /**
     * Obtiene la vida máxima de un Pokémon específico del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @param nombrePokemon Nombre del Pokémon a buscar
     * @return La vida máxima del Pokémon, o 0 si no se encuentra
     */
    public int getPokemonMaxHP(boolean esJugador1, String nombrePokemon) {
        // Busca el Pokémon por nombre en el coach correspondiente y retorna su vida máxima
        for (Pokemon p : battleArenaNormal.getCoach(esJugador1 ? 0 : 1).getPokemons()) {
            if (p.getName().equals(nombrePokemon)) {
                return p.getTotalPs();
            }
        }
        return 0; // O lanza una excepción si prefieres
    }

    /**
     * Obtiene el nombre del jugador 1.
     * @return Nombre del jugador 1
     */
    public String getNombreJugador1() {
        return battleArenaNormal.getCoach(0).getName();
    }

    /**
     * Obtiene el nombre del jugador 2.
     * @return Nombre del jugador 2
     */
    public String getNombreJugador2() {
        return battleArenaNormal.getCoach(1).getName();
    }

    /**
     * Obtiene los nombres de los Pokémon del jugador.
     * @param esJugador1 true si es el jugador 1, false si es el jugador 2
     * @return Lista de nombres de los Pokémon del jugador
     */
    public List<String> getPokemonsName(boolean esJugador1) {
        List<String> nombres = new ArrayList<>();
        for (Pokemon p : battleArenaNormal.getCoach(esJugador1 ? 0 : 1).getPokemons()) {
            nombres.add(p.getName());
        }
        return nombres;
    }

    /**
     * Guarda el estado actual de la partida en un archivo.
     * El archivo debe tener la extensión ".poob".
     *
     * @param archivo El archivo donde se guardará la partida.
     * @throws Exception Si ocurre un error al guardar la partida.
     */
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

    /**
     * Abre una partida guardada desde un archivo.
     * El archivo debe tener la extensión ".poob".
     *
     * @param archivo El archivo desde donde se abrirá la partida.
     * @return Un objeto Poobkemon con el estado de la partida.
     * @throws Exception Si ocurre un error al abrir la partida.
     */
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

    /**
     * Obtiene el color del jugador 1.
     * @return Color del jugador 1
     */
    public Color getColorJugador1() {
        return battleArenaNormal.getCoach(0).getColorCoach();
    }

    /**
     * Obtiene el color del jugador 2.
     * @return Color del jugador 2
     */
    public Color getColorJugador2() {
        return battleArenaNormal.getCoach(1).getColorCoach();
    }

    /**
     * Verifica si el jugador 1 es el que comienza la batalla.
     * @return true si el jugador 1 comienza, false en caso contrario.
     */
    public boolean jugador1Empieza() {
        return true;
    }

    /**
     * Obtiene el tipo de batalla actual.
     * @return Tipo de batalla (PVP, PVM, MVM, etc.)
     */
    public String getBattleType() {
        return battleType;
    }

    /**
     * Establece el tipo de batalla actual.
     * @param tipo Tipo de batalla (PVP, PVM, MVM, etc.)
     */
    public void setBattleType(String tipo) {
        this.battleType = tipo;
    }
}




