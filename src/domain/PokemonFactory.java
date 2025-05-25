package domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * {@code PokemonFactory} es una clase de utilidad que gestiona la creación de instancias de Pokémon en el juego Poobkemon.
 * Permite instanciar objetos {@link Pokemon} a partir de su nombre, centralizando la definición de sus estadísticas y atributos base.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Registra todos los Pokémon disponibles en el juego, asociando su nombre con un constructor parametrizado.</li>
 *   <li>Permite crear instancias de {@link Pokemon} de forma sencilla a partir de su nombre.</li>
 *   <li>Facilita la extensión y mantenimiento del catálogo de Pokémon del juego.</li>
 *   <li>Incluye validación para evitar la creación de Pokémon no registrados.</li>
 * </ul>
 *

 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PokemonFactory implements Serializable {
    private static final long serialVersionUID = 1L;
    // Mapa que asocia nombres de Pokémon con sus constructores
    public static final Map<String, Supplier<Pokemon>> POKEMON_REGISTRY = new HashMap<>();

    // Registramos todos los Pokémon disponibles
    static {
        POKEMON_REGISTRY.put("Blastoise", () -> new Pokemon("Blastoise", 2, 362, 280, 295, 291, 339, 328, "Agua",95));
        POKEMON_REGISTRY.put("Charizard", () -> new Pokemon("Charizard", 6, 360, 328, 348, 293, 295, 280, "Fuego", 105));
        POKEMON_REGISTRY.put("Raichu", () -> new Pokemon("Raichu", 14, 324, 350, 306, 306, 284, 229, "Electrico",110));
        POKEMON_REGISTRY.put("Gengar", () -> new Pokemon("Gengar", 4, 324, 350, 394, 251, 273, 240, "Fantasma",115));
        POKEMON_REGISTRY.put("Dragonite", () -> new Pokemon("Dragonite", 5, 386, 284, 328, 403, 328, 317, "Dragon",100));
        POKEMON_REGISTRY.put("Togetic", () -> new Pokemon("Togetic", 6, 314, 196, 284, 196, 339, 295, "Hada",110));
        POKEMON_REGISTRY.put("Tyranitar", () -> new Pokemon("Tyranitar", 7, 404, 243, 317, 403, 328, 350, "Roca",85));
        POKEMON_REGISTRY.put("Gardevoir", () -> new Pokemon("Gardevoir", 8, 340, 284, 383, 251, 361, 251, "Psiquico",105));
        POKEMON_REGISTRY.put("Snorlax", () -> new Pokemon("Snorlax", 9, 524, 174, 251, 350, 350, 251, "Normal",80));
        POKEMON_REGISTRY.put("Metagross", () -> new Pokemon("Metagross", 10, 364, 262, 317, 405, 306, 394, "Acero",90));
        POKEMON_REGISTRY.put("Machamp", () -> new Pokemon("Machamp", 12, 384, 229, 251, 394, 295, 284, "Lucha",100));
        POKEMON_REGISTRY.put("Donphan", () -> new Pokemon("Donphan", 13, 294, 273, 251, 229, 207, 207, "Hielo", 120));
        POKEMON_REGISTRY.put("Delibird", () -> new Pokemon("Delibird", 13, 294, 273, 251, 229, 207, 207, "Hielo", 120));
        POKEMON_REGISTRY.put("Venusaur", () -> new Pokemon("Venusaur", 3, 364, 284, 394, 251, 273, 240, "Planta",90));
        
    }

    /**
     * Crea una instancia de Pokémon a partir de su nombre.
     * 
     * @param name El nombre del Pokémon a crear.
     * @return Una instancia de {@link Pokemon} correspondiente al nombre proporcionado.
     * @throws IllegalArgumentException Si el nombre no está registrado en el catálogo de Pokémon.
     */
    public static Pokemon createPokemon(String name) {
        Supplier<Pokemon> constructor = POKEMON_REGISTRY.get(name);
        if (constructor == null) {
            throw new IllegalArgumentException("Pokémon no reconocido: " + name);
        }
        return constructor.get();
    }
}
