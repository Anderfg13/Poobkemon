package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;

/**
 * BattleArena es una clase abstracta que representa el campo de batalla donde se desarrollan los combates de Poobkemon.
 * Gestiona el flujo de la batalla, los entrenadores, el control de turnos, el temporizador, el uso de ítems y ataques,
 * así como la lógica para pausar, reanudar y finalizar la batalla.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Gestión de entrenadores (humanos o máquinas) y sus equipos de Pokémon e ítems.</li>
 *   <li>Control de turnos y temporizador para limitar el tiempo de cada acción.</li>
 *   <li>Métodos para atacar, usar ítems, cambiar de Pokémon y huir de la batalla.</li>
 *   <li>Soporte para pausar y reanudar la batalla, así como para determinar el estado de la misma.</li>
 *   <li>Permite configurar diferentes tipos de enfrentamientos: Humano vs Humano, Humano vs Máquina, Máquina vs Máquina.</li>
 *   <li>Proporciona utilidades para consultar el estado de los Pokémon, ítems y turnos activos.</li>
 * </ul>
 *
 * <p>Esta clase debe ser extendida para implementar la lógica específica de cada tipo de batalla.
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public abstract class BattleArena implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Número de turno actual. */
    protected int currentTurn = 0;
    /** Tiempo máximo por turno en segundos. */
    private static final int MAX_TIME_SECONDS = 20;
    /** Arreglo de entrenadores participantes en la batalla. */
    protected Coach[] coaches = new Coach[2];
    /** Indica si la batalla está pausada. */
    public boolean isPaused;
    /** Temporizador para controlar el tiempo de cada turno. */
    private Timer turnTimer;
    /** Generador de números aleatorios para decisiones y sorteos. */
    private Random rand = new Random();
    /** Tiempo restante para el turno actual en milisegundos. */
    private long timeRemaining = MAX_TIME_SECONDS * 1000L;
    /** Momento en que se pausó la batalla. */
    private long pauseStartTime;
    /** Indica si la batalla ha finalizado. */
    protected boolean battleFinished;
    /** Indica si es el turno del jugador 1. */
    protected boolean isPlayer1Turn = false;

    /**
     * Constructor base de la arena de batalla.
     * Inicializa los estados de pausa y finalización.
     */
    public BattleArena() {
        this.isPaused = false;
        this.battleFinished = false;
    }

    /**
     * Configura los entrenadores y determina quién inicia la batalla.
     *
     * @param coachName1     Nombre del primer entrenador.
     * @param coachName2     Nombre del segundo entrenador.
     * @param pokemons1      Lista de nombres de Pokémon del primer entrenador.
     * @param pokemons2      Lista de nombres de Pokémon del segundo entrenador.
     * @param items1         Lista de ítems del primer entrenador.
     * @param items2         Lista de ítems del segundo entrenador.
     * @param pokemAttacks1  Matriz de ataques para los Pokémon del primer entrenador.
     * @param pokemAttacks2  Matriz de ataques para los Pokémon del segundo entrenador.
     * @throws PoobkemonException Si ocurre un error al crear los entrenadores o Pokémon.
     */
    public void setupCoaches(String coachName1, String coachName2, ArrayList<String> pokemons1,
                             ArrayList<String> pokemons2, ArrayList<String> items1, ArrayList<String> items2,
                             String[][] pokemAttacks1, String[][] pokemAttacks2, Color player1Color, Color player2Color) throws PoobkemonException {
        boolean firstStarts = rand.nextBoolean();

        // Crear y asignar entrenadores
        if (firstStarts) {
            coaches[0] = new HumanCoach(coachName1, createPokemonList(pokemons1, pokemAttacks1), items1);
            coaches[1] = new HumanCoach(coachName2, createPokemonList(pokemons2, pokemAttacks2), items2);
        } else {
            coaches[0] = new HumanCoach(coachName2, createPokemonList(pokemons2, pokemAttacks2), items2);
            coaches[1] = new HumanCoach(coachName1, createPokemonList(pokemons1, pokemAttacks1), items1);
        }
        coaches[0].setColor(player1Color);
        coaches[1].setColor(player2Color);

        currentTurn = 0;
    }

    /**
     * Verifica si la batalla ha terminado.
     *
     * @return {@code true} si la batalla ha finalizado o todos los Pokémon de un entrenador están debilitados.
     */
    public boolean isBattleFinished() {
        System.out.println(coaches[0].areAllPokemonFainted() || coaches[1].areAllPokemonFainted());
        return this.battleFinished || coaches[0].areAllPokemonFainted() || coaches[1].areAllPokemonFainted();
    }

    /**
     * Inicia el temporizador para un turno específico.
     *
     * @param coachIndex Índice del entrenador cuyo turno es actual.
     */
    protected void startTurnTimer(final int coachIndex) {
        turnTimer = new Timer();
        turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    coaches[coachIndex].handleTurnTimeout();
                    cancelTurnTimer();
                    currentTurn = 1 - coachIndex; // Cambiar turno automáticamente
                }
            }
        }, timeRemaining);
    }

    /**
     * Cancela el temporizador de turno activo.
     */
    protected void cancelTurnTimer() {
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer = null;
        }
    }

    /**
     * Pausa la batalla y detiene el temporizador.
     */
    public void pauseBattle() {
        if (!isPaused) {
            this.isPaused = true;
            pauseStartTime = System.currentTimeMillis();
            cancelTurnTimer();
            System.out.println("La batalla ha sido pausada.");
        }
    }

    /**
     * Reanuda la batalla y ajusta el temporizador según el tiempo pausado.
     */
    public void resumeBattle() {
        if (isPaused) {
            this.isPaused = false;
            long pauseDuration = System.currentTimeMillis() - pauseStartTime;
            timeRemaining -= pauseDuration;
            System.out.println("La batalla ha sido reanudada. Tiempo restante: " + timeRemaining / 1000 + " segundos.");
            startTurnTimer(currentTurn);
        }
    }

    /**
     * Finaliza la batalla y limpia recursos asociados.
     */
    public void endBattle() {
        cancelTurnTimer();
    }

    /**
     * Obtiene el entrenador actual (el que tiene el turno).
     *
     * @return Entrenador actual.
     */
    public Coach getCurrentCoach() {
        return coaches[currentTurn];
    }

    /**
     * Obtiene el entrenador en la posición especificada.
     *
     * @param index Índice del entrenador (0 o 1).
     * @return Entrenador correspondiente o {@code null} si el índice es inválido.
     */
    public Coach getCoach(int index) {
        if (index >= 0 && index < coaches.length) {
            return coaches[index];
        }
        return null;
    }

    /**
     * Obtiene el número de turno actual.
     *
     * @return Número de turno.
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Obtiene el entrenador oponente al actual.
     *
     * @return Entrenador oponente.
     */
    public Coach getOpponentCoach() {
        return coaches[1 - currentTurn];
    }

    /**
     * Cambia al siguiente turno.
     */
    protected void nextTurn() {
        currentTurn = 1 - currentTurn;
    }

    /**
     * Obtiene el arreglo de entrenadores.
     *
     * @return Arreglo de entrenadores.
     */
    public Coach[] getCoaches() {
        return coaches;
    }

    /**
     * Realiza un ataque con el Pokémon activo del entrenador actual.
     *
     * @param nombreAtaque Nombre del ataque a usar.
     * @param toItself     {@code true} si el ataque es sobre sí mismo.
     * @param esJugador1   {@code true} si el entrenador actual es el jugador 1.
     * @return Daño infligido por el ataque.
     * @throws PoobkemonException Si el ataque no es válido o no existe.
     */
    public int attack(String nombreAtaque, boolean toItself, boolean esJugador1) throws PoobkemonException {
        Coach currentCoach = coaches[esJugador1 ? 0 : 1];
        Coach opponentCoach = coaches[esJugador1 ? 1 : 0];
        Pokemon attacker = currentCoach.getActivePokemon();
        Pokemon defender = opponentCoach.getActivePokemon();

        Attack attack = attacker.getAtaquePorNombre(nombreAtaque);

        if (attack == null) throw new PoobkemonException("El Pokémon no conoce este ataque.");

        if (toItself) {
            return attacker.attack(attacker, attack);
        } else {
            return attacker.attack(defender, attack);
        }
    }

    /**
     * Usa un ítem del entrenador actual.
     *
     * @param itemName Nombre del ítem a usar.
     * @throws PoobkemonException Si ocurre un error al usar el ítem.
     */
    public void useItem(String itemName) throws PoobkemonException {
        Coach currentCoach = getCurrentCoach();
        Item item = ItemFactory.createItem(itemName);
        currentCoach.useItem(item);
    }

    /**
     * Cambia al Pokémon activo del entrenador actual.
     *
     * @param index Índice del Pokémon al que se cambiará.
     * @throws PoobkemonException Si ocurre un error al cambiar de Pokémon.
     */
    public void switchToPokemon(int index) throws PoobkemonException {
        System.out.println(2);
        Coach currentCoach = getCurrentCoach();
        currentCoach.switchPokemon(index);
    }

    /**
     * Crea una lista de Pokémon a partir de nombres y ataques.
     *
     * @param pokemonNames  Lista de nombres de Pokémon.
     * @param pokemAttacks  Matriz de nombres de ataques para cada Pokémon.
     * @return Lista de instancias de Pokémon.
     */
    private ArrayList<Pokemon> createPokemonList(ArrayList<String> pokemonNames, String[][] pokemAttacks) {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        for (int i = 0; i < pokemonNames.size(); i++) {
            String pokemonName = pokemonNames.get(i);
            Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);

            for (String attackName : pokemAttacks[i]) {
                if (attackName != null) {
                    Attack attack = AttackFactory.createAttack(attackName);
                    pokemon.addAttack(attack);
                }
            }
            pokemonList.add(pokemon);
        }
        return pokemonList;
    }

    /**
     * Hace que el entrenador actual huya de la batalla.
     */
    public void flee() {
        getCurrentCoach().fleeBattle();
        setbattleFinished(true);
        endBattle();
    }

    /**
     * Marca la batalla como finalizada o no.
     *
     * @param battleFinished {@code true} si la batalla ha terminado.
     */
    public void setbattleFinished(boolean battleFinished) {
        this.battleFinished = battleFinished;
    }

    /**
     * Aplica efectos de estado al Pokémon activo del oponente.
     */
    public void statusEffect() {
        coaches[1].getActivePokemon().getStatus();
        if (coaches[0].getActivePokemon().getStatus() != 0 && coaches[0].getActivePokemon().getTurnStatus() > 0) {
            coaches[0].getActivePokemon().setTurnStatus(coaches[0].getActivePokemon().getTurnStatus() - 1);
            coaches[0].getActivePokemon().setPs(coaches[0].getActivePokemon().getPs() - 10);
        }
    }

    /**
     * Cambia el turno al siguiente entrenador.
     */
    public void changeTurn() {
        currentTurn = 1 - currentTurn;
    }

    /**
     * Cambia el Pokémon activo del entrenador actual al indicado por el índice.
     *
     * @param index Índice del Pokémon a activar.
     * @throws PoobkemonException Si ocurre un error al cambiar de Pokémon.
     */
    public void setCurrentPokemon(int index) throws PoobkemonException {
        Coach currentCoach = getCurrentCoach();
        currentCoach.switchToPokemon(index);
    }

    /**
     * Alterna el turno inicial entre los jugadores.
     *
     * @return {@code true} si es el turno del jugador 1, {@code false} en caso contrario.
     */
    public boolean whoStarts() {
        this.isPlayer1Turn = !this.isPlayer1Turn;
        return this.isPlayer1Turn;
    }

    /**
     * Obtiene la lista de movimientos del Pokémon activo de un jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Lista de nombres de ataques.
     */
    public List<String> getActivePokemonMoves(boolean esJugador1) {
        Pokemon activo = coaches[esJugador1 ? 0 : 1].getActivePokemon();
        return activo.getNombreAtaques();
    }

    /**
     * Obtiene el nombre del Pokémon activo de un jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Nombre del Pokémon activo.
     */
    public String getActivePokemonName(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getName();
    }

    /**
     * Obtiene los puntos de vida actuales del Pokémon activo de un jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Puntos de vida actuales.
     */
    public int getActivePokemonCurrentHP(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getPs();
    }

    /**
     * Obtiene los puntos de vida máximos del Pokémon activo de un jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Puntos de vida máximos.
     */
    public int getActivePokemonMaxHP(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getTotalPs();
    }

    /**
     * Obtiene los PP actuales de un ataque del Pokémon activo.
     *
     * @param esJugador1   {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @param nombreAtaque Nombre del ataque.
     * @return PP actuales del ataque.
     */
    public int getPPDeAtaqueActual(boolean esJugador1, String nombreAtaque) {
        Pokemon activo = coaches[esJugador1 ? 0 : 1].getActivePokemon();
        return activo.getPPDeAtaque(nombreAtaque);
    }

    /**
     * Obtiene los PP máximos de un ataque del Pokémon activo.
     *
     * @param esJugador1   {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @param nombreAtaque Nombre del ataque.
     * @return PP máximos del ataque.
     */
    public int getPPMaxDeAtaqueActual(boolean esJugador1, String nombreAtaque) {
        Pokemon activo = coaches[esJugador1 ? 0 : 1].getActivePokemon();
        return activo.getPPMaxDeAtaque(nombreAtaque);
    }

    /**
     * Obtiene la lista de ítems del jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Lista de nombres de ítems.
     */
    public List<String> getItemsJugador(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getNombreItems();
    }

    /**
     * Verifica si el jugador tiene Pokémon vivos.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return {@code true} si tiene al menos un Pokémon con vida.
     */
    public boolean tienePokemonesVivos(boolean esJugador1) {
        Coach coach = coaches[esJugador1 ? 0 : 1];
        for (Pokemon p : coach.getPokemons()) {
            if (p.getPs() > 0) return true;
        }
        return false;
    }

    /**
     * Obtiene la lista de nombres de Pokémon vivos del jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Lista de nombres de Pokémon vivos.
     */
    public List<String> getPokemonsVivos(boolean esJugador1) {
        List<String> vivos = new ArrayList<>();
        for (Pokemon p : coaches[esJugador1 ? 0 : 1].getPokemons()) {
            if (p.getPs() > 0) vivos.add(p.getName());
        }
        return vivos;
    }

    /**
     * Cambia el Pokémon activo del jugador por nombre.
     *
     * @param esJugador1    {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @param nombrePokemon Nombre del Pokémon a activar.
     */
    public void cambiarPokemonActivo(boolean esJugador1, String nombrePokemon) {
        coaches[esJugador1 ? 0 : 1].cambiarPokemonActivo(nombrePokemon);
    }

    /**
     * Usa un ítem por nombre para el jugador especificado.
     *
     * @param nombreItem  Nombre del ítem.
     * @param esJugador1  {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @throws PoobkemonException Si ocurre un error al usar el ítem.
     */
    public void useItem(String nombreItem, boolean esJugador1) throws PoobkemonException {
        coaches[esJugador1 ? 0 : 1].useItem(nombreItem);
    }

    /**
     * Obtiene la lista de nombres de Pokémon muertos del jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Lista de nombres de Pokémon muertos.
     */
    public List<String> getPokemonsMuertos(boolean esJugador1) {
        List<String> muertos = new ArrayList<>();
        for (Pokemon p : coaches[esJugador1 ? 0 : 1].getPokemons()) {
            if (p.getPs() == 0) muertos.add(p.getName());
        }
        return muertos;
    }

    /**
     * Revive un Pokémon del jugador especificado por nombre.
     *
     * @param esJugador1    {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @param nombrePokemon Nombre del Pokémon a revivir.
     * @throws PoobkemonException Si ocurre un error al revivir el Pokémon.
     */
    public void revivirPokemon(boolean esJugador1, String nombrePokemon) throws PoobkemonException {
        coaches[esJugador1 ? 0 : 1].revivirPokemon(nombrePokemon);
    }

    /**
     * Establece el estado del Pokémon activo del jugador.
     *
     * @param status      Nuevo estado.
     * @param esJugador1  {@code true} para el jugador 1, {@code false} para el jugador 2.
     */
    public void setPokemonStatus(int status, boolean esJugador1) {
        coaches[esJugador1 ? 0 : 1].getActivePokemon().setStatus(status);
    }

    /**
     * Obtiene el estado del Pokémon activo del jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Estado del Pokémon activo.
     */
    public int getPokemonStatus(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getStatus();
    }

    /**
     * Obtiene el número de turnos restantes del estado del Pokémon activo.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @return Número de turnos restantes del estado.
     */
    public int getTurnStatus(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getTurnStatus();
    }

    /**
     * Establece el número de turnos restantes del estado del Pokémon activo.
     *
     * @param turnStatus  Número de turnos.
     * @param esJugador1  {@code true} para el jugador 1, {@code false} para el jugador 2.
     */
    public void setTurnStatus(int turnStatus, boolean esJugador1) {
        coaches[esJugador1 ? 0 : 1].getActivePokemon().setTurnStatus(turnStatus);
    }

    /**
     * Elimina un ítem del inventario del jugador.
     *
     * @param esJugador1 {@code true} para el jugador 1, {@code false} para el jugador 2.
     * @param nombreItem Nombre del ítem a eliminar.
     */
    public void eliminarItem(boolean esJugador1, String nombreItem) {
        coaches[esJugador1 ? 0 : 1].eliminarItem(nombreItem);
    }

    /**
     * Establece un entrenador en la posición especificada.
     *
     * @param index Índice del entrenador (0 para el primer jugador, 1 para el segundo).
     * @param coach El entrenador a establecer.
     */
    public void setCoach(int index, Coach coach) {
        if (index >= 0 && index < coaches.length) {
            coaches[index] = coach;
        }
    }

    /**
     * Establece el turno actual.
     *
     * @param turn El número de turno a establecer.
     */
    public void setCurrentTurn(int turn) {
        this.currentTurn = turn;
    }

    /**
     * Configura una batalla entre un humano y una máquina.
     *
     * @param humanName     Nombre del entrenador humano.
     * @param machineName   Nombre de la máquina.
     * @param humanPokemon  Lista de Pokémon del humano.
     * @param machinePokemon Lista de Pokémon de la máquina.
     * @param humanItems    Lista de ítems del humano.
     * @param humanAttacks  Matriz de ataques para los Pokémon del humano.
     * @param machineType   Tipo de máquina a crear.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public abstract void setupHumanVsMachine(String humanName, String machineName,
                     ArrayList<String> humanPokemon, ArrayList<String> machinePokemon,
                     ArrayList<String> humanItems, String[][] humanAttacks,
                     String machineType, Color player1Color, Color player2Color) throws PoobkemonException;

    /**
     * Configura una batalla entre una máquina y un humano (máquina como player 1).
     *
     * @param machineName   Nombre de la máquina.
     * @param humanName     Nombre del entrenador humano.
     * @param machinePokemon Lista de Pokémon de la máquina.
     * @param humanPokemon  Lista de Pokémon del humano.
     * @param humanItems    Lista de ítems del humano.
     * @param humanAttacks  Matriz de ataques para los Pokémon del humano.
     * @param machineType   Tipo de máquina a crear.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public abstract void setupMachineVsHuman(String machineName, String humanName,
                     ArrayList<String> machinePokemon, ArrayList<String> humanPokemon,
                     ArrayList<String> humanItems, String[][] humanAttacks,
                     String machineType, Color player1Color, Color player2Color) throws PoobkemonException;

    /**
     * Configura una batalla entre dos máquinas.
     *
     * @param machine1Name   Nombre de la primera máquina.
     * @param machine2Name   Nombre de la segunda máquina.
     * @param machine1Pokemon Lista de Pokémon de la primera máquina.
     * @param machine2Pokemon Lista de Pokémon de la segunda máquina.
     * @param machine1Type   Tipo de la primera máquina.
     * @param machine2Type   Tipo de la segunda máquina.
     * @throws PoobkemonException Si ocurre un error al configurar la batalla.
     */
    public abstract void setupMachineVsMachine(String machine1Name, String machine2Name,
                     ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon,
                     String machine1Type, String machine2Type, Color player1Color, Color player2Color) throws PoobkemonException;
}

