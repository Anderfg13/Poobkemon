package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import java.util.List;
import java.util.ArrayList;

class PokemonDecoratorTest {
    
    private Pokemon basePokemon;
    private TestPokemonDecorator decorator;
    
    /**
     * Simple concrete decorator for testing the abstract PokemonDecorator
     */
    private static class TestPokemonDecorator extends PokemonDecorator {
        public TestPokemonDecorator(PokemonBase pokemon) {
            super(pokemon);
        }
        
        // Override a method to test custom behavior
        @Override
        public String getName() {
            return super.getName() + " (Test)";
        }
    }
    
    @BeforeEach
    void setUp() {
        // Create a base Pokémon for testing
        basePokemon = new Pokemon("Pikachu", 25, 100, 90, 55, 50, 40, 90, "Electrico", 100);
        
        // Add an attack for testing
        Attack attack = new PhysicalAttack("Placaje", "Normal", 40, 100, 10, null);
        basePokemon.addAttack(attack);
        
        // Create the decorator
        decorator = new TestPokemonDecorator(basePokemon);
    }
    
    @Test
    void shouldCorrectlyDelegateGetters() {
        // Test all getter delegation
        assertEquals("Pikachu (Test)", decorator.getName());
        assertEquals("Electrico", decorator.getType());
        assertEquals(25, decorator.getId());
        assertEquals(100, decorator.getTotalPs());
        assertEquals(100, decorator.getPs());
        assertEquals(90, decorator.getSpeed());
  
    }
    
    @Test
    void shouldCorrectlyDelegateSetters() {
        // Test all setter delegation
        decorator.setPs(75);
        assertEquals(75, basePokemon.getPs());
        
        decorator.setSpeed(80);
        assertEquals(80, basePokemon.getSpeed());
        
        decorator.setEvasion(60);
        assertEquals(60, basePokemon.getEvasion());
        
        decorator.setSpecialAttack(70);
        assertEquals(70, basePokemon.getSpecialAttack());
        
        decorator.setPhysicalAttack(65);
        assertEquals(65, basePokemon.getPhysicalAttack());
        
        decorator.setSpecialDefense(85);
        assertEquals(85, basePokemon.getSpecialDefense());
        
        decorator.setPhysicalDefense(75);
        assertEquals(75, basePokemon.getPhysicalDefense());
        
        decorator.setStatus(3);
        assertEquals(3, basePokemon.getStatus());
        
        decorator.setTurnStatus(2);
        assertEquals(2, basePokemon.getTurnStatus());
    }
    
    
    
    @Test
    void shouldCorrectlyDelegateAttacksList() {
        // Test getAtaques delegation
        List<Attack> attacks = decorator.getAtaques();
        assertEquals(1, attacks.size());
        assertEquals("Placaje", attacks.get(0).getName());
        
        // Test addAttack delegation
        Attack thunderbolt = new SpecialAttack("Rayo", "Electrico", 90, 100, 15, null);
        decorator.addAttack(thunderbolt);
        
        assertEquals(2, basePokemon.getAtaques().size());
        assertEquals("Rayo", basePokemon.getAtaques().get(1).getName());
        
        // Test getNombreAtaques delegation
        List<String> attackNames = decorator.getNombreAtaques();
        assertTrue(attackNames.contains("Placaje"));
        assertTrue(attackNames.contains("Rayo"));
    }
    
    @Test
    void shouldCorrectlyDelegateApplyEffectDamage() {
        // Create a PokemonBase stub that always sets the flag
        final boolean[] effectApplied = {false};
        PokemonBase trackedPokemon = new PokemonBase() {
            @Override public String getName() { return "Track"; }
            @Override public String getType() { return "Normal"; }
            @Override public int getId() { return 25; }
            @Override public int getTotalPs() { return 100; }
            @Override public int getPs() { return 100; }
            @Override public int getSpeed() { return 50; }
            @Override public int getEvasion() { return 50; }
            @Override public int getSpecialAttack() { return 50; }
            @Override public int getPhysicalAttack() { return 50; }
            @Override public int getSpecialDefense() { return 50; }
            @Override public int getPhysicalDefense() { return 50; }
            @Override public int getStatus() { return 0; }
            @Override public int getTurnStatus() { return 0; }
            @Override public void setPs(int ps) {}
            @Override public void setSpeed(int speed) {}
            @Override public void setEvasion(int evasion) {}
            @Override public void setSpecialAttack(int specialAttack) {}
            @Override public void setSpecialDefense(int specialDefense) {}
            @Override public void setPhysicalAttack(int physicalAttack) {}
            @Override public void setPhysicalDefense(int physicalDefense) {}
            @Override public void setStatus(int status) {}
            @Override public void setTurnStatus(int turnStatus) {}
            @Override public void addAttack(Attack attack) {}
            @Override public java.util.List<Attack> getAtaques() { return java.util.Collections.emptyList(); }
            @Override public java.util.List<String> getNombreAtaques() { return java.util.Collections.emptyList(); }
            @Override public void applyEffectDamage() { effectApplied[0] = true; }
            @Override public int attack(PokemonBase defensor, Attack attack) { return 0; }
        };

        PokemonDecorator testDecorator = new TestPokemonDecorator(trackedPokemon);
        testDecorator.applyEffectDamage();

        assertTrue(effectApplied[0], "applyEffectDamage should be delegated");
    }
}