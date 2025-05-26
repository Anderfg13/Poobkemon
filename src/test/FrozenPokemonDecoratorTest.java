package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import domain.*;
import java.util.Random;
import java.lang.reflect.Field;

class FrozenPokemonDecoratorTest {
    
    private Pokemon basePokemon;
    private FrozenPokemonDecorator frozenPokemon;
    private Attack testAttack;
    private Pokemon defender;
    
    @BeforeEach
    void setUp() {
        // Create a base Pokémon for testing
        basePokemon = new Pokemon("Squirtle", 7, 100, 43, 65, 50, 64, 44, "Agua", 100);
        testAttack = new PhysicalAttack("Placaje", "Normal", 40, 100, 10, "Cascada");
        basePokemon.addAttack(testAttack);
        
        frozenPokemon = new FrozenPokemonDecorator(basePokemon);
        defender = new Pokemon("Target", 25, 100, 50, 50, 50, 50, 50, "Normal", 100);
    }
    
    @Test
    void shouldSetStatusWhenCreated() {
        // Verify constructor sets the correct status
        assertEquals(4, basePokemon.getStatus());
    }
    
    @Test
    void shouldModifyName() {
        // Verify name includes frozen status
        assertTrue(frozenPokemon.getName().contains("Congelado"));
        assertTrue(frozenPokemon.getName().contains("Squirtle"));
    }
    
   
    
    @Test
    void shouldHaveChanceToThaw() {
        boolean thawed = false;
        
        // Make multiple attempts to thaw
        for (int i = 0; i < 100 && !thawed; i++) {
            // Attack might cause thawing
            frozenPokemon.attack(defender, testAttack);
            
            // Check if status was reset to normal
            if (basePokemon.getStatus() == 0) {
                thawed = true;
            }
        }
        
        // With enough attempts, the Pokemon should eventually thaw
        assertTrue(thawed, "Frozen Pokemon should have a chance to thaw");
    }
}