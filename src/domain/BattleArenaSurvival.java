package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BattleArenaSurvival implementa el modo de batalla "Supervivencia" para el juego Poobkemon.
 * En este modo, ambos jugadores reciben equipos aleatorios de Pokémon y movimientos, y compiten hasta que uno pierde todos sus Pokémon.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Asigna 6 Pokémon aleatorios a cada jugador, cada uno con 4 movimientos aleatorios.</li>
 *   <li>No permite batallas Humano vs Máquina ni Máquina vs Máquina en este modo.</li>
 *   <li>Gestiona el flujo de la batalla, ataques y configuración de equipos para el modo supervivencia.</li>
 *   <li>Permite atacar tanto al oponente como a sí mismo, según el movimiento seleccionado.</li>
 * </ul>
 *
 * <p>Esta clase extiende {@link BattleArena} y debe ser utilizada exclusivamente para batallas de tipo supervivencia.
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class BattleArenaSurvival extends BattleArena {
    
    /**
     * Crea una nueva instancia de la arena de batalla en modo supervivencia.
     */
    public BattleArenaSurvival() {
        super();
    }

    /**
     * Realiza un ataque con el Pokémon activo del entrenador actual.
     * Permite atacar al oponente o a sí mismo, dependiendo del parámetro {@code itself}.
     *
     * @param moveName Nombre del ataque a usar.
     * @param itself   Si es igual a "itself", el ataque se realiza sobre el propio Pokémon.
     * @throws PoobkemonException Si ocurre un error al realizar el ataque.
     */
    public void attack(String moveName, String itself) throws PoobkemonException {
        Coach currentCoach = getCurrentCoach();
        Coach opponentCoach = getOpponentCoach();

        Pokemon attacker = currentCoach.getActivePokemon();
        Pokemon defender = opponentCoach.getActivePokemon();

        // Encuentra el ataque por nombre
        Attack attack = attacker.getAtaques().stream()
            .filter(a -> a.getName().equals(moveName))
            .findFirst()
            .orElse(null); // Si no encuentra el ataque, devuelve null

        if (attack.getPowerPoint() > 0){
            if ("itself".equals(itself)) {
            // Realiza el ataque sobre sí mismo
            attacker.attack(attacker, attack);
            } else {
            // Realiza el ataque sobre el oponente
            attacker.attack(defender, attack);
            }
        } else {
            System.out.println("No puedes usar este ataque, no tienes PP.");
        }
    }

    /**
     * Configura una batalla de supervivencia entre dos jugadores humanos.
     * Asigna 6 Pokémon aleatorios y 4 movimientos aleatorios a cada jugador.
     *
     * @param coachName1 Nombre del primer entrenador.
     * @param coachName2 Nombre del segundo entrenador.
     * @throws PoobkemonException Si no hay suficientes Pokémon o movimientos disponibles.
     */
    public void setupSurvivalBattle(String coachName1, String coachName2) throws PoobkemonException {
        // Obtener todos los Pokémon disponibles
        List<String> allPokemon = Poobkemon.getAvailablePokemon();
        
        // Verificar que hay suficientes Pokémon
        if (allPokemon.size() < 12) {
            throw new PoobkemonException("No hay suficientes Pokémon disponibles para el modo Survival.");
        }
        
        // Mezclar la lista para obtener Pokémon aleatorios
        Collections.shuffle(allPokemon);
        
        // Seleccionar 6 Pokémon para cada jugador
        ArrayList<String> pokemons1 = new ArrayList<>(allPokemon.subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(allPokemon.subList(6, 12));
        
        // Asignar 4 movimientos aleatorios a cada Pokémon
        String[][] pokemAttacks1 = assignRandomMoves(pokemons1);
        String[][] pokemAttacks2 = assignRandomMoves(pokemons2);
        
        // Configurar los entrenadores con los Pokémon y movimientos asignados
        setupCoaches(coachName1, coachName2, pokemons1, pokemons2, 
                    new ArrayList<>(), new ArrayList<>(), pokemAttacks1, pokemAttacks2);
    }

    /**
     * Asigna 4 movimientos aleatorios a cada Pokémon de la lista proporcionada.
     *
     * @param pokemons Lista de nombres de Pokémon.
     * @return Matriz de nombres de movimientos asignados a cada Pokémon.
     * @throws PoobkemonException Si no hay suficientes movimientos disponibles.
     */
    private String[][] assignRandomMoves(List<String> pokemons) throws PoobkemonException {
        // Combinar todos los tipos de ataques disponibles
        List<String> allMoves = new ArrayList<>();
        allMoves.addAll(Poobkemon.getPhysicalAttacks());
        allMoves.addAll(Poobkemon.getSpecialAttacks());
        allMoves.addAll(Poobkemon.getStatusAttacks());

        // Verificar que hay suficientes movimientos disponibles
        if (allMoves.size() < 4) {
            throw new PoobkemonException("No hay suficientes movimientos disponibles para asignar a los Pokémon.");
        }

        String[][] pokemAttacks = new String[pokemons.size()][4];

        for (int i = 0; i < pokemons.size(); i++) {
            Collections.shuffle(allMoves); // Mezclar los movimientos
            for (int j = 0; j < 4; j++) {
                pokemAttacks[i][j] = allMoves.get(j); // Asignar los primeros 4 movimientos
            }
            // Mostrar los movimientos asignados en consola
            System.out.println("Pokémon: " + pokemons.get(i) + " - Movimientos: " + 
                String.join(", ", pokemAttacks[i]));
        }
        return pokemAttacks;
    }

    /**
     * Configura una batalla entre un humano y una máquina.
     * En el modo supervivencia, este tipo de batalla no está soportada.
     *
     * @param humanName    Nombre del entrenador humano.
     * @param machineName  Nombre de la máquina.
     * @param humanPokemon Lista de Pokémon del humano.
     * @param machinePokemon Lista de Pokémon de la máquina.
     * @param humanItems   Lista de ítems del humano.
     * @param humanAttacks Matriz de ataques para los Pokémon del humano.
     * @param machineType  Tipo de máquina.
     * @throws PoobkemonException Siempre, ya que este modo no soporta esta configuración.
     */
    @Override
    public void setupHumanVsMachine(String humanName, String machineName, 
                             ArrayList<String> humanPokemon, ArrayList<String> machinePokemon,
                             ArrayList<String> humanItems, String[][] humanAttacks, 
                             String machineType) throws PoobkemonException {
        throw new PoobkemonException("Las batallas Humano vs Máquina no están soportadas en el modo supervivencia.");
    }

    /**
     * Configura una batalla entre una máquina y un humano.
     * En el modo supervivencia, este tipo de batalla no está soportada.
     *
     * @param machineName  Nombre de la máquina.
     * @param humanName    Nombre del entrenador humano.
     * @param machinePokemon Lista de Pokémon de la máquina.
     * @param humanPokemon Lista de Pokémon del humano.
     * @param humanItems   Lista de ítems del humano.
     * @param humanAttacks Matriz de ataques para los Pokémon del humano.
     * @param machineType  Tipo de máquina.
     * @throws PoobkemonException Siempre, ya que este modo no soporta esta configuración.
     */
    @Override
    public void setupMachineVsHuman(String machineName, String humanName, 
                             ArrayList<String> machinePokemon, ArrayList<String> humanPokemon,
                             ArrayList<String> humanItems, String[][] humanAttacks, 
                             String machineType) throws PoobkemonException {
        throw new PoobkemonException("Las batallas Máquina vs Humano no están soportadas en el modo supervivencia.");
    }

    /**
     * Configura una batalla entre dos máquinas.
     * En el modo supervivencia, este tipo de batalla no está soportada.
     *
     * @param machine1Name    Nombre de la primera máquina.
     * @param machine2Name    Nombre de la segunda máquina.
     * @param machine1Pokemon Lista de Pokémon de la primera máquina.
     * @param machine2Pokemon Lista de Pokémon de la segunda máquina.
     * @param machine1Type    Tipo de la primera máquina.
     * @param machine2Type    Tipo de la segunda máquina.
     * @throws PoobkemonException Siempre, ya que este modo no soporta esta configuración.
     */
    @Override
    public void setupMachineVsMachine(String machine1Name, String machine2Name, 
                             ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon,
                             String machine1Type, String machine2Type) throws PoobkemonException {
        throw new PoobkemonException("Las batallas Máquina vs Máquina no están soportadas en el modo supervivencia.");
    }
}
