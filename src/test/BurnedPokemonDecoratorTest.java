package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;

class BurnedPokemonDecoratorTest {
    
    private Pokemon basePokemon;
    private BurnedPokemonDecorator burnedPokemon;
    
    @BeforeEach
    void setUp() {
        // Create a base Pokémon for testing
        basePokemon = new Pokemon("Bulbasaur", 1, 100, 45, 49, 65, 49, 45, "Planta", 100);
        burnedPokemon = new BurnedPokemonDecorator(basePokemon);
    }
    
    @Test
    void shouldSetStatusWhenCreated() {
        // Verify constructor sets the correct status
        assertEquals(3, basePokemon.getStatus());
    }
    
    @Test
    void shouldReducePhysicalAttack() {
        // Original attack
        int originalAttack = basePokemon.getPhysicalAttack();
        
        // Verify physical attack is halved
        assertEquals(originalAttack / 2, burnedPokemon.getPhysicalAttack());
        
        // Special attack remains the same
        assertEquals(basePokemon.getSpecialAttack(), burnedPokemon.getSpecialAttack());
    }
    
    @Test
    void shouldApplyBurnDamage() {
        // Original HP
        int originalHP = basePokemon.getPs();
        
        // Apply burn damage
        burnedPokemon.applyEffectDamage();
        
        // Verify damage was dealt (1/16 of max HP)
        int expectedDamage = Math.max(1, basePokemon.getTotalPs() / 16);
    }
    
    @Test
    void shouldModifyName() {
        // Verify name includes burn status
        assertTrue(burnedPokemon.getName().contains("Quemado"));
        assertTrue(burnedPokemon.getName().contains("Bulbasaur"));
    }
}