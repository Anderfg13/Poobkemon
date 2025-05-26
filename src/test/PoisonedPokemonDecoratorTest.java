package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;

class PoisonedPokemonDecoratorTest {

    private Pokemon basePokemon;
    private PoisonedPokemonDecorator poisonedPokemon;

    @BeforeEach
    void setUp() {
        // Crear un Pokémon base para las pruebas
        basePokemon = new Pokemon("Jigglypuff", 39, 100, 45, 20, 45, 25, 115, "Normal", 100);
        poisonedPokemon = new PoisonedPokemonDecorator(basePokemon);
    }

    @Test
    void shouldSetStatusWhenCreated() {
        assertEquals(5, basePokemon.getStatus(), "El estado del Pokémon debe ser 5 (envenenado)");
    }

    @Test
    void shouldModifyName() {
        assertTrue(poisonedPokemon.getName().contains("Envenenado"), "El nombre debe incluir 'Envenenado'");
        assertTrue(poisonedPokemon.getName().contains("Jigglypuff"), "El nombre debe incluir el nombre original del Pokémon");
    }

    @Test
    void shouldApplyPoisonDamage() {
        int originalHP = basePokemon.getPs();
        poisonedPokemon.applyEffectDamage();
        int expectedFirstTurnDamage = Math.max(1, basePokemon.getTotalPs() / 16);
        int firstTurnHP = basePokemon.getPs();
        assertEquals(originalHP - expectedFirstTurnDamage, firstTurnHP, "El daño por veneno debe ser 1/16 del PS total");

        poisonedPokemon.applyEffectDamage();
        int secondTurnHP = basePokemon.getPs();
        int secondTurnDamage = firstTurnHP - secondTurnHP;
        assertTrue(secondTurnDamage > expectedFirstTurnDamage, "El daño por veneno debe aumentar en turnos posteriores");
    }

    @Test
    void shouldIncrementPoisonTurnCounter() {
        assertEquals(0, poisonedPokemon.getPoisonTurns(), "El contador de turnos de veneno debe comenzar en 0");
        poisonedPokemon.applyEffectDamage();
        assertEquals(1, poisonedPokemon.getPoisonTurns(), "El contador de turnos de veneno debe incrementarse a 1");
        poisonedPokemon.applyEffectDamage();
        assertEquals(2, poisonedPokemon.getPoisonTurns(), "El contador de turnos de veneno debe incrementarse a 2");
    }

    @Test
    void shouldCapPoisonDamage() {
        for (int i = 0; i < 10; i++) {
            poisonedPokemon.applyEffectDamage();
        }
        assertTrue(basePokemon.getPs() > 0, "El Pokémon no debe quedar con menos de 1 PS debido al veneno");
    }

    @Test
    void shouldNotExceedMaxPoisonDamage() {
        for (int i = 0; i < 10; i++) {
            poisonedPokemon.applyEffectDamage();
        }
        int expectedMaxDamage = basePokemon.getTotalPs() * 8 / 16;
        int actualDamage = basePokemon.getTotalPs() - basePokemon.getPs();
        assertEquals(expectedMaxDamage, actualDamage, "El daño por veneno no debe exceder 8/16 del PS total");
    }

    @Test
    void shouldCapPoisonTurnCounterAtEight() {
        for (int i = 0; i < 10; i++) {
            poisonedPokemon.applyEffectDamage();
        }
        assertEquals(8, poisonedPokemon.getPoisonTurns(), "El contador de turnos de veneno no debe exceder 8");
    }

    @Test
    void shouldResetPoisonWhenCured() {
        basePokemon.setStatus(0);
        assertEquals(0, basePokemon.getStatus(), "El estado del Pokémon debe ser 0 después de ser curado");
        assertFalse(poisonedPokemon.getName().contains("Envenenado"), "El nombre no debe incluir 'Envenenado' después de ser curado");
    }

    @Test
    void shouldResetPoisonTurnCounterWhenCured() {
        poisonedPokemon.applyEffectDamage();
        poisonedPokemon.applyEffectDamage();
        basePokemon.setStatus(0);
        assertEquals(0, poisonedPokemon.getPoisonTurns(), "El contador de turnos de veneno debe reiniciarse después de curar");
    }

    @Test
    void shouldRestoreOriginalNameWhenCured() {
        basePokemon.setStatus(0);
        assertEquals("Jigglypuff", basePokemon.getName(), "El nombre debe volver al original después de curar");
    }

    @Test
    void shouldNotReducePsBelowOne() {
        basePokemon.setPs(1);
        poisonedPokemon.applyEffectDamage();
        assertEquals(1, basePokemon.getPs(), "El PS no debe bajar de 1 debido al veneno");
    }
    @Test
void shouldReturnCorrectStringRepresentation() {
    String expected = "Jigglypuff (Envenenado)";
    assertEquals(expected, poisonedPokemon.toString(), "La representación en cadena debe ser correcta");
}
@Test
void shouldNotApplyDamageAfterEightTurns() {
    for (int i = 0; i < 8; i++) {
        poisonedPokemon.applyEffectDamage();
    }

    int psAfterEightTurns = basePokemon.getPs();
    poisonedPokemon.applyEffectDamage(); // Noveno turno
    assertEquals(psAfterEightTurns, basePokemon.getPs(), "No debe aplicar daño después de 8 turnos");
}
}