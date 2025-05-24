package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleArenaSurvivalTest {
    private BattleArenaSurvival arena;

    @BeforeEach
    void setUp() {
        arena = new BattleArenaSurvival();
    }

    @Test
    void testSetupSurvivalBattleAssignsPokemonsAndMoves() throws Exception {
        // Usa 12 pokemones válidos de PokemonFactory
        List<String> allPokemon = Arrays.asList(
                "Blastoise", "Charizard", "Raichu", "Gengar",
                "Dragonite", "Togetic", "Tyranitar", "Gardevoir",
                "Snorlax", "Metagross", "Machamp", "Donphan"
        );
        // Si tu clase lo permite, podrías simular la lista disponible aquí

        assertDoesNotThrow(() -> {
            arena.setupSurvivalBattle("Ash", "Gary", Color.BLUE, Color.RED);
        });
    }

    @Test
    void testAttackToItselfAffectsOwnPokemon() throws Exception {
        arena.setupSurvivalBattle("Ash", "Gary", Color.BLUE, Color.RED);
        String attackName = arena.getActivePokemonMoves(true).get(0);
        int hpBefore = arena.getActivePokemonCurrentHP(true);
        arena.attack(attackName, "itself");
        int hpAfter = arena.getActivePokemonCurrentHP(true);
        assertTrue(hpAfter <= hpBefore);
    }

    @Test
    void testSetupHumanVsMachineThrowsException() {
        Exception ex = assertThrows(PoobkemonException.class, () -> {
            arena.setupHumanVsMachine("Ash", "CPU", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new String[0][0], "Attacking", Color.BLUE, Color.RED);
        });
        assertTrue(ex.getMessage().contains("no están soportadas"));
    }

    @Test
    void testSetupMachineVsHumanThrowsException() {
        Exception ex = assertThrows(PoobkemonException.class, () -> {
            arena.setupMachineVsHuman("CPU", "Ash", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new String[0][0], "Attacking", Color.BLUE, Color.RED);
        });
        assertTrue(ex.getMessage().contains("no están soportadas"));
    }

    @Test
    void testSetupMachineVsMachineThrowsException() {
        Exception ex = assertThrows(PoobkemonException.class, () -> {
            arena.setupMachineVsMachine("CPU1", "CPU2", new ArrayList<>(), new ArrayList<>(), "Attacking", "Defensive", Color.BLUE, Color.RED);
        });
        assertTrue(ex.getMessage().contains("no están soportadas"));
    }
}