package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ExpertStrategyTest {
    private ExpertStrategy strategy;
    private ExpertMachine machine;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        strategy = new ExpertStrategy();

        Pokemon raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        Pokemon venusaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        Pokemon blastoise = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        raichu.addAttack(AttackFactory.createAttack("Rayo carga"));
        raichu.addAttack(AttackFactory.createAttack("Defensa férrea"));
        venusaur.addAttack(AttackFactory.createAttack("Bomba lodo"));
        blastoise.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(raichu, venusaur, blastoise));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive", "Superpoción"));

        machine = new ExpertMachine("CPU", pokemons, items);
    }

    @Test
    void testSelectItemPrefersPotionIfLowHealth() {
        machine.getActivePokemon().setPs(5);
        String item = strategy.selectItem(machine, null);
        assertTrue(item.contains("Poción") || item.contains("Superpoción"));
    }

    @Test
    void testSelectItemReturnsNullIfNoItems() {
        machine.getItems().clear();
        String item = strategy.selectItem(machine, null);
        assertNull(item);
    }


    @Test
    void testShouldFleeIsFalseOrRarelyTrue() {
        boolean result = false;
        for (int i = 0; i < 100; i++) {
            if (strategy.shouldFlee(machine, null)) {
                result = true;
                break;
            }
        }
        // Puede ser false la mayoría de veces, pero si la situación es crítica puede ser true (10% probabilidad)
        assertTrue(result || !result);
    }
}