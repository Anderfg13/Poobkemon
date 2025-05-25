package test;

import domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MachineFactoryTest {

    @Test
    void testCreateAttackingMachine() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "CPU", 2);
        assertNotNull(m);
        assertEquals("Attacking", m.getMachineType());
        assertEquals("CPU", m.getMachineName());
        assertTrue(m.getPokemons().size() >= 2);
        assertTrue(m.getItems().size() >= 2);
    }

    @Test
    void testCreateDefensiveMachine() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.DEFENSIVE, "CPU", 3);
        assertNotNull(m);
        assertEquals("Defensive", m.getMachineType());
        assertEquals("CPU", m.getMachineName());
        assertTrue(m.getPokemons().size() >= 3);
        assertTrue(m.getItems().size() >= 3);
    }

    @Test
    void testCreateChangingMachine() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.CHANGING, "CPU", 1);
        assertNotNull(m);
        assertEquals("Changing", m.getMachineType());
        assertEquals("CPU", m.getMachineName());
        assertTrue(m.getPokemons().size() >= 1);
        assertTrue(m.getItems().size() >= 1);
    }

    @Test
    void testCreateExpertMachine() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.EXPERT, "CPU", 2);
        assertNotNull(m);
        assertEquals("Expert", m.getMachineType());
        assertEquals("CPU", m.getMachineName());
        assertTrue(m.getPokemons().size() >= 2);
        assertTrue(m.getItems().size() >= 2);
    }

    @Test
    void testCreateGeminiMachine() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.GEMINI, "CPU", 2);
        assertNotNull(m);
        assertEquals("Gemini", m.getMachineType());
        assertEquals("CPU", m.getMachineName());
        assertTrue(m.getPokemons().size() >= 2);
        assertTrue(m.getItems().size() >= 2);
    }

    @Test
    void testPokemonsHaveAttacks() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "CPU", 2);
        for (Pokemon p : m.getPokemons()) {
            assertFalse(p.getAtaques().isEmpty());
        }
    }

    @Test
    void testItemsAreValid() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "CPU", 2);
        List<Item> items = m.getItems();
        for (Item item : items) {
            assertNotNull(item.getName());
            assertFalse(item.getName().isEmpty());
        }
    }

    @Test
    void testCreateRandomPokemonsNoDuplicates() {
        // Indirecto: todos los pokémon deben ser distintos
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "CPU", 6);
        List<Pokemon> pokemons = m.getPokemons();
        long unique = pokemons.stream().map(Pokemon::getName).distinct().count();
        assertEquals(pokemons.size(), unique);
    }

    @Test
    void testAssignRandomAttacksNoDuplicates() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "CPU", 2);
        for (Pokemon p : m.getPokemons()) {
            long unique = p.getAtaques().stream().map(Attack::getName).distinct().count();
            assertEquals(p.getAtaques().size(), unique);
        }
    }

    @Test
    void testCreateRandomItemsNoDuplicates() {
        Machine m = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "CPU", 3);
        long unique = m.getItems().stream().map(Item::getName).distinct().count();
        assertEquals(m.getItems().size(), unique);
    }
}