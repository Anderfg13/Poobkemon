package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import java.lang.reflect.Field;

class SleepingPokemonDecoratorTest {
    
    private Pokemon basePokemon;
    private SleepingPokemonDecorator sleepingPokemon;
    private Attack testAttack;
    private Pokemon defender;
    
    @BeforeEach
    void setUp() {
        // Create a base Pokémon for testing
        basePokemon = new Pokemon("Snorlax", 143, 100, 30, 110, 65, 65, 160, "Normal", 100);
        testAttack = new PhysicalAttack("Placaje", "Normal", 40, 100, 10, null);
        basePokemon.addAttack(testAttack);
        
        sleepingPokemon = new SleepingPokemonDecorator(basePokemon);
        defender = new Pokemon("Target", 25, 100, 50, 50, 50, 50, 50, "Normal", 100);
    }
    
    @Test
    void shouldSetStatusWhenCreated() {
        // Verify constructor sets the correct status
        assertEquals(2, basePokemon.getStatus());
    }
    
    @Test
    void shouldSetRandomTurnCount() {
        // Verify turn status is set (should be between 1-3 turns)
        int turnStatus = basePokemon.getTurnStatus();
        assertTrue(turnStatus >= 1 && turnStatus <= 3, 
                  "Sleep turns should be between 1-3, was: " + turnStatus);
    }
    
    @Test
    void shouldModifyName() {
        // Verify name includes sleep status
        assertTrue(sleepingPokemon.getName().contains("Dormido"));
        assertTrue(sleepingPokemon.getName().contains("Snorlax"));
    }
@Test
void shouldPreventAttackWhileSleeping() {
    // Set explicit sleep turns for testing
    basePokemon.setTurnStatus(2);

    // Primer ataque: sigue dormido
    assertEquals(0, sleepingPokemon.attack(defender, testAttack));
    assertEquals(1, basePokemon.getTurnStatus());

    // Segundo ataque: sigue dormido, pero turnStatus llega a 0 y despierta
    assertEquals(0, sleepingPokemon.attack(defender, testAttack));
    assertEquals(0, basePokemon.getTurnStatus());

}

@Test
void shouldWakeUpAfterAllTurns() {
    // Set explicit sleep turns for testing
    basePokemon.setTurnStatus(1);

    // Primer ataque: sigue dormido, pero turnStatus llega a 0 y despierta
    assertEquals(0, sleepingPokemon.attack(defender, testAttack));
    assertEquals(0, basePokemon.getTurnStatus());
 
}
}