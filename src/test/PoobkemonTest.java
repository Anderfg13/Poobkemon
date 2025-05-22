package test;

import domain.Poobkemon;
import domain.PoobkemonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PoobkemonTest {
    private Poobkemon poobkemon;

    @BeforeEach
    public void setUp() {
        poobkemon = new Poobkemon();
    }

    @Test
    public void shouldReturnAvailablePokemons() {
        List<String> pokemons = Poobkemon.getAvailablePokemon();
        assertNotNull(pokemons);
        assertFalse(pokemons.isEmpty());
    }

    @Test
    public void shouldReturnAvailableItems() {
        List<String> items = Poobkemon.getAvailableItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void shouldStartBattleNormalSuccessfully() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> items1 = new ArrayList<>();
        ArrayList<String> items2 = new ArrayList<>();
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2);
        assertNotNull(poobkemon.getBattleArena());
    }

    @Test
    public void shouldHaveAlivePokemonsAfterBattleStart() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2);
        assertTrue(poobkemon.tienePokemonesVivos(true));
        assertTrue(poobkemon.tienePokemonesVivos(false));
    }

    @Test
    public void shouldNotUseInvalidItem() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2);
        assertThrows(PoobkemonException.class, () -> poobkemon.useItem("NoExiste", true));
    }

    @Test
    public void shouldReturnAliveAndDeadPokemonsCorrectly() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2);
        assertEquals(6, poobkemon.getPokemonsVivos(true).size());
        assertEquals(6, poobkemon.getPokemonsVivos(false).size());
        assertEquals(0, poobkemon.getPokemonsMuertos(true).size());
        assertEquals(0, poobkemon.getPokemonsMuertos(false).size());
    }
}