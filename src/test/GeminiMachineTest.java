package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GeminiMachineTest {
    private GeminiMachine machine;
    private Pokemon raichu, venusaur, blastoise;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        venusaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        blastoise = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        raichu.addAttack(AttackFactory.createAttack("Rayo carga"));
        raichu.addAttack(AttackFactory.createAttack("Defensa férrea"));
        venusaur.addAttack(AttackFactory.createAttack("Bomba lodo"));
        blastoise.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(raichu, venusaur, blastoise));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive", "Superpoción"));

        machine = new GeminiMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Gemini", machine.getMachineType());
    }
    



    @Test
    void testShouldUseItemWhenLowHealth() {
        machine.getActivePokemon().setPs(5); // Less than 40%
        assertTrue(machine.shouldUseItem());
    }

    @Test
    void testShouldNotUseItemWhenSufficientHealth() {
        machine.getActivePokemon().setPs(20); // More than 40%
        assertFalse(machine.shouldUseItem());
    }
    @Test
    void testShouldNotUseItemWhenHealthHigh() {
        machine.getActivePokemon().setPs(30); // More than 40%
        assertFalse(machine.shouldUseItem());
    }



    @Test
    void testShouldNotUseItemWhenNoStatusEffect() {
        machine.getActivePokemon().setStatus(0); // No status effect
        assertFalse(machine.shouldUseItem());
    }

    @Test
    void testSelectItemReturnsValidIndexWhenItemsAvailable() {
        machine.getActivePokemon().setPs(5); // Low health to ensure item usage
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size());
    }



    @Test
    void testSelectItemReturnsValidIndexWhenHealthLow() {
        machine.getActivePokemon().setPs(5); // Low health to ensure item usage
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size());
    }
    @Test
    void testSelectItemReturnsValidIndexWhenHealthHigh() {
        machine.getActivePokemon().setPs(30); // More than 40%
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size());
    }
    @Test
    void testSelectItemReturnsValidIndexWhenNoStatusEffect() {
        machine.getActivePokemon().setStatus(0); // No status effect
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size());
    }

    @Test
    void testSelectItemReturnsValidIndexWhenStatusEffect() {
        machine.getActivePokemon().setStatus(1); // Some status effect
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size());
    }

    @BeforeEach
    void setUp() {
        // Inicialización para GeminiMachine
    }
    
    @Test
    void testGeminiMachineType() {
        assertEquals("Gemini", machine.getMachineType());
    }
 

}