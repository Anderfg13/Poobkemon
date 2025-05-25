package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleArenaTest {

    private BattleArena arena;
    private HumanCoach coach1, coach2;
    private Pokemon poke1, poke2;

    // Subclase dummy para instanciar BattleArena
    static class DummyArena extends BattleArena {
        @Override
        public void setupHumanVsMachine(String humanName, String machineName, ArrayList<String> humanPokemon, ArrayList<String> machinePokemon, ArrayList<String> humanItems, String[][] humanAttacks, String machineType, Color player1Color, Color player2Color) {}
        @Override
        public void setupMachineVsHuman(String machineName, String humanName, ArrayList<String> machinePokemon, ArrayList<String> humanPokemon, ArrayList<String> humanItems, String[][] humanAttacks, String machineType, Color player1Color, Color player2Color) {}
        @Override
        public void setupMachineVsMachine(String machine1Name, String machine2Name, ArrayList<String> machine1Pokemon, ArrayList<String> machine2Pokemon, String machine1Type, String machine2Type, Color player1Color, Color player2Color) {}
    }

    @BeforeEach
    void setUp() {
        arena = new DummyArena();
        poke1 = new Pokemon("Raichu", 1, 100, 90, 55, 40, 50, 50, "Electrico", 100);
        poke2 = new Pokemon("Venusaur", 2, 120, 80, 49, 49, 65, 65, "Planta", 100);
        ArrayList<Pokemon> pokemons1 = new ArrayList<>();
        ArrayList<Pokemon> pokemons2 = new ArrayList<>();
        pokemons1.add(poke1);
        pokemons2.add(poke2);
        ArrayList<String> items = new ArrayList<>();
        coach1 = new HumanCoach("Ash", pokemons1, items);
        coach2 = new HumanCoach("Gary", pokemons2, items);
        arena.getCoaches()[0] = coach1;
        arena.getCoaches()[1] = coach2;
    }

    @Test
    void testGetCurrentCoachAndOpponentCoach() {
        arena.setCurrentTurn(0);
        assertEquals(coach1, arena.getCurrentCoach());
        assertEquals(coach2, arena.getCoach(1));
        assertEquals(coach2, arena.getOpponentCoach());
        arena.setCurrentTurn(1);
        assertEquals(coach2, arena.getCurrentCoach());
        assertEquals(coach1, arena.getOpponentCoach());
    }

    @Test
    void testChangeTurn() {
        arena.setCurrentTurn(0);
        arena.changeTurn();
        assertEquals(1, arena.getCurrentTurn());
        arena.changeTurn();
        assertEquals(0, arena.getCurrentTurn());
    }



    @Test
    void testSetAndGetBattleFinished() {
        arena.setbattleFinished(true);
        assertTrue(arena.isBattleFinished());
        arena.setbattleFinished(false);
        assertFalse(arena.isBattleFinished());
    }

    @Test
    void testGetItemsJugador() {
        List<String> items = arena.getItemsJugador(true);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void testGetPokemonsVivosAndMuertos() {
        List<String> vivos = arena.getPokemonsVivos(true);
        assertEquals(1, vivos.size());
        poke1.setPs(0);
        vivos = arena.getPokemonsVivos(true);
        assertEquals(0, vivos.size());
    }

    @Test
    void testTienePokemonesVivos() {
        assertTrue(arena.tienePokemonesVivos(true));
        poke1.setPs(0);
        assertFalse(arena.tienePokemonesVivos(true));
    }

    @Test
    void testSetTurnStatusAndGetTurnStatus() {
        arena.setTurnStatus(3, true);
        assertEquals(3, arena.getTurnStatus(true));
    }

}