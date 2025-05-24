package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HumanCoachTest {
    private HumanCoach coach;
    private Pokemon raichu, venusaur, blastoise;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        venusaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        blastoise = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        raichu.addAttack(AttackFactory.createAttack("Rayo carga"));
        raichu.addAttack(AttackFactory.createAttack("Defensa férrea"));
        venusaur.addAttack(AttackFactory.createAttack("Bomba lodo"));
        blastoise.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(raichu, venusaur, blastoise));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive", "Superpoción"));

        coach = new HumanCoach("Ash", pokemons, items);
    }

    @Test
    void testGetName() {
        assertEquals("Ash", coach.getName());
    }

    @Test
    void testGetActivePokemon() {
        assertEquals(raichu, coach.getActivePokemon());
    }

    @Test
    void testSwitchToPokemon() throws Exception {
        coach.switchToPokemon(2);
        assertEquals(blastoise, coach.getActivePokemon());
    }

    @Test
    void testAgregarPokemon() {
        coach.agregarPokemon("Snorlax");
        assertTrue(coach.getPokemons().stream().anyMatch(p -> p.getName().equals("Snorlax")));
    }

    @Test
    void testAgregarItem() {
        coach.agregarItem("Poción");
        assertTrue(coach.getNombreItems().contains("Poción"));
    }

    @Test
    void testUseItem() throws Exception {
        raichu.setPs(10);
        coach.useItem("Poción");
        assertTrue(raichu.getPs() > 10);
        assertFalse(coach.getNombreItems().contains("Poción"));
    }

    @Test
    void testFleeBattle() {
        coach.fleeBattle();
        assertTrue(coach.getHasFled());
    }

    @Test
    void testAreAllPokemonFainted() {
        raichu.setPs(0);
        venusaur.setPs(0);
        blastoise.setPs(0);
        assertTrue(coach.areAllPokemonFainted());
    }
}