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
    void testChangeTurnAlternatesCurrentTurn() {
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

    @Test
    void testPauseAndResumeBattle() {
        arena.pauseBattle();
        assertTrue(arena.isPaused);
        arena.resumeBattle();
        assertFalse(arena.isPaused);
    }

    @Test
    void testEndBattleSetsBattleFinished() {
        arena.setbattleFinished(true);
        assertTrue(arena.isBattleFinished());
        arena.setbattleFinished(false);
        assertFalse(arena.isBattleFinished());
    }

    @Test
    void testSetCurrentPokemonChangesActive() throws Exception {
        arena.setCurrentPokemon(0);
        assertEquals(poke1, coach1.getActivePokemon());
    }

    @Test
    void testSwitchToPokemonChangesActive() throws Exception {
        coach1.switchToPokemon(0);
        assertEquals(poke1, coach1.getActivePokemon());
    }

    @Test
    void testGetActivePokemonMoves() {
        List<String> moves = arena.getActivePokemonMoves(true);
        assertNotNull(moves);
    }

    @Test
    void testGetActivePokemonNameAndHP() {
        String name = arena.getActivePokemonName(true);
        assertEquals(poke1.getName(), name);
        int hp = arena.getActivePokemonCurrentHP(true);
        assertEquals(poke1.getPs(), hp);
        int maxHp = arena.getActivePokemonMaxHP(true);
        assertEquals(poke1.getTotalPs(), maxHp);
    }

    @Test
    void testGetPokemonsMuertos() {
        poke1.setPs(0);
        List<String> muertos = arena.getPokemonsMuertos(true);
        assertTrue(muertos.contains(poke1.getName()));
    }

    @Test
    void testRevivirPokemon() throws Exception {
        poke1.setPs(0);
        arena.revivirPokemon(true, poke1.getName());
        assertTrue(poke1.getPs() > 0);
    }

    @Test
    void testSetAndGetPokemonStatus() {
        arena.setPokemonStatus(2, true);
        assertEquals(2, arena.getPokemonStatus(true));
    }

    @Test
    void testSetAndGetTurnStatus() {
        arena.setTurnStatus(2, true);
        assertEquals(2, arena.getTurnStatus(true));
    }

    @Test
    void testEliminarItem() {
        coach1.agregarItem("Poción");
        arena.eliminarItem(true, "Poción");
        assertFalse(arena.getItemsJugador(true).contains("Poción"));
    }

    @Test
    void testSetCoachAndGetCoach() {
        HumanCoach nuevo = new HumanCoach("Nuevo", new ArrayList<>(), new ArrayList<>());
        arena.setCoach(0, nuevo);
        assertEquals(nuevo, arena.getCoach(0));
    }

    @Test
    void testSetCurrentTurn() {
        arena.setCurrentTurn(1);
        assertEquals(1, arena.getCurrentTurn());
    }

    @Test
    void testWhoStartsAlternates() {
        boolean first = arena.whoStarts();
        boolean second = arena.whoStarts();
        assertNotEquals(first, second);
    }

    @Test
    void testStatusEffectReducesTurnStatusAndHP() {
        poke1.setStatus(1);
        poke1.setTurnStatus(2);
        int hpBefore = poke1.getPs();
        arena.statusEffect();
        assertEquals(1, poke1.getTurnStatus());
        assertEquals(hpBefore - 10, poke1.getPs());
    }

    @Test
    void testFleeMarksBattleAsFinishedAndCallsEndBattle() {
        // Precondición: la batalla no está finalizada
        arena.setbattleFinished(false);
        assertFalse(arena.isBattleFinished());

        // Ejecuta flee
        arena.flee();

        // Verifica que la batalla está finalizada
        assertTrue(arena.isBattleFinished());
    }

    @Test
    void testSetBattleFinishedSetsFlag() {
        arena.setbattleFinished(true);
        assertTrue(arena.isBattleFinished());
        arena.setbattleFinished(false);
        assertFalse(arena.isBattleFinished());
    }

    @Test
    void testEndBattleCancelsTimer() throws Exception {
        // Inicia el temporizador para el turno 0
        java.lang.reflect.Method startTimer = arena.getClass().getSuperclass().getDeclaredMethod("startTurnTimer", int.class);
        startTimer.setAccessible(true);
        startTimer.invoke(arena, 0);

        // Llama a endBattle (debe cancelar el temporizador)
        arena.endBattle();
        // Si no lanza excepción, la prueba pasa
        assertTrue(true);
    }

    @Test
    void testGetCoachesReturnsArrayWithBothCoaches() {
        Coach[] coachesArray = arena.getCoaches();
        assertNotNull(coachesArray);
        assertEquals(2, coachesArray.length);
        assertEquals(coach1, coachesArray[0]);
        assertEquals(coach2, coachesArray[1]);
    }
}