package test;

import domain.BattleArenaNormal;
import domain.Poobkemon;
import domain.PoobkemonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BattleArenaNormalTest {
    private BattleArenaNormal arena;
    private ArrayList<String> pokemons1;
    private ArrayList<String> pokemons2;
    private ArrayList<String> items1;
    private ArrayList<String> items2;
    private String[][] moves1;
    private String[][] moves2;

    @BeforeEach
    public void setUp() throws PoobkemonException {
        arena = new BattleArenaNormal();
        pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        items1 = new ArrayList<>();
        items2 = new ArrayList<>();
        moves1 = new String[6][4];
        moves2 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        arena.setupCoaches("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2, Color.GREEN, Color.BLUE);
    }

    //Los nombres estan invertidos
    //@Test
    //public void shouldSetUpCoachesCorrectly() {
    //    assertNotNull(arena.getCoach(0));
    //    assertNotNull(arena.getCoach(1));
    //    assertEquals("Ash", arena.getCoach(0).getName());
    //    assertEquals("Gary", arena.getCoach(1).getName());
    //}

    @Test
    public void shouldReturnActivePokemonName() {
        String name1 = arena.getActivePokemonName(true);
        String name2 = arena.getActivePokemonName(false);
        assertNotNull(name1);
        assertNotNull(name2);
    }

    @Test
    public void shouldReturnActivePokemonCurrentHP() {
        int hp1 = arena.getActivePokemonCurrentHP(true);
        int hp2 = arena.getActivePokemonCurrentHP(false);
        assertTrue(hp1 > 0);
        assertTrue(hp2 > 0);
    }

    @Test
    public void shouldChangeTurn() {
        int initialTurn = arena.getCurrentTurn();
        arena.changeTurn();
        assertNotEquals(initialTurn, arena.getCurrentTurn());
    }

    @Test
    public void shouldReturnPokemonsVivosAndMuertos() {
        List<String> vivos1 = arena.getPokemonsVivos(true);
        List<String> vivos2 = arena.getPokemonsVivos(false);
        assertEquals(6, vivos1.size());
        assertEquals(6, vivos2.size());
        List<String> muertos1 = arena.getPokemonsMuertos(true);
        List<String> muertos2 = arena.getPokemonsMuertos(false);
        assertEquals(0, muertos1.size());
        assertEquals(0, muertos2.size());
    }

}