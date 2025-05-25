package test;

import domain.Coach;
import domain.HumanCoach;
import domain.Pokemon;
import domain.PokemonFactory;
import domain.PoobkemonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CoachTest {
    private Coach coach;

    @BeforeEach
    public void setUp() {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        // Agrega dos pokémon usando tu fábrica
        pokemons.add(PokemonFactory.createPokemon("Raichu"));
        pokemons.add(PokemonFactory.createPokemon("Venusaur"));
        ArrayList<String> items = new ArrayList<>();
        items.add("Poción");
        items.add("Superpoción");
        coach = new HumanCoach("Ash", pokemons, items);
    }

    @Test
    public void shouldReturnPokemons() {
        List<Pokemon> pokemons = coach.getPokemons();
        assertEquals(2, pokemons.size());
        assertEquals("Raichu", pokemons.get(0).getName());
    }

    @Test
    public void shouldSwitchActivePokemon() throws PoobkemonException {
        coach.switchToPokemon(1);
        assertEquals("Venusaur", coach.getActivePokemon().getName());
    }

    @Test
    public void shouldNotSwitchToInvalidIndex() {
        assertThrows(PoobkemonException.class, () -> coach.switchToPokemon(5));
    }

    @Test
    public void shouldAddItem() {
        coach.agregarItem("Superpoción");
        assertTrue(coach.getNombreItems().contains("Superpoción"));
    }

    @Test
    public void shouldRemoveItem() {
        coach.eliminarItem("Poción");
        assertFalse(coach.getNombreItems().contains("Poción"));
    }

    @Test
    public void shouldReturnActivePokemon() {
        assertEquals("Raichu", coach.getActivePokemon().getName());
    }

    @Test
    public void shouldReturnFalseIfAllPokemonsFainted() {
        for (Pokemon p : coach.getPokemons()) {
            p.setPs(0);
        }
        assertTrue(coach.areAllPokemonFainted());
    }

    @Test
    public void shouldAddPokemon() {
        int before = coach.getPokemons().size();
        coach.agregarPokemon("Blastoise");
        assertEquals(before + 1, coach.getPokemons().size());
        assertEquals("Blastoise", coach.getPokemons().get(before).getName());
    }

    @Test
    public void shouldCreateItems() {
        ArrayList<String> newItems = new ArrayList<>();
        newItems.add("Revive");
        coach.createItems(newItems);
        assertTrue(coach.getNombreItems().contains("Revive"));
    }

    @Test
    public void shouldCheckPokemonStatus() {
        Pokemon p = coach.getPokemons().get(0);
        p.setPs(0);
        assertTrue(coach.checkPokemonStatus(p));
        p.setPs(10);
        assertFalse(coach.checkPokemonStatus(p));
    }

    @Test
    public void shouldGetCurrentPokemon() {
        assertEquals(coach.getActivePokemon(), coach.getCurrentPokemon());
    }

    @Test
    public void shouldSetActivePokemon() {
        Pokemon p = coach.getPokemons().get(1);
        coach.setActivePokemon(p);
        assertEquals(p, coach.getActivePokemon());
    }

    @Test
    public void shouldGetItems() {
        List<domain.Item> items = coach.getItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void shouldGetColorCoachAndSetColor() {
        coach.setColor(java.awt.Color.RED);
        assertEquals(java.awt.Color.RED, coach.getColorCoach());
    }

    @Test
    public void shouldReturnIsMachineFalseForHumanCoach() {
        assertFalse(coach.isMachine());
    }
}