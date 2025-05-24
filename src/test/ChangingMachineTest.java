package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ChangingMachineTest {
    private ChangingMachine machine;
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

        machine = new ChangingMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Changing", machine.getMachineType());
    }

    @Test
    void testSelectMoveReturnsValidIndex() {
        // Simula un oponente
        DefensiveMachine opponent = new DefensiveMachine("CPU2", pokemons, items);
        machine.setOpponent(opponent);
        int moveIndex = machine.selectMove();
        assertTrue(moveIndex >= 0 && moveIndex < machine.getActivePokemon().getAtaques().size());
    }

    @Test
    void testSelectBestPokemonReturnsValidIndex() {
        DefensiveMachine opponent = new DefensiveMachine("CPU2", pokemons, items);
        machine.setOpponent(opponent);
        int idx = machine.selectBestPokemon();
        assertTrue(idx >= 0 && idx < pokemons.size());
    }

    @Test
    void testShouldUseItemWhenLowHealth() {
        machine.getActivePokemon().setPs(5); // Menos del 35%
        assertTrue(machine.shouldUseItem());
    }

    @Test
    void testSelectItemPrefersPotionIfLowHealth() {
        machine.getActivePokemon().setPs(5);
        int idx = machine.selectItem();
        assertTrue(idx >= 0 && idx < items.size());
        assertEquals("Poción", items.get(idx));
    }

}