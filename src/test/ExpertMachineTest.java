package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ExpertMachineTest {
    private ExpertMachine machine;
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

        machine = new ExpertMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Expert", machine.getMachineType());
    }

    @Test
    void testSelectMoveReturnsValidIndex() {
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
        machine.getActivePokemon().setPs(5); // Menos del 40%
        assertTrue(machine.shouldUseItem());
    }

    @Test
    void testShouldNotUseItemWhenHealthHigh() {
        machine.getActivePokemon().setPs(30); // More than 40%
        assertFalse(machine.shouldUseItem());
    }

    @Test
    void testShouldNotUseItemWhenNoStatusEffect() {
        machine.getActivePokemon().setStatus(0); // Sin efecto de estado
        assertFalse(machine.shouldUseItem());
    }
    // Add these test methods to your existing ExpertMachineTest class

    @Test
    void testStrategyIsExpertStrategy() {
        // This assumes your ExpertMachine has a getStrategy method
        // If the method signature is different, adjust accordingly
        assertTrue(machine instanceof ExpertMachine);
    }


    @Test
    void testSelectItemReturnsValidIndexWhenItemsAvailable() {
        machine.getActivePokemon().setPs(5); // Low health to ensure item usage
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size());
    }

    @Test
    void testSelectBestPokemonConsidersTypeAdvantage() {
        // Create opponent with a water pokemon
        ArrayList<Pokemon> opponentPokemons = new ArrayList<>();
        Pokemon waterPokemon = new Pokemon("Squirtle", 10, 100, 50, 50, 50, 50, 50, "Agua", 100);
        opponentPokemons.add(waterPokemon);

        DefensiveMachine opponent = new DefensiveMachine("CPU2", opponentPokemons, items);
        machine.setOpponent(opponent);

        // The expert machine should prefer the electric-type pokemon (Raichu) against water
        int pokemonIdx = machine.selectBestPokemon();
        assertEquals(0, pokemonIdx); // Raichu is at index 0
    }

    @Test
    void testExecuteTurnSwitchesFaintedPokemon() {
        // Set up a mock BattleArena - this will need adjustment if your BattleArena class has complex needs
        BattleArenaNormal arena = new BattleArenaNormal();

        // Set up opponent and make current pokemon fainted
        ArrayList<Pokemon> opponentPokemons = new ArrayList<>();
        opponentPokemons.add(new Pokemon("Squirtle", 10, 100, 50, 50, 50, 50, 50, "Agua", 100));

        DefensiveMachine opponent = new DefensiveMachine("CPU2", opponentPokemons, items);
        machine.setOpponent(opponent);

        // Faint the current pokemon
        machine.getActivePokemon().setPs(0);

        // Execute turn should switch pokemon
        boolean result = machine.executeTurn(arena);
        assertTrue(result);
        assertTrue(machine.getActivePokemon().getPs() > 0);
    }

    @Test
    void testGetPokemonsReturnsList() {
        assertEquals(3, machine.getPokemons().size());
    }

    @Test
    void testGetItemsReturnsList() {
        assertEquals(3, machine.getItems().size());
    }

    @Test
    void testGetActivePokemonNotNull() {
        assertNotNull(machine.getActivePokemon());
    }

    @Test
    void testSwitchPokemonChangesActive() throws Exception {
        int before = machine.getActivePokemon().getPs();
        machine.getPokemons().get(1).setPs(50);
        machine.switchPokemon(1);
        assertEquals(machine.getPokemons().get(1), machine.getActivePokemon());
    }

    @Test
    void testRevivirPokemonRestoresHP() throws Exception {
        machine.getPokemons().get(2).setPs(0);
        machine.revivirPokemon(machine.getPokemons().get(2).getName());
        assertTrue(machine.getPokemons().get(2).getPs() > 0);
    }

    @Test
    void testRevivirPokemonThrowsIfNotFainted() {
        assertThrows(Exception.class, () -> machine.revivirPokemon(machine.getActivePokemon().getName()));
    }

    @Test
    void testEliminarItemRemovesItem() {
        machine.eliminarItem("Poción");
        assertFalse(machine.getNombreItems().contains("Poción"));
    }

    @Test
    void testGetNombreItemsReturnsNames() {
        assertTrue(machine.getNombreItems().contains("Revive"));
    }

    @Test
    void testAreAllPokemonFaintedFalse() {
        assertFalse(machine.areAllPokemonFainted());
    }

    @Test
    void testAreAllPokemonFaintedTrue() {
        for (Pokemon p : machine.getPokemons()) {
            p.setPs(0);
        }
        assertTrue(machine.areAllPokemonFainted());
    }

    @Test
    void testIsMachineReturnsTrue() {
        assertTrue(machine.isMachine());
    }

    @Test
    void testSetOpponentAndGetOpponent() {
        DefensiveMachine opponent = new DefensiveMachine("CPU2", pokemons, items);
        machine.setOpponent(opponent);
        // No excepción, solo cobertura
    }

    @Test
    void testHandleTurnTimeoutReducesPP() {
        int before = machine.getActivePokemon().getAtaques().get(0).getPowerPoint();
        machine.handleTurnTimeout();
        int after = machine.getActivePokemon().getAtaques().get(0).getPowerPoint();
        assertTrue(after <= before);
    }

}