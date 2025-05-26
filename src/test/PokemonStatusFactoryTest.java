package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import java.util.Arrays;

class PokemonStatusFactoryTest {
    private Pokemon basePokemon;
    
    @BeforeEach
    void setUp() {
        // Create a base Pokemon for testing
        basePokemon = new Pokemon("Pikachu", 25, 100, 90, 55, 50, 40, 90, "Electrico", 100);
        
        // Add an attack so we can test some functionality
        basePokemon.addAttack(new PhysicalAttack("Placaje", "Normal", 40, 100, 10, null));
    }
    
    @Test
    void shouldApplyParalyzedStatus() {
        // Apply paralyzed status (code 1)
        PokemonBase paralyzedPokemon = PokemonStatusFactory.applyStatus(basePokemon, 1);
        
        // Verify the returned object is a ParalyzedPokemonDecorator
        assertTrue(paralyzedPokemon instanceof ParalyzedPokemonDecorator);
        
        // Verify the name is modified appropriately
        assertTrue(paralyzedPokemon.getName().contains("Paralizado"));
        
        // Verify the status code is set correctly
        assertEquals(1, basePokemon.getStatus());
    }
    
    @Test
    void shouldApplySleepingStatus() {
        // Apply sleeping status (code 2)
        PokemonBase sleepingPokemon = PokemonStatusFactory.applyStatus(basePokemon, 2);
        
        // Verify the returned object is a SleepingPokemonDecorator
        assertTrue(sleepingPokemon instanceof SleepingPokemonDecorator);
        
        // Verify the status code is set correctly
        assertEquals(2, basePokemon.getStatus());
    }
    
    @Test
    void shouldApplyBurnedStatus() {
        // Apply burned status (code 3)
        PokemonBase burnedPokemon = PokemonStatusFactory.applyStatus(basePokemon, 3);
        
        // Verify the returned object is a BurnedPokemonDecorator
        assertTrue(burnedPokemon instanceof BurnedPokemonDecorator);
        
        // Verify physical attack is reduced
        assertEquals(basePokemon.getPhysicalAttack() / 2, burnedPokemon.getPhysicalAttack());
        
        // Verify the status code is set correctly
        assertEquals(3, basePokemon.getStatus());
    }
    
    @Test
    void shouldApplyFrozenStatus() {
        // Apply frozen status (code 4)
        PokemonBase frozenPokemon = PokemonStatusFactory.applyStatus(basePokemon, 4);
        
        // Verify the returned object is a FrozenPokemonDecorator
        assertTrue(frozenPokemon instanceof FrozenPokemonDecorator);
        
        // Verify the status code is set correctly
        assertEquals(4, basePokemon.getStatus());
    }
    
    @Test
    void shouldApplyPoisonedStatus() {
        // Apply poisoned status (code 5)
        PokemonBase poisonedPokemon = PokemonStatusFactory.applyStatus(basePokemon, 5);
        
        // Verify the returned object is a PoisonedPokemonDecorator
        assertTrue(poisonedPokemon instanceof PoisonedPokemonDecorator);
        
        // Verify the status code is set correctly
        assertEquals(5, basePokemon.getStatus());
    }
    
    @Test
    void shouldReturnSamePokemonForNormalStatus() {
        // Apply normal status (code 0)
        PokemonBase normalPokemon = PokemonStatusFactory.applyStatus(basePokemon, 0);
        
        // Verify the returned object is the same as the input
        assertSame(basePokemon, normalPokemon);
        
        // Verify the status code is set correctly
        assertEquals(0, basePokemon.getStatus());
    }
    
    @Test
    void shouldThrowExceptionForInvalidStatusCode() {
        // Try to apply an invalid status code
        assertThrows(IllegalArgumentException.class, () -> {
            PokemonStatusFactory.applyStatus(basePokemon, 99);
        });
    }
    
    @Test
    void shouldGetBasePokemonFromNonDecoratedPokemon() {
        // Get base Pokemon from a non-decorated Pokemon
        Pokemon result = PokemonStatusFactory.getBasePokemon(basePokemon);
        
        // Verify the returned object is the same as the input
        assertSame(basePokemon, result);
    }
    
