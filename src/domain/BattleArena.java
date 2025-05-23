package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BattleArena {
    protected int currentTurn = 0;
    private static final int MAX_TIME_SECONDS = 20;
    protected Coach[] coaches = new Coach[2];
    private boolean isPaused;
    private Timer turnTimer;
    private Random rand = new Random();
    private long timeRemaining = MAX_TIME_SECONDS * 1000L; // Tiempo restante en milisegundos
    private long pauseStartTime; // Momento en que se pausó el juego
    protected boolean battleFinished;
    protected boolean isPlayer1Turn;


    public BattleArena() {
        this.isPaused = false;
        this.battleFinished = false;
    }

    /**
     * Configura los entrenadores y determina quién inicia.
     */
    public void setupCoaches(String coachName1, String coachName2, ArrayList<String> pokemons1,
                             ArrayList<String> pokemons2, ArrayList<String> items1, ArrayList<String> items2,
                             String[][] pokemAttacks1, String[][] pokemAttacks2) throws PoobkemonException {
        boolean firstStarts = rand.nextBoolean();

        // Crear y asignar entrenadores
        if (firstStarts) {
            coaches[0] = new HumanCoach(coachName1, createPokemonList(pokemons1, pokemAttacks1), items1);
            coaches[1] = new HumanCoach(coachName2, createPokemonList(pokemons2, pokemAttacks2), items2);
        } else {
            coaches[0] = new HumanCoach(coachName2, createPokemonList(pokemons2, pokemAttacks2), items2);
            coaches[1] = new HumanCoach(coachName1, createPokemonList(pokemons1, pokemAttacks1), items1);
        }

        currentTurn = 0;
    }

    /**
     * Verifica si la batalla ha terminado.
     */
    public boolean isBattleFinished() {
    	System.out.println(coaches[0].areAllPokemonFainted() || coaches[1].areAllPokemonFainted());
        return this.battleFinished||coaches[0].areAllPokemonFainted() || coaches[1].areAllPokemonFainted();
    }

    /**
     * Inicia el temporizador para un turno específico.
     */
    protected void startTurnTimer(final int coachIndex) {
        turnTimer = new Timer();
        turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    //System.out.println("Tiempo excedido para " + coaches[coachIndex].getName());
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
     * Pausa la batalla.
     */
    public void pauseBattle() {
        if (!isPaused) {
            this.isPaused = true;
            pauseStartTime = System.currentTimeMillis(); // Guardar el momento en que se pausó
            cancelTurnTimer(); // Detener el temporizador
            System.out.println("La batalla ha sido pausada.");
        }
    }

    /**
     * Reanuda la batalla.
     */
    public void resumeBattle() {
        if (isPaused) {
            this.isPaused = false;
            long pauseDuration = System.currentTimeMillis() - pauseStartTime; // Calcular la duración de la pausa
            timeRemaining -= pauseDuration; // Reducir el tiempo restante por la duración de la pausa
            System.out.println("La batalla ha sido reanudada. Tiempo restante: " + timeRemaining / 1000 + " segundos.");
            startTurnTimer(currentTurn); // Reiniciar el temporizador con el tiempo restante
        }
    }

    /**
     * Finaliza la batalla y limpia recursos.
     */
    public void endBattle() {
        cancelTurnTimer();
    }

    /**
     * Obtiene el entrenador actual.
     */
    public Coach getCurrentCoach() {
        return coaches[currentTurn];
    }

    /**
     * Obtiene el entrenador en la posición especificada.
     */
    public Coach getCoach(int index) {
        if (index >= 0 && index < coaches.length) {
            return coaches[index];
        }
        return null;
    }

    /**
     * Obtiene el turno actual.
     */
    public int getCurrentTurn() {
        return currentTurn;
    }


    /**
     * Obtiene el entrenador oponente.
     */
    protected Coach getOpponentCoach() {
        return coaches[1 - currentTurn];
    }

    /**
     * Cambia al siguiente turno.
     */
    protected void nextTurn() {
        currentTurn = 1 - currentTurn;
    }

    /**
     * Obtiene los entrenadores.
     */
    public Coach[] getCoaches() {
        return coaches;
    }

    public int attack(String nombreAtaque, boolean toItself, boolean esJugador1) throws PoobkemonException {
        Coach currentCoach = coaches[esJugador1 ? 0 : 1];
        Coach opponentCoach = coaches[esJugador1 ? 1 : 0];
        Pokemon attacker = currentCoach.getActivePokemon();
        Pokemon defender = opponentCoach.getActivePokemon();

        Attack attack = attacker.getAtaquePorNombre(nombreAtaque); // Implementa este método si no existe

        if (attack == null) throw new PoobkemonException("El Pokémon no conoce este ataque.");

        if (toItself) {
            return attacker.attack(attacker, attack);
        } else {
            return attacker.attack(defender, attack);
        }
    }


    public void useItem(String itemName) throws PoobkemonException {
        // Obtener el entrenador actual
        Coach currentCoach = getCurrentCoach();

        // Buscar el ítem por su nombre
        Item item = ItemFactory.createItem(itemName);

        // Delegar el uso del ítem al entrenador actual
        currentCoach.useItem(item);
    }

    public void switchToPokemon(int index) throws PoobkemonException {
        System.out.println(2);
        // Obtener el entrenador actual
        Coach currentCoach = getCurrentCoach();
        // Cambiar al Pokémon activo
        currentCoach.switchPokemon(index);
    }

    private ArrayList<Pokemon> createPokemonList(ArrayList<String> pokemonNames, String[][] pokemAttacks) {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        for (int i = 0; i < pokemonNames.size(); i++) {
            String pokemonName = pokemonNames.get(i);

            // Crear el Pokémon
            Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);

            // Asignar ataques al Pokémon
            for (String attackName : pokemAttacks[i]) {
                if (attackName != null) {
                    Attack attack = AttackFactory.createAttack(attackName);
                    pokemon.addAttack(attack);
                }
            }

            // Agregar el Pokémon a la lista
            pokemonList.add(pokemon);
        }

        return pokemonList;
    }

    public void flee() {
        getCurrentCoach().fleeBattle(); // Marca al entrenador actual como que ha huido
        setbattleFinished(true); // Marca la batalla como terminada
        endBattle(); // Finaliza la batalla
    }

    public void setbattleFinished(boolean battleFinished) {
        this.battleFinished = battleFinished;
    }

    public void statusEffect(){
        coaches[1].getActivePokemon().getStatus();
        if (coaches[0].getActivePokemon().getStatus() != 0 && coaches[0].getActivePokemon().getTurnStatus() > 0) {
            coaches[0].getActivePokemon().setTurnStatus(coaches[0].getActivePokemon().getTurnStatus() - 1);
            
            coaches[0].getActivePokemon().setPs(coaches[0].getActivePokemon().getPs() - 10);
        }
            
    }

    public void changeTurn() {
        currentTurn = 1 - currentTurn;
        //startTurnTimer(currentTurn);
    }   

    public void setCurrentPokemon(int index) throws PoobkemonException {
        // Cambia el Pokémon activo al indicado por el jugador
        Coach currentCoach = getCurrentCoach();
        currentCoach.switchToPokemon(index);
    }

    public boolean whoStarts(){
        this.isPlayer1Turn = rand.nextBoolean();
        return this.isPlayer1Turn;
    }

    public List<String> getActivePokemonMoves(boolean esJugador1) {
        // Si esJugador1 == true, coaches[0]; si esJugador1 == false, coaches[1]
        Pokemon activo = coaches[esJugador1 ? 0 : 1].getActivePokemon();
        return activo.getNombreAtaques();
    }

    public String getActivePokemonName(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getName();
    }

    public int getActivePokemonCurrentHP(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getPs();
    }

    public int getActivePokemonMaxHP(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getTotalPs();
    }

    public int getPPDeAtaqueActual(boolean esJugador1, String nombreAtaque) {
        Pokemon activo = coaches[esJugador1 ? 0 : 1].getActivePokemon();
        return activo.getPPDeAtaque(nombreAtaque);
    }

    public int getPPMaxDeAtaqueActual(boolean esJugador1, String nombreAtaque) {
        Pokemon activo = coaches[esJugador1 ? 0 : 1].getActivePokemon();
        return activo.getPPMaxDeAtaque(nombreAtaque);
    }

    public List<String> getItemsJugador(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getNombreItems(); // Ajusta según tu estructura
    }

    public boolean tienePokemonesVivos(boolean esJugador1) {
        Coach coach = coaches[esJugador1 ? 0 : 1];
        for (Pokemon p : coach.getPokemons()) {
            if (p.getPs() > 0) return true;
        }
        return false;
    }

    public List<String> getPokemonsVivos(boolean esJugador1) {
        List<String> vivos = new ArrayList<>();
        for (Pokemon p : coaches[esJugador1 ? 0 : 1].getPokemons()) {
            if (p.getPs() > 0) vivos.add(p.getName());
        }
        return vivos;
    }

    public void cambiarPokemonActivo(boolean esJugador1, String nombrePokemon) {
        coaches[esJugador1 ? 0 : 1].cambiarPokemonActivo(nombrePokemon);
    }

    public void useItem(String nombreItem, boolean esJugador1) throws PoobkemonException {
        coaches[esJugador1 ? 0 : 1].useItem(nombreItem);
    }

    public List<String> getPokemonsMuertos(boolean esJugador1) {
    List<String> muertos = new ArrayList<>();
        for (Pokemon p : coaches[esJugador1 ? 0 : 1].getPokemons()) {
            if (p.getPs() == 0) muertos.add(p.getName());
        }
        return muertos;
    }

    public void revivirPokemon(boolean esJugador1, String nombrePokemon) throws PoobkemonException {
        coaches[esJugador1 ? 0 : 1].revivirPokemon(nombrePokemon);
    }

    public void setPokemonStatus(int status, boolean esJugador1) {
        coaches[esJugador1 ? 0 : 1].getActivePokemon().setStatus(status);
    }

    public int getPokemonStatus(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getStatus();
    }

    public int getTurnStatus(boolean esJugador1) {
        return coaches[esJugador1 ? 0 : 1].getActivePokemon().getTurnStatus();
    }

    public void setTurnStatus(int turnStatus, boolean esJugador1) {
        coaches[esJugador1 ? 0 : 1].getActivePokemon().setTurnStatus(turnStatus);
    }

    public void eliminarItem(boolean esJugador1, String nombreItem) {
        coaches[esJugador1 ? 0 : 1].eliminarItem(nombreItem);
    }

    public int getPokemonHP(boolean esJugador1, String nombrePokemon) {
        Coach coach = getCoach(esJugador1 ? 0 : 1);
        Pokemon pokemon = coach.getPokemonByName(nombrePokemon);
        return pokemon != null ? pokemon.getPs() : 0;
    }

    public int getPokemonMaxHP(boolean esJugador1, String nombrePokemon) {
        Coach coach = getCoach(esJugador1 ? 0 : 1);
        Pokemon pokemon = coach.getPokemonByName(nombrePokemon);
        return pokemon != null ? pokemon.getTotalPs() : 0;
    }

    /**
     * Establece un entrenador en la posición especificada.
     * @param index Índice del entrenador (0 para el primer jugador, 1 para el segundo)
     * @param coach El entrenador a establecer
     */
    protected void setCoach(int index, Coach coach) {
        if (index >= 0 && index < coaches.length) {
            coaches[index] = coach;
        }
    }

    /**
     * Establece el turno actual.
     * @param turn El número de turno a establecer
     */
    protected void setCurrentTurn(int turn) {
        this.currentTurn = turn;
    }

    /**
     * Configura una batalla entre un humano y una máquina.
     * @param humanName Nombre del entrenador humano
     * @param machineName Nombre de la máquina
     * @param humanPokemon Lista de pokémon del humano
     * @param machinePokemon Lista de pokémon de la máquina
     * @param humanItems Lista de ítems del humano
     * @param humanAttacks Matriz de ataques para los pokémon del humano
     * @param machineType Tipo de máquina a crear
     * @throws PoobkemonException Si ocurre un error al configurar la batalla
     */
    public abstract void setupHumanVsMachine(String humanName, String machineName, 
                     ArrayList<String> humanPokemon, ArrayList<String> machinePokemon,
                     ArrayList<String> humanItems, String[][] humanAttacks, 
                     String machineType) throws PoobkemonException;

    /**
     * Configura una batalla entre una máquina y un humano (máquina como player 1).
     * @param machineName Nombre de la máquina
     * @param humanName Nombre del entrenador humano
     * @param machinePokemon Lista de pokémon de la máquina
     * @param humanPokemon Lista de pokémon del humano
     * @param humanItems Lista de ítems del humano
     * @param humanAttacks Matriz de ataques para los pokémon del humano
     * @param machineType Tipo de máquina a crear
     * @throws PoobkemonException Si ocurre un error al configurar la batalla
     */
    public abstract void setupMachineVsHuman(String machineName, String humanName, 
                     ArrayList<String> machinePokemon, ArrayList<String> humanPokemon,
                     ArrayList<String> humanItems, String[][] humanAttacks, 
                     String machineType) throws PoobkemonException;

    /**
     * Configura una batalla entre dos máquinas.
     * @param machine1Name Nombre de la primera máquina
     * @param machine2Name Nombre de la segunda máquina
     * @param machine1Pokemon Lista de pokémon de la primera máquina
     * @param machine2Pokemon Lista de pokémon de la segunda máquina
     * @param machine1Type Tipo de la primera máquina
     * @param machine2Type Tipo de la segunda máquina
     * @throws PoobkemonException Si ocurre un error al configurar la batalla
     */
    public abstract void setupMachineVsMachine(String machine1Name, String machine2Name, 
                     ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon,
                     String machine1Type, String machine2Type) throws PoobkemonException;
}

