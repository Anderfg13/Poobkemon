package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random; 


/**
 * MachineFactory es una fábrica para crear diferentes tipos de máquinas (jugadores automáticos)
 * con estrategias variadas para el juego Poobkemon.
 * 
 * <p>Permite generar instancias de máquinas con equipos y objetos aleatorios, 
 * asignando ataques y objetos de forma automática según el tipo y dificultad seleccionados.
 * 
 * <p>Tipos de máquina soportados:
 * <ul>
 *   <li>ATTACKING - Máquina enfocada en el ataque.</li>
 *   <li>DEFENSIVE - Máquina enfocada en la defensa.</li>
 *   <li>CHANGING - Máquina que cambia de estrategia según el rival.</li>
 *   <li>EXPERT - Máquina experta que combina todas las estrategias.</li>
 *   <li>GEMINI - Máquina que utiliza la IA generativa Gemini.</li>
 * </ul>
 * 
 * <p>La fábrica se encarga de:
 * <ul>
 *   <li>Seleccionar pokémon aleatorios y asignarles ataques aleatorios.</li>
 *   <li>Seleccionar ítems aleatorios para el equipo de la máquina.</li>
 *   <li>Instanciar la subclase de {@code Machine} correspondiente al tipo solicitado.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class MachineFactory implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Random random = new Random();
    
    /**
     * Tipos disponibles de máquina.
     */
    public enum MachineType {
        ATTACKING,    // Enfocado en el ataque
        DEFENSIVE,    // Enfocado en la defensa
        CHANGING,     // Cambia según el Pokémon rival
        EXPERT,        // Versión experta que combina todas las estrategias
        GEMINI      // Version que nos permite jugar contra Google Gemini IA generativa
    }
    
    /**
     * Crea una nueva máquina con la estrategia especificada.
     *
     * @param type       Tipo de máquina a crear (estrategia).
     * @param name       Nombre de la máquina.
     * @param difficulty Dificultad (afecta la cantidad de pokémon e ítems).
     * @return Instancia de {@code Machine} configurada según los parámetros.
     */
    public static Machine createMachine(MachineType type, String name, int difficulty) {
        // Crear lista de pokémon aleatorios
        ArrayList<Pokemon> pokemons = createRandomPokemons(2 + difficulty);
        
        // Crear lista de ítems aleatorios
        ArrayList<String> items = new ArrayList<>();
        ArrayList<Item> itemObjects = createRandomItems(difficulty);
        
        // Convertir objetos Item a nombres de ítems para el constructor
        for (Item item : itemObjects) {
            items.add(item.getName());
        }
        
        // Crear y devolver la máquina según el tipo
        switch (type) {
            case ATTACKING:
                return new AttackingMachine(name, pokemons, items);
            case DEFENSIVE:
                return new DefensiveMachine(name, pokemons, items);
            case CHANGING:
                return new ChangingMachine(name, pokemons, items);
            case EXPERT:
                return new ExpertMachine(name, pokemons, items);
            case GEMINI:
                return new GeminiMachine(name, pokemons, items);
            default:
                return new AttackingMachine(name, pokemons, items);
        }
    }
    
     /**
     * Crea una lista de pokémon aleatorios para la máquina.
     *
     * @param count Número de pokémon a seleccionar.
     * @return Lista con los ataques asignados.
     */
    private static ArrayList<Pokemon> createRandomPokemons(int count) {
        ArrayList<Pokemon> result = new ArrayList<>();
        List<String> availablePokemon = Poobkemon.getAvailablePokemon();
        
        for (int i = 0; i < count && !availablePokemon.isEmpty(); i++) {
            int index = random.nextInt(availablePokemon.size());
            String pokemonName = availablePokemon.get(index);
            Pokemon pokemon = PokemonFactory.createPokemon(pokemonName);
            
            if (pokemon != null) {
                // Asignar ataques aleatorios
                assignRandomAttacks(pokemon);
                result.add(pokemon);
                availablePokemon.remove(index); // Evitar duplicados
            }
        }
        
        return result;
    }
    
    /**
     * Asigna ataques aleatorios a un pokémon.
     *
     * @param pokemon a los que se les asignaran los ataques.
     */
  
    private static void assignRandomAttacks(Pokemon pokemon) {
        // Obtener ataques disponibles
        List<String> allAttacks = new ArrayList<>();
        allAttacks.addAll(Poobkemon.getPhysicalAttacks());
        allAttacks.addAll(Poobkemon.getSpecialAttacks());
        allAttacks.addAll(Poobkemon.getStatusAttacks());
        
        // Seleccionar hasta 4 ataques aleatorios
        int attackCount = Math.min(4, allAttacks.size());
        for (int i = 0; i < attackCount; i++) {
            int index = random.nextInt(allAttacks.size());
            String attackName = allAttacks.get(index);
            Attack attack = AttackFactory.createAttack(attackName);
            pokemon.addAttack(attack);
            allAttacks.remove(index); // Evitar duplicados
        }
    }
    
    /**
     * Crea una lista de ítems aleatorios para la máquina.
     *
     * @param count Número de ítems a seleccionar.
     * @return Lista Item.
     */
   
    private static ArrayList<Item> createRandomItems(int count) {
        ArrayList<Item> result = new ArrayList<>();
        List<String> availableItems = ItemFactory.getItemNames();
        
        for (int i = 0; i < count && !availableItems.isEmpty(); i++) {
            int index = random.nextInt(availableItems.size());
            String itemName = availableItems.get(index);
            Item item = ItemFactory.createItem(itemName);
            
            if (item != null) {
                result.add(item);
                availableItems.remove(index); // Evitar duplicados
            }
        }
        
        return result;
    }
}