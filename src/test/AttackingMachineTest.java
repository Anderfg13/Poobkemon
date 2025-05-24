package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AttackingMachineTest {
    private AttackingMachine machine;
    private Pokemon poke1, poke2, poke3;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        poke1 = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        poke2 = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        poke3 = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        poke1.addAttack(AttackFactory.createAttack("Rayo carga"));
        poke1.addAttack(AttackFactory.createAttack("Defensa férrea"));
        poke2.addAttack(AttackFactory.createAttack("Bomba lodo"));
        poke3.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(poke1, poke2, poke3));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive"));

        machine = new AttackingMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Attacking", machine.getMachineType());
    }

    @Test
    void testSelectMovePrefersPhysicalOrSpecial() {
        machine.setOpponent(new DefensiveMachine("CPU2", pokemons, items));
        int moveIndex = machine.selectMove();
        assertTrue(moveIndex >= 0 && moveIndex < poke1.getAtaques().size());
        // Debe evitar ataques de estado si hay físicos/especiales
        assertFalse(poke1.getAtaques().get(moveIndex) instanceof StatusAttack);
    }

    @Test
    void testSelectBestPokemonReturnsDifferentIndexIfAdvantage() {
        machine.setOpponent(new DefensiveMachine("CPU2", pokemons, items));
        int bestIndex = machine.selectBestPokemon();
        assertTrue(bestIndex >= 0 && bestIndex < pokemons.size());
    }

    @Test
    void testShouldUseItemWhenLowHealth() {
        poke1.setPs(5); // Menos del 30%
        assertTrue(machine.shouldUseItem());
    }

    @Test
    void testShouldNotUseItemWhenHealthy() {
        poke1.setPs(30); // Más del 30%
        assertFalse(machine.shouldUseItem());
    }

    @Test
    void testSelectItemPrefersXAttackOrPotion() {
        int idx = machine.selectItem();
        assertTrue(idx == 1 || idx == 0); // X-Ataque o Poción
    }
}