package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        // Si tu método requiere colores, pásalos aquí:
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2, Color.GREEN, Color.BLUE);
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        assertEquals(6, poobkemon.getPokemonsVivos(true).size());
        assertEquals(6, poobkemon.getPokemonsVivos(false).size());
        assertEquals(0, poobkemon.getPokemonsMuertos(true).size());
        assertEquals(0, poobkemon.getPokemonsMuertos(false).size());
    }

    @Test
    public void testStartBattleSurvivalAndGetSurvivalMoves() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        List<String> attacks = Poobkemon.getAvailableAttacks();
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleSurvival(pokemons1, pokemons2, moves1, moves2, Color.GREEN, Color.BLUE);
        Map<String, String[][]> survivalMoves = poobkemon.getSurvivalMoves();
        assertNotNull(survivalMoves);
        assertTrue(survivalMoves.containsKey("Player 1"));
        assertTrue(survivalMoves.containsKey("Player 2"));
        assertArrayEquals(moves1, survivalMoves.get("Player 1"));
        assertArrayEquals(moves2, survivalMoves.get("Player 2"));
        assertEquals("PVP", poobkemon.getBattleType());
    }

    @Test
    public void testFleeDoesNotThrow() throws PoobkemonException {
        // Prepara una batalla para que battleArenaNormal no sea null
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        List<String> attacks = Poobkemon.getAvailableAttacks();
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleSurvival(pokemons1, pokemons2, moves1, moves2, Color.GREEN, Color.BLUE);
        assertDoesNotThrow(() -> poobkemon.flee());
    }

    @Test
    public void testSwitchToPokemonDelegatesToArena() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        List<String> attacks = Poobkemon.getAvailableAttacks();
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleSurvival(pokemons1, pokemons2, moves1, moves2, Color.GREEN, Color.BLUE);
        // Cambia al segundo Pokémon (índice 1)
        assertDoesNotThrow(() -> poobkemon.switchToPokemon(1));
    }

    @Test
    public void testStatusEffectDelegatesToArena() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        List<String> attacks = Poobkemon.getAvailableAttacks();
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        assertDoesNotThrow(() -> poobkemon.statusEffect());
    }

    @Test
    public void testSetCurrentPokemonDelegatesToArena() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        List<String> attacks = Poobkemon.getAvailableAttacks();
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        assertDoesNotThrow(() -> poobkemon.setCurrentPokemon(1));
    }

    @Test
    public void testStartBattleHumanVsMachine() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> items1 = new ArrayList<>();
        String[][] moves1 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
            }
        }
        poobkemon.startBattleHumanVsMachine("Ash", "CPU", pokemons1, pokemons2, items1, moves1, "Attacking", Color.GREEN, Color.BLUE);
        assertNotNull(poobkemon.getBattleArena());
        assertEquals("PVM", poobkemon.getBattleType());
    }

    @Test
    public void testStartBattleMachineVsHuman() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> items1 = new ArrayList<>();
        String[][] moves1 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
            }
        }
        poobkemon.startBattleMachineVsHuman("CPU", "Ash", pokemons1, pokemons2, items1, moves1, "Attacking", Color.GREEN, Color.BLUE);
        assertNotNull(poobkemon.getBattleArena());
        assertEquals("PVM", poobkemon.getBattleType());
    }

    @Test
    public void testStartBattleMachineVsMachine() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        poobkemon.startBattleMachineVsMachine("CPU1", "CPU2", pokemons1, pokemons2, "Attacking", "Defensive", Color.GREEN, Color.BLUE);
        assertNotNull(poobkemon.getBattleArena());
        assertEquals("MVM", poobkemon.getBattleType());
    }

    @Test
    public void testWhoStartsDelegatesToArena() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        assertTrue(poobkemon.whoStarts() == true || poobkemon.whoStarts() == false);
    }

    @Test
    public void testGetActivePokemonMovesAndNameAndHP() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        assertNotNull(poobkemon.getActivePokemonMoves(true));
        assertNotNull(poobkemon.getActivePokemonName(true));
        assertTrue(poobkemon.getActivePokemonCurrentHP(true) >= 0);
        assertTrue(poobkemon.getActivePokemonMaxHP(true) > 0);
    }

    @Test
    public void testGetPPDeAtaqueActualAndGetItemsJugador() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        String attackName = poobkemon.getActivePokemonMoves(true).get(0);
        assertTrue(poobkemon.getPPDeAtaqueActual(true, attackName) >= 0);
        assertNotNull(poobkemon.getItemsJugador(true));
    }

    @Test
    public void testEsAtaqueSobreSiMismo() {
        List<String> statusAttacks = Poobkemon.getStatusAttacks();
        if (!statusAttacks.isEmpty()) {
            assertTrue(poobkemon.esAtaqueSobreSiMismo(statusAttacks.get(0)));
        }
        assertFalse(poobkemon.esAtaqueSobreSiMismo("AtaqueInventado"));
    }

    @Test
    public void testProcessMachineTurnNoBattle() {
        // No se ha iniciado batalla
        String result = poobkemon.processMachineTurn();
        assertEquals("No hay una batalla en curso", result);
    }

    @Test
    public void testProcessMachineTurnNotMachineTurn() throws PoobkemonException {
        // Inicia batalla normal con dos humanos (no máquina)
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2, Color.GREEN, Color.BLUE);
        // Forzamos el turno de un humano (no máquina)
        String result = poobkemon.processMachineTurn();
        assertEquals("No es el turno de la máquina", result);
    }

    @Test
    public void testProcessMachineTurnMachineAttack() throws PoobkemonException {
        // Inicia batalla humano vs máquina (máquina es player 2)
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> items1 = new ArrayList<>();
        String[][] moves1 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
            }
        }
        poobkemon.startBattleHumanVsMachine("Ash", "CPU", pokemons1, pokemons2, items1, moves1, "Attacking", Color.GREEN, Color.BLUE);
        // Cambia turno para que sea el turno de la máquina
        poobkemon.changeTurn();
        String result = poobkemon.processMachineTurn();
        assertTrue(result.startsWith("La máquina usó") || result.startsWith("La máquina cambió a"));
    }

    @Test
    public void testProcessMachineTurnMachineSwitch() throws PoobkemonException {
        // Inicia batalla humano vs máquina (máquina es player 2)
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> items1 = new ArrayList<>();
        String[][] moves1 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
            }
        }
        poobkemon.startBattleHumanVsMachine("Ash", "CPU", pokemons1, pokemons2, items1, moves1, "Attacking", Color.GREEN, Color.BLUE);
        // Cambia turno para que sea el turno de la máquina
        poobkemon.changeTurn();
        // Debilita el Pokémon activo de la máquina
        poobkemon.getBattleArena().getCoach(1).getActivePokemon().setPs(0);
        String result = poobkemon.processMachineTurn();
        assertTrue(result.startsWith("La máquina cambió a"));
    }

    @Test
    public void testChangeTurnDelegatesToArena() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2, Color.GREEN, Color.BLUE);
        int before = poobkemon.getBattleArena().getCurrentTurn();
        poobkemon.changeTurn();
        int after = poobkemon.getBattleArena().getCurrentTurn();
        assertNotEquals(before, after);
    }

    @Test
    public void testCambiarPokemonActivoDelegatesToArena() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        String nuevoActivo = pokemons1.get(1);
        poobkemon.cambiarPokemonActivo(true, nuevoActivo);
        assertEquals(nuevoActivo, poobkemon.getActivePokemonName(true));
    }

    @Test
    public void testRevivirPokemonDelegatesToArena() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        String nombre = pokemons1.get(0);
        // Debilita el Pokémon
        poobkemon.getBattleArena().getCoach(0).getActivePokemon().setPs(0);
        poobkemon.revivirPokemon(true, nombre);
        assertTrue(poobkemon.getPokemonHP(true, nombre) > 0);
    }

    @Test
    public void testEliminarItemDelegatesToArena() throws PoobkemonException {
        ArrayList<String> pokemons1 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(Poobkemon.getAvailablePokemon().subList(0, 6));
        ArrayList<String> items1 = new ArrayList<>();
        items1.add("Poción");
        String[][] moves1 = new String[6][4];
        String[][] moves2 = new String[6][4];
        List<String> attacks = Poobkemon.getAvailableAttacks();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                moves1[i][j] = attacks.get((i + j) % attacks.size());
                moves2[i][j] = attacks.get((i + j + 1) % attacks.size());
            }
        }
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        poobkemon.eliminarItem(true, "Poción");
        assertFalse(poobkemon.getItemsJugador(true).contains("Poción"));
    }

    @Test
    public void testGetPokemonHPAndMaxHP() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        String nombre = pokemons1.get(0);
        assertTrue(poobkemon.getPokemonHP(true, nombre) > 0);
        assertTrue(poobkemon.getPokemonMaxHP(true, nombre) > 0);
    }

    @Test
    public void testGetPokemonsName() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        List<String> names = poobkemon.getPokemonsName(true);
        assertEquals(6, names.size());
        assertTrue(names.containsAll(pokemons1));
    }

    @Test
    public void testGuardarPartidaThrowsExtension() {
        java.io.File file = new java.io.File("partida.txt");
        Exception ex = assertThrows(Exception.class, () -> poobkemon.guardarPartida(file));
        assertTrue(ex.getMessage().contains("extensión"));
    }

    @Test
    public void testGuardarPartidaAndOpen() throws Exception {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, new ArrayList<>(), new ArrayList<>(), moves1, moves2, Color.GREEN, Color.BLUE);
        java.io.File temp = java.io.File.createTempFile("test", ".poob");
        poobkemon.guardarPartida(temp);
        assertTrue(temp.exists());
        Poobkemon loaded = Poobkemon.open(temp);
        assertNotNull(loaded);
        temp.delete();
    }

    @Test
    public void testCreateMachineCoachReturnsCorrectSubclass() throws Exception {
        Poobkemon poobkemon = new Poobkemon();
        ArrayList<String> pokemons = new ArrayList<>(Arrays.asList("Raichu", "Venusaur"));
        ArrayList<String> items = new ArrayList<>(Arrays.asList("Poción", "Revive"));

        Method method = Poobkemon.class.getDeclaredMethod("createMachineCoach", String.class, String.class, ArrayList.class, ArrayList.class);
        method.setAccessible(true);

        Coach gemini = (Coach) method.invoke(poobkemon, "Gem", "Gemini", pokemons, items);
        assertEquals("GeminiMachine", gemini.getClass().getSimpleName());

        Coach attacking = (Coach) method.invoke(poobkemon, "CPU", "attacking", pokemons, items);
        assertEquals("AttackingMachine", attacking.getClass().getSimpleName());

        Coach defensive = (Coach) method.invoke(poobkemon, "CPU", "defensive", pokemons, items);
        assertEquals("DefensiveMachine", defensive.getClass().getSimpleName());

        Coach changing = (Coach) method.invoke(poobkemon, "CPU", "changing", pokemons, items);
        assertEquals("ChangingMachine", changing.getClass().getSimpleName());

        Coach expert = (Coach) method.invoke(poobkemon, "CPU", "expert", pokemons, items);
        assertEquals("ExpertMachine", expert.getClass().getSimpleName());

        Coach defaultType = (Coach) method.invoke(poobkemon, "CPU", "otro", pokemons, items);
        assertEquals("AttackingMachine", defaultType.getClass().getSimpleName());
    }

    @Test
    public void testGetColorJugador1And2() throws PoobkemonException {
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
        Color color1 = Color.GREEN;
        Color color2 = Color.BLUE;
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2, color1, color2);

        assertEquals(color1, poobkemon.getColorJugador1());
        assertEquals(color2, poobkemon.getColorJugador2());
    }

    @Test
    public void testAttackAffectsOpponent() throws PoobkemonException {
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
        poobkemon.startBattleNormal("Ash", "Gary", pokemons1, pokemons2, items1, items2, moves1, moves2, Color.GREEN, Color.BLUE);

        // Obtén el nombre del primer ataque del Pokémon activo del jugador 1
        String attackName = poobkemon.getActivePokemonMoves(true).get(0);
        int hpBefore = poobkemon.getActivePokemonCurrentHP(false); // HP del oponente antes del ataque

        // Ataca al oponente (toItself = false, esJugador1 = true)
        poobkemon.attack(attackName, false, true);

        int hpAfter = poobkemon.getActivePokemonCurrentHP(false); // HP del oponente después del ataque
        assertTrue(hpAfter <= hpBefore, "El HP del oponente debe disminuir o mantenerse igual tras el ataque");
    }

}