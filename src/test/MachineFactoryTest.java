package test;

import domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MachineFactoryTest {

    @Test
    @DisplayName("Test machine factory creates AttackingMachine")
    void testCreateAttackingMachine() {
        Machine machine = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "TestAttacker", 1);
        assertNotNull(machine);
        assertEquals("TestAttacker", machine.getMachineName());
        assertEquals("Attacking", machine.getMachineType());
        assertTrue(machine instanceof AttackingMachine);
    }
    
    @Test
    @DisplayName("Test machine factory creates DefensiveMachine")
    void testCreateDefensiveMachine() {
        Machine machine = MachineFactory.createMachine(MachineFactory.MachineType.DEFENSIVE, "TestDefender", 1);
        assertNotNull(machine);
        assertEquals("TestDefender", machine.getMachineName());
        assertEquals("Defensive", machine.getMachineType());
        assertTrue(machine instanceof DefensiveMachine);
    }
    
    @Test
    @DisplayName("Test machine factory creates ChangingMachine")
    void testCreateChangingMachine() {
        Machine machine = MachineFactory.createMachine(MachineFactory.MachineType.CHANGING, "TestChanger", 1);
        assertNotNull(machine);
        assertEquals("TestChanger", machine.getMachineName());
        assertEquals("Changing", machine.getMachineType());
        assertTrue(machine instanceof ChangingMachine);
    }
    
    @Test
    @DisplayName("Test machine factory creates ExpertMachine")
    void testCreateExpertMachine() {
        Machine machine = MachineFactory.createMachine(MachineFactory.MachineType.EXPERT, "TestExpert", 1);
        assertNotNull(machine);
        assertEquals("TestExpert", machine.getMachineName());
        assertEquals("Expert", machine.getMachineType());
        assertTrue(machine instanceof ExpertMachine);
    }
    
    @Test
    @DisplayName("Test difficulty affects pokemon and item count")
    void testDifficultyAffectsCreation() {
        Machine easyMachine = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "Easy", 1);
        Machine hardMachine = MachineFactory.createMachine(MachineFactory.MachineType.ATTACKING, "Hard", 3);
        
        // Higher difficulty should mean more Pokemon and/or items
        boolean hasMorePokemonOrItems = 
            hardMachine.getPokemons().size() > easyMachine.getPokemons().size() ||
            hardMachine.getItems().size() > easyMachine.getItems().size();
            
        assertTrue(hasMorePokemonOrItems);
    }
}