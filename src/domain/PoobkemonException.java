package domain;

/**
 * PoobkemonException es una excepción personalizada para el manejo de errores y condiciones especiales
 * en el juego Poobkemon. Permite identificar y comunicar situaciones excepcionales como límites de equipo,
 * errores de selección, uso inválido de ítems y estados de juego no permitidos.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Define mensajes de error estáticos para los casos más comunes en la lógica del juego.</li>
 *   <li>Permite lanzar excepciones específicas para controlar el flujo y la validación de reglas en Poobkemon.</li>
 *   <li>Facilita la depuración y el manejo centralizado de errores en la aplicación.</li>
 * </ul>
 *
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PoobkemonException extends Exception {
    public static final String MAX_CANT_POKEMON = "Un entrenador no puede tener más de 6 Pokémon.";
    public static final String NO_POKEMONS_SELECTED = "No existe ningun pokemon seleccionado";
    public static final String INVALID_POKEMON_INDEX = "Índice de Pokémon inválido";
    public static final String FAINTED_POKEMON = "El pokemon esta debilitado";
    public static final String POKEMON_HAS_BEEN_FAINTED = "El pokemon ha sido debilitado";
    public static final String POKEMON_GAME_OVER = "El juego ha terminado, no hay más pokemones disponibles";
    public static final String FULL_POKEMON_HEALTH ="No se puede usar un ítem en un Pokémon con PS completos.";
    public static final String CANT_USE_ITEM_ON_POKEMON_FAINTED = "No se puede usar un ítem en un Pokémon debilitado.";

    public PoobkemonException(String message) {
        super(message);
    }
}