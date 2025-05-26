package domain;

/**
 * Fábrica para crear y gestionar estados de Pokémon usando el patrón Decorator.
 */
public class PokemonStatusFactory {
    
    /**
     * Aplica un estado a un Pokémon usando el patrón decorador adecuado.
     * 
     * @param pokemon El Pokémon base
     * @param statusCode El código de estado a aplicar
     * @return Un Pokémon decorado con el estado correspondiente
     */
    public static PokemonBase applyStatus(PokemonBase pokemon, int statusCode) {
        switch (statusCode) {
            case 0:  // Normal
                return pokemon;
            case 1:  // Paralizado
                return new ParalyzedPokemonDecorator(pokemon);
            case 2:  // Dormido
                return new SleepingPokemonDecorator(pokemon);
            case 3:  // Quemado
                return new BurnedPokemonDecorator(pokemon);
            case 4:  // Congelado
                return new FrozenPokemonDecorator(pokemon);
            case 5:  // Envenenado
                return new PoisonedPokemonDecorator(pokemon);
            default:
                throw new IllegalArgumentException("Código de estado inválido: " + statusCode);
        }
    }
    
    /**
     * Devuelve el Pokémon base de un Pokémon decorado.
     * 
     * @param pokemon El Pokémon decorado
     * @return El Pokémon base
     */
    public static Pokemon getBasePokemon(PokemonBase pokemon) {
        if (pokemon instanceof PokemonDecorator) {
            return getBasePokemon(((PokemonDecorator) pokemon).pokemon);
        }
        return (Pokemon) pokemon;
    }
    
    /**
     * Aplica múltiples estados a un Pokémon en secuencia.
     * 
     * @param pokemon El Pokémon base
     * @param statusCodes Los códigos de estado a aplicar
     * @return Un Pokémon decorado con los estados correspondientes
     */
    public static PokemonBase applyMultipleStatus(PokemonBase pokemon, int... statusCodes) {
        PokemonBase result = pokemon;
        for (int code : statusCodes) {
            result = applyStatus(result, code);
        }
        return result;
    }
    
    /**
     * Cura todos los estados de un Pokémon.
     * 
     * @param pokemon El Pokémon posiblemente decorado
     * @return El Pokémon base sin decoradores
     */
    public static Pokemon cureAllStatus(PokemonBase pokemon) {
        if (pokemon instanceof PokemonDecorator) {
            return cureAllStatus(((PokemonDecorator) pokemon).pokemon);
        } else if (pokemon instanceof Pokemon) {
            Pokemon basePokemon = (Pokemon) pokemon;
            basePokemon.setStatus(0);
            basePokemon.setTurnStatus(0);
            return basePokemon;
        }
        throw new IllegalArgumentException("Tipo de Pokémon no reconocido");
    }
    
    /**
     * Verifica si un Pokémon tiene un estado específico.
     * 
     * @param pokemon El Pokémon a verificar
     * @param statusCode El código de estado a verificar
     * @return Verdadero si el Pokémon tiene el estado, falso en caso contrario
     */
    public static boolean hasStatus(PokemonBase pokemon, int statusCode) {
        return pokemon.getStatus() == statusCode;
    }
}