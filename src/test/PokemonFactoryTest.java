package test;

import domain.Pokemon;
import domain.PokemonFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonFactoryTest {

    @Test
    void testCreatePokemonReturnsCorrectPokemon() {
        Pokemon raichu = PokemonFactory.createPokemon("Raichu");
        assertEquals("Raichu", raichu.getName());
        assertEquals("Electrico", raichu.getType());
        assertTrue(raichu.getTotalPs() > 0);

        Pokemon venusaur = PokemonFactory.createPokemon("Venusaur");
        assertEquals("Venusaur", venusaur.getName());
        assertEquals("Planta", venusaur.getType());
    }

    @Test
    void testGetPokemonNamesContainsRegisteredPokemons() {
        List<String> names = new java.util.ArrayList<>(PokemonFactory.POKEMON_REGISTRY.keySet());
        assertTrue(names.contains("Raichu"));
        assertTrue(names.contains("Venusaur"));
        assertTrue(names.contains("Blastoise"));
    }

    @Test
    void testCreatePokemonThrowsForUnknownPokemon() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            PokemonFactory.createPokemon("NoExiste");
        });
        assertTrue(ex.getMessage().contains("no reconocido"));
    }
}