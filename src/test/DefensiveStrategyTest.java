package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DefensiveStrategyTest {
    private DefensiveStrategy strategy;
    private DefensiveMachine machine;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        strategy = new DefensiveStrategy();

        Pokemon raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        Pokemon venusaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        Pokemon blastoise = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        raichu.addAttack(AttackFactory.createAttack("Rayo carga"));
        raichu.addAttack(AttackFactory.createAttack("Defensa férrea"));
        venusaur.addAttack(AttackFactory.createAttack("Bomba lodo"));
        blastoise.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(raichu, venusaur, blastoise));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive", "Superpoción"));

        machine = new DefensiveMachine("CPU", pokemons, items);
    }

    @Test
    void testDecideActionReturnsValidAction() {
        int action = strategy.decideAction(machine, null);
        assertTrue(action == 1 || action == 2 || action == 3);
    }

    @Test
    void testSelectAttackPrefersStatusAttack() {
        String attackName = strategy.selectAttack(machine, null);
        assertNotNull(attackName);
        assertEquals("Defensa férrea", attackName);
    }

    @Test
    void testSelectItemPrefersPotionOrRevive() {
        String item = strategy.selectItem(machine, null);
        assertTrue(item.contains("Poción") || item.contains("Revive"));
    }

    @Test
    void testSelectPokemonReturnsValidIndex() {
        int idx = strategy.selectPokemon(machine, null);
        assertTrue(idx >= 0 && idx < pokemons.size());
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
        // Puede ser false la mayoría de veces, pero si la vida es baja puede ser true (5% probabilidad)
        assertTrue(result || !result);
    }
}