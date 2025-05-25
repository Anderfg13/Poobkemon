package domain;

/**
 * Fábrica para crear Pokémon con diferentes estados usando el patrón Decorator.
 */
public class PokemonStatusFactory {
    
    /**
     * Aplica un estado a un Pokémon usando el patrón decorador adecuado.
     * 
     * @param pokemon El Pokémon base
     * @param statusCode El código de estado a aplicar:
     *                  0: normal (retorna el Pokémon sin decorar)
     *                  1: paralizado
     *                  2: dormido
     *                  3: quemado
     *                  4: congelado
     *                  5: envenenado
     * @return Un Pokémon decorado con el estado correspondiente
     */
    public static PokemonBase applyStatus(PokemonBase pokemon, int statusCode) {
        switch (statusCode) {
            case 0:
                return pokemon; // Normal, sin decorador
            case 1:
                return new ParalyzedPokemonDecorator(pokemon);
            case 2:
                return new SleepingPokemonDecorator(pokemon);
            case 3:
                return new BurnedPokemonDecorator(pokemon);
            case 4:
                return new FrozenPokemonDecorator(pokemon);
            case 5:
                return new PoisonedPokemonDecorator(pokemon);
            default:
                throw new IllegalArgumentException("Código de estado inválido: " + statusCode);
        }
    }
    
    /**
     * Cura todos los estados de un Pokémon, eliminando todos los decoradores.
     * 
     * @param pokemonDecorated El Pokémon posiblemente decorado
     * @return El Pokémon base sin decoradores
     */
    public static Pokemon cureAllStatus(PokemonBase pokemonDecorated) {
        // Buscamos recursivamente el Pokémon base
        if (pokemonDecorated instanceof PokemonDecorator) {
            return cureAllStatus(((PokemonDecorator) pokemonDecorated).pokemon);
        } else if (pokemonDecorated instanceof Pokemon) {
            Pokemon basePokemon = (Pokemon) pokemonDecorated;
            basePokemon.setStatus(0); // Reiniciamos el estado a normal
            basePokemon.setTurnStatus(0);
            return basePokemon;
        }
        
        throw new IllegalArgumentException("El objeto no es un Pokémon válido");
    }
    
    /**
     * Aplica múltiples estados a un Pokémon (decoradores anidados).
     * 
     * @param pokemon El Pokémon base
     * @param statusCodes Array de códigos de estado a aplicar
     * @return Un Pokémon con múltiples decoradores aplicados
     */
    public static PokemonBase applyMultipleStatus(PokemonBase pokemon, int... statusCodes) {
        PokemonBase decoratedPokemon = pokemon;
        
        for (int statusCode : statusCodes) {
            if (statusCode != 0) { // Saltar estado normal
                decoratedPokemon = applyStatus(decoratedPokemon, statusCode);
            }
        }
        
        return decoratedPokemon;
    }
    
    /**
     * Verifica si un Pokémon tiene un estado específico.
     * 
     * @param pokemon El Pokémon a verificar
     * @param statusCode El código de estado a buscar
     * @return true si el Pokémon tiene ese estado
     */
    public static boolean hasStatus(PokemonBase pokemon, int statusCode) {
        switch (statusCode) {
            case 1:
                return pokemon instanceof ParalyzedPokemonDecorator;
            case 2:
                return pokemon instanceof SleepingPokemonDecorator;
            case 3:
                return pokemon instanceof BurnedPokemonDecorator;
            case 4:
                return pokemon instanceof FrozenPokemonDecorator;
            case 5:
                return pokemon instanceof PoisonedPokemonDecorator;
            default:
                return false;
        }
    }
    
    /**
     * Obtiene el Pokémon base desde cualquier nivel de decoración.
     * 
     * @param pokemon El Pokémon posiblemente decorado
     * @return El Pokémon base
     */
    public static Pokemon getBasePokemon(PokemonBase pokemon) {
        if (pokemon instanceof PokemonDecorator) {
            return getBasePokemon(((PokemonDecorator) pokemon).pokemon);
        } else if (pokemon instanceof Pokemon) {
            return (Pokemon) pokemon;
        }
        
        throw new IllegalArgumentException("El objeto no es un Pokémon válido");
    }
}