package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import java.lang.reflect.Field;

class ParalyzedPokemonDecoratorTest {
    
    private Pokemon basePokemon;
    private ParalyzedPokemonDecorator paralyzedPokemon;
    private Attack testAttack;
    private Pokemon defender;
    
    @BeforeEach
    void setUp() {
        // Create a base Pokémon for testing
        basePokemon = new Pokemon("Pikachu", 25, 100, 90, 55, 50, 40, 90, "Electrico", 100);
        testAttack = new PhysicalAttack("Placaje", "Normal", 40, 100, 10, null);
        basePokemon.addAttack(testAttack);
        
        paralyzedPokemon = new ParalyzedPokemonDecorator(basePokemon);
        defender = new Pokemon("Target", 25, 100, 50, 50, 50, 50, 50, "Normal", 100);
    }
    
    @Test
    void shouldSetStatusWhenCreated() {
        // Verify constructor sets the correct status
        assertEquals(1, basePokemon.getStatus());
    }
    
    @Test
    void shouldModifyName() {
        // Verify name includes paralyzed status
        assertTrue(paralyzedPokemon.getName().contains("Paralizado"));
        assertTrue(paralyzedPokemon.getName().contains("Pikachu"));
    }
    
    @Test
    void shouldReduceSpeed() {
        // Original speed
        int originalSpeed = 90;
        
        // Verify speed is halved
        assertEquals(originalSpeed / 2, paralyzedPokemon.getSpeed());
    }
    
}