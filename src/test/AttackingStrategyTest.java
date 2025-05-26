package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AttackingStrategyTest {
    private AttackingStrategy strategy;
    private AttackingMachine machine;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        strategy = new AttackingStrategy();

        Pokemon pikachu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        Pokemon bulbasaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        Pokemon squirtle = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        pikachu.addAttack(AttackFactory.createAttack("Rayo carga"));
        pikachu.addAttack(AttackFactory.createAttack("Defensa férrea"));
        bulbasaur.addAttack(AttackFactory.createAttack("Bomba lodo"));
        squirtle.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(pikachu, bulbasaur, squirtle));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive"));

        machine = new AttackingMachine("CPU", pokemons, items);
    }

    @Test
    void testSelectAttackPrefersPhysicalOrSpecial() {
        String attackName = strategy.selectAttack(machine, null);
        assertNotNull(attackName);
        // Debe ser "Rayo carga" porque es físico/especial y tiene daño
        assertEquals("Rayo carga", attackName);
    }

    @Test
    void testSelectAttackFallsBackToAnyIfNoPhysicalOrSpecial() {
        // Elimina ataques físicos/especiales de Pikachu
        machine.getActivePokemon().getAtaques().clear();
        machine.getActivePokemon().addAttack(AttackFactory.createAttack("Defensa férrea")); // Solo status
        String attackName = strategy.selectAttack(machine, null);
        assertEquals("Defensa férrea", attackName);
    }

    @Test
    void testSelectItemPrefersPotionIfLowHealth() {
        machine.getActivePokemon().setPs(5); // Menos del 50%
        String item = strategy.selectItem(machine, null);
        assertEquals("Poción", item);
    }

    @Test
    void testSelectItemRandomIfHealthy() {
        machine.getActivePokemon().setPs(30); // Más del 50%
        String item = strategy.selectItem(machine, null);
        assertTrue(items.contains(item));
    }

    @Test
    void testSelectPokemonReturnsBestAttacker() {
        int idx = strategy.selectPokemon(machine, null);
        assertTrue(idx >= 0 && idx < pokemons.size());
        // Debe evitar el Pokémon activo
        assertNotEquals(0, idx);
    }

    @Test
    void testShouldFleeAlwaysFalse() {
        assertFalse(strategy.shouldFlee(machine, null));
    }

    @Test
    void testDecideActionAttackWhenHealthy() {
        machine.getActivePokemon().setPs(30); // Salud suficiente
        int action = strategy.decideAction(machine, null);
        assertEquals(1, action); // Atacar
    }
}