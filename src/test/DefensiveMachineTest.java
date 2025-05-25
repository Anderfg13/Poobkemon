package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DefensiveMachineTest {
    private DefensiveMachine machine;
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

        machine = new DefensiveMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Defensive", machine.getMachineType());
    }

    @Test
    void testSelectMoveReturnsStatusIfLowHP() {
        raichu.setPs(5); // Menos del 40%
        int idx = machine.selectMove();
        assertTrue(idx >= 0 && idx < raichu.getAtaques().size());
        assertTrue(raichu.getAtaques().get(idx) instanceof StatusAttack);
    }

    @Test
    void testSelectBestPokemonReturnsBestDefensiveIfLowHP() {
        raichu.setPs(1); // Menos del 30%
        int idx = machine.selectBestPokemon();
        assertTrue(idx >= 0 && idx < pokemons.size());
        assertNotEquals(0, idx); // No debe ser el debilitado
    }

    @Test
    void testShouldUseItemWhenLowHealth() {
        raichu.setPs(5); // Menos del 40%
        assertTrue(machine.shouldUseItem());
    }

    @Test
    void testSelectItemPrefersPotion() {
        int idx = machine.selectItem();
        assertTrue(idx >= 0 && idx < items.size());
        assertTrue(items.get(idx).contains("Poción"));
    }



@Test
void testShouldUseItemWhenHealthHigh() {
    machine.getActivePokemon().setPs(100); // High health
    assertFalse(machine.shouldUseItem());
}

@Test
void testSelectItemWithNoItems() {
    // Create a machine with no items
    DefensiveMachine noItemMachine = new DefensiveMachine("NoItems", pokemons, new ArrayList<>());
    
    int itemIndex = noItemMachine.selectItem();
    assertEquals(-1, itemIndex); // Should return -1 when no items are available
}


}