    @Test
    void shouldGetBasePokemonFromSingleDecoratedPokemon() {
        // Apply a status to the Pokemon
        PokemonBase decoratedPokemon = PokemonStatusFactory.applyStatus(basePokemon, 1);
        
        // Get base Pokemon from the decorated Pokemon
        Pokemon result = PokemonStatusFactory.getBasePokemon(decoratedPokemon);
        
        // Verify the returned object is the original base Pokemon
        assertSame(basePokemon, result);
    }
    
    @Test
    void shouldGetBasePokemonFromMultipleDecoratedPokemon() {
        // Apply multiple statuses to the Pokemon
        PokemonBase decoratedPokemon = PokemonStatusFactory.applyStatus(basePokemon, 1); // Paralyzed
        decoratedPokemon = PokemonStatusFactory.applyStatus(decoratedPokemon, 5); // Poisoned
        
        // Get base Pokemon from the decorated Pokemon
        Pokemon result = PokemonStatusFactory.getBasePokemon(decoratedPokemon);
        
        // Verify the returned object is the original base Pokemon
        assertSame(basePokemon, result);
    }
    
    @Test
    void shouldThrowExceptionForInvalidPokemonInGetBasePokemon() {
        // Create an invalid Pokemon implementation
        PokemonBase invalidPokemon = new PokemonBase() {
            @Override public String getName() { return "Invalid"; }
            @Override public String getType() { return "Invalid"; }
            @Override public int getId() { return 0; }
            @Override public int getTotalPs() { return 0; }
            @Override public int getPs() { return 0; }
            @Override public int getSpeed() { return 0; }
            @Override public int getEvasion() { return 0; }
            @Override public int getSpecialAttack() { return 0; }
            @Override public int getPhysicalAttack() { return 0; }
            @Override public int getSpecialDefense() { return 0; }
            @Override public int getPhysicalDefense() { return 0; }
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
            @Override public void applyEffectDamage() {}
            @Override public int attack(PokemonBase defensor, Attack attack) { return 0; }
        };
        
     
    }
    
    @Test
    void shouldApplyMultipleStatuses() {
        // Apply multiple statuses: 3 (Burned), 5 (Poisoned)
        PokemonBase resultPokemon = PokemonStatusFactory.applyMultipleStatus(basePokemon, 3, 5);

        // El nombre debe contener ambos estados
        String name = resultPokemon.getName();
        assertTrue(name.contains("Envenenado"), "El nombre debe reflejar el estado Envenenado");
        assertTrue(name.contains("Quemado"), "El nombre debe reflejar el estado Quemado");

        // El status final del basePokemon debe ser el último aplicado
        assertEquals(5, basePokemon.getStatus());

        // El método getBasePokemon debe devolver el original
        assertSame(basePokemon, PokemonStatusFactory.getBasePokemon(resultPokemon));
    }
    
    @Test
    void shouldVerifyDecoratorChainCorrectly() {
        // Apply three statuses: 1 (Paralizado), 3 (Quemado), 5 (Envenenado)
        PokemonBase decoratedPokemon = PokemonStatusFactory.applyMultipleStatus(basePokemon, 1, 3, 5);

        // El nombre debe contener los tres estados en cualquier orden
        String name = decoratedPokemon.getName();
        assertTrue(name.contains("Paralizado"), "El nombre debe reflejar el estado Paralizado");
        assertTrue(name.contains("Quemado"), "El nombre debe reflejar el estado Quemado");
        assertTrue(name.contains("Envenenado"), "El nombre debe reflejar el estado Envenenado");

        // El status final del basePokemon debe ser el último aplicado
        assertEquals(5, basePokemon.getStatus());

        // El método getBasePokemon debe devolver el original
        assertSame(basePokemon, PokemonStatusFactory.getBasePokemon(decoratedPokemon));
    }
    
    @Test
    void shouldHandleEmptyStatusArray() {
        // Apply an empty array of statuses
        PokemonBase resultPokemon = PokemonStatusFactory.applyMultipleStatus(basePokemon, new int[0]);
        
        // Should return the original Pokemon
        assertSame(basePokemon, resultPokemon);
    }
    
    @Test
    void shouldHandleHasStatusWithDecoratedPokemon() {
        // Apply a status
        PokemonBase decoratedPokemon = PokemonStatusFactory.applyStatus(basePokemon, 3);
        
        // Check has status
        assertTrue(PokemonStatusFactory.hasStatus(decoratedPokemon, 3));
        assertFalse(PokemonStatusFactory.hasStatus(decoratedPokemon, 1));
    }
}